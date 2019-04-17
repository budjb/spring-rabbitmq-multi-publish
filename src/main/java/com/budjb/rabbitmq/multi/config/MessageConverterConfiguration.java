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

public class MessageConverterConfiguration {
    /**
     * Whether to enable the groovy JSON converter.
     */
    private boolean enableGroovyJson = true;

    /**
     * Whether to enable the ascii-based Long converter.
     */
    private boolean enableLong = true;

    /**
     * Whether to enable the ascii-based Integer converter.
     */
    private boolean enableInt = true;

    /**
     * Whether to enable the ascii-based String converter.
     */
    private boolean enableString = true;

    /**
     * Returns whether to enable the groovy JSON converter.
     *
     * @return Whether to enable the groovy JSON converter.
     */
    public boolean isEnableGroovyJson() {
        return enableGroovyJson;
    }

    /**
     * Sets whether to enable the groovy JSON converter.
     *
     * @param enableGroovyJson Whether to enable the groovy JSON converter.
     */
    public void setEnableGroovyJson(boolean enableGroovyJson) {
        this.enableGroovyJson = enableGroovyJson;
    }

    /**
     * Returns whether to enable the ascii-based Long converter.
     *
     * @return Whether to enable the ascii-based Long converter.
     */
    public boolean isEnableLong() {
        return enableLong;
    }

    /**
     * Sets whether to enable the ascii-based Long converter.
     *
     * @param enableLong Whether to enable the ascii-based Long converter.
     */
    public void setEnableLong(boolean enableLong) {
        this.enableLong = enableLong;
    }

    /**
     * Returns whether to enable the ascii-based Integer converter.
     *
     * @return Whether to enable the ascii-based Integer converter.
     */
    public boolean isEnableInt() {
        return enableInt;
    }

    /**
     * Sets whether to enable the ascii-based Integer converter.
     *
     * @param enableInt Whether to enable the ascii-based Integer converter.
     */
    public void setEnableInt(boolean enableInt) {
        this.enableInt = enableInt;
    }

    /**
     * Returns whether to enable the ascii-based String converter.
     *
     * @return Whether to enable the ascii-based String converter.
     */
    public boolean isEnableString() {
        return enableString;
    }

    /**
     * Sets whether to enable the ascii-based String converter.
     *
     * @param enableString Whether to enable the ascii-based String converter.
     */
    public void setEnableString(boolean enableString) {
        this.enableString = enableString;
    }
}
