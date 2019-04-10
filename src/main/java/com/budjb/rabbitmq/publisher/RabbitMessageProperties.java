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
package com.budjb.rabbitmq.publisher;

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
     * Content type.
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

    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public Map<String, Object> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, Object> headers) {
        this.headers = headers;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentEncoding() {
        return contentEncoding;
    }

    public void setContentEncoding(String contentEncoding) {
        this.contentEncoding = contentEncoding;
    }

    public Integer getDeliveryMode() {
        return deliveryMode;
    }

    public void setDeliveryMode(Integer deliveryMode) {
        this.deliveryMode = deliveryMode;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public String getExpiration() {
        return expiration;
    }

    /**
     * Sets the message TTL.
     *
     * @param expiration
     */
    void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    /**
     * Sets the message TTL.
     *
     * @param expiration
     */
    void setExpiration(Number expiration) {
        this.expiration = expiration.toString();
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public Boolean getAutoConvert() {
        return autoConvert;
    }

    public void setAutoConvert(Boolean autoConvert) {
        this.autoConvert = autoConvert;
    }

    public String getConnection() {
        return connection;
    }

    public void setConnection(String connection) {
        this.connection = connection;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    /**
     * Creates an AMQP basic properties object suitable for use in publishing messages.
     *
     * @return An AMQP basic properties object suitable for use in publishing messages.
     */
    public BasicProperties toBasicProperties() {
        // Create message properties
        BasicProperties.Builder builder = new BasicProperties.Builder();

        // Set any headers
        builder.headers(headers);

        // Content type
        if (contentType != null && contentType.length() > 0) {
            builder.contentType(contentType);
        }

        // Content encoding
        if (contentEncoding != null && !contentEncoding.isEmpty()) {
            builder.contentEncoding(contentEncoding);
        }

        // Delivery mode
        if (deliveryMode == 1 || deliveryMode == 2) {
            builder.deliveryMode(deliveryMode);
        }

        // Set priority
        if (priority != null) {
            builder.priority(priority);
        }

        // Set correlation id
        if (correlationId != null && !correlationId.isEmpty()) {
            builder.correlationId(correlationId);
        }

        // Reply-to
        if (replyTo != null && !replyTo.isEmpty()) {
            builder.replyTo(replyTo);
        }

        // Expiration
        if (expiration != null && !expiration.isEmpty()) {
            builder.expiration(expiration);
        }

        // Message ID
        if (messageId != null && !messageId.isEmpty()) {
            builder.messageId(messageId);
        }

        // Timestamp
        if (timestamp != null) {
            builder.timestamp(Date.from(timestamp));
        }

        // Type
        if (type != null && !type.isEmpty()) {
            builder.type(type);
        }

        // User ID
        if (userId != null && !userId.isEmpty()) {
            builder.userId(userId);
        }

        // Application ID
        if (appId != null && !appId.isEmpty()) {
            builder.appId(appId);
        }

        return builder.build();
    }
}
