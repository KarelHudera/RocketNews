package com.example.rocketnews

import android.app.Application
import androidx.room.Room
import com.example.rocketnews.database.AppDatabase

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: App
    }

    val appDatabase: AppDatabase by lazy {
        Room.databaseBuilder(this, AppDatabase::class.java, "my-database").build()
    }
}