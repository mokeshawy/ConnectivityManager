// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    dependencies {
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.57.2")
        classpath("com.android.tools.build:gradle:8.13.2")
    }
}

plugins {
    id("com.android.application") version "8.13.2" apply false
    id("com.android.library") version "8.13.2" apply false
    id("org.jetbrains.kotlin.android") version "2.2.0" apply false
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}