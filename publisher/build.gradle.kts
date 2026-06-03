plugins{
    java
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("maven-publish")
}

dependencies{
    implementation(project(":solver"))
    implementation(project(":generator"))
}

repositories{
    mavenCentral()
    maven("https://releases.usethesource.io/maven/")
}

group = "org.pl"
version = "1.0.0"

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifact(tasks.shadowJar)
        }
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/FormalLanguageConstrainedPathQuerying/UCFS")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
