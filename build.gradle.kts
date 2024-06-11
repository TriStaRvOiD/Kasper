// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false

    alias(libs.plugins.org.jetbrains.kotlin.android) apply false

    alias(libs.plugins.compose.compiler) apply false

    alias(libs.plugins.daggerHilt) apply false

    alias(libs.plugins.aboutLibrariesGradlePlugin) apply false

    alias(libs.plugins.googleSecrets) apply false

    alias(libs.plugins.googleKsp) apply false
}