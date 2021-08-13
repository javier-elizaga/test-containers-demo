package com.je.demo.testcontainers.com.je.demo.testcontainers.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile
import org.testcontainers.containers.OracleContainer
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy
import javax.sql.DataSource

@TestConfiguration
@Profile("oracle")
class OracleContainerConfiguration(
    @Value("\${application.test-containers.oracle-image}") private val oracleImage: String,
) {

    private val oracleContainer = OracleContainer(oracleImage)

    @PostConstruct
    fun setUp() = oracleContainer.start()

    @PreDestroy
    fun tearDown() = oracleContainer.stop()

    @Bean
    fun dataSource(): DataSource = DataSourceBuilder.create()
        .url(oracleContainer.jdbcUrl)
        .username(oracleContainer.username)
        .password(oracleContainer.password)
        .build()
}
