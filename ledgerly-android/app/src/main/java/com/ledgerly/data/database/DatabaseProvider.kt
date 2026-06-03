package com.ledgerly.data.database

import android.content.Context
import androidx.room.Room

object DatabaseProvider {

    @Volatile
    private var database: LedgerlyDatabase? = null

    fun getDatabase(context: Context): LedgerlyDatabase {
        return database ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                LedgerlyDatabase::class.java,
                "ledgerly_database"
            )
                .fallbackToDestructiveMigration(false)
                .build()

            database = instance
            instance
        }
    }
}