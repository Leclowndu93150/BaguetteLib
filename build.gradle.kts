plugins {
    id("dev.prism")
}

group = "com.leclowndu93150.baguettelib"
version = "2.0.5"

prism {
    metadata {
        modId = "baguettelib"
        name = "BaguetteLib"
        description = "A library mod about adding more events into the modding API (and more stuff)"
        license = "MIT"
        author("Leclowndu93150")
    }

    curseMaven()
    maven("Xander", "https://maven.isxander.dev/releases")

    version("1.20.1") {
        parchmentMinecraftVersion = "1.20.1"
        parchmentMappingsVersion = "2023.09.03"
        fabric {
            loaderVersion = "0.19.2"
            dependencies {
                modCompileOnly("curse.maven:modmenu-308702:5162837")
                modCompileOnlyApi("dev.isxander:yet-another-config-lib:3.6.6+1.20.1-fabric")
            }

            publishingDependencies {
                optional("yacl")
                optional("modmenu")
            }
        }
        forge {
            loaderVersion = "47.4.10"
            dependencies {
                modCompileOnlyApi("dev.isxander:yet-another-config-lib:3.6.6+1.20.1-forge")
            }

            publishingDependencies {
                optional("yacl")
            }
        }
    }

    version("1.21.1") {
        parchmentMinecraftVersion = "1.21.1"
        parchmentMappingsVersion = "2024.11.17"
        neoforge {
            loaderVersion = "21.1.171"
            loaderVersionRange = "[4,)"
            dependencies {
                runtimeOnly("curse.maven:modmenu-308702:7808230")
                implementation("curse.maven:curios-309927:6529130")
                modCompileOnlyApi("dev.isxander:yet-another-config-lib:3.8.1+1.21.1-neoforge")
                modRuntimeOnly("dev.isxander:yet-another-config-lib:3.8.1+1.21.1-neoforge")
            }

            publishingDependencies {
                optional("yacl")
            }
        }
    }

    version("26.1.2") {
        common {}
        fabric {
            loaderVersion = "0.18.6"
            fabricApi("0.145.4+26.1.2")
            dependencies {
                modCompileOnly("curse.maven:modmenu-308702:8065321")
                modRuntimeOnly("curse.maven:modmenu-308702:8065321")
                modCompileOnlyApi("dev.isxander:yet-another-config-lib:3.9.6+26.1-fabric")
                modRuntimeOnly("dev.isxander:yet-another-config-lib:3.9.6+26.1-fabric")
            }

            publishingDependencies {
                optional("yacl")
                optional("modmenu")
            }
        }
        neoforge {
            loaderVersion = "26.1.2.10-beta"
            loaderVersionRange = "[4,)"
            dependencies {
                implementation("curse.maven:curios-309927:7810501")
                modCompileOnlyApi("dev.isxander:yet-another-config-lib:3.9.6+26.1-neoforge")
                modRuntimeOnly("dev.isxander:yet-another-config-lib:3.9.6+26.1-neoforge")
            }

            publishingDependencies {
                optional("yacl")
            }
        }
    }

    version("26.2") {
        //minecraftVersions("26.2", "26.2.1", "26.2.2")
        common {}
        fabric {
            loaderVersion = "0.18.6"
            fabricApi("0.152.1+26.2")
            dependencies {
                modCompileOnly("curse.maven:modmenu-308702:8402669")
                modRuntimeOnly("curse.maven:modmenu-308702:8402669")
                modCompileOnlyApi("dev.isxander:yet-another-config-lib:3.9.6+26.2-fabric")
                modRuntimeOnly("dev.isxander:yet-another-config-lib:3.9.6+26.2-fabric")
            }

            publishingDependencies {
                optional("yacl")
                optional("modmenu")
            }
        }
        neoforge {
            loaderVersion = "26.2.0.1-beta"
            loaderVersionRange = "[4,)"
            dependencies {
                implementation("curse.maven:curios-309927:7810501")
                modCompileOnlyApi("dev.isxander:yet-another-config-lib:3.9.6+26.2-neoforge")
                modRuntimeOnly("dev.isxander:yet-another-config-lib:3.9.6+26.2-neoforge")
            }

            publishingDependencies {
                optional("yacl")
            }
        }
    }

    publishing {
        changelog = "Added 1.20.1 (Fabric + Forge) and YACL config screen plumbing: register a screen in common and it shows up in Mod Menu and the NeoForge/Forge mod list automatically."
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
