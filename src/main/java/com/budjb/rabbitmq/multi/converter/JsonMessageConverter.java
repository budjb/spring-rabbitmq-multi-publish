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

import groovy.json.JsonException;
import groovy.json.JsonOutput;
import groovy.json.JsonSlurper;
import org.springframework.util.MimeType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A converter that supports the conversion of a {@link Map} or a {@link List} to and from JSON.
 */
public class JsonMessageConverter implements ByteToObjectConverter, ObjectToByteConverter {
    /**
     * Mime type.
     */
    private static MimeType mimeType = MimeType.valueOf("application/json");

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(Class<?> type) {
        return List.class.isAssignableFrom(type) || Map.class.isAssignableFrom(type);
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
        try {
            return new ByteToObjectResult(new JsonSlurper().parse(input.getBytes(), input.getCharset().toString()));
        }
        catch (JsonException ignored) {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectToByteResult convert(ObjectToByteInput input) {
        return new ObjectToByteResult(JsonOutput.toJson(input.getObject()).getBytes(input.getCharset()),
            new MimeType(mimeType, new HashMap<String, String>() {{
                put("charset", input.getCharset().name());
            }})
        );
    }
}
