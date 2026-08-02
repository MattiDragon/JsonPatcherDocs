plugins {
    java
}

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://jitpack.io")
}

dependencies {
    implementation(libs.jsonpatcher.tools.docs)
    implementation(libs.bundles.commonmark)
    implementation(libs.thymeleaf)
    implementation(libs.slf4j.api)
    runtimeOnly(libs.logback)
    compileOnly(libs.jspecify)
}

tasks.withType<JavaCompile> {
    options.release = 25
}
