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

package com.budjb.rabbitmq.config;

import com.rabbitmq.client.ConnectionFactory;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Validated
public class ConnectionConfiguration {
    /**
     * AMQP URI.
     */
    @NotNull
    private URI uri;

    /**
     * Whether the connection should be considered the default connection.
     */
    private boolean isDefault;

    /**
     * Whether the connection should automatically recover from disconnections.
     */
    private boolean automaticReconnect = true;

    /**
     * Whether to enable metrics.
     */
    private boolean enableMetrics = true;

    /**
     * The overall thread pool size to use for consumers. {@code 0} means unlimited.
     * <p>
     * If a limit is set that is lower than the sum of all consumer threads on the connection,
     * contention for message delivery may occur.
     */
    @Min(0)
    private int threadPoolSize = 0;

    /**
     * Add custom client properties
     */
    private Map<String, Object> clientProperties = new HashMap<>();

    /**
     * Connection parameters parsed from the configuration.
     */
    private ConnectionParameters connectionParameters;

    /**
     * Sets the requested heartbeat delay, in seconds, that the server sends in the connection.tune frame.
     * <p>
     * 5 is the RabbitMQ default. 0 means unlimited.
     */
    private int requestedHeartbeat = ConnectionFactory.DEFAULT_HEARTBEAT;

    public Map<String, Object> getClientProperties() {
        return clientProperties;
    }

    public void setClientProperties(Map<String, Object> clientProperties) {
        this.clientProperties = clientProperties;
    }

    public int getRequestedHeartbeat() {
        return requestedHeartbeat;
    }

    public void setRequestedHeartbeat(int requestedHeartbeat) {
        this.requestedHeartbeat = requestedHeartbeat;
    }

    public boolean isEnableMetrics() {
        return enableMetrics;
    }

    public void setEnableMetrics(boolean enableMetrics) {
        this.enableMetrics = enableMetrics;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public boolean isAutomaticReconnect() {
        return automaticReconnect;
    }

    public void setAutomaticReconnect(boolean automaticReconnect) {
        this.automaticReconnect = automaticReconnect;
    }

    public int getThreadPoolSize() {
        return threadPoolSize;
    }

    public void setThreadPoolSize(int threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
    }

    /**
     * Returns a set of connection parameters used to make a connection to a RabbitMQ broker.
     * <p>
     * Note that this method lazy-loads connection parameters when necessasry.
     *
     * @return A set of connection parameters.
     */
    public ConnectionParameters getConnectionParameters() {
        if (connectionParameters == null) {
            synchronized (this) {
                if (connectionParameters == null) {
                    connectionParameters = ConnectionParameters.from(uri);
                }
            }
        }

        return connectionParameters;
    }

}
