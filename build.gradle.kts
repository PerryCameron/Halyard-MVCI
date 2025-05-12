import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import java.text.SimpleDateFormat
import java.util.*

plugins {
    application
    java
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("com.palantir.git-version") version "3.1.0" // trying to make sure this is set up and working
}

group = "org.ecsail"

// Retrieve the Git version directly from the project object
//val gitVersion: String by extra { versionDetails().version }
//val gitVersion: groovy.lang.Closure<String> by extra
//version = gitVersion()

val gitVersion: groovy.lang.Closure<String> by extra
version = gitVersion()

application {
    // Main class for the application
    mainClass.set("org.ecsail.BaseApplication")
}

tasks.jar {
    manifest {
        attributes["Implementation-Version"] = project.version
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}

val appVersion: String by project

dependencies {
    // SLF4J and Logback for logging
    implementation("org.slf4j:slf4j-api:2.0.16")
    implementation("ch.qos.logback:logback-classic:1.5.11")
    implementation("ch.qos.logback:logback-core:1.5.11")

    // Apache POI dependencies for Excel handling
    implementation("org.apache.poi:poi:5.2.5")
    implementation("org.apache.poi:poi-ooxml:5.2.5")

    // Log4J bridge to SLF4J
    implementation("org.apache.logging.log4j:log4j-to-slf4j:2.20.0")
    implementation("org.slf4j:jcl-over-slf4j:2.0.5")

//    // MariaDB JDBC Driver
//    implementation("org.mariadb.jdbc:mariadb-java-client:3.2.0")

//    // Spring JDBC
//    implementation("org.springframework:spring-jdbc:6.1.7")

//    // JSON library
//    implementation("org.json:json:20240303")

//    // Apache HTTP Client
//    implementation("org.apache.httpcomponents:httpcore:4.4.15")
//    implementation("org.apache.httpcomponents:httpclient:4.5.14")

//    // JSch for SSH connections
//    implementation("com.github.mwiede:jsch:0.2.7")

    // iText PDF
    implementation("com.itextpdf:itext7-core:7.2.6")
    implementation("com.itextpdf:layout:7.2.6")

    // OkHttp for HTTP requests
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    // JavaMail API
    implementation("javax.mail:mail:1.4.7")

    // Jackson for JSON processing
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.1")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.17.1")

    // testing
    // Use junit-jupiter to ensure aligned versions
    testImplementation(platform("org.junit:junit-bom:5.10.0")) // Use the latest stable JUnit BOM
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.mockito:mockito-core:5.17.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.17.0")
    // For JavaFX tests
    testImplementation("org.testfx:testfx-junit5:4.0.18")
    testImplementation("org.testfx:testfx-core:4.0.18")
    testImplementation("org.openjfx:javafx-controls:17")
    testImplementation("org.openjfx:javafx-graphics:17")

}

tasks.withType<JavaCompile> {
    options.compilerArgs.addAll(listOf("-Xlint:deprecation", "-Xlint:unchecked"))
}

// Create the fat JAR using shadowJar
tasks.withType<ShadowJar> {
    archiveBaseName.set("Halyard")  // Set the base name of the JAR
    archiveVersion.set("")          // Remove version from file name
    archiveClassifier.set("all")    // Add "all" to indicate a fat JAR
    manifest {
        attributes["Main-Class"] = "org.ecsail.BaseApplication"
    }
}

tasks.jar {
    manifest {
        archiveBaseName.set("Halyard")
        archiveVersion.set("")
        archiveClassifier.set("")
        attributes["Main-Class"] = "org.ecsail.BaseApplication"
    }
}

// Create the runtime using `jlink`
tasks.register<Exec>("generateRuntime") {
    group = "build"
    description = "Generates a custom Java runtime image using jlink"

    doFirst {
        delete(file("build/runtime"))
    }

    commandLine(
        "C:/Users/sesa91827/.jdks/jdk-21.0.6-full/bin/jlink.exe",  // Updated path
        "--module-path", "C:/Users/sesa91827/.jdks/jdk-21.0.6-full/jmods",
        "--add-modules",
        "java.base,java.desktop,java.prefs,java.sql.rowset,javafx.controls,javafx.fxml,javafx.media,java.net.http,jdk.crypto.ec,jdk.crypto.cryptoki",
        "--output",
        "build/runtime",
        "--strip-debug",
        "--compress",
        "2",
        "--no-header-files",
        "--no-man-pages"
    )
}

