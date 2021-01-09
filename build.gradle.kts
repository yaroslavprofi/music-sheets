plugins {
    kotlin("multiplatform") version "1.4.0" apply false
}

allprojects {

    repositories {
        mavenCentral()
        jcenter()
    }
}

tasks.register<Copy>("stage") {
    dependsOn("server:build")

    destinationDir = File("build/dist")

    from(tarTree("server/build/distributions/server-0.1.1.tar"))
}