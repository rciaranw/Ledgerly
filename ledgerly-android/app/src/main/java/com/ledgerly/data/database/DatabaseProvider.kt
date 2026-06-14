package com.ledgerly.data.database

import android.content.Context
import androidx.room.Room

object DatabaseProvider {

    @Volatile
    private var database:
        LedgerlyDatabase? = null

    fun getDatabase(
        context: Context
    ): LedgerlyDatabase {
        val existingDatabase =
            database

        if (existingDatabase != null) {
            return existingDatabase
        }

        return synchronized(this) {
            val databaseInsideLock =
                database

            if (databaseInsideLock != null) {
                databaseInsideLock
            } else {
                val newDatabase =
                    Room.databaseBuilder(
                        context.applicationContext,
                        LedgerlyDatabase::class.java,
                        "ledgerly_database"
                    )
                        .addMigrations(
                            DatabaseMigrations
                                .MIGRATION_5_6
                        )
                        .build()

                database =
                    newDatabase

                newDatabase
            }
        }
    }
}