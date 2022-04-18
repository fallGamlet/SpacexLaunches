package com.spacex.launches

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.spacex.launches.data.database.AppDatabase
import com.spacex.launches.data.database.DbLaunch
import kotlinx.coroutines.runBlocking
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Before
import java.util.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class AppDatabaseTest {

    private lateinit var db: AppDatabase

    @Before
    fun setUp() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(appContext, AppDatabase::class.java)
            .build()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun testDbLaunchAccess() {
        println("===> testDbLaunchAccess    started...")
        runBlocking {
            val dao = db.getLaunchDao()

            val items = (1..10).map { generateDbLaunch(it) }
                .toTypedArray()

            dao.insertAll(*items)

            val loadedItems = dao.getAllLaunches()
            assert(items.size == loadedItems.size) {
                "Loaded items length ${loadedItems.size} not equals to original items ${items.size}"
            }

            items.forEach {
                val loadedItem = dao.getLaunchById(it.id)
                println(it)
                println(loadedItem)
                assert(it == loadedItem) {
                    "Request launch with id ${it.id} but loaded wrong item $loadedItem"
                }
                println()
            }
        }
        println("===> testDbLaunchAccess    finished")
    }

    private fun generateDbLaunch(num: Int): DbLaunch {
        return DbLaunch(
            id = num.toString(),
            name = "Launch name #$num",
            description = "Description of launch #$num",
            wikipedia = "https://wikipedia/launches/$num",
            date = Date(),
            imageSmall = "https://launch/$num/image-small",
            rocketId = "rocket-launch-$num"
        )
    }
}
