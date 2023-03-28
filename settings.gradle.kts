pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
    plugins {
        id("com.android.application") version "7.4.1"
        id("org.jetbrains.kotlin.android") version "1.8.20-RC2"
    }
}

rootProject.name = "Heartbeat"
include(":heartbeat-handheld")
include(":heartbeat-wearable")
