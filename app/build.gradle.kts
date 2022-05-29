plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs.kotlin")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("org.jlleitschuh.gradle.ktlint") version "10.2.0"
    id("com.google.gms.google-services")
}

android {
    compileSdk = Versions.androidCompileSdkVersion

    defaultConfig {
        applicationId = "com.example.store"
        minSdk = Versions.androidMinSdkVersion
        targetSdk = Versions.androidTargetSdkVersion
        versionCode = 1
        versionName = "1.0"
        vectorDrawables.useSupportLibrary = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("debug") {
            buildConfigField("String", "apiUrl", "\"https://gist.githubusercontent.com\"")
        }

        getByName("release") {
            buildConfigField("String", "apiUrl", "\"https://gist.githubusercontent.com\"")

            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(Dependencies.androidCore)
    implementation(Dependencies.androidCompat)
    implementation(Dependencies.material)
    implementation(Dependencies.constraintLayout)
    implementation(Dependencies.navigationFragment)
    implementation(Dependencies.navigationUi)
    implementation(Dependencies.lifecycleViewModel)
    implementation(Dependencies.coroutinesCore)
    implementation(Dependencies.coroutinesAndroid)
    implementation(Dependencies.retrofit)
    implementation(Dependencies.interceptor)
    implementation(Dependencies.gsonConverter)
    implementation(Dependencies.liveData)
    implementation(Dependencies.arrow)
    kapt(Dependencies.hiltAndroidCompiler)
    implementation(Dependencies.hilt)
    implementation(platform("com.google.firebase:firebase-bom:30.0.0"))

    // Declare the dependencies for the Remote Config and Analytics libraries
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-config-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")

    testImplementation(Dependencies.junit)
    androidTestImplementation(Dependencies.junitExt)
    androidTestImplementation(Dependencies.espressoCore)
    testImplementation(Dependencies.mockk)
    testImplementation(Dependencies.kluent)
    testImplementation(Dependencies.robolectric)
}
