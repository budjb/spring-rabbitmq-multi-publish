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
package com.budjb.rabbitmq.multi.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.MimeType;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A class that manages message converters and acts as the entry point for conversion.
 */
public class MessageConverterManager {
    /**
     * Binary data mime type.
     */
    private final static MimeType APPLICATION_OCTET_STREAM = MimeType.valueOf("application/octet-stream");

    /**
     * Logger.
     */
    private final Logger log = LoggerFactory.getLogger(MessageConverterManager.class);

    /**
     * Registered message converters.
     */
    private final List<MessageConverter> messageConverters;

    /**
     * Constructor.
     *
     * @param messageConverters Message converters.
     */
    public MessageConverterManager(List<MessageConverter> messageConverters) {
        this.messageConverters = messageConverters;
    }

    /**
     * {@inheritDoc}
     */
    public List<ByteToObjectConverter> getByteToObjectConverters() {
        return messageConverters.stream().filter(it -> it instanceof ByteToObjectConverter)
            .map(it -> (ByteToObjectConverter) it).collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    public List<ObjectToByteConverter> getObjectToByteConverters() {
        return messageConverters.stream().filter(it -> it instanceof ObjectToByteConverter)
            .map(it -> (ObjectToByteConverter) it).collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    public ObjectToByteResult convert(ObjectToByteInput input) {
        Object body = input.getObject();

        if (body == null) {
            return null;
        }

        if (body instanceof byte[]) {
            return new ObjectToByteResult((byte[]) body, APPLICATION_OCTET_STREAM);
        }

        Class<?> type = body.getClass();

        for (ObjectToByteConverter messageConverter : getObjectToByteConverters()) {
            if (messageConverter.supports(type)) {
                ObjectToByteResult converted = attemptConversion(messageConverter, input);

                if (converted != null) {
                    return converted;
                }
            }
        }

        throw new NoConverterFoundException("no message converter found to convert to a byte array");
    }

    /**
     * {@inheritDoc}
     */
    public ByteToObjectResult convert(ByteToObjectInput input) {
        if (input.getBytes() == null) {
            return null;
        }

        if (input.getMessageConvertMethod() == MessageConvertMethod.DISABLED) {
            return null;
        }

        if (input.getMimeType() != null) {
            for (ByteToObjectConverter converter : getByteToObjectConverters()) {
                if (!converter.supports(input.getMimeType())) {
                    continue;
                }

                if (isInputAndConverterIncompatible(input, converter)) {
                    continue;
                }

                ByteToObjectResult result = attemptConversion(converter, input);

                if (result != null) {
                    return result;
                }
            }
        }

        if (input.getMessageConvertMethod() != MessageConvertMethod.HEADER) {
            for (ByteToObjectConverter converter : getByteToObjectConverters()) {
                if (isInputAndConverterIncompatible(input, converter)) {
                    continue;
                }

                ByteToObjectResult result = attemptConversion(converter, input);

                if (result != null) {
                    return result;
                }
            }
        }

        if (input.getClassFilter().contains(byte[].class)) {
            return new ByteToObjectResult(input.getBytes());
        }

        throw new NoConverterFoundException("no message converter found to convert a message body from a byte array");
    }

    /**
     * Attempts to convert some object to a <pre>byte[]</pre> with the given {@link ObjectToByteConverter}.
     */
    private ObjectToByteResult attemptConversion(ObjectToByteConverter messageConverter, ObjectToByteInput input) {
        try {
            return messageConverter.convert(input);
        }
        catch (Throwable e) {
            log.error("unhandled exception caught from message converter " + messageConverter.getClass().getName(), e);
            return null;
        }
    }

    /**
     * Attempts to convert a <pre>byte[]</pre> to some object with the given {@link ByteToObjectConverter}.
     */
    private ByteToObjectResult attemptConversion(ByteToObjectConverter messageConverter, ByteToObjectInput input) {
        try {
            return messageConverter.convert(input);
        }
        catch (Throwable e) {
            log.error("unhandled exception caught from message converter " + messageConverter.getClass().getName(), e);
            return null;
        }
    }

    /**
     * Determines whether the given converter supports the given byte-to-object input.
     */
    private boolean isInputAndConverterIncompatible(ByteToObjectInput input, ByteToObjectConverter converter) {
        if (input.getClassFilter() == null || input.getClassFilter().isEmpty()) {
            return false;
        }

        return input.getClassFilter().stream().noneMatch(converter::supports);
    }
}
