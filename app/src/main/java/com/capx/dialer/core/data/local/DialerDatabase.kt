package com.capx.dialer.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.capx.dialer.core.data.local.dao.FavoriteDao
import com.capx.dialer.core.data.local.dao.RecordingDao
import com.capx.dialer.core.data.local.entity.FavoriteEntity
import com.capx.dialer.core.data.local.entity.RecordingEntity

/**
 * Room database for the Dialer app.
 *
 * Houses two tables:
 * - **recordings** — persisted call recording metadata.
 * - **favorites** — user-favorited contacts stored locally.
 *
 * Schema export is enabled so that Room generates JSON schema files
 * under `app/schemas/`, which can be used for automated migration testing.
 */
@Database(
    entities = [
        RecordingEntity::class,
        FavoriteEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class DialerDatabase : RoomDatabase() {

    /** Provides access to recording CRUD operations. */
    abstract fun recordingDao(): RecordingDao

    /** Provides access to favorite CRUD operations. */
    abstract fun favoriteDao(): FavoriteDao

    companion object {
        /** Database file name on disk. */
        const val DATABASE_NAME = "dialer_database"
    }
}
