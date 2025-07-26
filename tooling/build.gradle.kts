plugins {
    java
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation(libs.bundles.commonmark)
    implementation(libs.thymeleaf)
    implementation(libs.slf4j.api)
    runtimeOnly(libs.logback)
    compileOnly(libs.jspecify)
}
