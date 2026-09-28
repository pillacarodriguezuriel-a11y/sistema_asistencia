package pe.unsch.ceis.asistencia.ui.base

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import pe.unsch.ceis.asistencia.core.coroutines.CoroutineDispatchers

@OptIn(ExperimentalCoroutinesApi::class)
class BaseViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `marks dependency graph as ready with injected dispatcher`() =
        runTest(mainDispatcherRule.dispatcher) {
            val testDispatcher = mainDispatcherRule.dispatcher
            val viewModel =
                BaseViewModel(
                    dispatchers =
                        CoroutineDispatchers(
                            main = testDispatcher,
                            io = testDispatcher,
                            default = testDispatcher,
                        ),
                )

            assertFalse(viewModel.uiState.value.dependencyGraphReady)
            advanceUntilIdle()
            assertTrue(viewModel.uiState.value.dependencyGraphReady)
        }
}
