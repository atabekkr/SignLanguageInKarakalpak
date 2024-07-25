package com.shaxsanem.signlanguage.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.shaxsanem.signlanguage.data.models.Word

@Database(entities = [Word::class], version = 2)
abstract class SignLanguageDb: RoomDatabase() {

    abstract fun getSLDao(): SLDao

}