buildscript {
    dependencies { classpath("com.google.gms:google-services:4.3.15") }
}

ext {
    extra["compose_version"] = "2.4.1"
    extra["dagger_hilt_version"] = "2.42"
}

plugins {
    id("com.android.application") version "7.4.2" apply false
    id("com.android.library") version "7.4.2" apply false
    id("org.jetbrains.kotlin.android") version "1.7.0" apply false
    id("com.google.dagger.hilt.android") version "2.42" apply false
}