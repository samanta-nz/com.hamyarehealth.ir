plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

// مقدارهای Appwrite از ریشه‌ی پروژه می‌آیند (local.properties / متغیر محیطی / ‑P).
// اگر projectId خالی باشد، اپ در «حالت محلی» بالا می‌آید و چیزی به سرور نمی‌فرستد.
val appwriteEndpoint = findProperty("resolvedAppwriteEndpoint") as? String
    ?: "https://sgp.cloud.appwrite.io/v1"
val appwriteProjectId = findProperty("resolvedAppwriteProjectId") as? String ?: "6abb134a002025222005"
val appwriteDatabaseId = findProperty("resolvedAppwriteDatabaseId") as? String ?: "6abb238d000d05730d10"
val googleWebClientId = findProperty("resolvedGoogleWebClientId") as? String ?: ""

android {
    namespace = "com.hamyarehealth.ir"

    // امضای ثابت (keystore در ریشه‌ی ریپو؛ PKCS12) — تا APK هر ران CI امضای
    // یکسان داشته باشد و نصب روی نسخه‌ی قبلی همیشه «آپدیت» بماند.
    signingConfigs {
        getByName("debug") {
            storeFile = rootProject.file("debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
            storeType = "pkcs12"
        }
    }

    compileSdk = 37

    defaultConfig {
        // بستهٔ اختصاصی «همیار سلامتی من» — کاملاً جدا از com.hamyareman.*
        applicationId = "com.hamyarehealth.ir"
        minSdk = 29
        targetSdk = 36
        // نسخهٔ مستقل این اپ (کانال انتشارِ خودش).
        versionCode = (findProperty("healthVersionCode") as? String)?.toIntOrNull() ?: 2
        versionName = (findProperty("healthVersionName") as? String)?.trim()?.takeIf { it.isNotEmpty() } ?: "1.0.1"
        ndk {
            // فقط معماری‌های واقعیِ گوشی (هم‌قرارداد با همیار من).
            abiFilters += listOf("arm64-v8a", "armeabi-v7a")
        }
        buildConfigField("String", "APPWRITE_ENDPOINT", "\"$appwriteEndpoint\"")
        buildConfigField("String", "APPWRITE_PROJECT_ID", "\"$appwriteProjectId\"")
        manifestPlaceholders["appwriteProjectId"] = appwriteProjectId
        buildConfigField("String", "APPWRITE_PROJECT_NAME", "\"همیار سلامتی من\"")
        buildConfigField("String", "APPWRITE_DATABASE_ID", "\"$appwriteDatabaseId\"")
        buildConfigField("String", "GOOGLE_WEB_CLIENT_ID", "\"$googleWebClientId\"")

        // ------------------------------------------------------------------
        // هویت این APK: یک نسخهٔ واحد (بدون flavor) برای اپ سلامتی.
        //   GRADE_ID همان «قرارداد سرور» برای قفل ایمیل/دستگاه است و عمداً
        //   دست‌نخورده می‌ماند تا ورود/ثبت‌نام کاربران فعلی همیار من مسدود نشود.
        //   APP_ID نشان می‌دهد این نشست از کدام اپ آمده است (همیار سلامتی من).
        // ------------------------------------------------------------------
        buildConfigField("int", "GRADE_NUM", "9")
        buildConfigField("String", "GRADE_ID", "\"grade9\"")
        buildConfigField("String", "GRADE_FA_SHORT", "\"سلامتی\"")
        buildConfigField("String", "GRADE_FA_NUMERAL", "\"۹\"")
        buildConfigField("String", "BOOKS_FOLDER", "\"Base-09\"")
        buildConfigField("String", "EDUCATION_STAGE_ID", "\"stage2\"")
        buildConfigField("String", "EDUCATION_STAGE_FA", "\"متوسطه اول\"")
        buildConfigField("String", "CONTENT_PROFILE_ID", "\"health\"")
        buildConfigField("String", "APP_ID", "\"com.hamyarehealth.ir\"")
        buildConfigField("String", "APP_TITLE", "\"همیار سلامتی من\"")
        // کانال آپدیت مستقل: ردیف Appwrite مخصوص همین اپ.
        buildConfigField("String", "UPDATE_ROW_ID", "\"app_release_health\"")
        resValue("string", "app_name", "همیار سلامتی من")
    }

    buildTypes {
        debug {
            // بدون applicationIdSuffix: یک Platform در کنسول Appwrite کافی است.
        }
        release {
            // امضا با همان کلیدِ داخلِ ریپو (قرارداد پروژه).
            signingConfig = signingConfigs.getByName("debug")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
        buildConfig = true
        resValues = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/DEPENDENCIES"
        }
    }

    lint {
        disable += "UnsafeOptInUsageError"
        disable += "JavascriptInterface"
    }
}

// AGP 9: پلاگین Kotlin Android حذف شده (Kotlin داخلی است).
kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(libs.coil.compose)
    implementation(libs.androidx.biometric)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.work.runtime)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(project(":core-common"))
    implementation(project(":core-designsystem"))
    implementation(project(":core-appwrite"))
    implementation(project(":core-security"))
    implementation(project(":core-notifications"))
    implementation(project(":feature-pairing"))
    implementation(project(":feature-hearttoheart"))
    implementation(project(":feature-calls"))
    implementation(project(":feature-playback"))
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.ui)
    implementation(project(":feature-study"))
    debugImplementation(libs.androidx.compose.ui.tooling)
    testImplementation(libs.junit)
    implementation(project(":core-sync"))
}
