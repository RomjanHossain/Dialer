package com.capx.dialer.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.capx.dialer.core.data.local.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for the [FavoriteEntity] table.
 *
 * Read queries return [Flow] for reactive updates except [isFavorite],
 * which is a one-shot query used for toggle logic.
 */
@Dao
interface FavoriteDao {

    /**
     * Observes the full list of favorited contact IDs, ordered by
     * the time they were added (most recent first).
     */
    @Query("SELECT contactId FROM favorites ORDER BY addedAt DESC")
    fun getAllFavoriteIds(): Flow<List<Long>>

    /**
     * Inserts a contact as a favorite. If the contact is already
     * favorited, the existing row is replaced (updating [FavoriteEntity.addedAt]).
     *
     * @param favorite The entity to persist.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteEntity)

    /**
     * Removes a contact from favorites.
     *
     * @param contactId The system contact ID to un-favorite.
     */
    @Query("DELETE FROM favorites WHERE contactId = :contactId")
    suspend fun deleteFavorite(contactId: Long)

    /**
     * One-shot check whether a contact is currently favorited.
     *
     * Uses `EXISTS` for efficiency — avoids loading the full row.
     *
     * @param contactId The system contact ID to check.
     * @return `true` if the contact is in the favorites table.
     */
    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE contactId = :contactId)")
    suspend fun isFavorite(contactId: Long): Boolean
}
