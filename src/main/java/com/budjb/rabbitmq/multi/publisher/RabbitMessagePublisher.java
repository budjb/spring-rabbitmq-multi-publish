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
package com.budjb.rabbitmq.multi.publisher;

import com.budjb.rabbitmq.multi.MessageContext;
import com.budjb.rabbitmq.multi.connection.ConnectionManager;
import com.budjb.rabbitmq.multi.converter.*;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class RabbitMessagePublisher {
    /**
     * Connection manager.
     */
    private final ConnectionManager connectionManager;

    /**
     * Message converter manager.
     */
    private final MessageConverterManager messageConverterManager;

    /**
     * Constructor.
     *
     * @param connectionManager Connection manager.
     */
    public RabbitMessagePublisher(ConnectionManager connectionManager, MessageConverterManager messageConverterManager) {
        this.connectionManager = connectionManager;
        this.messageConverterManager = messageConverterManager;
    }

    /**
     * Returns the connection manager.
     *
     * @return The connection manager.
     */
    public ConnectionManager getConnectionManager() {
        return connectionManager;
    }

    /**
     * Returns the message converter manager.
     *
     * @return The message converter manager.
     */
    public MessageConverterManager getMessageConverterManager() {
        return messageConverterManager;
    }

    /**
     * Sends a Rabbit message with a given set of message properties.
     *
     * @param properties Rabbit message properties.
     */
    public void send(RabbitMessageProperties properties) throws IOException, TimeoutException {
        verifyRoutingRequirements(properties);

        byte[] body = convert(properties);

        BasicProperties basicProperties = properties.toBasicProperties();

        boolean closeChannel = false;

        Channel channel = properties.getChannel();

        if (channel == null) {
            channel = connectionManager.createChannel(properties.getConnection());
            closeChannel = true;
        }

        try {
            channel.basicPublish(properties.getExchange(), properties.getRoutingKey(), basicProperties, body);
        }
        finally {
            if (closeChannel) {
                channel.close();
            }
        }
    }

    /**
     * Sends a Rabbit message with a given routing key and payload.
     *
     * @param routingKey Routing key.
     * @param body       Body of the message.
     */
    public void send(String routingKey, Object body) throws IOException, TimeoutException {
        send(new RabbitMessageProperties() {{
            setRoutingKey(routingKey);
            setBody(body);
        }});
    }

    /**
     * Sends a rabbit message with a given exchange, routing key, and payload.
     *
     * @param exchange   Exchange.
     * @param routingKey Routing key.
     * @param body       Body of the message.
     */
    public void send(String exchange, String routingKey, Object body) throws IllegalArgumentException, IOException, TimeoutException {
        send(new RabbitMessageProperties() {{
            setRoutingKey(routingKey);
            setExchange(exchange);
            setBody(body);
        }});
    }

    /**
     * Sends a message to the bus and waits for a reply, up to the "timeout" property.
     * <p>
     * This method returns a Message object if autoConvert is set to false, or some
     * other object type (string, list, map) if autoConvert is true.
     * <p>
     * The logic for the handler is based on the RPC handler found in spring's RabbitTemplate.
     */
    @SuppressWarnings("unchecked")
    public <T> T rpc(RabbitMessageProperties properties) throws TimeoutException, ShutdownSignalException, IOException, IllegalArgumentException, InterruptedException {
        verifyRoutingRequirements(properties);

        byte[] body = convert(properties);


        boolean closeChannel = false;

        boolean consuming = false;

        String temporaryQueue;

        Channel channel = properties.getChannel();
        if (channel == null) {
            channel = connectionManager.createChannel(properties.getConnection());
            closeChannel = true;
        }

        String consumerTag = UUID.randomUUID().toString();

        try {
            temporaryQueue = channel.queueDeclare().getQueue();

            properties.setReplyTo(temporaryQueue);

            BasicProperties basicProperties = properties.toBasicProperties();

            SynchronousQueue<MessageContext> replyHandOff = new SynchronousQueue<>();

            DefaultConsumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String replyConsumerTag, Envelope replyEnvelope, BasicProperties replyProperties, byte[] replyBody) {
                    MessageContext context = new MessageContext(replyProperties, body);

                    try {
                        replyHandOff.put(context);
                    }
                    catch (InterruptedException ignore) {
                        Thread.currentThread().interrupt();
                    }
                }
            };

            channel.basicConsume(temporaryQueue, false, consumerTag, true, true, null, consumer);
            consuming = true;

            channel.basicPublish(properties.getExchange(), properties.getRoutingKey(), basicProperties, body);

            MessageContext reply = (properties.getTimeout() < 0) ? replyHandOff.take() : replyHandOff.poll(properties.getTimeout(), TimeUnit.MILLISECONDS);

            if (reply == null) {
                throw new TimeoutException("timeout of " + properties.getTimeout() + " milliseconds reached while waiting for a response in an RPC message to exchange " +
                    properties.getExchange() + " and routingKey " + properties.getRoutingKey());
            }

            if (!properties.getAutoConvert()) {
                return (T) reply;
            }

            return (T) convert(reply);
        }
        finally {
            // If we've started consuming, stop consumption.
            // This cleans up some tracking objects internal to the RabbitMQ
            // library when using auto-recovering connections.
            // A memory leak results without this.
            if (consuming) {
                channel.basicCancel(consumerTag);
            }

            if (closeChannel) {
                channel.close();
            }
        }
    }

    /**
     * Sends a message to the bus and waits for a reply, up to the "timeout" property.
     * <p>
     * This method returns a Message object if autoConvert is set to false, or some
     * other object type (string, list, map) if autoConvert is true.
     *
     * @param routingKey Routing key to send the message to.
     * @param body       Message payload.
     */
    public <T> T rpc(String routingKey, Object body) throws TimeoutException, ShutdownSignalException, IOException, IllegalArgumentException, InterruptedException {
        return this.rpc(new RabbitMessageProperties() {{
            setRoutingKey(routingKey);
            setBody(body);
        }});
    }

    /**
     * Sends a message to the bus and waits for a reply, up to the "timeout" property.
     * <p>
     * This method returns a Message object if autoConvert is set to false, or some
     * other object type (string, list, map) if autoConvert is true.
     *
     * @param exchange   Exchange to send the message to.
     * @param routingKey Routing key to send the message to.
     * @param body       Message payload.
     */
    public <T> T rpc(String exchange, String routingKey, Object body) throws TimeoutException, ShutdownSignalException, IOException, IllegalArgumentException, InterruptedException {
        return rpc(new RabbitMessageProperties() {{
            setExchange(exchange);
            setRoutingKey(routingKey);
            setBody(body);
        }});
    }

    /**
     * Verifies that either a routing key or exchange is present.
     *
     * @param properties Rabbit message properties.
     */
    private void verifyRoutingRequirements(RabbitMessageProperties properties) {
        if ((properties.getRoutingKey() == null || properties.getRoutingKey().isEmpty()) && (properties.getExchange() == null || properties.getExchange().isEmpty())) {
            throw new IllegalArgumentException("exchange and/or routing key required");
        }
    }

    /**
     * Converts the body contained in the message properties. This will set the content type of
     * the message if one has not already been set.
     *
     * @param properties Rabbit message properties.
     * @return The body converted to bytes.
     */
    protected byte[] convert(RabbitMessageProperties properties) {
        if (properties.getBody() instanceof byte[]) {
            return (byte[]) properties.getBody();
        }

        ObjectToByteResult result = messageConverterManager.convert(new ObjectToByteInput(properties.getBody(), properties.getContentType()));

        if (result == null) {
            return null;
        }

        if (properties.getContentType() == null || properties.getContentType().isEmpty()) {
            properties.setContentType(result.getMimeType().toString());
        }

        return result.getResult();
    }

    /**
     * Converts the body contained in the message context. Will attempt to respect the content type
     * and/or the character set of the message if possible.
     *
     * @param messageContext Message context.
     * @return The converted message body.
     */
    protected Object convert(MessageContext messageContext) {
        ByteToObjectResult result = messageConverterManager.convert(new ByteToObjectInput(messageContext.getBody(), messageContext.getProperties().getContentType()));

        if (result != null) {
            return result.getResult();
        }

        return null;
    }
}
