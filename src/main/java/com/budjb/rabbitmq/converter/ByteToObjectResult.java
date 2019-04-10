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

/**
 * Contains the result of a conversion of a byte array to some object.
 */
public class ByteToObjectResult {
    /**
     * Result of the conversion.
     */
    private final Object result;

    /**
     * Constructor.
     */
    public ByteToObjectResult(Object result) {
        this.result = result;
    }

    /**
     * Returns the result of the conversion.
     *
     * @return The result of the conversion.
     */
    public Object getResult() {
        return result;
    }
}
