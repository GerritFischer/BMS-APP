import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.androidApplication)
}

android {
    namespace = "com.example.rubmotorsportgroup2"
    compileSdk = 34


    defaultConfig {
        applicationId = "com.example.rubmotorsportgroup2"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"


        val prop = gradleLocalProperties(rootDir, providers)
        buildConfigField("String", "USERNAME", "\"${prop.getProperty("USERNAME")}\"")
        buildConfigField("String", "PASSWORD", "\"${prop.getProperty("PASSWORD")}\"")
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
    buildFeatures {
        viewBinding = true
        buildConfig = true //needed for readout of username and pw for websocket
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0") //added for charts
    implementation("com.squareup.okhttp3:okhttp:4.10.0") //added for websocket
}