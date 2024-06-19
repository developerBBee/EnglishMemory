// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    val hiltVersion = "2.49"
    id("com.android.application") version "8.4.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.10" apply false
    id("com.google.dagger.hilt.android") version hiltVersion apply false
    id("com.google.gms.google-services") version "4.4.0" apply false
    id("com.google.firebase.crashlytics") version "2.9.9" apply false
    id("com.google.devtools.ksp") version "1.9.10-1.0.13" apply false
}