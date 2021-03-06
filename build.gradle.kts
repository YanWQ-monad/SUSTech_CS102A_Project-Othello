import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.31"
    id("org.jetbrains.compose") version "1.0.0"
}

group = "com.monadx.othello"
version = "1.0"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(compose.desktop.currentOs)

    @kotlin.OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
    api(compose.material3)

    api("org.apache.logging.log4j:log4j-core:2.15.0")
    api("org.apache.logging.log4j:log4j-api:2.15.0")
}

sourceSets.main {
    java.srcDirs("src/main/java", "src/main/kotlin")
}

tasks.test {
    useJUnitPlatform()

    // to access private field of `java.io.PipedInputStream` in `ThreadPipedInputStream`
    jvmArgs("--add-opens", "java.base/java.io=ALL-UNNAMED")
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}

tasks.withType<Jar> {
    // specify duplicate handling strategy explicitly
    // see: https://youtrack.jetbrains.com/issue/KT-46165
    duplicatesStrategy = DuplicatesStrategy.INCLUDE

    manifest {
        attributes["Main-Class"] = "com.monadx.othello.MainKt"
    }
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
}

compose.desktop {
    application {
        mainClass = "com.monadx.othello.MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.monadx.othello"
            packageVersion = "1.0.0"
        }
    }
}