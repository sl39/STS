plugins {
    java
    id("org.springframework.boot") version "3.2.9"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "org.ex"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    compileOnly("org.projectlombok:lombok")

    runtimeOnly("org.mariadb.jdbc:mariadb-java-client")

    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	// JSON 변환
	implementation("com.google.code.gson:gson:2.10.1")

	// jjwt
	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

	//firebase연결
	implementation("com.google.firebase:firebase-admin:9.3.0")

	//coolsms
	implementation("net.nurigo:sdk:4.3.0")
	implementation ("org.springframework.boot:spring-boot-starter-validation")
    // JSON 변환
    implementation("com.google.code.gson:gson:2.10.1")

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation ("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.13.0") // 최신 버전으로 변경

}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-parameters") // 여기서 -parameters 플래그 추가
}

tasks.withType<Test> {
    useJUnitPlatform()
}
