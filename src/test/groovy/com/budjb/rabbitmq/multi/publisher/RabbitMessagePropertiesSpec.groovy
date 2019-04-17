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

import com.rabbitmq.client.BasicProperties
import com.rabbitmq.client.Channel
import spock.lang.Specification

import java.time.Instant

class RabbitMessagePropertiesSpec extends Specification {
    def 'Default values are present'() {
        setup:
        RabbitMessageProperties properties = new RabbitMessageProperties()

        expect:
        properties.routingKey == ''
        properties.exchange == ''
        properties.timeout == 5000
        properties.body == null
        properties.headers.isEmpty()
        properties.contentType == null
        properties.contentEncoding == null
        properties.deliveryMode == 0
        properties.priority == 0
        properties.correlationId == null
        properties.replyTo == null
        properties.expiration == null
        properties.messageId == null
        properties.timestamp == null
        properties.type == null
        properties.userId == null
        properties.appId == null
        properties.autoConvert
        properties.connection == null
        properties.channel == null
    }

    def 'Non-default values are correct and the correct BasicProperties is generated'() {
        setup:
        Instant instant = Instant.now()
        Channel channel = Mock(Channel)
        RabbitMessageProperties properties = new RabbitMessageProperties()

        when:
        properties.routingKey = 'foo'
        properties.exchange = 'bar'
        properties.timeout = 5
        properties.body = 'hi'
        properties.headers = ['foo': 'bar']
        properties.contentType = 'baz'
        properties.contentEncoding = 'bam'
        properties.deliveryMode = 1
        properties.priority = 2
        properties.correlationId = '3'
        properties.replyTo = '4'
        properties.expiration = '5'
        properties.messageId = '6'
        properties.timestamp = instant
        properties.type = '7'
        properties.userId = '8'
        properties.appId = '9'
        properties.autoConvert = false
        properties.connection = 'foo'
        properties.channel = channel

        then:
        properties.routingKey == 'foo'
        properties.exchange == 'bar'
        properties.timeout == 5
        properties.body == 'hi'
        properties.headers == ['foo': 'bar']
        properties.contentType == 'baz'
        properties.contentEncoding == 'bam'
        properties.deliveryMode == 1
        properties.priority == 2
        properties.correlationId == '3'
        properties.replyTo == '4'
        properties.expiration == '5'
        properties.messageId == '6'
        properties.timestamp == instant
        properties.type == '7'
        properties.userId == '8'
        properties.appId == '9'
        !properties.autoConvert
        properties.connection == 'foo'
        properties.channel.is channel

        when:
        BasicProperties basicProperties = properties.toBasicProperties()

        then:
        basicProperties.headers == ['foo': 'bar']
        basicProperties.contentType == 'baz'
        basicProperties.contentEncoding == 'bam'
        basicProperties.deliveryMode == 1
        basicProperties.priority == 2
        basicProperties.correlationId == '3'
        basicProperties.replyTo == '4'
        basicProperties.expiration == '5'
        basicProperties.messageId == '6'
        basicProperties.timestamp == Date.from(instant)
        basicProperties.type == '7'
        basicProperties.userId == '8'
        basicProperties.appId == '9'
    }

    def 'Setting a numeric expiration generates the correct string'() {
        setup:
        RabbitMessageProperties properties = new RabbitMessageProperties()

        when:
        properties.expiration = 1234

        then:
        properties.expiration == '1234'
    }

    def 'A proper BasicProperties is generated from the configuration'() {

    }
}
