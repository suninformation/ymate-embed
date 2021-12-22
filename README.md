# YMATE-EMBED

[![Maven Central status](https://img.shields.io/maven-central/v/net.ymate.module/ymate-module-embed.svg)](https://search.maven.org/#search%7Cga%7C1%7Cnet.ymate.platform)
[![LICENSE](https://img.shields.io/github/license/suninformation/ymate-embed.svg)](https://gitee.com/suninformation/ymate-embed/blob/master/LICENSE)

本项目为可执行嵌入式 Web 容器，在原始 WAR 包文件结构的基础上为其指定引导程序及相关依赖文件，并通过命令行方式直接启动 Web 服务，从而达到简化 Web 工程部署流程的目的。

目前支持以下 Web 容器：

- [Apache Tomcat® - 8.5.x / 9.0.x](https://tomcat.apache.org/) 


## 命令行格式

```shell
java -jar <WAR包文件> [--参数项 [参数值]]
```



**示例：**

```shell
java -jar demo.war --port 8088 --contextPath /demo --redeploy
```


> 提示：
> 
> 项目文件在运行时将默认被释放到 `${user.home}/.embeddedWorks` 目录下，若需更改请在运行时调整 `${user.home}` 路径设置，示例如下：
> 
> ```shell
> java -jar -Duser.home=/Temp/webapps demo.war --port 8088 --contextPath /demo
> ```



## 命令参数项

| 参数项                  | 说明 | 默认值    |
| ----------------------- | ---- | --------- |
| --redeploy              | 重新部署<br />*服务启动时将 WAR 包中的相关资源文件重新解压并覆盖* |      |
| --cleanup               | 清理部署目录<br />*删除上次部署的内容* | |
| --conf | 指定配置文件名称（不包含扩展名） | `tomcat` |



## 服务参数项

以下参数项可以通过命令行或使用文件进行配置，若两者同时存在则命令行配置优先，该文件必须放置在类路径下，默认名称为：`tomcat.properties` ，允许自定义名称，如：`test.properties`，但需要通过命令行参数项 `--conf test` 使其生效。

| 参数项                  | 说明 | 默认值    |
| ----------------------- | ---- | --------- |
| --port                  | 服务监听的端口号 | `8080`      |
| --protocol              | 协议名称 | `HTTP/1.1`  |
| --contextPath           | 上下文映射路径 | `""` |
| --hostName              | 服务的主机名称 | `localhost` |
| --uriEncoding           | URI 请求的编码格式 | `UTF-8`     |
| --fileEncoding          | 文件编码格式 | `UTF-8`     |
| --asyncTimeout          | 异步请求的默认超时时间（毫秒） | `30000`     |
| --connectionTimeout     | 连接的默认超时时间（毫秒） | `20000`     |
| --maxConnections        | 最大连接数 | `10000`     |
| --maxThreads            | 最大工作线程数 | `200`       |
| --useBodyEncodingForURI | 请求参数的编码方式采用请求体的编码方式 | `true`      |
| --maxCookieCount | The maximum number of cookies permitted for a request. Use a value less than zero for no limit. | `200` |
| --scheme | Set the scheme that will be assigned to requests received through this connector. | `http` |
| --allowTrace | Set the allowTrace flag, to disable or enable the TRACE HTTP method. | `false` |
| --discardFacades | Set the recycling strategy for the object facades. | `false` |
| --enableLookups | Set the "enable DNS lookups" flag. | `false` |
| --maxParameterCount | Set the maximum number of parameters (GET plus POST) that will be automatically parsed by the container. A value of less than 0 means no limit. | `10000` |
| --maxPostSize | Set the maximum size of a POST which will be automatically parsed by the container. | `2097152` |
| --parseBodyMethods | Set list of HTTP methods which should allow body parameter parsing. | `POST` |
| --proxyName | Set the proxy server name for this Connector. |  |
| --proxyPort | Set the proxy server port for this Connector. | `0` |
| --redirectPort | The redirect port for non-SSL to SSL redirects. | `443` |
| --secure | The secure connection flag that will be set on all requests received through this connector. | `false` |
| --domain | Specify the domain under which this component should be registered. |  |
| --server | Set the server header name. |  |
| --useIPVHosts | Enable the use of IP-based virtual hosting. | `false` |
| --throwOnFailure | Configure if a `LifecycleException` thrown by a sub-class during `initInternal()` , `startInternal()` , `stopInternal()` or `destroyInternal()` will be re-thrown for the caller to handle or if it will be logged instead. | `true` |
| --ciphers | Set the new cipher configuration. Note: Regardless of the format used to set the configuration, it is always stored in OpenSSL format. |  |
| --processorCache | The maximum number of idle processors that will be retained in the cache and re-used with a subsequent request. A value of -1 means unlimited. | `200` |
| --allowHostHeaderMismatch | Will Tomcat accept an HTTP 1.1 request where the host header does not agree with the host specified (if any) in the request line? | `true` |
| --clientAuth |  |  |
| --connectionUploadTimeout | Specifies a different (usually longer) connection timeout during data upload. | `300000` |
| --continueResponseTiming |  |  |
| --defaultSSLHostConfigName |  |  |
| --disableUploadTimeout | Set the flag to control whether a separate connection timeout is used during upload of a request body. | `true` |
| --keyAlias |  |  |
| --keyPass |  |  |
| --keystoreFile |  |  |
| --keystorePass |  |  |
| --keystoreProvider |  |  |
| --keystoreType |  | `JKS` |
| --maxExtensionSize | Maximum size of extension information in chunked encoding. | `8192` |
| --maxHttpHeaderSize | Maximum size of the HTTP message header. | `8192` |
| --maxKeepAliveRequests | Set the maximum number of Keep-Alive requests to allow. This is to safeguard from DoS attacks. Setting to a negative value disables the limit. | `100` |
| --maxSavePostSize | Set the maximum size of a POST which will be buffered during FORM or CLIENT-CERT authentication. When a POST is received where the security constraints require a client certificate, the POST body needs to be buffered while an SSL handshake takes place to obtain the certificate. A similar buffering is required during FDORM auth. | `4096` |
| --maxSwallowSize | Maximum amount of request body to swallow. | `2097152` |
| --maxTrailerSize | Maximum size of trailing headers in bytes. | `8192` |
| --rejectIllegalHeader | If an HTTP request is received that contains an illegal header name or value (e.g. the header name is not a token) should the request be rejected (with a 400 response) or should the illegal header be ignored? | `false` |
| --serverRemoveAppProvidedValues | Should application provider values for the HTTP Server header be removed. Note that if `server` is set, any application provided value will be over-ridden. | `false` |
| --sessionCacheSize |  | `-1` |
| --sessionTimeout |  | `86400` |
| --caCertificateFile |  |  |
| --caCertificatePath |  |  |
| --caRevocationFile |  |  |
| --caRevocationPath |  |  |
| --SSLEnabled |  | `false` |
| --SSLProtocol |  |  |
| --SSLVerifyDepth |  | `10` |
| --sslProtocol |  | `TLS` |
| --certificateChainFile |  |  |
| --certificateFile |  |  |
| --certificateKeyFile |  |  |
| --disableCompression |  | `true` |
| --disableSessionTickets |  | `false` |
| --trustManagerClassName |  |  |
| --useKeepAliveResponseHeader |  | `true` |
| --acceptCount | Allows the server developer to specify the acceptCount (backlog) that should be used for server sockets. | `100` |
| --acceptorThreadPriority | Priority of the acceptor threads. | `5` |
| --clientCertProvider | When client certificate information is presented in a form other than instances of `java.security.cert.X509Certificate` it needs to be converted before it can be used and this property controls which JSSE provider is used to perform the conversion. For example it is used with the AJP connectors, the HTTP APR connector and with the `org.apache.catalina.valves.SSLValve`. If not specified, the default provider will be used. |  |
| --maxHeaderCount | The maximum number of headers in a request that are allowed. A value of less than 0 means no limit. | `100` |
| --keepAliveTimeout | Keepalive timeout, if not set the soTimeout is used. | `20000` |



## 配置说明



### STEP 1：设置 Tomcat 及依赖包版本

```xml
<properties>
    <tomcat.version>8.5.73</tomcat.version>
    <ymate.module.embed.version>1.0.1</ymate.module.embed.version>
</properties>
```



### STEP 2：添加必要依赖包

```xml
<dependency>
    <groupId>net.ymate.module</groupId>
    <artifactId>ymate-module-embed-tomcat</artifactId>
    <version>${ymate.module.embed.version}</version>
    <scope>provided</scope>
</dependency>
```



### STEP 3：配置相关插件



- 设置启动类

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-war-plugin</artifactId>
    <version>2.2</version>
    <configuration>
      <filteringDeploymentDescriptors>true</filteringDeploymentDescriptors>
      <archive>
        <manifest>
          <mainClass>net.ymate.module.embed.Main</mainClass>
        </manifest>
      </archive>
    </configuration>
</plugin>
```



- 配置构建规则

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-dependency-plugin</artifactId>
    <version>2.8</version>
    <executions>
        <execution>
            <id>embedded-main</id>
            <phase>generate-resources</phase>
            <goals>
                <goal>unpack-dependencies</goal>
            </goals>
            <configuration>
                <includeGroupIds>net.ymate.module</includeGroupIds>
                <includeArtifactIds>ymate-module-embed</includeArtifactIds>
                <includeScope>provided</includeScope>
                <includes>**/*.class</includes>
                <outputDirectory>${project.build.directory}/${project.build.finalName}</outputDirectory>
            </configuration>
        </execution>
        <execution>
            <id>copy-dependencies</id>
            <phase>generate-resources</phase>
            <goals>
                <goal>copy</goal>
            </goals>
            <configuration>
                <artifactItems>
                    <artifactItem>
                        <groupId>org.apache.tomcat</groupId>
                        <artifactId>tomcat-annotations-api</artifactId>
                        <version>${tomcat.version}</version>
                    </artifactItem>
                    <artifactItem>
                        <groupId>org.apache.tomcat.embed</groupId>
                        <artifactId>tomcat-embed-core</artifactId>
                        <version>${tomcat.version}</version>
                    </artifactItem>
                    <artifactItem>
                        <groupId>org.apache.tomcat.embed</groupId>
                        <artifactId>tomcat-embed-jasper</artifactId>
                        <version>${tomcat.version}</version>
                    </artifactItem>
                    <artifactItem>
                        <groupId>org.apache.tomcat.embed</groupId>
                        <artifactId>tomcat-embed-el</artifactId>
                        <version>${tomcat.version}</version>
                    </artifactItem>
                    <artifactItem>
                        <groupId>org.apache.tomcat.embed</groupId>
                        <artifactId>tomcat-embed-websocket</artifactId>
                        <version>${tomcat.version}</version>
                    </artifactItem>
                    <artifactItem>
                        <groupId>org.eclipse.jdt</groupId>
                        <artifactId>ecj</artifactId>
                        <version>3.12.3</version>
                    </artifactItem>
                    <artifactItem>
                        <groupId>net.ymate.module</groupId>
                        <artifactId>ymate-module-embed-tomcat</artifactId>
                        <version>${ymate.module.embed.version}</version>
                    </artifactItem>
                </artifactItems>
                <outputDirectory>${project.build.directory}/${project.build.finalName}/META-INF/dependencies</outputDirectory>
                <stripVersion>false</stripVersion>
                <overWriteIfNewer>true</overWriteIfNewer>
                <overWriteReleases>false</overWriteReleases>
                <overWriteSnapshots>true</overWriteSnapshots>
            </configuration>
        </execution>
    </executions>
</plugin>
```



### STEP 4：配置 SSL 证书（可选）

**示例一：** 通过服务参数配置 JKS 格式证书，配置参数项如下：

```properties
--SSLEnabled=true
--keystoreFile=/home/..../keystore.jks
--keystorePass=12345678x
--keystoreType=JKS
```

> **注：** 请根据实际情况调整以上配置，可以直接通过命令行或者通过 `tomcat.properties` 文件进行设置。



**示例二：** 本例以配置  JKS 格式证书为例，演示如何通过 SPI 方式对嵌入式 Tomcat 容器进行自定义配置，代码如下：

```java
package net.ymate.demo;

import net.ymate.module.embed.CommandLineHelper;
import net.ymate.module.embed.tomcat.ITomcatCustomizer;
import org.apache.catalina.Context;
import org.apache.catalina.Host;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.apache.coyote.http11.AbstractHttp11Protocol;

public class TomcatCustomizer implements ITomcatCustomizer {

    @Override
    public void customize(CommandLineHelper configs, Tomcat tomcat, Connector connector, Host host, Context context) {
        if (connector.getProtocolHandler() instanceof AbstractHttp11Protocol) {
            AbstractHttp11Protocol<?> http11NioProtocol = (AbstractHttp11Protocol<?>) connector.getProtocolHandler();
            http11NioProtocol.setSSLEnabled(true);
            http11NioProtocol.setKeystoreFile("/home/..../keystore.jks");
            http11NioProtocol.setKeystorePass("12345678x");
            http11NioProtocol.setKeystoreType("JKS");
        }
    }
}
```

将此实现类的名称配置到目标工程中的资源目录下 `META-INF/services/net.ymate.module.embed.tomcat.ITomcatCustomizer` 文件中使其能够被 SPI 服务正确加载，在本例中的实现类名称如下：

```properties
net.ymate.demo.TomcatCustomizer
```





## One More Thing

YMP 不仅提供便捷的 Web 及其它 Java 项目的快速开发体验，也将不断提供更多丰富的项目实践经验。

感兴趣的小伙伴儿们可以加入官方 QQ 群：480374360，一起交流学习，帮助 YMP 成长！

如果喜欢 YMP，希望得到你的支持和鼓励！

![Donation Code](https://ymate.net/img/donation_code.png)

了解更多有关 YMP 框架的内容，请访问官网：[https://ymate.net](https://ymate.net)

