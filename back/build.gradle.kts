plugins {
	java
	id("org.springframework.boot") version "3.2.9"
	id("io.spring.dependency-management") version "1.1.6"
	id("org.jetbrains.kotlin.jvm") version "1.8.0"
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
	//implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
	//implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	compileOnly("org.projectlombok:lombok")

	runtimeOnly("org.mariadb.jdbc:mariadb-java-client")

	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
