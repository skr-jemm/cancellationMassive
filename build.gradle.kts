plugins {
    id("io.vertx.vertx-plugin") version "1.3.0"
}

group = "mx.sekura"
version = "1.0"
var Verticle = "mx.sekura.cancelationMassive.MainVerticle"
val jacksonDataBin = "2.13.1"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    implementation("io.vertx:vertx-core:4.2.3")
    implementation("io.vertx:vertx-web:4.2.3")
    implementation("io.vertx:vertx-config:4.2.3")
    implementation("io.vertx:vertx-web-client:4.2.3")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.17.2")
    implementation("org.apache.poi:poi:5.2.2")
    implementation("org.apache.poi:poi-ooxml:5.2.2")
    implementation ("com.fasterxml.jackson.core:jackson-databind:$jacksonDataBin")
    // https://mvnrepository.com/artifact/org.glassfish.jersey.core/jersey-client
    implementation("org.glassfish.jersey.core:jersey-client:3.1.0-M8")
    // https://mvnrepository.com/artifact/org.glassfish.jersey.media/jersey-media-multipart
    implementation("org.glassfish.jersey.media:jersey-media-multipart:3.1.0-M8")
    // https://mvnrepository.com/artifact/org.glassfish.jersey.inject/jersey-hk2
    implementation("org.glassfish.jersey.inject:jersey-hk2:3.1.0-M8")


}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
vertx { // (1)
    mainVerticle = Verticle
}
java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}