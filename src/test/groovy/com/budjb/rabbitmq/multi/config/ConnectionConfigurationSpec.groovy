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

package com.budjb.rabbitmq.multi.config

import com.rabbitmq.client.ConnectionFactory
import spock.lang.Specification

class ConnectionConfigurationSpec extends Specification {
    def 'Default values are returned correctly'() {
        setup:
        ConnectionConfiguration configuration = new ConnectionConfiguration()

        expect:
        configuration.uri == null
        !configuration.isDefault()
        configuration.isAutomaticReconnect()
        configuration.getThreadPoolSize() == 0
        configuration.getClientProperties().isEmpty()
        configuration.getRequestedHeartbeat() == ConnectionFactory.DEFAULT_HEARTBEAT
    }

    def 'Non-default values are returned correctly'() {
        setup:
        URI uri = new URI('amqp://foo.com')
        ConnectionConfiguration configuration = new ConnectionConfiguration()

        configuration.uri = uri
        configuration.default = true
        configuration.automaticReconnect = false
        configuration.threadPoolSize = 1
        configuration.clientProperties = ['foo': 'bar']
        configuration.requestedHeartbeat = 1234

        expect:
        configuration.uri.is uri
        configuration.isDefault()
        !configuration.isAutomaticReconnect()
        configuration.getThreadPoolSize() == 1
        configuration.getClientProperties() == ['foo': 'bar']
        configuration.getRequestedHeartbeat() == 1234
    }
}
