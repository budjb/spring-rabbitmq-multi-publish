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

import org.springframework.util.MimeType;

import java.util.HashMap;

/**
 * A converter that supports conversion to and from a {@link String}.
 */
public class StringMessageConverter implements ByteToObjectConverter, ObjectToByteConverter {
    /**
     * Mime type.
     */
    private static final MimeType mimeType = MimeType.valueOf("text/plain");

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(Class<?> type) {
        return String.class.isAssignableFrom(type);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(MimeType mimeType) {
        return mimeType.isCompatibleWith(mimeType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ByteToObjectResult convert(ByteToObjectInput input) {
        return new ByteToObjectResult(new String(input.getBytes(), input.getCharset()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectToByteResult convert(ObjectToByteInput input) {
        return new ObjectToByteResult(((String) input.getObject()).getBytes(input.getCharset()), new MimeType(mimeType, new HashMap<String, String>() {{
            put("charset", input.getCharset().name());
        }}));
    }
}
