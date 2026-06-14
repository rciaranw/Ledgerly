package com.ledgerly.data.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object DatabaseMigrations {

    val MIGRATION_5_6 =
        object : Migration(
            startVersion = 5,
            endVersion = 6
        ) {
            override fun migrate(
                database:
                    SupportSQLiteDatabase
            ) {
                database.execSQL(
                    """
                    ALTER TABLE settings
                    ADD COLUMN dateFormat TEXT NOT NULL
                    DEFAULT 'DD/MM/YYYY'
                    """.trimIndent()
                )

                database.execSQL(
                    """
                    ALTER TABLE settings
                    ADD COLUMN language TEXT NOT NULL
                    DEFAULT 'English'
                    """.trimIndent()
                )

                database.execSQL(
                    """
                    ALTER TABLE settings
                    ADD COLUMN textSize TEXT NOT NULL
                    DEFAULT 'Standard'
                    """.trimIndent()
                )

                database.execSQL(
                    """
                    ALTER TABLE settings
                    ADD COLUMN biometricLockEnabled INTEGER NOT NULL
                    DEFAULT 0
                    """.trimIndent()
                )
            }
        }
}