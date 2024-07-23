package com.shaxsanem.signlanguage.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "words")
data class Word(
    @PrimaryKey
    val id: Int,
    val name: String,
    @ColumnInfo("group_name")
    val groupName: String,
    val content: String,
    @ColumnInfo("is_favourite")
    val isFavourite: Boolean
)
