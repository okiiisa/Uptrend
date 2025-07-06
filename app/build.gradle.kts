plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.mainapplicationproject"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.mainapplicationproject"
        minSdk = 24
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation("androidx.compose.material:material-icons-extended:1.6.1")
    implementation(platform("com.google.firebase:firebase-bom:33.15.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth:23.2.1")
    implementation("com.google.firebase:firebase-firestore:25.1.4")
    implementation("com.google.firebase:firebase-bom:33.15.0")
    implementation ("androidx.navigation:navigation-compose:2.7.7")
    implementation("io.coil-kt:coil-compose:2.5.0")
    implementation("androidx.compose.material3:material3:1.2.1")
    implementation ("com.google.firebase:firebase-storage-ktx")
    implementation ("com.google.firebase:firebase-firestore-ktx")
    implementation ("com.github.bumptech.glide:glide:4.15.1")
    implementation ("androidx.activity:activity-ktx:1.9.0")
    implementation ("androidx.compose:compose-bom:2025.06.01")
    implementation ("androidx.compose.material3:material3")
    implementation ("androidx.activity:activity-compose:1.9.0")
    implementation("androidx.compose.material3:material3:1.3.2") // or latest
    implementation ("io.coil-kt:coil-compose:2.4.0") // for image loading




    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

}
