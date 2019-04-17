/*
 * Copyright 2019 Bud Byrd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.budjb.rabbitmq.multi.connection

import com.budjb.rabbitmq.multi.RunningState
import com.budjb.rabbitmq.multi.config.ConnectionConfiguration
import com.budjb.rabbitmq.multi.config.MockConnectionProvider
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import spock.lang.Specification

import java.util.concurrent.ExecutorService

class ConnectionContextSpec extends Specification {
    def 'The correct name and configuration are returned'() {
        setup:
        ConnectionConfiguration configuration = new ConnectionConfiguration()

        when:
        ConnectionContext context = new ConnectionContext('foo', configuration, null)

        then:
        context.name == 'foo'
        context.connectionConfiguration.is configuration
        !context.default
    }

    def 'The correct interactions occur when starting a connection context'() {
        setup:
        Connection connection = Mock(Connection)
        ConnectionFactory factory = Mock(ConnectionFactory)
        factory.newConnection((ExecutorService) _) >> connection
        ConnectionProvider provider = new MockConnectionProvider(factory)

        ConnectionConfiguration configuration = new ConnectionConfiguration()
        configuration.uri = new URI('amqps://localhost/foo')

        ConnectionContext context = new ConnectionContext('foo', configuration, provider)

        when:
        context.start()

        then:
        1 * factory.setHost('localhost')
        1 * factory.setPort(5671)
        1 * factory.setUsername('guest')
        1 * factory.setPassword('guest')
        1 * factory.setVirtualHost('foo')
        1 * factory.useSslProtocol()
        1 * factory.setAutomaticRecoveryEnabled(true)
        1 * factory.setRequestedHeartbeat(ConnectionFactory.DEFAULT_HEARTBEAT)

        context.runningState == RunningState.RUNNING
    }

    def 'The correct interactions occur when stopping a connection context'() {
        setup:
        Connection connection = Mock(Connection)
        ConnectionFactory factory = Mock(ConnectionFactory)
        factory.newConnection((ExecutorService) _) >> connection
        ConnectionProvider provider = new MockConnectionProvider(factory)

        ConnectionConfiguration configuration = new ConnectionConfiguration()
        configuration.uri = new URI('amqps://localhost/foo')

        ConnectionContext context = new ConnectionContext('foo', configuration, provider)
        context.start()

        connection.isOpen() >> true

        when:
        context.stop()

        then:
        1 * connection.close()
        context.runningState == RunningState.STOPPED
    }

    def 'The correct interactions occur when creating a channel'() {
        setup:
        Connection connection = Mock(Connection)
        ConnectionFactory factory = Mock(ConnectionFactory)
        factory.newConnection((ExecutorService) _) >> connection
        ConnectionProvider provider = new MockConnectionProvider(factory)

        ConnectionConfiguration configuration = new ConnectionConfiguration()
        configuration.uri = new URI('amqps://localhost/foo')

        ConnectionContext context = new ConnectionContext('foo', configuration, provider)
        context.start()

        when:
        context.createChannel()

        then:
        1 * connection.createChannel()
    }
}
