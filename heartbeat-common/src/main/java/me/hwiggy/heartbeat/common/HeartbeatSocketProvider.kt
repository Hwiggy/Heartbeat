package me.hwiggy.heartbeat.common

import android.util.Log
import okhttp3.*
import okio.ByteString
import java.io.Closeable
import java.util.concurrent.TimeUnit
import java.util.function.Supplier

private const val TAG = "Heartbeat Socket Provider"
class HeartbeatSocketProvider : WebSocketListener(), Closeable, Supplier<WebSocket> {
    private val http = OkHttpClient.Builder()
        .pingInterval(30, TimeUnit.SECONDS)
        .build()
    private val req = Request.Builder().url("wss://heartbeat.hwiggy.me/socket").build()

    private lateinit var wsc: WebSocket

    override fun onOpen(webSocket: WebSocket, response: Response) {
        Log.i(TAG, "Socket connected!")
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        Log.i(TAG, "Incoming: $bytes")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        Log.i(TAG, "Incoming: $text")
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        Log.i(TAG, "Socket closing...")
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        Log.i(TAG, "Socket closed!")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        Log.e(TAG, "Socket Failure!", t)
        wsc = openSocket()
    }

    private fun openSocket(): WebSocket = http.newWebSocket(req, this)
    override fun close() { wsc.close(1000, "Graceful Exit") }
    override fun get(): WebSocket {
        if (!this::wsc.isInitialized) {
            wsc = openSocket()
        }
        return wsc
    }
}