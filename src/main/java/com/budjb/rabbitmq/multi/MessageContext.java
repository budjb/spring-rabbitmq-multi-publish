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
package com.budjb.rabbitmq.multi;

import com.rabbitmq.client.BasicProperties;

public class MessageContext {
    /**
     * Message properties.
     */
    private final BasicProperties properties;

    /**
     * Body of the message.
     */
    private final byte[] body;

    /**
     * Constructor.
     *
     * @param basicProperties Message properties.
     * @param body            Body of the message.
     */
    public MessageContext(BasicProperties basicProperties, byte[] body) {
        this.properties = basicProperties;
        this.body = body;
    }

    /**
     * Returns the message properties.
     *
     * @return The message properties.
     */
    public BasicProperties getProperties() {
        return properties;
    }

    /**
     * Returns the body of the message.
     *
     * @return The body of the message.
     */
    public byte[] getBody() {
        return body;
    }
}
