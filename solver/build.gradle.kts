plugins {
    java
    kotlin("jvm") version "1.9.20"
    kotlin("plugin.allopen") version "1.9.20"
    jacoco
}

jacoco{
    toolVersion = "0.8.14"
}

repositories {
    mavenCentral()
    maven("https://releases.usethesource.io/maven/")
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.5")
    implementation ("io.github.oshai:kotlin-logging-jvm:5.1.0")
    //needed for .dot parsing
    implementation("org.antlr:antlr4:4.13.1")
    // https://mvnrepository.com/artifact/org.slf4j/slf4j-api
    //needed for kotlin-logging
    implementation("org.slf4j:slf4j-api:2.0.17")
    implementation("ch.qos.logback:logback-classic:1.5.18")

    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
}

kotlin { jvmToolchain(11) }

tasks.test { 
    useJUnitPlatform() 
}

tasks.jacocoTestReport{
    dependsOn(":test-shared:test")
    dependsOn(tasks.test)

    val localExec = layout.buildDirectory.dir("jacoco").map { it.file("test.exec").asFile }
    val testSharedExec = project(":test-shared").layout.buildDirectory.dir("jacoco").map { it.file("test.exec").asFile }
    val classData1 = layout.buildDirectory.dir("classes/java/main").map{ it.asFile }
    val classData2 = layout.buildDirectory.dir("classes/kotlin/main").map{ it.asFile }

    classDirectories.setFrom(files(classData1, classData2))
    sourceDirectories.setFrom(files("src/main/java", "src/main/kotlin"))
    executionData.setFrom(files(localExec, testSharedExec))

    reports{
        xml.required.set(true)
        html.required.set(true)
        csv.required.set(true)
    }
}

tasks.jacocoTestCoverageVerification{
    dependsOn(tasks.jacocoTestReport)
    violationRules{
        rule{
            isEnabled = true
            limit {
                counter = "INSTRUCTION"
                value = "COVEREDRATIO"
                minimum = "0.95".toBigDecimal()
            }
            limit {
                counter = "BRANCH"
                value = "COVEREDRATIO"
                minimum = "0.8".toBigDecimal()
            }
            limit{
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = "0.8".toBigDecimal()
            }
            limit{
                counter = "METHOD"
                value = "COVEREDRATIO"
                minimum = "0.85".toBigDecimal()
            }
            limit{
                counter = "CLASS"
                value = "COVEREDRATIO"
                minimum = "0.9".toBigDecimal()
            }
        }
    }
}

tasks.check{
    dependsOn(tasks.jacocoTestCoverageVerification)
}
