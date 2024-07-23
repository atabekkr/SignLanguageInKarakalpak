package com.shaxsanem.signlanguage.data.db

import androidx.room.Dao
import androidx.room.Query
import com.shaxsanem.signlanguage.data.models.Word

@Dao
interface SLDao {

    @Query("Select * from words")
    suspend fun getWords(): List<Word>

}