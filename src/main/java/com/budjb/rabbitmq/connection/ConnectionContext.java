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
import com.budjb.rabbitmq.config.ConnectionConfiguration;
import com.budjb.rabbitmq.config.ConnectionParameters;
import com.codahale.metrics.MetricRegistry;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.impl.StandardMetricsCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

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
     * Connection to RabbitMQ.
     */
    private Connection connection;

    /**
     * Constructor.
     *
     * @param name                    Name of the connection.
     * @param connectionConfiguration Connection configuration.
     */
    public ConnectionContext(String name, ConnectionConfiguration connectionConfiguration) {
        this.name = name;
        this.connectionConfiguration = connectionConfiguration;
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
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(connectionParameters.getHost());
            factory.setPort(connectionParameters.getPort());
            factory.setVirtualHost(connectionParameters.getVirtualHost());
            factory.setUsername(connectionParameters.getUsername());
            factory.setPassword(connectionParameters.getPassword());

            if (connectionParameters.isSsl()) {
                factory.useSslProtocol();
            }

            factory.setAutomaticRecoveryEnabled(connectionConfiguration.isAutomaticReconnect());
            factory.setRequestedHeartbeat(connectionConfiguration.getRequestedHeartbeat());

            if (connectionConfiguration.getClientProperties().size() > 0) {
                factory.setClientProperties(new HashMap<String, Object>() {{
                    putAll(factory.getClientProperties());
                    putAll(connectionConfiguration.getClientProperties());
                }});
            }

            if (connectionConfiguration.isEnableMetrics()) {
                MetricRegistry metricRegistry = new MetricRegistry();
                StandardMetricsCollector metrics = new StandardMetricsCollector(metricRegistry);
                factory.setMetricsCollector(metrics);
            }

            ExecutorService executorService = connectionConfiguration.getThreadPoolSize() > 0 ?
                Executors.newFixedThreadPool(connectionConfiguration.getThreadPoolSize()) : Executors.newCachedThreadPool();

            this.connection = factory.newConnection(executorService);

            log.info("Opened connection to RabbitMQ server " + getName() + " (" + connectionParameters + ")");
        }
        catch (NoSuchAlgorithmException | KeyManagementException e) {
            log.error("Connection to RabbitMQ server " + getName() + " (" + connectionParameters + ") did not start because it encountered an SSL problem", e);
        }
        catch (IOException | TimeoutException e) {
            log.error("Connection to RabbitMQ server " + getName() + " (" + connectionParameters + ") did not start because it encountered a connection problem", e);
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
     * @return THe context's running state.
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
    public boolean getIsDefault() {
        return connectionConfiguration.isDefault();
    }
}
