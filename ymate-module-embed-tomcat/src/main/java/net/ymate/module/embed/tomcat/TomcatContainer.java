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
import net.ymate.module.embed.Main;
import org.apache.catalina.Context;
import org.apache.catalina.Host;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.ContextConfig;
import org.apache.catalina.startup.Tomcat;
import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.apache.tomcat.util.net.SSLHostConfig;
import org.apache.tomcat.util.net.SSLHostConfigCertificate;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.ServiceLoader;

/**
 * @author 刘镇 (suninformation@163.com) on 2021/03/23 20:58
 * @since 1.0.0
 */
public class TomcatContainer implements IContainer {

    private final static String CONFIG_KEY_HOST_NAME = "hostName";

    private final static String CONFIG_KEY_PROTOCOL = "protocol";

    private final static String CONFIG_KEY_CONTEXT_PATH = "contextPath";

    private final static String CONFIG_KEY_URI_ENCODING = "uriEncoding";

    private final static String CONFIG_KEY_FILE_ENCODING = "fileEncoding";

    private final static String CONFIG_KEY_PORT = "port";

    private final static String CONFIG_KEY_ASYNC_TIMEOUT = "asyncTimeout";

    private final static String CONFIG_KEY_CONNECTION_TIMEOUT = "connectionTimeout";

    private final static String CONFIG_KEY_MAX_CONNECTIONS = "maxConnections";

    private final static String CONFIG_KEY_MAX_THREADS = "maxThreads";

    private final static String CONFIG_KEY_USE_BODY_ENCODING_FOR_URI = "useBodyEncodingForURI";

    private final static String CONFIG_KEY_MAX_COOKIE_COUNT = "maxCookieCount";

    private final static String CONFIG_KEY_SCHEME = "scheme";

    private final static String CONFIG_KEY_ALLOW_TRACE = "allowTrace";

    private final static String CONFIG_KEY_DISCARD_FACADES = "discardFacades";

    private final static String CONFIG_KEY_ENABLE_LOOKUPS = "enableLookups";

    private final static String CONFIG_KEY_MAX_PARAMETER_COUNT = "maxParameterCount";

    private final static String CONFIG_KEY_MAX_POST_SIZE = "maxPostSize";

    private final static String CONFIG_KEY_PARSE_BODY_METHODS = "parseBodyMethods";

    private final static String CONFIG_KEY_PROXY_NAME = "proxyName";

    private final static String CONFIG_KEY_PROXY_PORT = "proxyPort";

    private final static String CONFIG_KEY_REDIRECT_PORT = "redirectPort";

    private final static String CONFIG_KEY_SECURE = "secure";

    private final static String CONFIG_KEY_DOMAIN = "domain";

    private final static String CONFIG_KEY_SERVER = "server";

    private final static String CONFIG_KEY_USE_IP_VHOSTS = "useIPVHosts";

    private final static String CONFIG_KEY_THROW_ON_FAILURE = "throwOnFailure";

    private final static String CONFIG_KEY_PROCESSOR_CACHE = "processorCache";

    private final static String CONFIG_KEY_CONNECTION_UPLOAD_TIMEOUT = "connectionUploadTimeout";

    private final static String CONFIG_KEY_CONTINUE_RESPONSE_TIMING = "continueResponseTiming";

    private final static String CONFIG_KEY_DEFAULT_SSL_HOST_CONFIG_NAME = "defaultSSLHostConfigName";

    private final static String CONFIG_KEY_DISABLE_UPLOAD_TIMEOUT = "disableUploadTimeout";

    private final static String CONFIG_KEY_MAX_EXTENSION_SIZE = "maxExtensionSize";

    private final static String CONFIG_KEY_MAX_HTTP_HEADER_SIZE = "maxHttpHeaderSize";

    private final static String CONFIG_KEY_MAX_KEEP_ALIVE_REQUESTS = "maxKeepAliveRequests";

    private final static String CONFIG_KEY_MAX_SAVE_POST_SIZE = "maxSavePostSize";

    private final static String CONFIG_KEY_MAX_SWALLOW_SIZE = "maxSwallowSize";

    private final static String CONFIG_KEY_MAX_TRAILER_SIZE = "maxTrailerSize";

