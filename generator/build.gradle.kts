plugins {
    kotlin("jvm") version "1.9.20"
    jacoco
}

jacoco{
    toolVersion = "0.8.14"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":solver"))
    implementation("com.squareup:kotlinpoet:1.16.0")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.jacocoTestReport{
    dependsOn(":test-shared:test")

    val testSharedExec = project(":test-shared").layout.buildDirectory.dir("jacoco").map { it.file("test.exec").asFile }
    val kotlinClassData = layout.buildDirectory.dir("classes/kotlin/main").map{ it.asFile }
    val lavaClassData = layout.buildDirectory.dir("classes/java/main").map{ it.asFile }

    classDirectories.setFrom(files(kotlinClassData, lavaClassData))
    sourceDirectories.setFrom(files("src/main/kotlin", "src/main/java"))
    executionData.setFrom(testSharedExec)

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
            limit{
                counter = "INSTRUCTION"
                value = "COVEREDRATIO"
                minimum = "0.7".toBigDecimal()
            }
            limit{
                counter = "BRANCH"
                value = "COVEREDRATIO"
                minimum = "0.7".toBigDecimal()
            }
            limit{
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = "0.7".toBigDecimal()
            }
            limit{
                counter = "METHOD"
                value = "COVEREDRATIO"
                minimum = "0.85".toBigDecimal()
            }
            limit {
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

kotlin {
    jvmToolchain(11)
}