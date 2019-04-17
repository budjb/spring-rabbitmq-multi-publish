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

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RabbitMessageProperties {
    /**
     * Default timeout for RPC calls (5 seconds).
     */
    private static final Integer DEFAULT_TIMEOUT = 5000;

    /**
     * Routing key to send the message to.
     */
    private String routingKey = "";

    /**
     * Exchange to send the message to.
     */
    private String exchange = "";

    /**
     * RPC timeout, in milliseconds.
     */
    private Integer timeout = DEFAULT_TIMEOUT;

    /**
     * Message body.
     */
    private Object body;

    /**
     * Message headers.
     */
    private Map<String, Object> headers = new HashMap<>();

    /**
     * Content type of the message body.
     */
    private String contentType;

    /**
     * Content encoding.
     */
    private String contentEncoding;

    /**
     * Delivery mode (1 == non-persistent, 2 == persistent)
     */
    private Integer deliveryMode = 0;

    /**
     * Priority.
     */
    private Integer priority = 0;

    /**
     * Correlation id.
     */
    private String correlationId;

    /**
     * Queue to reply to.
     */
    private String replyTo;

    /**
     * Message TTL.
     */
    private String expiration;

    /**
     * Message ID.
     */
    private String messageId;

    /**
     * Message timestamp.
     */
    private Instant timestamp;

    /**
     * Message type name.
     */
    private String type;

    /**
     * User ID.
     */
    private String userId;

    /**
     * Application ID.
     */
    private String appId;

    /**
     * Whether to auto-convertToBytes the reply payload.
     */
    private Boolean autoConvert = true;

    /**
     * Connection name.
     */
    private String connection;

    /**
     * Channel to publish messages through.
     */
    private Channel channel;

    /**
     * Returns the routing key to send the message to.
     *
     * @return The routing key to send the message to.
     */
    public String getRoutingKey() {
        return routingKey;
    }

    /**
     * Sets the routing key to send the message to.
     *
     * @param routingKey The routing key to send the message to.
     */
    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    /**
     * Returns the exchange to send the message to.
     *
     * @return The exchange to send the message to.
     */
    public String getExchange() {
        return exchange;
    }

    /**
     * Sets the exchange to send the message to.
     *
     * @param exchange The exchange to send the message to.
     */
    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    /**
     * Returns the RPC timeout, in milliseconds.
     *
     * @return The RPC timeout, in milliseconds.
     */
    public Integer getTimeout() {
        return timeout;
    }

    /**
     * Sets the RPC timeout, in milliseconds.
     *
     * @param timeout RPC timeout, in milliseconds.
     */
    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    /**
     * Returns the message body.
     *
     * @return The message body.
     */
    public Object getBody() {
        return body;
    }

    /**
     * Sets the message body.
     *
     * @param body The message body.
     */
    public void setBody(Object body) {
        this.body = body;
    }

    /**
     * Returns the message headers.
     *
     * @return The message headers.
     */
    public Map<String, Object> getHeaders() {
        return headers;
    }

    /**
     * Sets the message headers.
     *
     * @param headers The message headers.
     */
    public void setHeaders(Map<String, Object> headers) {
        this.headers = headers;
    }

    /**
     * Returns the content type of the message body.
     *
     * @return The content type of the message body.
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * Sets the content type of the message body.
     *
     * @param contentType The content type of the message body.
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * Returns the content encoding.
     *
     * @return The content encoding.
     */
    public String getContentEncoding() {
        return contentEncoding;
    }

    /**
     * Sets the content encoding.
     *
     * @param contentEncoding The content encoding.
     */
    public void setContentEncoding(String contentEncoding) {
        this.contentEncoding = contentEncoding;
    }

    /**
     * Returns the delivery mode.
     *
     * @return The delivery mode.
     */
    public Integer getDeliveryMode() {
        return deliveryMode;
    }

    /**
     * Sets the delivery mode.
     *
     * @param deliveryMode The delivery mode.
     */
    public void setDeliveryMode(Integer deliveryMode) {
        this.deliveryMode = deliveryMode;
    }

    /**
     * Returns the priority.
     *
     * @return The priority.
     */
    public Integer getPriority() {
        return priority;
    }

    /**
     * Sets the priority.
     *
     * @param priority The priority.
     */
    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    /**
     * Returns the correlation id.
     *
     * @return The correlation id.
     */
    public String getCorrelationId() {
        return correlationId;
    }

    /**
     * Sets the correlation id.
     *
     * @param correlationId The correlation id.
     */
    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    /**
     * Returns the queue to reply to.
     *
     * @return The queue to reply to.
     */
    public String getReplyTo() {
        return replyTo;
    }

    /**
     * Sets the queue to reply to.
     *
     * @param replyTo The queue to reply to.
     */
    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    /**
     * Returns the message TTL.
     *
     * @return The message TTL.
     */
    public String getExpiration() {
        return expiration;
    }

    /**
     * Sets the message TTL.
     *
     * @param expiration The message TTL.
     */
    void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    /**
     * Sets the message TTL.
     *
     * @param expiration The message TTL.
     */
    void setExpiration(Number expiration) {
        this.expiration = expiration.toString();
    }

    /**
     * Returns the message ID.
     *
     * @return The message ID.
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * Sets the message ID.
     *
     * @param messageId The message ID.
     */
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    /**
     * Returns the message timestamp.
     *
     * @return The message timestamp.
     */
    public Instant getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the message timestamp.
     *
     * @param timestamp The message timestamp.
     */
    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Returns the message type name.
     *
     * @return The message type name.
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the message type name.
     *
     * @param type The message type name.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Returns the user ID.
     *
     * @return The user ID.
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets the user ID.
     *
     * @param userId The user ID.
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Returns the application ID.
     *
     * @return The application ID.
     */
    public String getAppId() {
        return appId;
    }

    /**
     * Sets the application ID.
     *
     * @param appId The application ID.
     */
    public void setAppId(String appId) {
        this.appId = appId;
    }

    /**
     * Returns whether to auto-convertToBytes the reply payload.
     *
     * @return Whether to auto-convertToBytes the reply payload.
     */
    public Boolean getAutoConvert() {
        return autoConvert;
    }

    /**
     * Sets whether to auto-convertToBytes the reply payload.
     *
     * @param autoConvert Whether to auto-convertToBytes the reply payload.
     */
    public void setAutoConvert(Boolean autoConvert) {
        this.autoConvert = autoConvert;
    }

    /**
     * Returns the connection name.
     *
     * @return The connection name.
     */
    public String getConnection() {
        return connection;
    }

    /**
     * Sets the connection name.
     *
     * @param connection The connection name.
     */
    public void setConnection(String connection) {
        this.connection = connection;
    }

    /**
     * Returns the channel to publish messages through.
     *
     * @return The channel to publish messages through.
     */
    public Channel getChannel() {
        return channel;
    }

    /**
     * Sets the channel to publish messages through.
     *
     * @param channel The channel to publish messages through.
     */
    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    /**
     * Creates an AMQP basic properties object suitable for use in publishing messages.
     *
     * @return An AMQP basic properties object suitable for use in publishing messages.
     */
    public BasicProperties toBasicProperties() {
        BasicProperties.Builder builder = new BasicProperties.Builder();

        builder.headers(headers);

        if (contentType != null && contentType.length() > 0) {
            builder.contentType(contentType);
        }

        if (contentEncoding != null && !contentEncoding.isEmpty()) {
            builder.contentEncoding(contentEncoding);
        }

        if (deliveryMode == 1 || deliveryMode == 2) {
            builder.deliveryMode(deliveryMode);
        }

        if (priority != null) {
            builder.priority(priority);
        }

        if (correlationId != null && !correlationId.isEmpty()) {
            builder.correlationId(correlationId);
        }

        if (replyTo != null && !replyTo.isEmpty()) {
            builder.replyTo(replyTo);
        }

        if (expiration != null && !expiration.isEmpty()) {
            builder.expiration(expiration);
        }

        if (messageId != null && !messageId.isEmpty()) {
            builder.messageId(messageId);
        }

        if (timestamp != null) {
            builder.timestamp(Date.from(timestamp));
        }

        if (type != null && !type.isEmpty()) {
            builder.type(type);
        }

        if (userId != null && !userId.isEmpty()) {
            builder.userId(userId);
        }

        if (appId != null && !appId.isEmpty()) {
            builder.appId(appId);
        }

        return builder.build();
    }
}
