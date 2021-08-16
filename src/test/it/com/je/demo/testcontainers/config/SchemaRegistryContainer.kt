package com.je.demo.testcontainers.config

import org.testcontainers.containers.GenericContainer

class SchemaRegistryContainer(
    image: String,
    bootstrapServer: String
) : GenericContainer<SchemaRegistryContainer>(image) {

    val url: String
        get() = "http://${this.containerIpAddress}:${getMappedPort(SCHEMA_REGISTRY_INTERNAL_PORT)}"

    companion object {
        private const val SCHEMA_REGISTRY_INTERNAL_PORT = 8081
        private const val NETWORK_ALIAS = "schema-registry"
        private const val HOST_NAME = "schema-registry"
        private const val LISTENERS = "http://0.0.0.0:8081"
    }

    init {
        withEnv("SCHEMA_REGISTRY_HOST_NAME", HOST_NAME)
        withEnv("SCHEMA_REGISTRY_LISTENERS", LISTENERS)
        withEnv("SCHEMA_REGISTRY_KAFKASTORE_BOOTSTRAP_SERVERS", bootstrapServer)
        withExposedPorts(SCHEMA_REGISTRY_INTERNAL_PORT)
        withNetworkAliases(NETWORK_ALIAS)
    }
}
