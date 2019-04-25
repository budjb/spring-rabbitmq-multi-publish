# Multi-Connection RabbitMQ Publisher for Spring

This is a library that allows multiple RabbitMQ connections to be configured in an
application and message routing between those various connections.

## Getting Started

The library is hosted on jcenter and Maven Central. To use the library, use the
following dependency.

build.gradle:
```groovy
dependencies {
    implementation 'com.budjb:spring-rabbitmq-multi-publish:0.1.0'
}
```

A connection is configured using an [AMQP URI](https://www.rabbitmq.com/uri-spec.html).
For example, in `application.properties`:

```properties
rabbitmq.connections.my-connection.uri=amqp://foo:bar@localhost/vhost
```

Multiples of these connections may be configured, and each must have a unique name. The
name of the connection is provided in the configuration key after the
`rabbitmq.connections` part. Therefore, in the previous example, the name of the connection
is `my-connection`.

## Changelog

### 0.1.0

* Change metrics to use micrometer.

### 0.1.0

* Initial release with tests.
* Added a `ConnectionProvider` to abstract the functionality of building a RabbitMQ
  connection.
* Add a README file.

### 0.0.1

 * Initial release of functional code.
