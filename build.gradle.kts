// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    dependencies {
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.43.1")
        classpath("com.google.gms:google-services:4.3.13")
        classpath("com.android.tools.build:gradle:7.4.2")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.1")
    }
}

plugins {
    id("com.android.application") version "7.4.2" apply false
    id("com.android.library") version "7.4.2" apply false
    id("org.jetbrains.kotlin.android") version "1.7.0" apply false
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}