    private final static String CONFIG_KEY_SERVER_REMOVE_APP_PROVIDED_VALUES = "serverRemoveAppProvidedValues";

    private final static String CONFIG_KEY_SSL_ENABLED = "SSLEnabled";

    private final static String CONFIG_KEY_SSL_VERIFY_DEPTH = "SSLVerifyDepth";

    private final static String CONFIG_KEY_TRUST_MANAGER_CLASS_NAME = "trustManagerClassName";

    private final static String CONFIG_KEY_CIPHERS = "ciphers";

    private final static String CONFIG_KEY_CLIENT_AUTH = "clientAuth";

    private final static String CONFIG_KEY_KEY_ALIAS = "keyAlias";

    private final static String CONFIG_KEY_KEY_PASS = "keyPass";

    private final static String CONFIG_KEY_KEYSTORE_FILE = "keystoreFile";

    private final static String CONFIG_KEY_KEYSTORE_PASS = "keystorePass";

    private final static String CONFIG_KEY_KEYSTORE_PROVIDER = "keystoreProvider";

    private final static String CONFIG_KEY_KEYSTORE_TYPE = "keystoreType";

    private final static String CONFIG_KEY_SESSION_CACHE_SIZE = "sessionCacheSize";

    private final static String CONFIG_KEY_SESSION_TIMEOUT = "sessionTimeout";

    private final static String CONFIG_KEY_CA_CERTIFICATE_FILE = "caCertificateFile";

    private final static String CONFIG_KEY_CA_CERTIFICATE_PATH = "caCertificatePath";

    private final static String CONFIG_KEY_CA_REVOCATION_FILE = "caRevocationFile";

    private final static String CONFIG_KEY_CA_REVOCATION_PATH = "caRevocationPath";

    private final static String CONFIG_KEY_SSL_PROTOCOL = "SSLProtocol";

    private final static String CONFIG_KEY_SSL_PROTOCOL_2 = "sslProtocol";

    private final static String CONFIG_KEY_CERTIFICATE_CHAIN_FILE = "certificateChainFile";

    private final static String CONFIG_KEY_CERTIFICATE_FILE = "certificateFile";

    private final static String CONFIG_KEY_CERTIFICATE_KEY_FILE = "certificateKeyFile";

    private final static String CONFIG_KEY_DISABLE_COMPRESSION = "disableCompression";

    private final static String CONFIG_KEY_DISABLE_SESSION_TICKETS = "disableSessionTickets";

    private final static String CONFIG_KEY_USE_KEEP_ALIVE_RESPONSE_HEADER = "useKeepAliveResponseHeader";

    private final static String CONFIG_KEY_ACCEPT_COUNT = "acceptCount";

    private final static String CONFIG_KEY_ACCEPTOR_THREAD_PRIORITY = "acceptorThreadPriority";

    private final static String CONFIG_KEY_CLIENT_CERT_PROVIDER = "clientCertProvider";

    private final static String CONFIG_KEY_MAX_HEADER_COUNT = "maxHeaderCount";

    private final static String CONFIG_KEY_KEEP_ALIVE_TIMEOUT = "keepAliveTimeout";

    private volatile Tomcat tomcat;

