package com.andmar.flint

import android.app.Application

class FlintApplication: Application() {

    lateinit var flintRepository: FlintRepository

    override fun onCreate() {
        super.onCreate()

        initDependencies()
    }
}