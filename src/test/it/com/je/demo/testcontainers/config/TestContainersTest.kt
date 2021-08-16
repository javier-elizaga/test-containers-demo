package com.je.demo.testcontainers.config

import com.je.demo.testcontainers.Application
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [Application::class])
@Import(value = [OracleTestContainersConfiguration::class, KafkaTestContainersConfiguration::class])
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@ActiveProfiles("default", "oracle", "kafka", "kafka-test-producers")
annotation class TestContainersTest
