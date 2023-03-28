package me.hwiggy.heartbeat.wearable

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import me.hwiggy.heartbeat.wearable.databinding.ActivityHeartbeatBinding
import okhttp3.*
import okio.ByteString

private const val TAG = "Heartbeat WearOS"
private const val WS_URL = "ws://192.168.0.27:8080"

class Heartbeat : Activity() {
    private val http = OkHttpClient()
    private val wsReq = Request.Builder().url(WS_URL).build()
    private val wsc = http.newWebSocket(wsReq, SocketListener())

    private lateinit var binding: ActivityHeartbeatBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHeartbeatBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        wsc.send("First Message")
    }

    private class SocketListener : WebSocketListener() {
        private val handler = Handler()
        override fun onOpen(webSocket: WebSocket, response: Response) {
            handler.post {
                Log.i(TAG, "Connected to socket")
            }
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            Log.i(TAG, "Received from $webSocket: $text")
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            Log.i(TAG, "Received from $webSocket: $bytes")
        }
    }
}