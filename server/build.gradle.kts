plugins {
    id("java")
    kotlin("jvm")
    id("application")
    id("distribution")
}

ext{
    ktorVersion
}

val ktorVersion = "1.3.2"
val logbackVersion = "1.2.3"

dependencies {
    implementation(kotlin("stdlib"))
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-html-builder:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
}

application {
    mainClassName = "io.ktor.server.netty.EngineMain"
}

tasks.withType<Copy>().named("processResources") {
    from(project(":client").tasks.named("browserDistribution"))
}