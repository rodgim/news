plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.parcelize)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.navigation.safeargs.kotlin )
    alias(libs.plugins.jacoco)
}

val newsApiKey: String by project
val baseUrl: String by project

android {
    namespace = "com.rodgim.news"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.rodgim.news"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        buildConfigField("String", "API_KEY", newsApiKey)
        buildConfigField("String", "BASE_URL", baseUrl)

        testInstrumentationRunner = "com.rodgim.news.HiltTestRunner"
    }

    buildTypes {
        getByName("debug") {
            resValue("string", "clear_text_config","true")
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
        }
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
            resValue("string", "clear_text_config","false")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    sourceSets {
        getByName("main") {
            assets.srcDirs("src/main/assets", "src/androidTest/assets")
        }
    }

    tasks.withType<Test>().configureEach {
        extensions.configure(JacocoTaskExtension::class.java) {
            isIncludeNoLocationClasses = true
            excludes = listOf("jdk.internal.*")
        }
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
        unitTests.isIncludeAndroidResources = true
    }

    testCoverage.jacocoVersion = "0.8.10"
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)

    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.fragment.ktx)

    // Room
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.okhttp)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)

    // Navigation Components
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // Glide
    implementation(libs.glide)
    ksp(libs.compiler)

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)

    implementation(libs.androidx.core)
    testImplementation(libs.junit)
    testImplementation(libs.hamcrest.all)
    testImplementation(libs.androidx.core.testing)
    testImplementation(libs.robolectric)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.truth)
    testImplementation(libs.mockito.core)


    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.core.testing)
    androidTestImplementation(libs.truth)
    androidTestImplementation(libs.mockito.android)
    androidTestImplementation(libs.androidx.espresso.contrib)
    androidTestImplementation(libs.mockwebserver)
    androidTestImplementation(libs.okhttp3.idling.resource)
    androidTestImplementation(libs.androidx.espresso.idling.resource)
    androidTestImplementation(libs.androidx.navigation.testing)
    androidTestImplementation(libs.hilt.android.testing)
    kspAndroidTest(libs.hilt.android.compiler)

    debugImplementation(libs.androidx.fragment.testing)
    debugImplementation(libs.androidx.monitor)
}

tasks.register<JacocoReport>("jacocoTestReport") {
    dependsOn("testDebugUnitTest")
    group = "Reporting"
    description = "Generate Jacoco coverage reports after running tests."

    val androidExclusion = listOf(
            "**/databinding/**/*.*",
            "**/android/databinding/*Binding.*",
            "**/BR.*",
            "**/R.*",
            "**/R$*.*",
            "**/BuildConfig.*",
            "**/Manifest*.*",
            "**/*_MembersInjector.*",
            "**/Dagger*Component.*",
            "**/*Hilt*",
            "**/*hilt*",
            "**/*Impl*",
            "**/*Module_*Factory.*",
            "**/*Fragment*.*",
            "**/*Activity*.*",
            "**/*Adapter*.*",
            "**/*ViewPager*.*",
            "**/*ViewHolder*.*",
            "**/*Module*.*",
            "**/*Interceptor*.*",
            "**/*Database*.*"
    )

    reports {
        xml.required.set(true)
        html.required.set(true)
    }
    classDirectories.setFrom(
        files(
            fileTree(
                // Java generated classes (debug build)
                "build/intermediates/javac/debug/classes"
            ) {
                exclude(androidExclusion)
            },
            fileTree(
                // Kotlin generated classes (debug build)
                "build/tmp/kotlin-classes/debug"
            ) {
                exclude(androidExclusion)
            }
        )
    )
    sourceDirectories.setFrom(files("src/main/java/"))
    executionData.setFrom(files("build/outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec"))
}

configurations.configureEach {
    resolutionStrategy {
        eachDependency {
            if ("org.jacoco" == requested.group) {
                useVersion("0.8.10")
            }
        }
    }
}