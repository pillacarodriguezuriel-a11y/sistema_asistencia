package pe.unsch.ceis.asistencia.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
abstract class BaseDaoTest {
    protected lateinit var database: AppDatabase

    @Before
    fun createDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database =
            Room
                .inMemoryDatabaseBuilder(
                    context,
                    AppDatabase::class.java,
                ).allowMainThreadQueries()
                .build()
    }

    @After
    fun closeDatabase() {
        database.close()
    }
}
