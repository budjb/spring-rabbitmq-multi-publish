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

package com.budjb.rabbitmq.config;

public class MessageConverterConfiguration {
    private boolean enableGroovyJson = true;
    private boolean enableLong = true;
    private boolean enableInt = true;
    private boolean enableString = true;

    public boolean isEnableGroovyJson() {
        return enableGroovyJson;
    }

    public void setEnableGroovyJson(boolean enableGroovyJson) {
        this.enableGroovyJson = enableGroovyJson;
    }

    public boolean isEnableLong() {
        return enableLong;
    }

    public void setEnableLong(boolean enableLong) {
        this.enableLong = enableLong;
    }

    public boolean isEnableInt() {
        return enableInt;
    }

    public void setEnableInt(boolean enableInt) {
        this.enableInt = enableInt;
    }

    public boolean isEnableString() {
        return enableString;
    }

    public void setEnableString(boolean enableString) {
        this.enableString = enableString;
    }
}
