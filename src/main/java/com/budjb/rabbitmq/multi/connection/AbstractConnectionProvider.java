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

import com.budjb.rabbitmq.multi.config.ConnectionConfiguration;
import com.budjb.rabbitmq.multi.config.ConnectionParameters;
import com.codahale.metrics.MetricRegistry;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.impl.StandardMetricsCollector;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class AbstractConnectionProvider implements ConnectionProvider {
    /**
     * Returns the {@link ConnectionFactory} to use to create a connection to RabbitMQ.
     *
     * @return The {@link ConnectionFactory} to use to create a connection to RabbitMQ.
     */
    protected abstract ConnectionFactory getConnectionFactory();

    /**
     * {@inheritDoc}
     */
    @Override
    public Connection createConnection(ConnectionConfiguration connectionConfiguration) throws ConnectionException {
        try {
            ConnectionParameters connectionParameters = connectionConfiguration.getConnectionParameters();

            ConnectionFactory factory = getConnectionFactory();

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

            return factory.newConnection(executorService);
        }
        catch (Exception e) {
            throw new ConnectionException(e);
        }
    }
}
