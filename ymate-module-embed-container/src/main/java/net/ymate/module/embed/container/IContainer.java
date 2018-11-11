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
package net.ymate.module.embed.container;

import net.ymate.module.embed.container.impl.DefaultContainer;
import net.ymate.platform.core.util.ClassUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author 刘镇 (suninformation@163.com) on 2018/3/15 下午2:30
 * @version 1.0
 */
public interface IContainer {

    /**
     * @return 返回容器名称
     */
    String getName();

    /**
     * 启动容器
     *
     * @throws Exception 可能产生的任何异常
     */
    void start() throws Exception;

    /**
     * 停止容器
     *
     * @throws Exception 可能产生的任何异常
     */
    void stop() throws Exception;

    class Manager {

        private static final Map<String, IContainer> __containers = new LinkedHashMap<String, IContainer>();

        private static IContainer __defaultContainer;

        public static synchronized IContainer getDefaultContainer() {
            if (__defaultContainer == null) {
                __defaultContainer = ClassUtils.loadClass(IContainer.class);
                if (__defaultContainer == null) {
                    __defaultContainer = new DefaultContainer();
                }
            }
            return __defaultContainer;
        }

        public static void register(IContainer container) throws Exception {
            if (DefaultContainer.class.equals(container.getClass())) {
                return;
            }
            String _key = container.getClass().getName().toLowerCase();
            if (!__containers.containsKey(_key)) {
                __containers.put(_key, container);
            }
        }

        public static Collection<IContainer> getContainers() {
            return Collections.unmodifiableCollection(__containers.values());
        }
    }
}
