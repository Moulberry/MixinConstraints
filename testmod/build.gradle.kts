import xyz.wagyourtail.unimined.api.unimined
import xyz.wagyourtail.unimined.util.OSUtils

plugins {
    java
    id("xyz.wagyourtail.unimined") version("1.3.3")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
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
    implementation(rootProject.sourceSets["main"].output)
}

unimined.minecraft(sourceSets["main"]) {
    combineWith(rootProject.sourceSets["main"])
    version = "1.20.6"

    mappings {
        mojmap()
        parchment(version = "2024.06.16")

        devFallbackNamespace("official")
    }

    runs {
        off = true

        config("server") {
            enabled = false
        }
    }

    defaultRemapJar = false
}

unimined.minecraft(fabric) {
    combineWith(sourceSets["main"])
    combineWith(rootProject.sourceSets["fabric"])

    fabric {
        loader("0.15.11")
    }

    runs.off = false
}

unimined.minecraft(forge) {
    combineWith(sourceSets["main"])
    combineWith(rootProject.sourceSets["forge"])

    minecraftForge {
        loader("50.1.0")
        mixinConfig("testmod.mixins.json")
    }

    runs {
        off = false

        config("client") {
            if(OSUtils.oSId == OSUtils.OSX) {
                jvmArgs("-XstartOnFirstThread")
            }
        }
    }
}

unimined.minecraft(neoforge) {
    combineWith(sourceSets["main"])
    combineWith(rootProject.sourceSets["neoforge"])

    neoForge {
        loader("119")
        mixinConfig("testmod.mixins.json")
    }

    minecraftRemapper.config {
        // neoforge adds 1 conflict, where 2 interfaces have a method with the same name on yarn/mojmap,
        // but the method has different names in the intermediary mappings.
        // this is a conflict because they have a class that extends both interfaces.
        // this shouldn't be a problem as long as named mappings don't make the name of those 2 methods different.
        ignoreConflicts(true)
    }

    runs {
        off = false

        config("client") {
            if(OSUtils.oSId == OSUtils.OSX) {
                jvmArgs("-XstartOnFirstThread")
            }
        }
    }
}
