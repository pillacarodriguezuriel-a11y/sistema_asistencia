package pe.unsch.ceis.asistencia.ui.state

sealed interface PersistenceUiState {
    data object Idle : PersistenceUiState

    data object Saving : PersistenceUiState

    data class Success(
        val message: String,
    ) : PersistenceUiState

    data class Error(
        val errorMsg: String,
    ) : PersistenceUiState
}
