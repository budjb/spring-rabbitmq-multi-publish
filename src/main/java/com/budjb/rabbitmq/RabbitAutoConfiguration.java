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

package com.budjb.rabbitmq;

import com.budjb.rabbitmq.config.RabbitConfigurationProperties;
import com.budjb.rabbitmq.connection.ConnectionContext;
import com.budjb.rabbitmq.connection.ConnectionManager;
import com.budjb.rabbitmq.converter.*;
import com.budjb.rabbitmq.publisher.RabbitMessagePublisher;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Configuration
@EnableConfigurationProperties(RabbitConfigurationProperties.class)
public class RabbitAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public ConnectionManager connectionManager(RabbitConfigurationProperties rabbitConfigurationProperties) {
        return new ConnectionManager(rabbitConfigurationProperties, rabbitConfigurationProperties.getConnections().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> new ConnectionContext(e.getKey(), e.getValue()))));
    }

    @Bean
    @ConditionalOnMissingBean
    public MessageConverterManager messageConverterManager(Optional<List<MessageConverter>> messageConverters, RabbitConfigurationProperties rabbitConfigurationProperties) {
        List<MessageConverter> converters = messageConverters.orElse(new LinkedList<>());


        if (rabbitConfigurationProperties.getMessageConverterConfiguration().isEnableGroovyJson()) {
            converters.add(new JsonMessageConverter());
        }
        if (rabbitConfigurationProperties.getMessageConverterConfiguration().isEnableInt()) {
            converters.add(new IntegerMessageConverter());
        }
        if (rabbitConfigurationProperties.getMessageConverterConfiguration().isEnableLong()) {
            converters.add(new LongMessageConverter());
        }
        if (rabbitConfigurationProperties.getMessageConverterConfiguration().isEnableString()) {
            converters.add(new StringMessageConverter());
        }

        return new MessageConverterManager(converters);
    }

    @Bean
    @ConditionalOnMissingBean
    public RabbitMessagePublisher rabbitMessagePublisher(ConnectionManager connectionManager, MessageConverterManager messageConverterManager) {
        return new RabbitMessagePublisher(connectionManager, messageConverterManager);
    }
}
