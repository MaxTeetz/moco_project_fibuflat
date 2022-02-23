package com.example.moco_project_fibuflat.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserInfoDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(databaseUser: DatabaseUser)

    @Update
    suspend fun update(databaseUser: DatabaseUser)

    @Delete
    suspend fun delete(databaseUser: DatabaseUser)

    @Query("SELECT * from databaseUser WHERE id = :id")
    fun getItem(id: Int): Flow<DatabaseUser>

    @Query("SELECT * from databaseUser ORDER BY email ASC")
    fun getItems(): Flow<List<DatabaseUser>>
}