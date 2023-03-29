package me.hwiggy.heartbeat.wearable

import android.app.Activity
import android.os.Bundle
import me.hwiggy.heartbeat.common.HeartbeatSocketProvider
import me.hwiggy.heartbeat.wearable.databinding.ActivityHeartbeatBinding

class Heartbeat : Activity() {
    private val socketProvider = HeartbeatSocketProvider()
    private lateinit var binding: ActivityHeartbeatBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHeartbeatBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        val wsc = socketProvider.open()
        wsc.send("First Message")
    }
}