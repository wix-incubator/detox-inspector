plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.gladed.androidgitversion")
}

android {
    namespace = "com.wix.detox.layoutinspectorapp"
    compileSdk = libs.versions.compileSdkVersion.get().toInt()

    defaultConfig {
        applicationId = "com.wix.detox.layoutinspectorapp"
        minSdk = libs.versions.minSdkVersion.get().toInt()
        targetSdk = libs.versions.compileSdkVersion.get().toInt()

        versionCode = androidGitVersion.code()
        versionName = androidGitVersion.name()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(mapOf("path" to ":inspector")))

    implementation(libs.timber)


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifeCycle.runtime.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.google.material)
    implementation(libs.androidx.constraintlayout)

    androidTestImplementation(libs.androidx.junit.ktx)
    testImplementation(libs.junit4)
}