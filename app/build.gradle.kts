plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.tawsila"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.tawsila"
        minSdk = 22
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

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
    kotlinOptions {
        jvmTarget = "1.8"
    }
}
dependencies {
    implementation ("androidx.core:core-ktx:1.7.0") // Use the correct version
    implementation ("androidx.appcompat:appcompat:1.4.0") // Use the correct version
    implementation ("com.google.android.material:material:1.5.0") // Use the correct version
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.core:core-ktx:+")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.0.0") // Keep this version
    testImplementation ("junit:junit:4.13.2") // Keep this version
    androidTestImplementation ("androidx.test.ext:junit:1.1.5") // Keep this version
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.1") // Keep this version
    implementation ("com.google.android.material:material:1.1.0")
}
