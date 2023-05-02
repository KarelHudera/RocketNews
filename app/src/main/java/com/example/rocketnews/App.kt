package com.example.rocketnews

import android.app.Application
import androidx.room.Room
import com.example.rocketnews.databaseNasa.DatabaseNasa
import com.example.rocketnews.databaseSpaceX.DatabaseSpaceX

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: App
    }

    val databaseNasa: DatabaseNasa by lazy {
        Room.databaseBuilder(this, DatabaseNasa::class.java, "database_nasa").build()
    }

    val databaseSpaceX: DatabaseSpaceX by lazy {
        Room.databaseBuilder(this, DatabaseSpaceX::class.java, "database_spacex").build()
    }
}