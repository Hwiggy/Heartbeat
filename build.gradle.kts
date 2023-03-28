subprojects {
    repositories {
        mavenCentral()
        google()
    }

    buildscript {
        repositories {
            mavenCentral()
            google()
        }
        dependencies {
            classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.10")
        }
    }
}

group = "me.hwiggy"
version = "1.0-SNAPSHOT"