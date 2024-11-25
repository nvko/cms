plugins {
	java
	id("org.springframework.boot") version "3.3.5"
	id("io.spring.dependency-management") version "1.1.6"
}

group = "com.nvko"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.kafka:spring-kafka")
	implementation("org.springframework.boot:spring-boot-devtools")
	implementation("org.springframework.boot:spring-boot-starter-validation")

	implementation("com.fasterxml.jackson.core:jackson-core:2.18.1")
	implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.18.1")
	implementation("org.jeasy:easy-rules-core:4.1.0")
	implementation("org.apache.commons:commons-lang3:3.17.0")

	compileOnly("org.projectlombok:lombok:1.18.34")
	annotationProcessor("org.projectlombok:lombok:1.18.34")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.kafka:spring-kafka-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
