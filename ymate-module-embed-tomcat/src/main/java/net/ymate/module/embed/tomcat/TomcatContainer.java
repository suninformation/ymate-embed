/*
 * Copyright 2007-2021 the original author or authors.
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
package net.ymate.module.embed.tomcat;

import net.ymate.module.embed.CommandLineHelper;
import net.ymate.module.embed.IContainer;
import org.apache.catalina.Context;
import org.apache.catalina.Host;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.ContextConfig;
import org.apache.catalina.startup.Tomcat;
import org.apache.coyote.ProtocolHandler;
import org.apache.coyote.http11.Http11NioProtocol;

import java.io.File;

/**
 * @author 刘镇 (suninformation@163.com) on 2021/03/23 20:58
 * @since 1.0.0
 */
public class TomcatContainer implements IContainer {

    private Tomcat tomcat;

    @Override
    public void start(String... args) {
        CommandLineHelper configs = CommandLineHelper.bind(args);
        //
        String hostName = configs.getString("hostName", "localhost");
        String protocol = configs.getString("protocol", "HTTP/1.1");
        String contextPath = configs.getString("contextPath", "");
        String uriEncoding = configs.getString("uriEncoding", "UTF-8");
        String fileEncoding = configs.getString("fileEncoding", "UTF-8");
        int port = configs.getInt("port", 8080);
        int asyncTimeout = configs.getInt("asyncTimeout", 30000);
        int connectionTimeout = configs.getInt("connectionTimeout", 20000);
        int maxConnections = configs.getInt("maxConnections", 10000);
        int maxThreads = configs.getInt("maxThreads", 200);
        //
        final File contextPathDir = new File(System.getProperty("embedded.home"));
        //
        tomcat = new Tomcat();
        tomcat.setPort(port);
        tomcat.setHostname(hostName);
        tomcat.setBaseDir(contextPathDir.getParentFile().getAbsolutePath());
        Connector connector = new Connector(protocol);
        connector.setPort(port);
        connector.setAsyncTimeout(asyncTimeout);
        connector.setURIEncoding(uriEncoding);
        connector.setUseBodyEncodingForURI(configs.has("useBodyEncodingForURI", true));
        connector.setProperty("file.encoding", fileEncoding);
        connector.setProperty("org.apache.catalina.STRICT_SERVLET_COMPLIANCE", "false");
        ProtocolHandler protocolHandler = connector.getProtocolHandler();
        if (protocolHandler instanceof Http11NioProtocol) {
            Http11NioProtocol http11NioProtocol = (Http11NioProtocol) protocolHandler;
            http11NioProtocol.setConnectionTimeout(connectionTimeout);
            http11NioProtocol.setMaxThreads(maxThreads);
            http11NioProtocol.setMaxConnections(maxConnections);
        }
        tomcat.setConnector(connector);

        Host host = tomcat.getHost();
        host.setAutoDeploy(false);

        Context context = tomcat.addContext(host, contextPath, contextPathDir.getAbsolutePath());
        context.setParentClassLoader(TomcatContainer.class.getClassLoader());
        context.addLifecycleListener(new ContextConfig());
        context.addLifecycleListener(new Tomcat.DefaultWebXmlListener());
        try {
            tomcat.start();
        } catch (LifecycleException e) {
            throw new Error(e.getMessage(), e);
        }
    }

    @Override
    public void stop() {
        if (tomcat != null) {
            try {
                tomcat.stop();
            } catch (LifecycleException e) {
                throw new Error(e.getMessage(), e);
            }
        }
    }
}
