plugins {
    alias(libs.plugins.androidApplication)
    id("com.google.gms.google-services")
}

android {
    namespace = "sg.edu.np.mad.NP_Eats_Team03"
    compileSdk = 34

    defaultConfig {
        applicationId = "sg.edu.np.mad.NP_Eats_Team03"
        minSdk = 24
        targetSdk = 34
        versionCode = 2
        versionName = "2.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
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
    buildFeatures{
        viewBinding = true
    }

}

dependencies {
    // Exclude sceneform-base from core to avoid duplicate classes
    implementation("com.gorisse.thomas.sceneform:sceneform:1.23.0")
    implementation("com.gorisse.thomas.sceneform:core:1.23.0")
    implementation("com.gorisse.thomas.sceneform:ux:1.23.0")
    implementation ("com.google.android.gms:play-services-location:21.3.0")
    // Add mapbox dependency
    implementation("com.mapbox.maps:android:11.5.0")
    implementation("com.mapbox.mapboxsdk:mapbox-sdk-services:7.0.0")
    // Add mapbox navigational tools
    implementation("com.mapbox.navigationcore:android:3.2.0-rc.1")
    implementation("com.mapbox.navigationcore:ui-components:3.2.0-rc.1")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(platform("com.google.firebase:firebase-bom:33.0.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation(libs.firebase.firestore)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}



