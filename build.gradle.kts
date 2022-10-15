plugins {
    id("java")
}

group = "nl.mossoft.lo"
version = "3.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.libreoffice:officebean:7.4.1")
    implementation("org.libreoffice:libreoffice:7.4.1")
}

tasks.withType<Jar> {
    manifest {
        attributes["RegistrationClassName"] = "nl.mossoft.lo.comp.RegistrationHandler"
    }
    from(sourceSets["main"].allSource) {
        exclude("**/*.java")
        exclude("**/*.properties")
    }
}

tasks.register<Copy>("copyRegistrationFilesForDeploy") {
    from(layout.projectDirectory.dir("registry"))
    into(layout.buildDirectory.dir("toArchive/registry"))

}
tasks.register<Copy>("copyDescriptionDirForDeploy") {
    from(layout.projectDirectory.dir("description"))
    include("*.txt")
    into(layout.buildDirectory.dir("toArchive/description"))
}

tasks.register<Copy>("copyDescriptionXMLForDeploy") {
    from(layout.projectDirectory.dir("description"))
    include("*.xml")
    into(layout.buildDirectory.dir("toArchive"))
}

tasks.register<Copy>("copyIDLDirForDeploy") {
    from(layout.projectDirectory.dir("idl"))
    into(layout.buildDirectory.dir("toArchive/idl"))
}

tasks.register<Copy>("copyImagesDirForDeploy") {
    from(layout.projectDirectory.dir("images"))
    into(layout.buildDirectory.dir("toArchive/images"))
}

tasks.register<Copy>("copyLicenseFileForDeploy") {
    from(layout.projectDirectory.dir("COPYING"))
    into(layout.buildDirectory.dir("toArchive"))
}

tasks.register<Copy>("copyManifestDirForDeploy") {
    from(layout.projectDirectory.dir("META-INF"))
    into(layout.buildDirectory.dir("toArchive/META-INF"))
}

tasks.register<Copy>("copyJarFileForDeploy") {
    dependsOn("build")
    from(layout.buildDirectory.dir("libs/${project.name}-${version}.jar"))
    into(layout.buildDirectory.dir("toArchive"))

    rename("${project.name}-${version}.jar", "${project.name}.jar")
}

tasks.register<Copy>("copyQuranDirForDeploy") {
    from(layout.projectDirectory.dir("data"))
    exclude("**/QuranText.Dutch.IUR.xml")
    into(layout.buildDirectory.dir("toArchive/data"))
}

tasks.register<Copy>("copyRDBFileForDeploy") {
    dependsOn("compileRDBfile")
    from(layout.buildDirectory.dir("rdb/types.rdb"))
    into(layout.buildDirectory.dir("toArchive"))
}

tasks.register<Exec>("compileIDLfile") {
    commandLine(
        "/usr/lib/libreoffice/sdk/bin/idlc",
        "-Obuild/urd",
        "-I/usr/lib/libreoffice/sdk/idl",
        "idl/nl/mossoft/lo/QuranLOAddon/InsertQuranText.idl"
    )
}

tasks.register<Exec>("compileRDBfile") {
    dependsOn("compileIDLfile")
    commandLine(
        "/usr/lib/libreoffice/program/regmerge",
        "build/rdb/types.rdb",
        "/UCR",
        "build/urd/InsertQuranText.urd"
    )

    doFirst {
        mkdir("build/rdb")
    }
}

tasks.register<Zip>("prepareDistributionPackage") {
    dependsOn("copyQuranDirForDeploy")
    dependsOn("copyDescriptionDirForDeploy")
    dependsOn("copyDescriptionXMLForDeploy")
    dependsOn("copyImagesDirForDeploy")
    dependsOn("copyLicenseFileForDeploy")
    dependsOn("copyManifestDirForDeploy")
    dependsOn("copyRegistrationFilesForDeploy")
    dependsOn("copyIDLDirForDeploy")
    dependsOn("copyRDBFileForDeploy")
    dependsOn("copyJarFileForDeploy")
    archiveBaseName.set("${project.name}")
    archiveVersion.set("${project.version}")
    archiveExtension.set("oxt")
    destinationDirectory.set(layout.buildDirectory.dir("dist"))
    from(layout.buildDirectory.dir("toArchive"))
}

tasks.register<Exec>("InstallDistributionPackage") {
    dependsOn("prepareDistributionPackage")
    commandLine(
        "/usr/bin/unopkg",
        "add",
        "--force",
        "--suppress-license",
        "build/dist/${project.name}-${version}.oxt"
    )
}
