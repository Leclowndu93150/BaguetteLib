plugins {
    id("dev.prism")
}

group = "com.leclowndu93150.baguettelib"
version = "2.0.2"

prism {
    metadata {
        modId = "baguettelib"
        name = "BaguetteLib"
        description = "A library mod about adding more events into the modding API (and more stuff)"
        license = "MIT"
        author("Leclowndu93150")
    }

    curseMaven()

    version("26.1") {
        fabric {
            loaderVersion = "0.18.4"
            fabricApi("0.144.0+26.1")
            dependencies {
                runtimeOnly("curse.maven:modmenu-308702:7808230")
            }
        }
        neoforge {
            loaderVersion = "26.1.0.1-beta"
            loaderVersionRange = "[4,)"
        }
    }

    publishing {
        changelog = "Added particle and color utilities"
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
