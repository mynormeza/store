object Versions {
    const val androidMinSdkVersion = 24
    const val androidTargetSdkVersion = 32
    const val androidCompileSdkVersion = 32

    const val material = "1.6.0"
    const val androidCore = "1.7.0"
    const val androidCompat = "1.4.1"
    const val constraintLayout = "2.1.3"
    const val navigation = "2.4.2"
    const val hilt = "2.40.5"

    const val androidLifecycle = "2.4.0"
    const val coroutines = "1.6.0"
    const val retrofit = "2.9.0"
    const val interceptor = "4.9.3"
    const val arrow = "1.0.1"
    const val junitVersion = "4.13.2"
    const val junitExtVersion = "1.1.3"
    const val espressoVersion = "3.4.0"

}

object Dependencies {
    const val material = "com.google.android.material:material:${Versions.material}"
    const val androidCore = "androidx.core:core-ktx:${Versions.androidCore}"
    const val androidCompat = "androidx.appcompat:appcompat:${Versions.androidCompat}"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val navigationUi = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"
    const val navigationFragment = "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    const val junit = "junit:junit:${Versions.junitVersion}"
    const val junitExt = "androidx.test.ext:junit:${Versions.junitExtVersion}"
    const val espressoCore = "androidx.test.espresso:espresso-core:${Versions.espressoVersion}"

    const val hilt = "com.google.dagger:hilt-android:${Versions.hilt}"
    const val hiltAndroidCompiler = "com.google.dagger:hilt-android-compiler:${Versions.hilt}"
    const val lifecycleViewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.androidLifecycle}"
    const val coroutinesCore ="org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    const val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
    const val gsonConverter =  "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
    const val retrofit =  "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val interceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.interceptor}"
    const val arrow ="io.arrow-kt:arrow-core:${Versions.arrow}"
    const val liveData = "androidx.lifecycle:lifecycle-livedata:${Versions.androidLifecycle}"
}

