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
package com.budjb.rabbitmq;

import com.budjb.rabbitmq.connection.ConnectionContext;
import com.rabbitmq.client.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Envelope;

public class MessageContext {
    /**
     * Channel
     */
    private Channel channel;

    /**
     * Consumer tag.
     */
    private String consumerTag;

    /**
     * Message envelope
     */
    private Envelope envelope;

    /**
     * Message properties
     */
    private BasicProperties properties;

    /**
     * Raw message body
     */
    private byte[] body;

    /**
     * Connection context associated with the message.
     */
    private ConnectionContext connectionContext;

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public String getConsumerTag() {
        return consumerTag;
    }

    public void setConsumerTag(String consumerTag) {
        this.consumerTag = consumerTag;
    }

    public Envelope getEnvelope() {
        return envelope;
    }

    public void setEnvelope(Envelope envelope) {
        this.envelope = envelope;
    }

    public BasicProperties getProperties() {
        return properties;
    }

    public void setProperties(BasicProperties properties) {
        this.properties = properties;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public ConnectionContext getConnectionContext() {
        return connectionContext;
    }

    public void setConnectionContext(ConnectionContext connectionContext) {
        this.connectionContext = connectionContext;
    }
}
