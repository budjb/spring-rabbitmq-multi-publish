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

package com.budjb.rabbitmq.multi.publisher

import com.budjb.rabbitmq.multi.connection.ConnectionManager
import com.budjb.rabbitmq.multi.converter.MessageConverterManager
import spock.lang.Specification

class RabbitMessagePublisherSpec extends Specification {
    ConnectionManager connectionManager
    MessageConverterManager messageConverterManager

    def setup() {
        connectionManager = Mock(ConnectionManager)
        messageConverterManager = Mock(MessageConverterManager)
    }

    def 'The correct dependency injections are returned'() {
        when:
        RabbitMessagePublisher rabbitMessagePublisher = new RabbitMessagePublisher(connectionManager, messageConverterManager)

        then:
        rabbitMessagePublisher.connectionManager.is connectionManager
        rabbitMessagePublisher.messageConverterManager.is messageConverterManager
    }

    def 'Attempting to send a message without a routing key or exchange results in an exception'() {
        setup:
        RabbitMessagePublisher rabbitMessagePublisher = new RabbitMessagePublisher(connectionManager, messageConverterManager)

        when:
        rabbitMessagePublisher.send(new RabbitMessageProperties())

        then:
        thrown IllegalArgumentException
    }
}
