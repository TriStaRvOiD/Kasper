// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    //Android
    id("com.android.application") version "8.2.2" apply false

    //Kotlin
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false

    //AboutLibraries
    id("com.mikepenz.aboutlibraries.plugin") version "10.10.0" apply false

    //Hilt
    id("com.google.dagger.hilt.android") version "2.50" apply false

    //Key hider
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version "2.0.1" apply false

    //Annotation Processing
    id("com.google.devtools.ksp") version "1.9.22-1.0.17" apply false //ksp
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}