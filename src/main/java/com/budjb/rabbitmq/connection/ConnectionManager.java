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

package com.budjb.rabbitmq.connection;

import com.budjb.rabbitmq.RunningState;
import com.budjb.rabbitmq.config.RabbitConfigurationProperties;
import com.rabbitmq.client.Channel;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.io.IOException;
import java.util.Map;

public class ConnectionManager {
    /**
     * Rabbit configuration properties.
     */
    private final RabbitConfigurationProperties rabbitConfigurationProperties;

    /**
     * List of managed connection contexts.
     */
    private final Map<String, ConnectionContext> connections;

    /**
     * Constructor.
     *
     * @param rabbitConfigurationProperties Rabbit message properties.
     * @param connections                   List of managed connection contexts.
     */
    public ConnectionManager(RabbitConfigurationProperties rabbitConfigurationProperties, Map<String, ConnectionContext> connections) {
        this.rabbitConfigurationProperties = rabbitConfigurationProperties;
        this.connections = connections;
    }

    /**
     * Starts all connections.
     */
    public void start() {
        for (ConnectionContext connectionContext : connections.values()) {
            if (connectionContext.getRunningState() == RunningState.STOPPED) {
                connectionContext.start();
            }
        }
    }

    /**
     * Stops all connections.
     */
    public void stop() {
        for (ConnectionContext connectionContext : connections.values()) {
            if (connectionContext.getRunningState() == RunningState.RUNNING) {
                connectionContext.stop();
            }
        }
    }

    /**
     * Automatically starts all RabbitMQ connections if the configuration allows it.
     *
     * @param event Application ready event.
     */
    @EventListener
    public void autoStart(ApplicationReadyEvent event) {
        if (rabbitConfigurationProperties.isAutoStart()) {
            start();
        }
    }

    /**
     * Returns the default connection context.
     *
     * @return The default connection context.
     */
    public ConnectionContext getContext() {
        return connections.values().stream().findFirst()
            .orElseThrow(() -> new IllegalArgumentException("no default connection was found"));
    }

    /**
     * Returns the connection context with the given name.
     *
     * @param name Name of the connection.
     * @return The connection context with the given name.
     */
    public ConnectionContext getContext(String name) {
        if (name == null) {
            return getContext();
        }

        if (connections.containsKey(name)) {
            return connections.get(name);
        }

        throw new ContextNotFoundException("no connection with name " + name + " was found");
    }

    /**
     * Creates a channel from the default connection.
     *
     * @return A channel from the default connection.
     * @throws IOException When an underlying IO exception occurs.
     */
    public Channel createChannel() throws IOException {
        return getContext().createChannel();
    }

    /**
     * Creates a channel from the connection with the given name.
     *
     * @param connectionName Name of the connection.
     * @return A channel from the connection with the given name.
     * @throws IOException When an underlying IO exception occurs.
     */
    public Channel createChannel(String connectionName) throws IOException {
        return getContext(connectionName).createChannel();
    }
}
