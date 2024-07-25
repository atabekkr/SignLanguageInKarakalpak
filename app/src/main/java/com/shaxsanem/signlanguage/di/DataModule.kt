package com.shaxsanem.signlanguage.di

import android.content.Context
import androidx.room.Room
import com.shaxsanem.signlanguage.data.db.SLDao
import com.shaxsanem.signlanguage.data.db.SignLanguageDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.FileOutputStream
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): SignLanguageDb {
        val dbFile = context.getDatabasePath("sign_language.db")
        if (!dbFile.exists()) {
            context.assets.open("sign_language.db").use { inputStream ->
                FileOutputStream(dbFile).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
        }
        return Room.databaseBuilder(context.applicationContext, SignLanguageDb::class.java, "sign_language.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideSLDao(db: SignLanguageDb): SLDao {
        return db.getSLDao()
    }


}