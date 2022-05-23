package com.example.tutuproject.others

sealed class ConnectionMode {
    object Online: ConnectionMode()
    object Offline: ConnectionMode()
}