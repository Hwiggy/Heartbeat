package me.hwiggy.heartbeat.common

import android.content.Context

object Extensions {
    fun <T> Context.service(name: String) = getSystemService(name) as T
}