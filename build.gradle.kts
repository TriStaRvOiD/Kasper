// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    //Android
    id("com.android.application") version "8.0.0" apply false
    id("com.android.library") version "8.0.0" apply false

    //Kotlin
    id("org.jetbrains.kotlin.android") version "1.8.20" apply false

    //AboutLibraries
    id("com.mikepenz.aboutlibraries.plugin") version "10.6.2" apply false

    //Hilt
    id("com.google.dagger.hilt.android") version "2.44" apply false

    //Key hider
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version "2.0.1" apply false

    //Annotation Processing
    id("org.jetbrains.kotlin.kapt") version "1.8.20" //kapt
    id("com.google.devtools.ksp") version "1.8.20-1.0.10" apply false //ksp
}