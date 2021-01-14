plugins {
    kotlin("multiplatform") version "1.4.0" apply false
}

allprojects {
    version = "0.1.1"

    repositories {
        mavenCentral()
        jcenter()
    }
}

tasks.register<Copy>("stageServer") {
    dependsOn("server:build")

    destinationDir = File("build/dist/server")

    from(tarTree("server/build/distributions/server-0.1.1.tar"))
}

tasks.register<Copy>("stageDbproperties") {
    dependsOn("server:build")

    destinationDir = File("build/dist")

    from("server/db.properties")
}

tasks.register<Copy>("stageClient") {
    dependsOn("client:browserDistribution")

    destinationDir = File("build/dist/client")

    from("client/build/distributions")
}

tasks.register("stage") {
    dependsOn("stageServer")
    dependsOn("stageClient")
    dependsOn("stageDbproperties")
}