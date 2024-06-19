import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.gms)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.ksp)
}

android {
    namespace = "jp.developer.bbee.englishmemory"
    compileSdk = 34

    val properties = runCatching {
        Properties().apply { load(rootProject.file("local.properties").inputStream()) }
    }.onFailure {
        println("The local.properties file was not found, but there is no problem with CI.\n${it.message}")
    }.getOrDefault(Properties())

    defaultConfig {
        applicationId = "jp.developer.bbee.englishmemory"
        minSdk = 26
        targetSdk = 34
        versionCode = 3
        versionName = "1.0.0"

        val baseUrl = getProp("AWS_BASE_URL", "baseUrl", properties)
        buildConfigField("String", "BASE_URL", "\"$baseUrl\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }

    // Encapsulates signing configurations.
    signingConfigs {
        // Creates a signing configuration called "release".
        create("release") {
            // Specifies the path to your keystore file.
            storeFile = file("release.keystore")
            // Specifies the password for your keystore.
            storePassword = getProp("KEYSTORE_PASSWORD", "propStorePassword", properties)
            // Specifies the identifying name for your key.
            keyAlias = getProp("KEY_ALIAS", "propKeyAlias", properties)
            // Specifies the password for your key.
            keyPassword = getProp("KEY_PASSWORD", "propKeyPassword", properties)
        }
    }
    buildTypes {
        release {
            // Enables code shrinking, obfuscation, and optimization for only
            // your project's release build type.
            isMinifyEnabled = true

            // Enables resource shrinking, which is performed by the
            // Android Gradle plugin.
            isShrinkResources = true

            // Includes the default ProGuard rules files that are packaged with
            // the Android Gradle plugin. To learn more, go to the section about
            // R8 configuration files.
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    // for GitHub Actions
    applicationVariants.all {
        val dir = projectDir
        val file = File(dir, "google-services.json")
        if (!file.exists()) {
            file.writeText(System.getenv("GOOGLE_SERVICES_JSON") ?: "")
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.accompanist.systemuicontroller)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics.ktx)
    implementation(libs.firebase.analytics.ktx)
    implementation(libs.firebase.auth.ktx)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.moshi)
    implementation(libs.moshi.kotlin)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.hilt.navigation.compose)

    // Room
    implementation(libs.room.runtime)
    ksp(libs.room.compiler)
    implementation(libs.room.ktx)

    // Navigation
    implementation(libs.navigation.compose)

    // DataStore
    implementation(libs.datastore.preferences)

    testImplementation(libs.junit)

    // Coroutine test
    testImplementation(libs.kotlinx.coroutines.test)

    // Mockito
    testImplementation (libs.mockito.kotlin)

    // Truth
    testImplementation(libs.truth)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

fun getProp(actionsKey: String, localKey: String, localProperties: Properties): String =
    System.getenv(actionsKey) ?: run { localProperties.getProperty(localKey) }
    ?: throw IllegalArgumentException("No $actionsKey or $localKey found")