plugins {
    application
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

repositories {
    jcenter()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    implementation("org.eclipse.jetty:jetty-servlet:9.4.3.v20170317")
    implementation("org.xerial:sqlite-jdbc:3.32.3")
}

application {
    mainClass.set("com.intuit.jaludden.App")
}

tasks.test {
    useJUnitPlatform()
}
