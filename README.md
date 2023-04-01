# Stock streaming service
[![<PiotrMichalowski96>](https://circleci.com/gh/PiotrMichalowski96/stock-streaming-service.svg?style=svg)](https://circleci.com/gh/PiotrMichalowski96/stock-streaming-service)

[![SonarCloud](https://sonarcloud.io/images/project_badges/sonarcloud-black.svg)](https://sonarcloud.io/dashboard?id=PiotrMichalowski96_stock-streaming-service)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=PiotrMichalowski96_stock-streaming-service&metric=bugs)](https://sonarcloud.io/dashboard?id=PiotrMichalowski96_stock-streaming-service)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=PiotrMichalowski96_stock-streaming-service&metric=coverage)](https://sonarcloud.io/dashboard?id=PiotrMichalowski96_stock-streaming-service)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=PiotrMichalowski96_stock-streaming-service&metric=ncloc)](https://sonarcloud.io/dashboard?id=PiotrMichalowski96_stock-streaming-service)

Stock REST API using stream processing with Apache Kafka and KSQL database. 
## Table of contents
* [Overview](#Overview)
* [Technologies](#Technologies)
* [Setup](#Setup)

## Overview
Application is exposing stock data via REST API. It was build using Contract-First approach.

Apache Camel framework is used for routing and processing data.
Stock service is build on top of Apache Kafka. ksqlDB is used for retrieving data by performing SQL syntax queries.

It is fully tested solution with both unit tests and integration tests. For integration tests there are used technologies such as testcontainers and Cucumber.
## Technologies
### Application
- Java 17
- Spring Boot
- Apache Camel
- Apache Kafka
- KSQL
- Open API 3.0
### Testing
- Test containers
- Cucumber
- JUnit 5
## Setup
To run this project, build it locally with Maven:
```
$ mvn clean package
```
Then you need to create docker image of stock streaming application:
```
$ mvn docker:build
```
Then you can run all services containers with docker compose file:
```
$ cd <project-path>\stock-streaming-service\src\main\docker
$ docker compose up -d
```
It will allow you to run whole cluster: zookeeper, Kafka broker, KSQL database server and streaming application.