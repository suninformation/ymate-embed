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
import net.ymate.module.embed.container.ITomcatConfig;
import net.ymate.platform.webmvc.support.DispatchServlet;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

/**
 * @author 刘镇 (suninformation@163.com) on 2018/11/1 下午12:40
 * @version 1.0
 */
public class TomcatContainer implements IContainer {

    public static IContainer withDispatcher(ITomcatConfig config) {
        return new TomcatContainer(config) {
            @Override
            protected Context buildContext(Tomcat tomcat) {
                Context _context = super.buildContext(tomcat);
                //
                Tomcat.addServlet(_context, "dispatcher", new DispatchServlet());
                _context.addServletMapping("/*", "dispatcher");
                //
                return _context;
            }
        };
    }

    private Tomcat __tomcat;

    private ITomcatConfig __config;

    public TomcatContainer(ITomcatConfig config) {
        if (config == null) {
            throw new NullArgumentException("config");
        }
        __config = config;
        __tomcat = new Tomcat();
        __tomcat.setHostname(config.getHostName());
        __tomcat.setBaseDir(getConfig().getBaseDir());
        __tomcat.setPort(getConfig().getPort());
        //
        buildConnector(__tomcat);
        buildContext(__tomcat);
    }

    public ITomcatConfig getConfig() {
        return __config;
    }

    protected Context buildContext(Tomcat tomcat) {
        return tomcat.addContext(getConfig().getContextPath(), StringUtils.defaultIfBlank(getConfig().getDocBase(), getConfig().getBaseDir()));
    }

    protected Connector buildConnector(Tomcat tomcat) {
        Connector _connector = tomcat.getConnector();
        //
        _connector.setProtocol(getConfig().getProtocol());
        if (StringUtils.isNotBlank(getConfig().getProtocolHandlerClassName())) {
            _connector.setProtocolHandlerClassName(getConfig().getProtocolHandlerClassName());
        }
        if (getConfig().getMaxParameterCount() > 0) {
            _connector.setMaxParameterCount(getConfig().getMaxParameterCount());
        }
        if (getConfig().getMaxPostSize() > 0) {
            _connector.setMaxPostSize(getConfig().getMaxPostSize());
        }
        if (null != getConfig().getParseBodyMethods() && !getConfig().getParseBodyMethods().isEmpty()) {
            _connector.setParseBodyMethods(getConfig().getParseBodyMethods());
        }
        if (StringUtils.isNotBlank(getConfig().getScheme())) {
            _connector.setScheme(getConfig().getScheme());
        }
        //
        for (Map.Entry<String, String> _attr : getConfig().getAttributes().entrySet()) {
            _connector.setAttribute(_attr.getKey(), _attr.getValue());
        }

        return _connector;
    }

    @Override
    public String getName() {
        return "tomcat";
    }

    @Override
    public void start() throws Exception {
        __tomcat.start();
    }

    @Override
    public void stop() throws Exception {
        __tomcat.stop();
    }
}
