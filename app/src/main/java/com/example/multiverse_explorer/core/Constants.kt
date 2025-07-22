package com.example.multiverse_explorer.core


object Constants {

    object Filter {

        const val ALL = "All"
        const val ALIVE = "Alive"
        const val DEAD = "Dead"
        const val UNKNOWN = "unknown"

        val STATUS_OPTIONS = listOf(ALL, ALIVE, DEAD, UNKNOWN)


    }


    object Database {
        const val DATABASE_NAME = "character_database"

        const val CHARACTER_TABLE = "character_table"
        const val LOCATION_TABLE = "location_table"
        const val ORIGIN_TABLE = "origin_table"

        const val EPISODE_TABLE = "episode_table"
    }


}