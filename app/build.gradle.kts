plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.app"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    flavorDimensionList.add("environment")
    productFlavors {
        create("stage") {
            // Assigns this product flavor to the "version" flavor dimension.
            // If you are using only one dimension, this property is optional,
            // and the plugin automatically assigns all the module's flavors to
            // that dimension.
            dimension = "environment"

            applicationIdSuffix = ".stage"
            versionNameSuffix = "-stage"
        }
        create("prod") {
            dimension = "environment"

            applicationIdSuffix = ".prod"
            versionNameSuffix = "-prod"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

}

dependencies {

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation(project(":fragments"))

    add("prodApi", project(":schema")) {
        capabilities {
            requireCapability("com.schema:prod")
        }
    }

    add("stageApi", project(":schema")) {
        capabilities {
            requireCapability("com.schema:stage")
        }
    }

    add("prodApi", project(":fragments")) {
        capabilities {
            requireCapability("com.fragments:prod")
        }
    }

    add("stageApi", project(":fragments")) {
        capabilities {
            requireCapability("com.fragments:stage")
        }
    }
}