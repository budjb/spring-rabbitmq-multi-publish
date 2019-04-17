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
import spock.lang.Unroll

class ConnectionParametersSpec extends Specification {
    @Unroll
    def 'The URI #uri parses correctly into connection parameters'() {
        when:
        ConnectionParameters parameters = ConnectionParameters.from(new URI(uri))

        then:
        parameters.host == host
        parameters.port == port
        parameters.virtualHost == vhost
        parameters.username == username
        parameters.password == password
        parameters.ssl == ssl

        where:
        uri                               | host        | port | vhost | username | password | ssl
        'amqp://localhost'                | 'localhost' | 5672 | '/'   | 'guest'  | 'guest'  | false
        'amqps://localhost/foo'           | 'localhost' | 5671 | 'foo' | 'guest'  | 'guest'  | true
        'amqp://foo:bar@foo.com:1234/meh' | 'foo.com'   | 1234 | 'meh' | 'foo'    | 'bar'    | false
        'amqps://foo@foo.com'             | 'foo.com'   | 5671 | '/'   | 'foo'    | 'guest'  | true
    }
}
