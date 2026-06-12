plugins{
    java
    id("maven-publish")
}

dependencies{
    implementation("org.antlr:antlr4:4.13.1")
    implementation("io.github.oshai:kotlin-logging-jvm:5.1.0")
    implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.5")
    implementation("org.slf4j:slf4j-api:2.0.17")
    implementation("ch.qos.logback:logback-classic:1.5.18")
    implementation("com.squareup:kotlinpoet:1.16.0")
}

tasks.jar {
    from(project(":solver").sourceSets["main"].output)
    from(project(":generator").sourceSets["main"].output)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

repositories{
    mavenCentral()
    maven("https://releases.usethesource.io/maven/")
}

group = "org.pl"
version = "0.2.0"

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = "ucfs"
            from(components["java"])
        }
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/GRIGAeo/UCFS")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
