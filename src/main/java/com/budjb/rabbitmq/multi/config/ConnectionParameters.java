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

import java.net.URI;

public class ConnectionParameters {
    /**
     * Default username.
     */
    private static final String DEFAULT_USERNAME = "guest";

    /**
     * Default password.
     */
    private static final String DEFAULT_PASSWORD = "guest";

    /**
     * Hostname of the RabbitMQ broker.
     */
    private final String host;

    /**
     * Port of the RabbitMQ broker.
     */
    private final int port;

    /**
     * Virtual host of the connection.
     */
    private final String virtualHost;

    /**
     * Username to authenticate to RabbitMQ with.
     */
    private final String username;

    /**
     * Password to authenticate to RabbitMQ with.
     */
    private final String password;

    /**
     * Whether the connection should use SSL.
     */
    private final boolean ssl;

    /**
     * Constructor.
     *
     * @param host        Hostname.
     * @param port        Port.
     * @param virtualHost Virtual host.
     * @param username    Username.
     * @param password    Password.
     * @param ssl         Whether the connection should use SSL.
     */
    private ConnectionParameters(String host, int port, String virtualHost, String username, String password, boolean ssl) {
        this.host = host;
        this.port = port;
        this.virtualHost = virtualHost;
        this.username = username;
        this.password = password;
        this.ssl = ssl;
    }

    /**
     * Parses connection parameters from a URI.
     *
     * @param uri URI of the connection.
     * @return A parsed set of connection parameters.
     */
    public static ConnectionParameters from(URI uri) {
        boolean ssl = uri.getScheme().equals("amqps");

        int port = uri.getPort();
        if (port < 1) {
            if (ssl) {
                port = 5671;

            }
            else {
                port = 5672;
            }
        }

        String host = uri.getHost();

        String virtualHost = uri.getPath();
        if (virtualHost.isEmpty()) {
            virtualHost = "/";
        }
        else if (virtualHost.charAt(0) == '/' && virtualHost.length() > 1) {
            virtualHost = virtualHost.substring(1);
        }

        String userInfo = uri.getUserInfo();
        String username = DEFAULT_USERNAME;
        String password = DEFAULT_PASSWORD;

        if (userInfo != null) {
            String[] parts = uri.getUserInfo().split(":");

            username = parts[0];

            if (parts.length > 1) {
                password = parts[1];
            }
        }

        return new ConnectionParameters(host, port, virtualHost, username, password, ssl);
    }

    /**
     * Returns the hostname of the RabbitMQ broker.
     *
     * @return The hostname of the RabbitMQ broker.
     */
    public String getHost() {
        return host;
    }

    /**
     * Returns the port of the RabbitMQ broker.
     *
     * @return The port of the RabbitMQ broker.
     */
    public int getPort() {
        return port;
    }

    /**
     * Returns the virtual host of the connection.
     *
     * @return The virtual host of the connection.
     */
    public String getVirtualHost() {
        return virtualHost;
    }

    /**
     * Returns the username to authenticate to RabbitMQ with.
     *
     * @return The username to authenticate to RabbitMQ with.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the password to authenticate to RabbitMQ with.
     *
     * @return The password to authenticate to RabbitMQ with.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns whether the connection should use SSL.
     *
     * @return Whether the connection should use SSL.
     */
    public boolean isSsl() {
        return ssl;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return (ssl ? "amqps" : "amqp") + "://" + host + ":" + port + (!virtualHost.equals("/") ? "/" + virtualHost : "");
    }
}
