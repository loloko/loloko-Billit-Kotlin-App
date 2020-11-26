object Versions {
    const val kotlinVersion = "1.4.10"
    const val gradleVersion = "4.1.1"
    const val googleServicesVersion = "4.3.4"

    // Implementation
    const val kotlinStdVersion = "1.4.10"
    const val kotlinCoreVersion = "1.3.2"
    const val appCompactVersion = "1.2.0"
    const val googleMaterialVersion = "1.2.1"
    const val constraintLayoutVersion = "2.0.4"
    const val recyclerviewSelectionVersion = "1.0.0"
    const val coroutinesCoreVersion = "1.4.1"
    const val coroutinesPlayServicesVersion = "1.1.1"
    const val navigationVersion = "2.3.1"
    const val coilVersion = "0.11.0"
    const val gsonVersion = "2.8.6"
    const val facebookAuthVersion = "5.15.3"
    const val googleAuthVersion = "19.0.0"
    const val rxJavaVersion = "2.1.1"
    const val daggerVersion = "2.29.1"
    const val lifecycleVersion = "2.2.0"

    // Test Implementation
    const val jUnitVersion = "4.13.1"
    const val mockkVersion = "1.10.2"
    const val mockitoVersion = "3.3.3"
    const val archCoreVersion = "2.1.0"
    const val testTruthVersion = "1.3.1-alpha02"
    const val coroutinesTestVersion = "1.4.1"

    // Android Test Implementation
    const val espressoVersion = "3.3.0"
    const val testRulesVersion = "1.3.0"
    const val testRunnerVersion = "1.3.0"
    const val extJUnitVersion = "1.1.2"
}

object Dependencies {

    val implementations = listOf(
        "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlinStdVersion}",
        "androidx.core:core-ktx:${Versions.kotlinCoreVersion}",
        "androidx.appcompat:appcompat:${Versions.appCompactVersion}",
        "com.google.android.material:material:${Versions.googleMaterialVersion}",
        "androidx.constraintlayout:constraintlayout:${Versions.constraintLayoutVersion}",
        "androidx.recyclerview:recyclerview-selection:${Versions.recyclerviewSelectionVersion}",

        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutinesCoreVersion}",
        "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:${Versions.coroutinesPlayServicesVersion}",

        "androidx.navigation:navigation-fragment-ktx:${Versions.navigationVersion}",
        "androidx.navigation:navigation-ui-ktx:${Versions.navigationVersion}",

        "io.coil-kt:coil:${Versions.coilVersion}",
        "com.google.code.gson:gson:${Versions.gsonVersion}",

        "com.facebook.android:facebook-login:${Versions.facebookAuthVersion}",
        "com.google.android.gms:play-services-auth:${Versions.googleAuthVersion}",

        "com.google.firebase:firebase-analytics-ktx",
        "com.google.firebase:firebase-auth-ktx",
        "com.google.firebase:firebase-storage-ktx",
        "com.google.firebase:firebase-firestore-ktx",

        "io.reactivex.rxjava2:rxandroid:${Versions.rxJavaVersion}",

        "com.google.dagger:dagger:${Versions.daggerVersion}",
        "com.google.dagger:dagger-compiler:${Versions.daggerVersion}",
        "com.google.dagger:dagger-android:${Versions.daggerVersion}",
        "com.google.dagger:dagger-android-support:${Versions.daggerVersion}",

        "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycleVersion}",
        "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycleVersion}",
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycleVersion}"

    )

    val kapt = listOf(
        "com.google.dagger:dagger-compiler:${Versions.daggerVersion}",
        "com.google.dagger:dagger-android-processor:${Versions.daggerVersion}"
    )

    val testImplementations = listOf(
        "junit:junit:${Versions.jUnitVersion}",
        "io.mockk:mockk:${Versions.mockkVersion}",
        "org.mockito:mockito-inline:${Versions.mockitoVersion}",
        "androidx.arch.core:core-testing:${Versions.archCoreVersion}",
        "androidx.test.ext:truth:${Versions.testTruthVersion}",
        "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutinesTestVersion}"
    )

    val androidTestImplementation = listOf(
        "androidx.test.espresso:espresso-core:${Versions.espressoVersion}",
        "androidx.test:rules:${Versions.testRulesVersion}",
        "androidx.test:runner:${Versions.testRunnerVersion}",
        "androidx.test.ext:junit:${Versions.extJUnitVersion}"
    )
}