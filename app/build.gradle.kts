plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.rentify"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.example.rentify"
        minSdk = 24
        targetSdk = 36
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

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    // RecyclerView
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // Material Design (Bottom Navigation, CardView, dll)
    implementation("com.google.android.material:material:1.11.0")

    // Navigation Component (Bottom Nav)
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.6")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.6")

    // Glide (untuk load gambar di RecyclerView)
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // Shimmer loading effect (opsional tapi keren)
    implementation("com.facebook.shimmer:shimmer:0.5.0")
}