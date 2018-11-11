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

import java.util.Map;

/**
 * @author 刘镇 (suninformation@163.com) on 2018/11/10 6:53 PM
 * @version 1.0
 */
public interface ITomcatConfig {

    String HOST_NAME = "host_name";

    String PORT = "port";

    String CONTEXT_NAME = "context_name";

    String CONTEXT_PATH = "context_path";

    String BASE_DIR = "base_dir";

    String DOC_BASE = "doc_base";

    String PROTOCOL = "protocol";

    String PROTOCOL_HANDLER_CLASS = "protocol_handler_class";

    String SCHEME = "scheme";

    String MAX_PARAMETER_COUNT = "max_parameter_count";

    String MAX_POST_SIZE = "max_post_size";

    String PARSE_BODY_METHODS = "parse_body_methods";

    //

    String MAX_THREADS = "maxThreads";

    String MAX_CONNECTIONS = "maxConnections";

    String URI_ENCODING = "URIEncoding";

    String CONNECTION_TIMEOUT = "connectionTimeout";

    String MAX_KEEP_ALIVE_REQUESTS = "maxKeepAliveRequests";

    String ALLOW_TRACE = "allowTrace";

    String ASYNC_TIMEOUT = "asyncTimeout";

    String ENABLE_LOOKUPS = "enableLookups";

    String MAX_HEADER_COUNT = "maxHeaderCount";

    String MAX_SAVE_POST_SIZE = "maxSavePostSize";

    String PROXY_NAME = "proxyName";

    String PROXY_PROT = "proxyPort";

    String REDIRECT_PORT = "redirectPort";

    String SECURE = "secure";

    String USE_BODY_ENCODING_FOR_URI = "useBodyEncodingForURI";

    String USE_IP_VHOSTS = "useIPVHosts";

    String X_POWERED_BY = "xpoweredBy";

    //

    String getHostName();

    int getPort();

    String getContextName();

    String getContextPath();

    String getBaseDir();

    String getDocBase();

    String getProtocol();

    String getProtocolHandlerClassName();

    String getScheme();

    int getMaxParameterCount();

    int getMaxPostSize();

    String getParseBodyMethods();

    Map<String, String> getAttributes();
}
