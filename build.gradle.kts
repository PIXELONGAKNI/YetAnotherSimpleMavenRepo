plugins {
    kotlin("jvm") version "1.3.72"
}

group = "net.perfectdreams"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("io.github.microutils:kotlin-logging:1.8.0.1")
    implementation("ch.qos.logback:logback-classic:1.3.0-alpha5")
    implementation("io.ktor:ktor-server-core:1.3.1")
    implementation("io.ktor:ktor-server-netty:1.3.1")
    implementation("org.yaml:snakeyaml:1.26")
}

tasks {
    val fatJar = task("fatJar", type = Jar::class) {
        println("Building fat jar for ${project.name}...")

        archiveBaseName.set("${project.name}-fat")

        manifest {
            attributes["Main-Class"] = "net.perfectdreams.yetanothersimplemavenrepo.YetAnotherSimpleMavenRepoLauncher"
            attributes["Class-Path"] = configurations.runtimeClasspath.get().joinToString(" ", transform = { "libs/" + it.name })
        }

        val libs = File(rootProject.projectDir, "libs")
        // libs.deleteRecursively()
        libs.mkdirs()

        from(configurations.runtimeClasspath.get().mapNotNull {
            val output = File(libs, it.name)

            if (!output.exists())
                it.copyTo(output, true)

            null
        })

        with(jar.get() as CopySpec)
    }

    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }

    "build" {
        dependsOn(fatJar)
    }
}