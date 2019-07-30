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

import com.budjb.rabbitmq.multi.config.RabbitConfigurationProperties
import spock.lang.Specification

class ConnectionManagerSpec extends Specification {
    def 'When a default connection is requested and a connection is configured as default, it is returned'() {
        setup:
        ConnectionContext c1 = Mock(ConnectionContext)
        ConnectionContext c2 = Mock(ConnectionContext)
        ConnectionContext c3 = Mock(ConnectionContext)

        c2.isDefault() >> true

        RabbitConfigurationProperties rabbitConfigurationProperties = new RabbitConfigurationProperties()
        ConnectionManager connectionManager = new ConnectionManager(rabbitConfigurationProperties, [c1: c1, c2: c2, c3: c3])

        when:
        ConnectionContext result = connectionManager.getContext()

        then:
        result.is c2
    }

    def 'When a default connection is requested and none are configured, an exception is throw'() {
        RabbitConfigurationProperties rabbitConfigurationProperties = new RabbitConfigurationProperties()
        ConnectionManager connectionManager = new ConnectionManager(rabbitConfigurationProperties, [:])

        when:
        connectionManager.getContext()

        then:
        thrown IllegalArgumentException
    }
}
