plugins {
    id("dev.prism")
}

group = "com.leclowndu93150.baguettelib"
version = "2.0.3"

prism {
    metadata {
        modId = "baguettelib"
        name = "BaguetteLib"
        description = "A library mod about adding more events into the modding API (and more stuff)"
        license = "MIT"
        author("Leclowndu93150")
    }

    curseMaven()

    version("1.21.1") {
        parchmentMinecraftVersion = "1.21.1"
        parchmentMappingsVersion = "2024.11.17"
        neoforge {
            loaderVersion = "21.1.171"
            loaderVersionRange = "[4,)"
            dependencies {
                runtimeOnly("curse.maven:modmenu-308702:7808230")
                implementation("curse.maven:curios-309927:6529130")
            }
        }
    }

    version("26.1.2") {
        common {}
        fabric {
            loaderVersion = "0.18.6"
            fabricApi("0.145.4+26.1.2")
            dependencies {
                runtimeOnly("curse.maven:modmenu-308702:7808230")
            }
        }
        neoforge {
            loaderVersion = "26.1.2.10-beta"
            loaderVersionRange = "[4,)"
            dependencies {
                implementation("curse.maven:curios-309927:7810501")
            }
        }
    }

    publishing {
        changelog = "Add statistic tracking utilities"
        type = STABLE

        curseforge {
            accessToken = providers.environmentVariable("CURSEFORGE_TOKEN")
            projectId = "1264423"
        }

        modrinth {
            accessToken = providers.environmentVariable("MODRINTH_TOKEN")
            projectId = "OfKzpbRU"
        }

        publishCommonJar = true

        maven {
            name = "Leclown"
            url = "https://maven.leclowndu93150.dev/releases"
            credentialsFromEnv("MAVEN_USER", "MAVEN_PASS")
        }
    }
}
