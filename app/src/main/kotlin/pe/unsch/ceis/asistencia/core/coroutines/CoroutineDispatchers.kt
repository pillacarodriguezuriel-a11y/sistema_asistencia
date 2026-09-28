package pe.unsch.ceis.asistencia.core.coroutines

import kotlinx.coroutines.CoroutineDispatcher

/**
 * Dispatchers used by the application.
 *
 * Keeping them behind one injectable value lets unit tests replace every
 * dispatcher without changing production code.
 */
data class CoroutineDispatchers(
    val main: CoroutineDispatcher,
    val io: CoroutineDispatcher,
    val default: CoroutineDispatcher,
)
