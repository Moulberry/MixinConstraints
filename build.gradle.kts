plugins {
    id("com.vanniktech.maven.publish") version("0.28.0")
    id("java")
}

version = "1.0.1"
group = "com.moulberry.mixinconstraints"

repositories {
    mavenCentral()
    maven("https://maven.fabricmc.net/")
    maven("https://repo.spongepowered.org/maven")
    maven("https://jitpack.io")
}

val fabric: SourceSet by sourceSets.creating {
    compileClasspath += sourceSets.main.get().output
}

val forge: SourceSet by sourceSets.creating {
    compileClasspath += sourceSets.main.get().output
}

dependencies {
    implementation("org.spongepowered:mixin:0.8.5")
    implementation("org.ow2.asm:asm-tree:9.7")
    implementation("org.slf4j:slf4j-api:2.0.12")
    implementation("io.github.llamalad7:mixinextras-common:0.3.6")

    "fabricImplementation"("net.fabricmc:fabric-loader:0.15.0")
    "forgeImplementation"("net.minecraftforge:fmlloader:1.20.6-50.1.0")
    "forgeImplementation"("net.minecraftforge:fmlcore:1.20.6-50.1.0")
}