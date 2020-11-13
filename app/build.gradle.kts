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
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    implementation("org.eclipse.jetty:jetty-servlet:9.4.3.v20170317")
}

application {
    mainClass.set("com.intuit.jaludden.App")
}

tasks.test {
    useJUnitPlatform()
}
