package pe.unsch.ceis.asistencia.domain.model

sealed interface ResultState<out T> {
    data class Success<T>(val data: T) : ResultState<T>

    data class Error(
        val message: String,
        val cause: Throwable? = null,
    ) : ResultState<Nothing>

    data object Loading : ResultState<Nothing>
}

