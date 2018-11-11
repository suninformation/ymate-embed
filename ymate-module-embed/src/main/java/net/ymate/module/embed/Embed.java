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
package net.ymate.module.embed;

import net.ymate.module.embed.container.IContainer;
import net.ymate.module.embed.container.ITomcatConfig;
import net.ymate.module.embed.container.Main;
import net.ymate.module.embed.impl.DefaultModuleCfg;
import net.ymate.platform.core.Version;
import net.ymate.platform.core.YMP;
import net.ymate.platform.core.module.IModule;
import net.ymate.platform.core.module.annotation.Module;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author 刘镇 (suninformation@163.com) on 2018/11/10 下午 18:22
 * @version 1.0
 */
@Module
public class Embed implements IModule, IEmbed {

    private static final Log _LOG = LogFactory.getLog(Embed.class);

    public static final Version VERSION = new Version(1, 0, 0, Embed.class.getPackage().getImplementationVersion(), Version.VersionType.Alphal);

    private static volatile IEmbed __instance;

    private YMP __owner;

    private IEmbedModuleCfg __moduleCfg;

    private boolean __inited;

    public static IEmbed get() {
        if (__instance == null) {
            synchronized (VERSION) {
                if (__instance == null) {
                    __instance = YMP.get().getModule(Embed.class);
                }
            }
        }
        return __instance;
    }

    public static IEmbed get(YMP owner) {
        return owner.getModule(Embed.class);
    }

    @Override
    public String getName() {
        return IEmbed.MODULE_NAME;
    }

    @Override
    public void init(YMP owner) throws Exception {
        if (!__inited) {
            //
            _LOG.info("Initializing ymate-module-embed-" + VERSION);
            //
            __owner = owner;
            __moduleCfg = new DefaultModuleCfg(owner);
            //
            __owner.registerExcludedClass(IContainer.class);
            __owner.registerExcludedClass(ITomcatConfig.class);
            //
            __moduleCfg.getContainer().start();
            //
            __inited = true;
        }
    }

    @Override
    public boolean isInited() {
        return __inited;
    }

    @Override
    public void destroy() throws Exception {
        if (__inited) {
            __inited = false;
            //
            __moduleCfg.getContainer().stop();
            //
            __moduleCfg = null;
            __owner = null;
        }
    }

    @Override
    public YMP getOwner() {
        return __owner;
    }

    @Override
    public IEmbedModuleCfg getModuleCfg() {
        return __moduleCfg;
    }

    public static void main(String[] args) {
        Main.main(args);
    }
}
