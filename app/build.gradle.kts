plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("androidx.navigation.safeargs.kotlin")
    id("jacoco")
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

    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")

    implementation("androidx.activity:activity-ktx:1.10.0")
    implementation("androidx.fragment:fragment-ktx:1.8.5")

    // Room
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-android-compiler:2.51.1")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")

    // Navigation Components
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.6")
    implementation("androidx.navigation:navigation-ui-ktx:2.8.6")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.13.0")
    kapt("com.github.bumptech.glide:compiler:4.13.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")

    implementation("androidx.test:core:1.6.1")
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.hamcrest:hamcrest-all:1.3")
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    testImplementation("org.robolectric:robolectric:4.3.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1")
    testImplementation("com.google.truth:truth:1.1.3")
    testImplementation("org.mockito:mockito-core:5.4.0")


    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation("junit:junit:4.13.2")
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1")
    androidTestImplementation("androidx.arch.core:core-testing:2.2.0")
    androidTestImplementation("com.google.truth:truth:1.1.3")
    androidTestImplementation("org.mockito:mockito-android:5.4.0")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.6.1")
    androidTestImplementation("com.squareup.okhttp3:mockwebserver:4.9.0")
    androidTestImplementation("com.jakewharton.espresso:okhttp3-idling-resource:1.0.0")
    androidTestImplementation("androidx.test.espresso:espresso-idling-resource:3.6.1")
    androidTestImplementation("androidx.navigation:navigation-testing:2.8.6")
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.38.1")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.51.1")

    debugImplementation("androidx.fragment:fragment-testing:1.8.5")
    debugImplementation("androidx.test:monitor:1.7.2")
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