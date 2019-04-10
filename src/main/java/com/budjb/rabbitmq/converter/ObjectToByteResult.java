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

/**
 * Contains the result of a conversion from some object to a byte array.
 */
public class ObjectToByteResult {
    /**
     * Result of the conversion.
     */
    private final byte[] result;

    /**
     * Mime type of the converted object.
     */
    private final MimeType mimeType;

    /**
     * Constructor.
     */
    public ObjectToByteResult(byte[] result, MimeType mimeType) {
        this.result = result;
        this.mimeType = mimeType;
    }

    /**
     * Returns the result of the conversion.
     *
     * @return The result of the conversion.
     */
    public byte[] getResult() {
        return result;
    }

    /**
     * Returns the mime type of the converted object.
     *
     * @return The mime type of the converted object.
     */
    public MimeType getMimeType() {
        return mimeType;
    }
}
