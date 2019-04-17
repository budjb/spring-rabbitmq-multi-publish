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

import spock.lang.Specification

class MessageConverterConfigurationSpec extends Specification {
    def 'Default values are returned correctly'() {
        setup:
        MessageConverterConfiguration configuration = new MessageConverterConfiguration()

        expect:
        configuration.enableGroovyJson
        configuration.enableLong
        configuration.enableInt
        configuration.enableString
    }

    def 'Non-default values are returned correctly'() {
        setup:
        MessageConverterConfiguration configuration = new MessageConverterConfiguration()

        configuration.enableGroovyJson = false
        configuration.enableLong = false
        configuration.enableInt = false
        configuration.enableString = false

        expect:
        !configuration.enableGroovyJson
        !configuration.enableLong
        !configuration.enableInt
        !configuration.enableString
    }
}
