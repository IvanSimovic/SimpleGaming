plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.symbol.processing)
    id("org.jetbrains.kotlin.kapt")
}

android {
    namespace = "com.simovic.simplegaming"
    compileSdk =
        libs.versions.compile.sdk
            .get()
            .toInt()

    defaultConfig {
        applicationId = "com.simovic.meappsimple"
        minSdk =
            libs.versions.min.sdk
                .get()
                .toInt()
        targetSdk =
            libs.versions.target.sdk
                .get()
                .toInt()
        versionCode = 1
        versionName = "1.0"

        defaultConfig {
            buildConfigField("String", "apiBaseUrl", "${project.findProperty("apiBaseUrl") ?: ""}")
            buildConfigField("String", "apiToken", "${project.findProperty("apiToken") ?: ""}")
        }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        multiDexEnabled = true

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
        compose = true
    }

    packaging {
        resources {
            excludes += "META-INF/AL2.0"
            excludes += "META-INF/licenses/**"
            excludes += "**/attach_hotspot_windows.dll"
            excludes += "META-INF/LGPL2.1"
        }
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
    }
}

dependencies {
    implementation(libs.kotlin.reflect)
    implementation(libs.core.ktx)
    implementation(libs.timber)
    implementation(libs.coroutines)
    implementation(libs.material)
    implementation(libs.compose.material)
    implementation(libs.compose.icons)

    // Compose dependencies
    implementation(platform(libs.compose.bom))
    implementation(libs.tooling.preview)
    implementation(libs.bundles.compose)
    debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.compose.ui.test.manifest)
    implementation(libs.navigation.compose)

    // Room
    implementation(libs.bundles.room)
    ksp(libs.room.compiler)

    // Koin
    implementation(platform(libs.koin.bom))
    implementation(libs.bundles.koin)

    // Retrofit
    implementation(libs.bundles.retrofit)
    implementation(libs.bundles.tikxml)
    kapt(libs.tikxml.compiler)

    // Lifecycle & UI
    implementation(libs.viewmodel.ktx)
    implementation(libs.core.splashscreen)

    // Date picker
    implementation(libs.datepicker)

    // Test
    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.junit.jupiter.engine)
}
