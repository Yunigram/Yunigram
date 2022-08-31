plugins {
    id("com.android.application") version "7.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.7.10" apply false
    id("com.google.gms.google-services") version "4.3.10" apply false
}

val clean by tasks.registering {
    delete(rootProject.buildDir)
}
