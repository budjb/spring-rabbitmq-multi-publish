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

package com.budjb.rabbitmq.multi.connection;

import com.budjb.rabbitmq.multi.RunningState;
import com.budjb.rabbitmq.multi.config.ConnectionConfiguration;
import com.budjb.rabbitmq.multi.config.ConnectionParameters;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ConnectionContext {
    /**
     * Logger.
     */
    private final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * Name of the connection.
     */
    private final String name;

    /**
     * Connection configuration.
     */
    private final ConnectionConfiguration connectionConfiguration;

    /**
     * Connection factory provider.
     */
    private final ConnectionProvider connectionProvider;

    /**
     * Micrometer meter registry.
     */
    private final MeterRegistry meterRegistry;

    /**
     * Connection to RabbitMQ.
     */
    private Connection connection;

    /**
     * Constructor.
     *
     * @param name                    Name of the connection.
     * @param connectionConfiguration Connection configuration.
     * @param connectionProvider      Connection provider.
     * @param meterRegistry           Micrometer meter registry.
     */
    public ConnectionContext(
        String name,
        ConnectionConfiguration connectionConfiguration,
        ConnectionProvider connectionProvider,
        MeterRegistry meterRegistry
    ) {
        this.name = name;
        this.connectionConfiguration = connectionConfiguration;
        this.connectionProvider = connectionProvider;
        this.meterRegistry = meterRegistry;
    }

    /**
     * Returns the name of the connection.
     *
     * @return The name of the connection.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the connection configuration.
     *
     * @return The connection configuration.
     */
    public ConnectionConfiguration getConnectionConfiguration() {
        return connectionConfiguration;
    }

    /**
     * Opens the connection to the RabbitMQ broker.
     */
    public void start() {
        ConnectionParameters connectionParameters = connectionConfiguration.getConnectionParameters();

        if (this.connection != null) {
            throw new IllegalStateException("Attempted to start RabbitMQ connection " + getName() + " (" + connectionParameters + ") but it has already been started");
        }

        try {
            this.connection = connectionProvider.createConnection(connectionConfiguration, meterRegistry);
            log.info("Opened connection to RabbitMQ server " + getName() + " (" + connectionParameters + ")");
        }
        catch (ConnectionException e) {
            log.error("Connection to RabbitMQ server " + getName() + " (" + connectionParameters + ") did not start because it encountered an issue while connecting", e);
        }
    }

    /**
     * Closes the RabbitMQ connection.
     */
    public void stop() {
        if (connection == null) {
            return;
        }
        else if (!connection.isOpen()) {
            return;
        }

        try {
            connection.close();
        }
        catch (IOException e) {
            log.error("Unable to close connection to RabbitMQ server " + getName() + " (" + connectionConfiguration.getConnectionParameters() + ")");
        }
        finally {
            connection = null;
        }

        log.info("Closed connection to the RabbitMQ server " + getName() + " (" + connectionConfiguration.getConnectionParameters() + ")");
    }

    /**
     * Get the context's state.
     *
     * @return The context's running state.
     */
    public RunningState getRunningState() {
        return this.connection == null ? RunningState.STOPPED : RunningState.RUNNING;
    }

    /**
     * Creates an un-tracked channel.
     * <p>
     * Callers of this method must put in some due diligence to ensure that the channel is closed.
     *
     * @return A new, un-tracked channel.
     */
    public Channel createChannel() throws IllegalStateException, IOException {
        return getConnection().createChannel();
    }

    /**
     * Returns the connection associated with the context.
     *
     * @return The connection associated with the context.
     * @throws IllegalStateException When a connection is not active.
     */
    public Connection getConnection() throws IllegalStateException {
        if (connection == null) {
            throw new IllegalStateException("Connection " + getName() + " (" + connectionConfiguration.getConnectionParameters() + ") is not active");
        }
        return connection;
    }

    /**
     * Returns whether the context is the default connection.
     *
     * @return Whether the context is the default connection.
     */
    public boolean isDefault() {
        return connectionConfiguration.isDefault();
    }
}
