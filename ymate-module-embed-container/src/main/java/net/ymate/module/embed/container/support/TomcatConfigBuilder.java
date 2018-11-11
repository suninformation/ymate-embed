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
package net.ymate.module.embed.container.support;

import net.ymate.module.embed.container.ITomcatConfig;
import net.ymate.platform.core.YMP;
import net.ymate.platform.core.lang.BlurObject;
import net.ymate.platform.core.util.RuntimeUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 刘镇 (suninformation@163.com) on 2018/11/10 11:01 PM
 * @version 1.0
 */
public final class TomcatConfigBuilder {

    private String hostName;

    private int port;

    private String contextName;

    private String contextPath;

    private String baseDir;

    private String docBase;

    private String protocol;

    private String protocolHandlerClassName;

    private String scheme;

    private int maxParameterCount;

    private int maxPostSize;

    private String parseBodyMethods;

    private Map<String, String> attributes = new HashMap<String, String>();

    public static TomcatConfigBuilder create() {
        return new TomcatConfigBuilder();
    }

    public static TomcatConfigBuilder create(Map<String, String> configs) {
        return create().hostName(configs.get(ITomcatConfig.HOST_NAME))
                .port(BlurObject.bind(configs.get(ITomcatConfig.PORT)).toIntValue())
                .contextName(configs.get(ITomcatConfig.CONTEXT_NAME))
                .contextPath(configs.get(ITomcatConfig.CONTEXT_PATH))
                .baseDir(configs.get(ITomcatConfig.BASE_DIR))
                .docBase(configs.get(ITomcatConfig.DOC_BASE))
                .protocol(configs.get(ITomcatConfig.PROTOCOL))
                .protocolHandlerClassName(configs.get(ITomcatConfig.PROTOCOL_HANDLER_CLASS))
                .scheme(configs.get(ITomcatConfig.SCHEME))
                .maxParameterCount(BlurObject.bind(configs.get(ITomcatConfig.MAX_PARAMETER_COUNT)).toIntValue())
                .maxPostSize(BlurObject.bind(configs.get(ITomcatConfig.MAX_POST_SIZE)).toIntValue())
                .parseBodyMethods(configs.get(ITomcatConfig.PARSE_BODY_METHODS))
                .addAttributes(RuntimeUtils.keyStartsWith(configs, "connector."));
    }

    private TomcatConfigBuilder() {
    }

    public TomcatConfigBuilder hostName(String hostName) {
        this.hostName = hostName;
        return this;
    }

    public TomcatConfigBuilder port(int port) {
        this.port = port;
        return this;
    }

    public TomcatConfigBuilder contextName(String contextName) {
        this.contextName = contextName;
        return this;
    }

    public TomcatConfigBuilder contextPath(String contextPath) {
        this.contextPath = contextPath;
        return this;
    }

    public TomcatConfigBuilder baseDir(String baseDir) {
        this.baseDir = baseDir;
        return this;
    }

    public TomcatConfigBuilder docBase(String docBase) {
        this.docBase = docBase;
        return this;
    }

    public TomcatConfigBuilder protocol(String protocol) {
        this.protocol = protocol;
        return this;
    }

    public TomcatConfigBuilder protocolHandlerClassName(String protocolHandlerClassName) {
        this.protocolHandlerClassName = protocolHandlerClassName;
        return this;
    }

    public TomcatConfigBuilder scheme(String scheme) {
        this.scheme = scheme;
        return this;
    }

    public TomcatConfigBuilder maxParameterCount(int maxParameterCount) {
        this.maxParameterCount = maxParameterCount;
        return this;
    }

    public TomcatConfigBuilder maxPostSize(int maxPostSize) {
        this.maxPostSize = maxPostSize;
        return this;
    }

    public TomcatConfigBuilder parseBodyMethods(String parseBodyMethods) {
        this.parseBodyMethods = parseBodyMethods;
        return this;
    }

    public TomcatConfigBuilder addAttribute(String key, String value) {
        this.attributes.put(key, value);
        return this;
    }

    public TomcatConfigBuilder addAttributes(Map<String, String> attributes) {
        this.attributes.putAll(attributes);
        return this;
    }

    private void __putValueIfNeed(String key, String value) {
        if (!this.attributes.containsKey(key)) {
            this.attributes.put(key, value);
        }
    }

    //

    public TomcatConfigBuilder maxThreads(int maxThreads) {
        return addAttribute(ITomcatConfig.MAX_THREADS, String.valueOf(maxThreads));
    }

    public TomcatConfigBuilder maxConnections(int maxConnections) {
        return addAttribute(ITomcatConfig.MAX_CONNECTIONS, String.valueOf(maxConnections));
    }

    public TomcatConfigBuilder maxHeaderCount(int maxHeaderCount) {
        return addAttribute(ITomcatConfig.MAX_HEADER_COUNT, String.valueOf(maxHeaderCount));
    }

