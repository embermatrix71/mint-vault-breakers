package com.Tofwure2.Kestub.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Tofwure2.Kestub.core.config.GameConfig
import com.Tofwure2.Kestub.data.sample.SampleData
import com.Tofwure2.Kestub.domain.model.GameSettings
import com.Tofwure2.Kestub.domain.model.IdSource
import com.Tofwure2.Kestub.domain.model.RoundOutcome
import com.Tofwure2.Kestub.domain.model.RoundSummary
import com.Tofwure2.Kestub.domain.model.VaultBoard
import com.Tofwure2.Kestub.domain.repository.ProgressRepository
import com.Tofwure2.Kestub.domain.repository.SettingsRepository
import com.Tofwure2.Kestub.domain.usecase.CollapseAndRefillUseCase
import com.Tofwure2.Kestub.domain.usecase.EvaluateRoundUseCase
import com.Tofwure2.Kestub.domain.usecase.GenerateBoardUseCase
import com.Tofwure2.Kestub.domain.usecase.ResolveStrikeUseCase
import com.Tofwure2.Kestub.domain.usecase.UpdateMissionsUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

class GameViewModel(
    private val progressRepository: ProgressRepository,
    private val settingsRepository: SettingsRepository,
    private val generateBoard: GenerateBoardUseCase,
    private val resolveStrike: ResolveStrikeUseCase,
    private val collapseAndRefill: CollapseAndRefillUseCase,
    private val evaluateRound: EvaluateRoundUseCase,
    private val updateMissions: UpdateMissionsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GameUiState())
    val state: StateFlow<GameUiState> = _state.asStateFlow()

    private val _events = Channel<GameEvent>(Channel.BUFFERED)
    val events: Flow<GameEvent> = _events.receiveAsFlow()

    private var board: VaultBoard = VaultBoard(GameConfig.COLS, GameConfig.ROWS, emptyList())
    private var random: Random = Random(1)
    private var ids = IdSource()
    private var settings = GameSettings()
    private var chainJob: Job? = null
    private var sealFound = false
    private var loadedStage = -1
    private var loadedAttempt = -1

    fun ensureStage(stageIndex: Int, attempt: Int) {
        if (loadedStage == stageIndex && loadedAttempt == attempt && !_state.value.finished) return
        startStage(stageIndex, attempt)
    }

    private fun startStage(stageIndex: Int, attempt: Int) {
        chainJob?.cancel()
        loadedStage = stageIndex
        loadedAttempt = attempt
        sealFound = false
        viewModelScope.launch {
            settings = settingsRepository.load()
            val progress = progressRepository.load()
            val stage = SampleData.stageAt(stageIndex)
            ids = IdSource()
            val generated = generateBoard(stageIndex, attempt, ids)
            board = generated.board
            random = generated.random
            _state.update {
                GameUiState(
                    stageIndex = stage.index,
                    stageNameRes = stage.nameRes,
                    quota = stage.quota,
                    coins = 0,
                    strikesLeft = stage.strikes,
                    alarm = 0,
                    cols = board.cols,
                    rows = board.rows,
                    cells = board.toCells(),
                    busy = false,
                    paused = false,
                    finished = false,
                    pryCharges = 0,
                    pryArmed = false,
                    persistentHint = settings.persistentHint,
                    highContrast = settings.highContrast,
                    activePlate = progress.activePlate
                )
            }
        }
    }

    fun pause() {
        chainJob?.cancel()
        _state.update { it.copy(busy = false, chainActive = emptySet()) }
    }

    fun openPause() {
        _state.update { it.copy(paused = true) }
    }

    fun closePause() {
        _state.update { it.copy(paused = false) }
    }

    fun togglePry() {
        val current = _state.value
        if (current.finished || current.busy || current.paused) return
        if (current.pryCharges <= 0 && !current.pryArmed) return
        _state.update { it.copy(pryArmed = !it.pryArmed) }
    }

    fun dropCharge() {
        val current = _state.value
        if (current.finished || current.busy || current.paused) return
        val occupied = board.cells.indices.filter { board.cells[it] != null }
        if (occupied.isEmpty()) return
        val target = occupied[random.nextInt(occupied.size)]
        if (current.pryArmed) {
            _state.update { it.copy(pryArmed = false) }
        }
        runStrike(target)
    }

    fun tapCell(index: Int) {
        val current = _state.value
        if (current.finished || current.busy || current.paused) return
        val lock = board.lockAt(index) ?: return
        if (current.pryArmed) {
            val cells = board.cells.toMutableList()
            cells[index] = lock.copy(direction = lock.direction.rotatedClockwise())
            board = board.withCells(cells)
            _state.update {
                it.copy(
                    cells = board.toCells(),
                    pryArmed = false,
                    pryCharges = max(0, it.pryCharges - 1)
                )
            }
            emitPulse(false)
            return
        }
        runStrike(index)
    }

    private fun runStrike(index: Int) {
        val stage = SampleData.stageAt(_state.value.stageIndex)
        chainJob?.cancel()
        chainJob = viewModelScope.launch {
            _state.update {
                it.copy(
                    busy = true,
                    strikesLeft = max(0, it.strikesLeft - 1),
                    strikesMade = it.strikesMade + 1,
                    surgeShown = false
                )
            }
            val result = resolveStrike(board, index)
            emitPulse(false)
            if (result.links.isEmpty()) {
                board = result.board
                _state.update { it.copy(cells = board.toCells()) }
                delay(160L)
            } else {
                val cells = board.cells.toMutableList()
                for (link in result.links) {
                    cells[link.toIndex] = null
                    board = board.withCells(cells.toList())
                    _state.update {
                        it.copy(
                            cells = board.toCells(),
                            coins = it.coins + link.coins,
                            chainActive = setOf(link.toIndex),
                            locksCracked = it.locksCracked + 1
                        )
                    }
                    delay(settings.chainSpeed.stepMs)
                }
                emitPulse(true)
                if (result.surge) {
                    _state.update {
                        it.copy(coins = it.coins + GameConfig.SURGE_BONUS, surgeShown = true)
                    }
                }
            }
            if (result.sealCracked) {
                sealFound = true
            }
            val chainSize = result.links.size
            _state.update {
                it.copy(
                    alarm = (it.alarm + result.alarmDelta).coerceIn(0, GameConfig.ALARM_MAX),
                    longestChain = max(it.longestChain, chainSize),
                    pryCharges = if (chainSize >= GameConfig.PRY_CHAIN) {
                        min(it.pryCharges + 1, GameConfig.PRY_MAX)
                    } else {
                        it.pryCharges
                    },
                    chainActive = emptySet()
                )
            }
            delay(GameConfig.COLLAPSE_STEP_MS)
            board = collapseAndRefill(board, random, ids)
            _state.update { it.copy(cells = board.toCells()) }
            val snapshot = _state.value
            val outcome = evaluateRound(
                coins = snapshot.coins,
                quota = stage.quota,
                strikesLeft = snapshot.strikesLeft,
                alarm = snapshot.alarm
            )
            if (outcome == null) {
                _state.update { it.copy(busy = false) }
            } else {
                finishRound(outcome)
            }
        }
    }

    private suspend fun finishRound(outcome: RoundOutcome) {
        val snapshot = _state.value
        _state.update { it.copy(finished = true, busy = true, chainActive = emptySet()) }
        delay(GameConfig.RESULT_DELAY_MS)
        val stage = SampleData.stageAt(snapshot.stageIndex)
        val before = progressRepository.load()
        val newBest = snapshot.coins > before.bestVault
        val stageBest = HashMap(before.stageBest)
        val previousStageBest = stageBest[stage.index] ?: 0
        if (snapshot.coins > previousStageBest) {
            stageBest[stage.index] = snapshot.coins
        }
        val unlocked = if (outcome == RoundOutcome.CRACKED) {
            min(max(before.stagesUnlocked, stage.index + 1), SampleData.stages.size)
        } else {
            before.stagesUnlocked
        }
        val seals = if (sealFound) before.seals + stage.sealId else before.seals
        val silent = snapshot.alarm == 0
        val advanced = before.copy(
            coinsTotal = before.coinsTotal + snapshot.coins,
            bestVault = max(before.bestVault, snapshot.coins),
            longestChain = max(before.longestChain, snapshot.longestChain),
            locksCracked = before.locksCracked + snapshot.locksCracked,
            stagesUnlocked = unlocked,
            stageBest = stageBest,
            seals = seals
        )
        val missionUpdate = updateMissions(
            progress = advanced,
            missions = SampleData.missions,
            stageIndex = stage.index,
            outcome = outcome,
            longestChain = snapshot.longestChain,
            silentAlarm = silent
        )
        progressRepository.save(missionUpdate.progress)
        _events.send(
            GameEvent.Finished(
                RoundSummary(
                    stageIndex = stage.index,
                    outcome = outcome,
                    coins = snapshot.coins,
                    quota = stage.quota,
                    locksCracked = snapshot.locksCracked,
                    longestChain = snapshot.longestChain,
                    sealFound = sealFound,
                    silentAlarm = silent,
                    newBest = newBest,
                    completedMissions = missionUpdate.completed
                )
            )
        )
    }

    private fun emitPulse(strong: Boolean) {
        if (!settings.haptics) return
        _events.trySend(GameEvent.Pulse(strong))
    }

    private fun VaultBoard.toCells(): List<LockCell> = cells.map { lock ->
        if (lock == null) {
            LockCell(0L, 0, null, false)
        } else {
            LockCell(lock.id, lock.armour, lock.direction, lock.seal)
        }
    }
}
