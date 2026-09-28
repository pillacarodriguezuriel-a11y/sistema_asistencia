package pe.unsch.ceis.asistencia.ui.components.feedback

import org.junit.Assert.assertTrue
import org.junit.Test

class PersistenceFeedbackContractTest {
    @Test
    fun `terminal feedback duration is shorter than two seconds`() {
        assertTrue(PERSISTENCE_FEEDBACK_MILLIS in 1 until 2_000)
    }
}
