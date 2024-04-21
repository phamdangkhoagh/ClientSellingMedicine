plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.clientsellingmedicine"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.clientsellingmedicine"
        minSdk = 24
        targetSdk = 34
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

}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("androidx.cardview:cardview:1.0.0")
    // Retrofit and GSON
    implementation ("com.squareup.retrofit2:retrofit:2.3.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.3.0")
    //Jsoup
    implementation ("org.jsoup:jsoup:1.14.3")

    // Logging
    implementation ("com.squareup.okhttp3:logging-interceptor:3.9.0")
    // load image
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    //slider
    implementation ("com.github.denzcoskun:ImageSlideshow:0.1.2")
    //lombok
    implementation ("org.projectlombok:lombok:1.18.20")
    annotationProcessor ("org.projectlombok:lombok:1.18.20")

    implementation ("androidx.activity:activity-ktx:1.4.0")
    //webview
    implementation("androidx.webkit:webkit:1.8.0")

    //cloudinary
    implementation ("com.cloudinary:cloudinary-android:2.7.1")
    //Oauth google
    implementation ("com.google.android.gms:play-services-auth:20.6.0")
}