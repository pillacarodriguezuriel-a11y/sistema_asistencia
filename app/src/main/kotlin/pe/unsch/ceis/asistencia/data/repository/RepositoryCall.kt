package pe.unsch.ceis.asistencia.data.repository

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import pe.unsch.ceis.asistencia.domain.model.ResultState

internal suspend inline fun <T> repositoryCall(
    dispatcher: CoroutineDispatcher,
    errorMessage: String,
    crossinline block: suspend () -> T,
): ResultState<T> =
    withContext(dispatcher) {
        try {
            ResultState.Success(block())
        } catch (cancellation: CancellationException) {
            throw cancellation
        } catch (exception: Exception) {
            ResultState.Error(
                message = errorMessage,
                cause = exception,
            )
        }
    }
