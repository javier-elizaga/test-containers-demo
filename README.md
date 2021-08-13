# Hexagonal Architecture

A hexagonal architecture it's a way of organizing your code highly decoupling the business logic with the technology underneath.

## Why Hexagonal Architecture?

One of the key benefits of this architecture is the ability to swap data sources without impacting the business logic. The basic idea behind Hexagonal Architecture is to put input and output at the edges of the Application's core. The Business logic should not depend on whether we expose Rest, GraphQL, and it should not depend on where we get/store data from (a database, an API,...)

## Basics Hexagonal Architecture

To archive this decoupled structure we have to define three concepts: Core, Ports, and Adapters.

- The core of the application or domain layer is where the business logic happens
- The Ports are interfaces needed to communicate with the Application Core. There are two ports:
  + Primary ports: Also known as Input ports or Driving ports. These are the first communication points between the outside and the application core. An example of a primary port is the service the RestController uses when a request is received.
  + Secondary port: Also known as Output ports or Driven ports. These ports are used by the Application Core to communicate with the exterior. Examples of secondary ports are repositories.
- The Adapters are just the implementation of Ports. As you might imagine we have two adapters, Primary and Secondary. Primary Adapters are the business logic of the application. Primary adapters are often called Use Cases and are implemented in the Application Domain or Application Core.
  The Secondary adapters are the implementations of the Secondary ports and many people organize them based on technology, for example JPA, Ibatis, Kafka, ... The implementation is located outside the domain or core.

# Project Structure

Most people using Hexagonal Architecture divide the code into three packages:
- `application`: the entrance of the application. This is the package where RestControllers, GraphQL APIs are located. The primary ports are used to communicate from the application layer to the domain one.
- `domain`: Core of the application. This package will contain the primary adapters with the business logic of the app. The primary adapters use the secondary ports to communicate with the exterior (database, rest service, ...). The domain of the application should be independent of any technology (note in the code that the domain is not using Spring at all)
- `infrastructure`: This package contains the secondary adaptors and all configurations needed to make the app work.

> Note that the project divides the code under each layer into features. This project is a simple POC that only contains one feature: `todo`.

# Running the app locally

## H2 database

Run the app as a spring-boot application

```shell
./mvnw spring-boot:run
```

## Run application with Oracle DB

This project uses TestContainer to create an Oracle Container and connects the application to it. The oracle profile will create a DataSource bean to override application.yml properties. Check OracleContainerConfiguration class.

### Run with IntelliJ

1. Run Docker service. For example DockerDesktop
2. Within IntelliJ, just run `LocalApplication` as a Kotlin Application with an environment variable: `spring.profiles.active=oracle` to enable the profile oracle.

# API Documentation

Start the app and go to [swagger](http://localhost:8080/swagger-ui/index.html)

# Style guide

## check code style (it's also bound to "mvn verify")
$ mvn antrun:run@ktlint
src/main/kotlin/Main.kt:10:10: Unused import

## fix code style deviations (runs built-in formatter)
$ mvn antrun:run@ktlint-format