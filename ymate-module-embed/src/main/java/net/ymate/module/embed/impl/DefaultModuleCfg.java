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
package net.ymate.module.embed.impl;

import net.ymate.module.embed.IEmbed;
import net.ymate.module.embed.IEmbedModuleCfg;
import net.ymate.module.embed.container.IContainer;
import net.ymate.module.embed.container.ITomcatConfig;
import net.ymate.module.embed.container.impl.TomcatContainer;
import net.ymate.module.embed.container.support.TomcatConfigBuilder;
import net.ymate.platform.core.YMP;
import net.ymate.platform.core.util.ClassUtils;

import java.util.Map;

/**
 * @author 刘镇 (suninformation@163.com) on 2018/11/10 下午 18:22
 * @version 1.0
 */
public class DefaultModuleCfg implements IEmbedModuleCfg {

    private IContainer __container;

    public DefaultModuleCfg(YMP owner) {
        Map<String, String> _moduleCfgs = owner.getConfig().getModuleConfigs(IEmbed.MODULE_NAME);
        //
        ITomcatConfig _tomcatConfig = TomcatConfigBuilder.create(_moduleCfgs).build();
        //
        __container = ClassUtils.impl(_moduleCfgs.get("container_class"), IContainer.class, getClass(), new Class<?>[]{ITomcatConfig.class}, new Object[]{_tomcatConfig}, true);
        if (__container == null) {
            __container = ClassUtils.impl(_moduleCfgs.get("container_class"), IContainer.class, getClass());
            if (__container == null) {
                __container = TomcatContainer.withDispatcher(_tomcatConfig);
            }
        }
    }

    @Override
    public IContainer getContainer() {
        return __container;
    }
}