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
import com.rabbitmq.client.Connection;

/**
 * A class used to provide dependency injection for the creation of RabbitMQ
 * {@link Connection connections}.
 */
public interface ConnectionProvider {
    /**
     * Creates new connection to a RabbitMQ broker.
     *
     * @param connectionConfiguration Connection configuration.
     * @return A new connection.
     */
    Connection createConnection(ConnectionConfiguration connectionConfiguration) throws ConnectionException;
}
