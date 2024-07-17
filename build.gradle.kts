import com.vanniktech.maven.publish.JavaLibrary
import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.SonatypeHost

plugins {
    java
    idea
    id("com.vanniktech.maven.publish") version("0.28.0")
}

version = "1.0.1"
group = "com.moulberry.mixinconstraints"

idea.module.isDownloadSources = true

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
    maven("https://maven.fabricmc.net/")
    maven("https://repo.spongepowered.org/maven")
    maven("https://maven.neoforged.net/releases")
}

val fabric: SourceSet by sourceSets.creating {
    compileClasspath += sourceSets.main.get().output
}

val forge: SourceSet by sourceSets.creating {
    compileClasspath += sourceSets.main.get().output
}

val neoforge: SourceSet by sourceSets.creating {
    compileClasspath += sourceSets.main.get().output
}

dependencies {
    implementation("org.spongepowered:mixin:0.8.5")
    implementation("org.ow2.asm:asm-tree:9.7")
    implementation("org.slf4j:slf4j-api:2.0.12")

    "fabricImplementation"("net.fabricmc:fabric-loader:0.15.0")

    "forgeImplementation"("net.minecraftforge:fmlloader:1.20.6-50.1.0")
    "forgeImplementation"("net.minecraftforge:fmlcore:1.20.6-50.1.0")

    "neoforgeImplementation"("net.neoforged.fancymodloader:loader:3.0.45")
}

tasks.jar {
    from(fabric.output)
    from(forge.output)
}

tasks.register<Jar>("sourcesJar") {
    group = "build"
    archiveClassifier.set("sources")
    sourceSets.map { it.allSource }.forEach {
        from(it)
    }
}

mavenPublishing {
    configure(JavaLibrary(JavadocJar.Javadoc(), true))

    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    signAllPublications()

    coordinates("com.moulberry", "mixinconstraints", version.toString())

    pom {
        name = "MixinConstraints"
        description = "Library to enable/disable mixins using annotations"
        url = "https://github.com/Moulberry/MixinConstraints"

        licenses {
            license {
                name = "MIT License"
                url = "https://opensource.org/license/mit"
            }
        }
        developers {
            developer {
                name = "Moulberry"
            }
        }
        scm {
            url = "https://github.com/Moulberry/MixinConstraints/"
            connection = "scm:git:git://github.com/Moulberry/MixinConstraints.git"
            developerConnection = "scm:git:ssh://git@github.com/Moulberry/MixinConstraints.git"
        }
    }
}