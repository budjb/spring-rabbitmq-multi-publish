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

package com.budjb.rabbitmq.multi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties("rabbitmq")
public class RabbitConfigurationProperties {
    /**
     * Connection configurations.
     */
    @NestedConfigurationProperty
    private Map<String, ConnectionConfiguration> connections = new HashMap<>();

    /**
     * Whether to automatically start RabbitMQ on application startup.
     */
    private boolean autoStart = true;

    /**
     * Message converter configuration.
     */
    @NestedConfigurationProperty
    private MessageConverterConfiguration messageConverterConfiguration = new MessageConverterConfiguration();

    /**
     * Returns the message converter configuration.
     *
     * @return The message converter configuration.
     */
    public MessageConverterConfiguration getMessageConverterConfiguration() {
        return messageConverterConfiguration;
    }

    /**
     * Sets the message converter configuration.
     *
     * @param messageConverterConfiguration The message converter configuration.
     */
    public void setMessageConverterConfiguration(MessageConverterConfiguration messageConverterConfiguration) {
        this.messageConverterConfiguration = messageConverterConfiguration;
    }

    /**
     * Returns whether to automatically start RabbitMQ on application startup.
     *
     * @return Whether to automatically start RabbitMQ on application startup.
     */
    public boolean isAutoStart() {
        return autoStart;
    }

    /**
     * Sets whether to automatically start RabbitMQ on application startup.
     *
     * @param autoStart Whether to automatically start RabbitMQ on application startup.
     */
    public void setAutoStart(boolean autoStart) {
        this.autoStart = autoStart;
    }

    /**
     * Returns the connection configurations.
     *
     * @return The Connection configurations.
     */
    public Map<String, ConnectionConfiguration> getConnections() {
        return connections;
    }

    /**
     * Sets the connection configurations.
     *
     * @param connections The connection configurations.
     */
    public void setConnections(Map<String, ConnectionConfiguration> connections) {
        this.connections = connections;
    }
}