    @Override
    public void start(String... args) {
        CommandLineHelper configs = CommandLineHelper.bind(args);
        // 处理配置文件参数
        String confFileName = configs.getString("conf", "tomcat");
        try (InputStream inputStream = TomcatContainer.class
                .getResourceAsStream(String.format("/%s.properties", confFileName))) {
            if (inputStream != null) {
                Properties tomcatConf = new Properties();
                tomcatConf.load(inputStream);
                if (!tomcatConf.isEmpty()) {
                    configs.appendIfNotExist(tomcatConf);
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load Tomcat configuration from " + confFileName + ".properties",
                    e);
        }
        //
        int port = configs.getIntValue(CONFIG_KEY_PORT, 8080);
        Boolean secure = configs.getBoolean(CONFIG_KEY_SECURE);
        //
        final File contextPathDir = new File(Main.getEmbeddedHome());
        //
        tomcat = new Tomcat();
        tomcat.setPort(port);
        tomcat.setHostname(configs.getString(CONFIG_KEY_HOST_NAME, "localhost"));
        tomcat.setBaseDir(contextPathDir.getParentFile().getAbsolutePath());
        //
        Connector connector = new Connector(configs.getString(CONFIG_KEY_PROTOCOL, "HTTP/1.1"));
        connector.setPort(port);
        connector.setURIEncoding(configs.getString(CONFIG_KEY_URI_ENCODING, "UTF-8"));
        connector.setUseBodyEncodingForURI(configs.has(CONFIG_KEY_USE_BODY_ENCODING_FOR_URI, true));
        connector.setScheme(configs.getString(CONFIG_KEY_SCHEME, "http"));
        if (secure != null) {
            connector.setSecure(secure);
        }
        connector.setUseIPVHosts(configs.getBooleanValue(CONFIG_KEY_USE_IP_VHOSTS, false));
        connector.setAllowTrace(configs.getBooleanValue(CONFIG_KEY_ALLOW_TRACE, false));
        connector.setEnableLookups(configs.getBooleanValue(CONFIG_KEY_ENABLE_LOOKUPS, false));
        connector.setProperty("file.encoding", configs.getString(CONFIG_KEY_FILE_ENCODING, "UTF-8"));
        connector.setProperty("org.apache.catalina.STRICT_SERVLET_COMPLIANCE", "false");
        //
        int asyncTimeout = configs.getIntValue(CONFIG_KEY_ASYNC_TIMEOUT);
        if (asyncTimeout > 0) {
            connector.setAsyncTimeout(asyncTimeout);
        }
        int maxCookieCount = configs.getIntValue(CONFIG_KEY_MAX_COOKIE_COUNT);
        if (maxCookieCount > 0) {
            connector.setMaxCookieCount(maxCookieCount);
        }
        Boolean discardFacades = configs.getBoolean(CONFIG_KEY_DISCARD_FACADES);
        if (discardFacades != null) {
            connector.setDiscardFacades(discardFacades);
        }
        int maxParameterCount = configs.getIntValue(CONFIG_KEY_MAX_PARAMETER_COUNT);
        if (maxParameterCount > 0) {
            connector.setMaxParameterCount(maxParameterCount);
        }
        int maxPostSize = configs.getIntValue(CONFIG_KEY_MAX_POST_SIZE);
        if (maxPostSize > 0) {
            connector.setMaxPostSize(maxPostSize);
        }
        String parseBodyMethods = configs.getString(CONFIG_KEY_PARSE_BODY_METHODS, null);
        if (parseBodyMethods != null) {
            connector.setParseBodyMethods(parseBodyMethods);
        }
        String proxyName = configs.getString(CONFIG_KEY_PROXY_NAME, null);
        if (proxyName != null) {
            connector.setProxyName(proxyName);
        }
        int proxyPort = configs.getIntValue(CONFIG_KEY_PROXY_PORT);
        if (proxyPort > 0) {
            connector.setProxyPort(proxyPort);
        }
        int redirectPort = configs.getIntValue(CONFIG_KEY_REDIRECT_PORT);
        if (redirectPort > 0) {
            connector.setRedirectPort(redirectPort);
        }
        String domain = configs.getString(CONFIG_KEY_DOMAIN, null);
        if (domain != null) {
            connector.setDomain(domain);
        }
        Boolean throwOnFailure = configs.getBoolean(CONFIG_KEY_THROW_ON_FAILURE);
        if (throwOnFailure != null) {
            connector.setThrowOnFailure(throwOnFailure);
        }
        //
        if (connector.getProtocolHandler() instanceof AbstractHttp11Protocol) {
            AbstractHttp11Protocol<?> http11NioProtocol = (AbstractHttp11Protocol<?>) connector.getProtocolHandler();
            int connectionTimeout = configs.getIntValue(CONFIG_KEY_CONNECTION_TIMEOUT);
            if (connectionTimeout > 0) {
                http11NioProtocol.setConnectionTimeout(connectionTimeout);
            }
            int maxThreads = configs.getIntValue(CONFIG_KEY_MAX_THREADS);
            if (maxThreads > 0) {
                http11NioProtocol.setMaxThreads(maxThreads);
            }
            int maxConnections = configs.getIntValue(CONFIG_KEY_MAX_CONNECTIONS);
            if (maxConnections > 0) {
                http11NioProtocol.setMaxConnections(maxConnections);
            }
            int connectionUploadTimeout = configs.getIntValue(CONFIG_KEY_CONNECTION_UPLOAD_TIMEOUT);
            if (connectionUploadTimeout > 0) {
                http11NioProtocol.setConnectionUploadTimeout(connectionUploadTimeout);
            }
            String continueResponseTiming = configs.getString(CONFIG_KEY_CONTINUE_RESPONSE_TIMING, null);
            if (continueResponseTiming != null) {
                http11NioProtocol.setContinueResponseTiming(continueResponseTiming);
            }
            String defaultSSLHostConfigName = configs.getString(CONFIG_KEY_DEFAULT_SSL_HOST_CONFIG_NAME, null);
            if (defaultSSLHostConfigName != null) {
                http11NioProtocol.setDefaultSSLHostConfigName(defaultSSLHostConfigName);
            }
            Boolean disableUploadTimeout = configs.getBoolean(CONFIG_KEY_DISABLE_UPLOAD_TIMEOUT);
            if (disableUploadTimeout != null) {
                http11NioProtocol.setDisableUploadTimeout(disableUploadTimeout);
            }
            int maxExtensionSize = configs.getIntValue(CONFIG_KEY_MAX_EXTENSION_SIZE);
            if (maxExtensionSize > 0) {
                http11NioProtocol.setMaxExtensionSize(maxExtensionSize);
            }
            int maxHttpHeaderSize = configs.getIntValue(CONFIG_KEY_MAX_HTTP_HEADER_SIZE);
            if (maxHttpHeaderSize > 0) {
                http11NioProtocol.setMaxHttpHeaderSize(maxHttpHeaderSize);
            }
            int maxKeepAliveRequests = configs.getIntValue(CONFIG_KEY_MAX_KEEP_ALIVE_REQUESTS);
            if (maxKeepAliveRequests > 0) {
                http11NioProtocol.setMaxKeepAliveRequests(maxKeepAliveRequests);
            }
            int maxSavePostSize = configs.getIntValue(CONFIG_KEY_MAX_SAVE_POST_SIZE);
            if (maxSavePostSize > 0) {
                http11NioProtocol.setMaxSavePostSize(maxSavePostSize);
            }
            int maxSwallowSize = configs.getIntValue(CONFIG_KEY_MAX_SWALLOW_SIZE);
            if (maxSwallowSize > 0) {
                http11NioProtocol.setMaxSwallowSize(maxSwallowSize);
            }
            int maxTrailerSize = configs.getIntValue(CONFIG_KEY_MAX_TRAILER_SIZE);
            if (maxTrailerSize > 0) {
                http11NioProtocol.setMaxTrailerSize(maxTrailerSize);
            }
            if (secure != null) {
                http11NioProtocol.setSecure(secure);
            }
            String server = configs.getString(CONFIG_KEY_SERVER, null);
            if (server != null) {
                http11NioProtocol.setServer(server);
            }
            Boolean serverRemoveAppProvidedValues = configs.getBoolean(CONFIG_KEY_SERVER_REMOVE_APP_PROVIDED_VALUES);
            if (serverRemoveAppProvidedValues != null) {
                http11NioProtocol.setServerRemoveAppProvidedValues(serverRemoveAppProvidedValues);
            }
            Boolean useKeepAliveResponseHeader = configs.getBoolean(CONFIG_KEY_USE_KEEP_ALIVE_RESPONSE_HEADER);
            if (useKeepAliveResponseHeader != null) {
                http11NioProtocol.setUseKeepAliveResponseHeader(useKeepAliveResponseHeader);
            }
            int acceptCount = configs.getIntValue(CONFIG_KEY_ACCEPT_COUNT);
            if (acceptCount > 0) {
                http11NioProtocol.setAcceptCount(acceptCount);
            }
            int acceptorThreadPriority = configs.getIntValue(CONFIG_KEY_ACCEPTOR_THREAD_PRIORITY);
            if (acceptorThreadPriority > 0) {
                http11NioProtocol.setAcceptorThreadPriority(acceptorThreadPriority);
            }
            String clientCertProvider = configs.getString(CONFIG_KEY_CLIENT_CERT_PROVIDER, null);
            if (clientCertProvider != null) {
                http11NioProtocol.setClientCertProvider(clientCertProvider);
            }
            int keepAliveTimeout = configs.getIntValue(CONFIG_KEY_KEEP_ALIVE_TIMEOUT);
            if (keepAliveTimeout > 0) {
                http11NioProtocol.setKeepAliveTimeout(keepAliveTimeout);
            }
            int maxHeaderCount = configs.getIntValue(CONFIG_KEY_MAX_HEADER_COUNT);
            if (maxHeaderCount > 0) {
                http11NioProtocol.setMaxHeaderCount(maxHeaderCount);
            }
            int processorCache = configs.getIntValue(CONFIG_KEY_PROCESSOR_CACHE);
            if (processorCache > 0) {
                http11NioProtocol.setProcessorCache(processorCache);
            }
            Boolean SSLEnabled = configs.getBoolean(CONFIG_KEY_SSL_ENABLED);
            if (SSLEnabled != null) {
                http11NioProtocol.setSSLEnabled(SSLEnabled);
                if (SSLEnabled) {
                    SSLHostConfig sslHostConfig = new SSLHostConfig();
                    String SSLProtocol = configs.getString(CONFIG_KEY_SSL_PROTOCOL, null);
                    if (SSLProtocol != null) {
                        sslHostConfig.setProtocols(SSLProtocol);
                    }
                    String sslProtocol = configs.getString(CONFIG_KEY_SSL_PROTOCOL_2, null);
                    if (sslProtocol != null) {
                        sslHostConfig.setSslProtocol(sslProtocol);
                    }
                    String ciphers = configs.getString(CONFIG_KEY_CIPHERS, null);
                    if (ciphers != null) {
                        sslHostConfig.setCiphers(ciphers);
                    }
                    String clientAuth = configs.getString(CONFIG_KEY_CLIENT_AUTH, null);
                    if (clientAuth != null) {
                        sslHostConfig.setCertificateVerification(clientAuth);
                    }
                    int SSLVerifyDepth = configs.getIntValue(CONFIG_KEY_SSL_VERIFY_DEPTH);
                    if (SSLVerifyDepth > 0) {
                        sslHostConfig.setCertificateVerificationDepth(SSLVerifyDepth);
                    }
                    String trustManagerClassName = configs.getString(CONFIG_KEY_TRUST_MANAGER_CLASS_NAME, null);
                    if (trustManagerClassName != null) {
                        sslHostConfig.setTrustManagerClassName(trustManagerClassName);
                    }
                    int sessionCacheSize = configs.getIntValue(CONFIG_KEY_SESSION_CACHE_SIZE);
                    if (sessionCacheSize > 0) {
                        sslHostConfig.setSessionCacheSize(sessionCacheSize);
                    }
                    int sessionTimeout = configs.getIntValue(CONFIG_KEY_SESSION_TIMEOUT);
                    if (sessionTimeout > 0) {
                        sslHostConfig.setSessionTimeout(sessionTimeout);
                    }
                    Boolean disableCompression = configs.getBoolean(CONFIG_KEY_DISABLE_COMPRESSION);
                    if (disableCompression != null) {
                        sslHostConfig.setDisableCompression(disableCompression);
                    }
                    Boolean disableSessionTickets = configs.getBoolean(CONFIG_KEY_DISABLE_SESSION_TICKETS);
                    if (disableSessionTickets != null) {
                        sslHostConfig.setDisableSessionTickets(disableSessionTickets);
                    }
                    String caCertificateFile = configs.getString(CONFIG_KEY_CA_CERTIFICATE_FILE, null);
                    if (caCertificateFile != null) {
                        sslHostConfig.setCaCertificateFile(Main.replaceEnvVariable(caCertificateFile));
                    }
                    String caCertificatePath = configs.getString(CONFIG_KEY_CA_CERTIFICATE_PATH, null);
                    if (caCertificatePath != null) {
                        sslHostConfig.setCaCertificatePath(caCertificatePath);
                    }
                    String caRevocationFile = configs.getString(CONFIG_KEY_CA_REVOCATION_FILE, null);
                    if (caRevocationFile != null) {
                        sslHostConfig.setCertificateRevocationListFile(Main.replaceEnvVariable(caRevocationFile));
                    }
                    String caRevocationPath = configs.getString(CONFIG_KEY_CA_REVOCATION_PATH, null);
                    if (caRevocationPath != null) {
                        sslHostConfig.setCertificateRevocationListPath(Main.replaceEnvVariable(caRevocationPath));
                    }
                    //
                    SSLHostConfigCertificate certificate = new SSLHostConfigCertificate(sslHostConfig, SSLHostConfigCertificate.Type.RSA);
                    String certificateFile = configs.getString(CONFIG_KEY_CERTIFICATE_FILE, null);
                    if (certificateFile != null) {
                        certificate.setCertificateFile(Main.replaceEnvVariable(certificateFile));
                    }
                    String certificateKeyFile = configs.getString(CONFIG_KEY_CERTIFICATE_KEY_FILE, null);
                    if (certificateKeyFile != null) {
                        certificate.setCertificateKeyFile(Main.replaceEnvVariable(certificateKeyFile));
                    }
                    String certificateChainFile = configs.getString(CONFIG_KEY_CERTIFICATE_CHAIN_FILE, null);
                    if (certificateChainFile != null) {
                        certificate.setCertificateChainFile(Main.replaceEnvVariable(certificateChainFile));
                    }
                    String keyAlias = configs.getString(CONFIG_KEY_KEY_ALIAS, null);
                    if (keyAlias != null) {
                        certificate.setCertificateKeyAlias(keyAlias);
                    }
                    String keyPass = configs.getString(CONFIG_KEY_KEY_PASS, null);
                    if (keyPass != null) {
                        certificate.setCertificateKeyPassword(keyPass);
                    }
                    String keystoreFile = configs.getString(CONFIG_KEY_KEYSTORE_FILE, null);
                    if (keystoreFile != null) {
                        certificate.setCertificateKeystoreFile(Main.replaceEnvVariable(keystoreFile));
                    }
                    String keystorePass = configs.getString(CONFIG_KEY_KEYSTORE_PASS, null);
                    if (keystorePass != null) {
                        certificate.setCertificateKeystorePassword(keystorePass);
                    }
                    String keystoreProvider = configs.getString(CONFIG_KEY_KEYSTORE_PROVIDER, null);
                    if (keystoreProvider != null) {
                        certificate.setCertificateKeystoreProvider(keystoreProvider);
                    }
                    String keystoreType = configs.getString(CONFIG_KEY_KEYSTORE_TYPE, null);
                    if (keystoreType != null) {
                        certificate.setCertificateKeystoreType(keystoreType);
                    }
                    sslHostConfig.addCertificate(certificate);
                    http11NioProtocol.addSslHostConfig(sslHostConfig);
                }
            }
        }
        tomcat.setConnector(connector);

        Host host = tomcat.getHost();
        host.setAutoDeploy(false);

        Context context = tomcat.addContext(host, configs.getString(CONFIG_KEY_CONTEXT_PATH, CommandLineHelper.EMPTY), contextPathDir.getAbsolutePath());
        context.setParentClassLoader(TomcatContainer.class.getClassLoader());
        context.addLifecycleListener(new ContextConfig());
        context.addLifecycleListener(new Tomcat.DefaultWebXmlListener());
        try {
            ServiceLoader<ITomcatCustomizer> tomcatCustomizers = ServiceLoader.load(ITomcatCustomizer.class);
            for (ITomcatCustomizer customizer : tomcatCustomizers) {
                if (customizer != null) {
                    try {
                        customizer.customize(configs, tomcat, connector, host, context);
                    } catch (Exception e) {
                        System.err.println("Failed to apply customizer: " + customizer.getClass().getName());
                        e.printStackTrace();
                    }
                }
            }
            tomcat.start();
        } catch (LifecycleException e) {
            throw new IllegalStateException("Failed to start Tomcat container", e);
        }
    }

    @Override
    public void stop() {
        Tomcat localTomcat = this.tomcat;
        if (localTomcat != null) {
            try {
                localTomcat.stop();
                localTomcat.destroy();
            } catch (LifecycleException e) {
                throw new IllegalStateException("Failed to stop Tomcat container", e);
            } finally {
                this.tomcat = null;
            }
        }
    }
}
