package com.shaxsanem.signlanguage.data.db

import androidx.room.Dao
import androidx.room.Query
import com.shaxsanem.signlanguage.data.models.Word

@Dao
interface SLDao {

    @Query("Select * from words")
    suspend fun getWords(): List<Word>

    @Query("Select * from words where name like :name and group_name like :groupName")
    suspend fun search(name: String, groupName: String): List<Word>

    @Query("Select * from words where content like :content limit 1")
    suspend fun getWordByContent(content: String): Word?

}