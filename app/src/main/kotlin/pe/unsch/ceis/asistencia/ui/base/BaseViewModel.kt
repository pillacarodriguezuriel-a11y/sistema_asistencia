package pe.unsch.ceis.asistencia.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pe.unsch.ceis.asistencia.core.coroutines.CoroutineDispatchers

data class BaseUiState(
    val dependencyGraphReady: Boolean = false,
)

/**
 * Minimal feature ViewModel used to validate Hilt + Navigation Compose.
 * Future screen ViewModels should follow the same constructor-injection pattern.
 */
@HiltViewModel
class BaseViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
) : ViewModel() {
    private val _uiState = MutableStateFlow(BaseUiState())
    val uiState: StateFlow<BaseUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch(dispatchers.default) {
            _uiState.update { it.copy(dependencyGraphReady = true) }
        }
    }
}
