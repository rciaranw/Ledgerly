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
        return database
            ?: synchronized(this) {
                val existingDatabase =
                    database

                if (existingDatabase != null) {
                    return@synchronized
                        existingDatabase
                }

                val instance =
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
                    instance

                instance
            }
    }
}