/*
 * Copyright (c) 2020-2025. <mossie@mossoft.nl>
 *
 * This is free software:  you can redistribute it and/or modify it under the terms of the  GNU
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <https://www.gnu.org/
 */

/*
* Make sure JAVA_TOOL_OPTIONS="--enable-native-access=ALL-UNNAMED"  has been set.*
* this prevents the following warning to be shown (in future an error)
*
* WARNING: A restricted method in java.lang.System has been called
* WARNING: java.lang.System::load has been called by com.sun.star.lib.util.NativeLibraryLoader in an unnamed module (file:/usr/lib/libreoffice/program/classes/libreoffice.jar)
* WARNING: Use --enable-native-access=ALL-UNNAMED to avoid a warning for callers in this module
* WARNING: Restricted methods will be blocked in a future release unless native access is enabled
 */

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

plugins {
    java
    jacoco
    id("com.gradleup.shadow") version "9.0.0-beta2"
    id("com.diffplug.spotless") version "7.2.1"
    id("com.github.spotbugs") version "6.2.6"
}

group = "nl.mossoft.lo"
version = "25.0.1.2"

val buildTimestamp: String? = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
val buildVersion = "$version-$buildTimestamp"

val mainClassName = "nl.mossoft.lo.comp.RegistrationHandler"

val assertjCoreVersion = "3.27.4"
val junitPlatformLauncherVersion = "1.13.4"
val junitVersion = "5.13.4"
val libreofficeVersion = "24.2.5"
val log4jVersion = "2.23.1"
val mockitoCoreVersion = "5.2.0"
val slf4jVersion = "2.0.11"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.libreoffice:libreoffice:$libreofficeVersion")
    compileOnly("org.libreoffice:officebean:$libreofficeVersion")

    testImplementation("org.libreoffice:libreoffice:${libreofficeVersion}")
    testImplementation("org.libreoffice:officebean:${libreofficeVersion}")

    testImplementation(platform("org.junit:junit-bom:$junitVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
    testImplementation("org.mockito:mockito-core:$mockitoCoreVersion") // for static mocking
    testImplementation("org.assertj:assertj-core:$assertjCoreVersion")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:$junitPlatformLauncherVersion\"")

    implementation("org.slf4j:slf4j-api:$slf4jVersion")
    implementation("org.apache.logging.log4j:log4j-api:$log4jVersion")
    implementation("org.apache.logging.log4j:log4j-core:$log4jVersion")
    implementation("org.apache.logging.log4j:log4j-slf4j2-impl:$log4jVersion")
    implementation("com.google.guava:guava:33.4.8-jre")
}

spotless {
    java {
        googleJavaFormat()  // Apply Google Java Format
        target("src/**/*.java")  // Specify the Java files to format
    }
}

tasks.register<Exec>("compileIDLfile") {
    group = "build"
    description = "Compiles IDL files into a types.rdb file."

    val idlDir = layout.projectDirectory.dir("src/main/oxt/idl")
    val outputDir = layout.buildDirectory.dir("toArchive").get()

    outputs.dir(outputDir)

    commandLine(
        "/usr/lib/libreoffice/sdk/bin/unoidl-write",
        "/usr/lib64/libreoffice/program/types.rdb",
        "/usr/lib64/libreoffice/program/types/offapi.rdb",
        idlDir, // Input directory for IDL files
        outputDir.file("types.rdb").asFile // Output path for the generated types.rdb
    )
}

tasks.withType<Jar> {
    manifest {
        // Explicitly use mapOf for attributes to avoid type mismatch
        attributes(
            mapOf(
                "RegistrationClassName" to mainClassName, // Use the defined mainClassName
                "Implementation-Title" to "QuranLOAddon",
                // Using 'version' for consistency with OXT naming
                "Implementation-Version" to buildVersion
            )
        )
    }
    from(sourceSets.main.get().allSource) {
        exclude("**/*.java")
        exclude("**/*.xml")
        exclude("**/*.classes")
        exclude("**/*.properties")
    }
}

tasks.shadowJar {
    archiveFileName.set("QuranLOAddon.jar")
}

tasks.register<Zip>("prepareDistributionPackage") {
    group = "build"
    description = "Assembles the LibreOffice extension (.oxt) file."

    archiveFileName.set("QuranLOAddon-${version}.oxt")
    destinationDirectory.set(layout.buildDirectory.dir("dist"))

    dependsOn(
        tasks.named("spotlessJavaApply"),
        tasks.named("shadowJar"),
        tasks.named("compileIDLfile"),
        tasks.named("check")
    )

    from(tasks.shadowJar.get().archiveFile) {
        rename { "QuranLOAddon.jar" }
    }

    from(layout.buildDirectory.dir("toArchive")) {}

    from("src/main/oxt") {
        include("description.xml")
        expand("version" to project.version)
    }

    from("src/main/oxt") {
        exclude("description.xml")
    }

}

tasks.register<Exec>("InstallDistributionPackage") {
    dependsOn("prepareDistributionPackage")
    commandLine(
        "/usr/bin/unopkg",
        "add",
        "--force",
        "--suppress-license",
        layout.buildDirectory.dir("dist/${rootProject.name}-${version}.oxt").get()
    )
}

tasks.build {
    dependsOn(
        "prepareDistributionPackage"
    )
}

tasks.test {
    useJUnitPlatform() // If you're using JUnit 5 (JUnit Platform)
    finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
}