tasks.register<Exec>("packageApp") {
    group = "build"
    description = "Packages the application with a bundled JRE using jpackage"

    doFirst {
        delete(file("build/jpackage/TSENotes"))
    }

    // Directly point to the jpackage tool in your Java 21 SDK
    commandLine(
        "C:/Users/sesa91827/.jdks/jdk-21.0.6-full/bin/jpackage",  // Path to your jpackage
        "--input",
        "build/libs",  // Path to the JAR file directory
        "--main-jar",
        "Halyard-all.jar",  // Replace with your actual JAR name
        "--main-class",
        "org.ecsail.BaseApplication",  // Replace with your actual main class
        "--name",
        "Halyard",  // Name of the app
        "--type",
        "app-image",  // You can also use pkg, dmg, exe, etc.
        "--runtime-image",
        "C:/Users/sesa91827/.jdks/jdk-21.0.6-full",  // Path to Java 21 runtime image
        "--dest",
        "build/jpackage",  // Output destination
        "--icon",
        "src/main/resources/images/app-icon.ico"  // Optional: Path to your app's icon
    )
}

// Package the macOS app image using jpackage
tasks.register<Exec>("packageAppMac") {
    group = "build"
    description = "Packages the application with a bundled JRE for macOS using jpackage"

    val sanitizedVersion = sanitizeVersion(project.version.toString())  // For jpackage compliance
    val fileNameVersion = fileNameVersion(project.version.toString())  // Full version for the final filename

    doFirst {
        delete(file("build/jpackage/Halyard"))
    }

    commandLine(
        "/Users/parrishcameron/.sdkman/candidates/java/17.0.11.fx-librca/bin/jpackage",
        "--input", "build/libs",  // Path to the JAR file directory
        "--main-jar", "Halyard-all.jar",  // Name of the fat JAR
        "--main-class", "org.ecsail.BaseApplication",  // Main application class
        "--name", "Halyard",  // Name of the app
        "--type", "dmg",  // Type of package: "dmg" for macOS
        "--runtime-image", "build/runtime",  // Path to custom runtime image
        "--app-version", sanitizedVersion,  // Sanitized version
        "--dest", "build/jpackage",  // Output directory
        "--icon", "src/main/resources/images/app-icon.icns"  // macOS .icns icon
    )

    doLast {
        val dmgFile = file("build/jpackage/Halyard-$sanitizedVersion.dmg")
        val renamedFile = file("build/jpackage/Halyard-$fileNameVersion.dmg")
        if (dmgFile.exists()) {
            dmgFile.renameTo(renamedFile)
            println("Packaged application: ${renamedFile.name}")
        } else {
            println("DMG file not found!")
        }
    }

    doLast {
        println("Packaged Halyard version: $sanitizedVersion")
    }
}

fun sanitizeVersion(version: String): String {
    val regex = Regex("""(\d+)\.(\d+)\.(\d+)""") // Matches the first valid X.Y.Z format
    return regex.find(version)?.value ?: "1.0.0" // Default to "1.0.0" if no match
}

fun fileNameVersion(version: String): String {
    val regex = Regex("""(\d+)\.(\d+)\.(\d+)-(\d+)""") // Matches X.Y.Z-x format
    return regex.find(version)?.value ?: "1.0.0-0" // Default to "1.0.0-0" if no match
}

// Task for Windows (adjusted for app name "Halyard")
tasks.register<Exec>("packageAppWindows") {
    group = "build"
    description = "Packages the application as a Windows executable"

    doFirst {
        delete(file("build/jpackage/HalyardInstaller"))
    }

    commandLine(
        "/Users/parrishcameron/.sdkman/candidates/java/17.0.11.fx-librca/bin/jpackage",
        "--input", "build/libs",
        "--main-jar", "Halyard-all.jar",
        "--main-class", "org.ecsail.BaseApplication",
        "--name", "Halyard",
        "--type", "exe",
        "--runtime-image", "/Users/parrishcameron/.sdkman/candidates/java/17.0.11.fx-librca",
        "--dest", "build/jpackage/HalyardInstaller",
        "--install-dir", System.getenv("UserProfile") + "/Halyard",
        "--icon", "src/main/resources/images/app-icon.ico",
        "--win-menu",
        "--win-shortcut",
        "--win-console"
    )
}

tasks.register("generateBuildInfoProperties") {
    doLast {
        // Create timestamp
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val buildTimestamp = dateFormat.format(Date())
        val javaVersion = System.getProperty("java.version") ?: "Unknown"

        // Generate the properties file
        val propertiesFile = file("src/main/resources/version.properties")
        propertiesFile.parentFile.mkdirs()
        propertiesFile.writeText(
            """
           version=${project.version}
           build.timestamp=$buildTimestamp
           java.version=$javaVersion
       """.trimIndent()
        )
    }
}

// Ensure build info is generated during the build
tasks.processResources {
    dependsOn("generateBuildInfoProperties")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
        showStandardStreams = true
        showExceptions = true
        showStackTraces = true
    }
    jvmArgs("--add-modules", "javafx.controls,javafx.graphics")
    systemProperty("javafx.headless", "true")
}

// Ensure runtime is generated before packaging
tasks.named("packageAppMac") {
    dependsOn("generateRuntime")
}