    public TomcatConfigBuilder maxSavePostSize(int maxSavePostSize) {
        return addAttribute(ITomcatConfig.MAX_SAVE_POST_SIZE, String.valueOf(maxSavePostSize));
    }

    public TomcatConfigBuilder maxKeepAliveRequests(int maxKeepAliveRequests) {
        return addAttribute(ITomcatConfig.MAX_KEEP_ALIVE_REQUESTS, String.valueOf(maxKeepAliveRequests));
    }

    public TomcatConfigBuilder connectionTimeout(long connectionTimeout) {
        return addAttribute(ITomcatConfig.CONNECTION_TIMEOUT, String.valueOf(connectionTimeout));
    }

    public TomcatConfigBuilder asyncTimeout(long asyncTimeout) {
        return addAttribute(ITomcatConfig.ASYNC_TIMEOUT, String.valueOf(asyncTimeout));
    }

    public TomcatConfigBuilder allowTrace(boolean allowTrace) {
        return addAttribute(ITomcatConfig.ALLOW_TRACE, String.valueOf(allowTrace));
    }

    public TomcatConfigBuilder enableLookups(boolean enableLookups) {
        return addAttribute(ITomcatConfig.ENABLE_LOOKUPS, String.valueOf(enableLookups));
    }

    public TomcatConfigBuilder secure(boolean secure) {
        return addAttribute(ITomcatConfig.SECURE, String.valueOf(secure));
    }

    public TomcatConfigBuilder URIEncoding(String URIEncoding) {
        return addAttribute(ITomcatConfig.URI_ENCODING, URIEncoding);
    }

    public TomcatConfigBuilder useBodyEncodingForURI(boolean useBodyEncodingForURI) {
        return addAttribute(ITomcatConfig.USE_BODY_ENCODING_FOR_URI, String.valueOf(useBodyEncodingForURI));
    }

    public TomcatConfigBuilder useIPVHosts(boolean useIPVHosts) {
        return addAttribute(ITomcatConfig.USE_IP_VHOSTS, String.valueOf(useIPVHosts));
    }

    public TomcatConfigBuilder proxyName(String proxyName) {
        return addAttribute(ITomcatConfig.PROXY_NAME, proxyName);
    }

    public TomcatConfigBuilder proxyPort(int proxyPort) {
        return addAttribute(ITomcatConfig.PROXY_PROT, String.valueOf(proxyPort));
    }

    public TomcatConfigBuilder redirectPort(int redirectPort) {
        return addAttribute(ITomcatConfig.REDIRECT_PORT, String.valueOf(redirectPort));
    }

    public TomcatConfigBuilder xPoweredBy(String xpoweredBy) {
        return addAttribute(ITomcatConfig.X_POWERED_BY, xpoweredBy);
    }

    //

    public ITomcatConfig build() {
        //
        contextPath = StringUtils.trimToEmpty(contextPath);
        baseDir = StringUtils.defaultIfBlank(baseDir, new File(System.getProperty("java.io.tmpdir")).getAbsolutePath());
        hostName = StringUtils.defaultIfBlank(hostName, "localhost");
        //
        if (port <= 0) {
            port = 8080;
        }
        //
        __putValueIfNeed(ITomcatConfig.PROTOCOL, "org.apache.coyote.http11.Http11NioProtocol");
        __putValueIfNeed(ITomcatConfig.MAX_THREADS, "1000");
        __putValueIfNeed(ITomcatConfig.MAX_CONNECTIONS, "-1");
        __putValueIfNeed(ITomcatConfig.URI_ENCODING, "UTF-8");
        __putValueIfNeed(ITomcatConfig.CONNECTION_TIMEOUT, "60000");
        //
        __putValueIfNeed(ITomcatConfig.X_POWERED_BY, "YMP-" + YMP.VERSION);
        //
        return new ITomcatConfig() {

            @Override
            public String getHostName() {
                return hostName;
            }

            @Override
            public int getPort() {
                return port;
            }

            @Override
            public String getContextName() {
                return contextName;
            }

            @Override
            public String getContextPath() {
                return contextPath;
            }

            @Override
            public String getBaseDir() {
                return baseDir;
            }

            @Override
            public String getDocBase() {
                return docBase;
            }

            @Override
            public String getProtocol() {
                return protocol;
            }

            @Override
            public String getProtocolHandlerClassName() {
                return protocolHandlerClassName;
            }

            @Override
            public String getScheme() {
                return scheme;
            }

            @Override
            public int getMaxParameterCount() {
                return maxParameterCount;
            }

            @Override
            public int getMaxPostSize() {
                return maxPostSize;
            }

            @Override
            public String getParseBodyMethods() {
                return parseBodyMethods;
            }

            @Override
            public Map<String, String> getAttributes() {
                return Collections.unmodifiableMap(attributes);
            }
        };
    }
}
