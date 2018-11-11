/*
 * Copyright 2007-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.ymate.module.embed.container.impl;

import net.ymate.module.embed.container.IContainer;
import net.ymate.platform.core.YMP;

/**
 * @author 刘镇 (suninformation@163.com) on 2018/3/16 上午1:16
 * @version 1.0
 */
public final class DefaultContainer implements IContainer {

    @Override
    public String getName() {
        return "default";
    }

    @Override
    public void start() throws Exception {
        YMP.get().init();
    }

    @Override
    public void stop() throws Exception {
        YMP.get().destroy();
    }
}
