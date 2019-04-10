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
package com.budjb.rabbitmq.converter;

import org.springframework.util.MimeType;

import java.nio.charset.Charset;
import java.util.List;

/**
 * Represents a request to convert a byte array to some object.
 */
public class ByteToObjectInput {
    /**
     * UTF-8 character set.
     */
    public final static Charset UTF_8 = Charset.forName("UTF-8");

    /**
     * Bytes to convert.
     */
    private final byte[] bytes;

    /**
     * Mime type of the data.
     */
    private final MimeType mimeType;

    /**
     * How the message converter should behave.
     */
    private final MessageConvertMethod messageConvertMethod;

    /**
     * List of classes the converter should limit its conversion to.
     */
    private final List<Class<?>> classFilter;

    /**
     * Constructor for conversions that do not have specific mime type, filter, or converter behavior requirements.
     */
    public ByteToObjectInput(byte[] bytes) {
        this(bytes, (MimeType) null);
    }

    /**
     * Constructor for conversions that do not have specific filter or converter behavior requirements.
     */
    public ByteToObjectInput(byte[] bytes, String contentType) {
        this(bytes, contentType != null && !contentType.isEmpty() ? MimeType.valueOf(contentType) : null);
    }

    /**
     * Constructor for conversions that do not have specific filter or converter behavior requirements.
     */
    public ByteToObjectInput(byte[] bytes, MimeType mimeType) {
        this(bytes, mimeType, null, null);
    }

    /**
     * Constructor for conversions with specific mime type, filter, and converter behavior requirements.
     */
    public ByteToObjectInput(byte[] bytes, String mimeType, MessageConvertMethod messageConvertMethod, List<Class<?>> classFilter) {
        this(bytes, mimeType != null && !mimeType.isEmpty() ? MimeType.valueOf(mimeType) : null, messageConvertMethod, classFilter);
    }

    /**
     * Constructor for conversions with specific mime type, filter, and converter behavior requirements.
     */
    public ByteToObjectInput(byte[] bytes, MimeType mimeType, MessageConvertMethod messageConvertMethod, List<Class<?>> classFilter) {
        this.bytes = bytes;
        this.mimeType = mimeType;
        this.messageConvertMethod = messageConvertMethod;
        this.classFilter = classFilter;
    }

    /**
     * Returns the bytes to convert.
     *
     * @return The bytes to convert.
     */
    public byte[] getBytes() {
        return bytes;
    }

    /**
     * Returns the mime type of the data.
     *
     * @return The mime type of the data.
     */
    public MimeType getMimeType() {
        return mimeType;
    }

    /**
     * Returns the message convert method.
     *
     * @return The message convert method.
     */
    public MessageConvertMethod getMessageConvertMethod() {
        return messageConvertMethod;
    }

    /**
     * Returns the list of classes the converter should limit its conversion to.
     *
     * @return The list of classes the converter should limit its conversion to.
     */
    public List<Class<?>> getClassFilter() {
        return classFilter;
    }

    /**
     * Returns the character set of the data.
     */
    Charset getCharset() {
        if (mimeType != null) {
            Charset charset = mimeType.getCharset();

            if (charset != null) {
                return charset;
            }
        }

        return UTF_8;
    }
}
