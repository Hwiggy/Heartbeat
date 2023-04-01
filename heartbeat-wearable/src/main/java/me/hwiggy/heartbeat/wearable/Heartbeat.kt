package me.hwiggy.heartbeat.wearable

import android.Manifest
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.provider.Contacts.Intents
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.wear.ongoing.OngoingActivity
import me.hwiggy.heartbeat.common.Extensions
import me.hwiggy.heartbeat.common.Extensions.service
import me.hwiggy.heartbeat.common.HeartbeatSocketProvider
import me.hwiggy.heartbeat.wearable.databinding.ActivityHeartbeatBinding
import okhttp3.WebSocket

class Heartbeat : ComponentActivity() {

    private lateinit var binding: ActivityHeartbeatBinding

    private val permissionBodySensor = Manifest.permission.BODY_SENSORS
    private val socketProvider = HeartbeatSocketProvider()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHeartbeatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        reqSensorPermission()

        val persistNoti = NotificationCompat.Builder(
            this, "PERSISTENT"
        ).apply {
            setContentTitle("Heartbeat Service")
            setContentText("Automatically updating heartbeat!")
            setSmallIcon(R.drawable.ic_heart)
            setOngoing(true)
        }

        val ongoing = OngoingActivity.Builder(this, 0, persistNoti)
            .setTouchIntent(PendingIntent.getActivity(this, 0, Intent(), 0))
            .setStaticIcon(R.drawable.ic_heart)
            .build()
        ongoing.apply(this)

        val notificationManager = service<NotificationManager>(Service.NOTIFICATION_SERVICE)
        notificationManager.notify(0, persistNoti.build())

        val sensorManager = service<SensorManager>(Service.SENSOR_SERVICE)
        val heartSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)
        sensorManager.registerListener(sensorListener, heartSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    val sensorListener = object : SensorEventListener {
        override fun onSensorChanged(p0: SensorEvent) {
            publishMetric(p0.values[0])
        }

        override fun onAccuracyChanged(p0: Sensor, p1: Int) {

        }
    }

    private fun reqSensorPermission() {
        when (ContextCompat.checkSelfPermission(
            applicationContext,
            permissionBodySensor
        )) {
            PermissionChecker.PERMISSION_DENIED -> registerForActivityResult(RequestPermission()) {
                if (!it) finishAndRemoveTask()
            }.launch(permissionBodySensor)
            else -> {}
        }
    }

    private fun <T> publishMetric(value: T, adapter: (T) -> String = { it.toString() }) {
        socketProvider.get().send(adapter(value))
    }
}