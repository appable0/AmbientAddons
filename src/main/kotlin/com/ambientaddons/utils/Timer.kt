package com.ambientaddons.utils

class Timer {
    private var previousTime: Long = 0
    private var startTime: Long? = null

    var isRunning: Boolean = false
        private set

    private val sessionTime: Long
        get() {
            return startTime?.let { System.currentTimeMillis() - it } ?: 0
        }

    private val time: Long
        get() {
            return sessionTime?.let { it + previousTime } ?: 0
        }

    fun start() {
        isRunning = true
        startTime = System.currentTimeMillis()
    }

    fun stop() {
        isRunning = false
        previousTime = sessionTime
        startTime = null
    }
}