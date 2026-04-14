# YMATE-EMBED

[![Maven Central status](https://img.shields.io/maven-central/v/net.ymate.module/ymate-module-embed.svg)](https://search.maven.org/artifact/net.ymate.module/ymate-module-embed)
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
> 项目文件在运行时将默认被释放到 `${user.home}/.embeddedWorks` 目录下，若需更改请使用 `--targetDir` 参数设置，当 `--targetDir` 取值为 `.` 则表示目标位置在当前目录，示例如下：
>
> ```shell
> java -jar demo.war --targetDir /Temp/webapps --port 8088 --contextPath /demo
> ```
>
> 也可以通过 `--homeDir` 参数直接运行解压后的WAR文件目录，示例如下：
>
> ```shell
> java -jar demo.war --homeDir /Temp/webapps/demo/ROOT --port 8088 --contextPath /demo
> ```
>
> 由于 JDK 9 及以上版本对反射进行了限制，调整参数配置，示例如下：
>
> ```shell
> java --add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.base/java.io=ALL-UNNAMED --add-opens=java.rmi/sun.rmi.transport=ALL-UNNAMED -jar demo.war --targetDir /Temp/webapps --port 8088 --contextPath /demo --redeploy
> ```



## 命令参数项

| 参数项                  | 说明 | 默认值    |
| ----------------------- | ---- | --------- |
| --redeploy              | 重新部署<br />*服务启动时将 WAR 包中的相关资源文件重新解压并覆盖* |      |
| --cleanup               | 清理部署目录<br />*删除上次部署的内容* | |
| --conf | 指定配置文件名称（不包含扩展名） | `tomcat` |
| --targetDir | 指定目标目录（用于存放被释放的项目文件） |  |
| --homeDir | 直接运行WAR文件解压后的目录 | |



## 服务参数项

以下参数项可以通过命令行或使用文件进行配置，若两者同时存在则命令行配置优先，该文件必须放置在类路径下，默认名称为：`tomcat.properties` ，允许自定义名称，如：`test.properties`，但需要通过命令行参数项 `--conf test` 使其生效。

| 参数项                  | 说明 | 默认值    |
| ----------------------- | ---- | --------- |
| --port                  | 服务监听的端口号 | `8080`      |
| --protocol              | 协议名称，如 HTTP/1.1、HTTP/2 等 | `HTTP/1.1`  |
| --contextPath           | 上下文映射路径（应用访问路径前缀），为空表示根路径 | `""` |
| --hostName              | 服务的主机名称 | `localhost` |
| --uriEncoding           | URI 请求的编码格式，用于解析 URL 中的中文字符 | `UTF-8`     |
| --fileEncoding          | 文件编码格式，用于读取资源文件时的编码 | `UTF-8`     |
| --asyncTimeout          | 异步请求的默认超时时间（毫秒），超过此时间异步请求将被终止 | `30000`     |
| --connectionTimeout     | 连接的默认超时时间（毫秒），建立连接的最大等待时间 | `20000`     |
| --maxConnections        | 最大连接数，服务器同时接受的最大连接数量 | `10000`     |
| --maxThreads            | 最大工作线程数，处理请求的线程池大小 | `200`       |
| --useBodyEncodingForURI | 请求参数的编码方式采用请求体的编码方式，确保 POST 和 GET 参数编码一致 | `true`      |
| --maxCookieCount | 单个请求允许的最大 Cookie 数量，设置为负数表示无限制 | `200` |
| --scheme | 分配给通过此连接器接收的请求的方案（http 或 https） | `http` |
| --allowTrace | 是否启用 TRACE HTTP 方法，出于安全考虑建议禁用 | `false` |
| --discardFacades | 设置对象外观的回收策略，影响内存使用性能 | `false` |
| --enableLookups | 是否启用 DNS 查找，启用后会通过 IP 反查主机名，影响性能 | `false` |
| --maxParameterCount | 自动解析的最大参数数量（GET + POST），设置为负数表示无限制 | `10000` |
| --maxPostSize | POST 请求体的最大大小（字节），超过此大小的 POST 请求将被拒绝，设置为 -1 表示无限制 | `2097152` (2MB) |
| --parseBodyMethods | 允许解析请求体参数的 HTTP 方法列表，多个方法用逗号分隔 | `POST` |
| --proxyName | 代理服务器名称，当 Tomcat 位于反向代理后面时设置 |  |
| --proxyPort | 代理服务器端口，当 Tomcat 位于反向代理后面时设置 | `0` |
| --redirectPort | 非 SSL 到 SSL 重定向的端口号，当请求需要安全连接时重定向到此端口 | `443` |
| --secure | 安全连接标志，设置后所有通过此连接器接收的请求都标记为安全连接 | `false` |
| --domain | 指定此组件注册所在的 JMX 域名 |  |
| --server | 设置 HTTP Server 响应头的值，用于隐藏或自定义服务器标识 |  |
| --useIPVHosts | 是否启用基于 IP 的虚拟主机 | `false` |
| --throwOnFailure | 配置在生命周期方法中抛出 LifecycleException 时是否重新抛出给调用者处理，否则仅记录日志 | `true` |
| --ciphers | 设置加密套件配置，无论使用何种格式设置，始终以 OpenSSL 格式存储 |  |
| --processorCache | 缓存中保留的空闲处理器最大数量，将在后续请求中重用，设置为 -1 表示无限制 | `200` |
| --clientAuth | 客户端认证模式，可选值：none（不要求）、want（可选）、true（必须） |  |
| --connectionUploadTimeout | 数据上传期间的连接超时时间（毫秒），通常比正常超时时间更长 | `300000` (5分钟) |
| --continueResponseTiming | 控制 100 Continue 响应的发送时机 |  |
| --defaultSSLHostConfigName | 默认的 SSL 主机配置名称 |  |
| --disableUploadTimeout | 是否禁用上传期间的独立连接超时，启用后上传期间使用普通超时 | `true` |
| --keyAlias | 密钥库中证书的别名，用于标识特定证书 |  |
| --keyPass | 密钥密码，保护私钥的密码 |  |
| --keystoreFile | 密钥库文件路径，支持 ${root}、${user.dir}、${user.home} 变量占位 |  |
| --keystorePass | 密钥库密码，访问密钥库的密码 |  |
| --keystoreProvider | 密钥库提供者名称，指定使用的安全提供者 |  |
| --keystoreType | 密钥库类型，常见类型：JKS、PKCS12、JCEKS 等 | `JKS` |
| --maxExtensionSize | 分块编码中扩展信息的最大大小（字节） | `8192` (8KB) |
| --maxHttpHeaderSize | HTTP 消息头的最大大小（字节），超过此大小的请求将被拒绝 | `8192` (8KB) |
| --maxKeepAliveRequests | 允许的最大 Keep-Alive 请求数量，防止 DoS 攻击，设置为负数表示无限制 | `100` |
| --maxSavePostSize | 在 FORM 或 CLIENT-CERT 认证期间缓冲的 POST 请求最大大小（字节） | `4096` (4KB) |
| --maxSwallowSize | 被拒绝请求的请求体最大读取大小（字节），用于安全地消耗掉客户端发送的数据，防止资源浪费 | `2097152` (2MB) |
| --maxTrailerSize | 尾部头部的最大大小（字节） | `8192` (8KB) |
| --serverRemoveAppProvidedValues | 是否移除应用程序提供的 HTTP Server 头部值，如果设置了 server 参数，应用程序提供的值将被覆盖 | `false` |
| --sessionCacheSize | SSL 会话缓存大小，设置为 -1 表示使用默认值 | `-1` |
| --sessionTimeout | SSL 会话超时时间（秒） | `86400` (24小时) |
| --caCertificateFile | CA 证书文件路径，用于验证客户端证书 |  |
| --caCertificatePath | CA 证书目录路径，包含多个 CA 证书文件 |  |
| --caRevocationFile | CA 吊销列表文件路径，用于检查证书是否被吊销 |  |
| --caRevocationPath | CA 吊销列表目录路径，包含多个吊销列表文件 |  |
| --SSLEnabled | 是否启用 SSL/TLS 加密连接 | `false` |
| --SSLProtocol | SSL/TLS 协议版本，如 TLSv1.2、TLSv1.3 等，多个协议用逗号分隔 |  |
| --SSLVerifyDepth | 证书验证链的最大深度 | `10` |
| --sslProtocol | SSL 协议名称，默认为 TLS | `TLS` |
| --certificateChainFile | 证书链文件路径（PEM 格式） |  |
| --certificateFile | 证书文件路径（PEM 格式） |  |
| --certificateKeyFile | 证书私钥文件路径（PEM 格式） |  |
| --disableCompression | 是否禁用 SSL 压缩，出于安全考虑建议禁用以防止 CRIME 攻击 | `true` |
| --disableSessionTickets | 是否禁用 SSL 会话票据 | `false` |
| --trustManagerClassName | 自定义 TrustManager 类名，用于实现自定义证书信任逻辑 |  |
| --useKeepAliveResponseHeader | 是否在响应中使用 Keep-Alive 头部 | `true` |
| --acceptCount | 服务器套接字的接受队列长度（backlog），当所有工作线程都在忙时，新连接在此队列中等待 | `100` |
| --acceptorThreadPriority | 接受器线程的优先级（1-10），5 为普通优先级 | `5` |
| --clientCertProvider | 客户端证书转换使用的 JSSE 提供者名称，用于 AJP 连接器等特殊场景 |  |
| --maxHeaderCount | 请求中允许的最大头部数量，设置为负数表示无限制 | `100` |
| --keepAliveTimeout | Keep-Alive 超时时间（毫秒），如果未设置则使用 connectionTimeout | `20000` |



## 配置说明

### 配置文件位置与加载顺序

配置参数可以通过以下两种方式设置（优先级从高到低）：
1. **命令行参数**：通过 `--参数名 参数值` 的方式直接指定
2. **配置文件**：在类路径下放置 properties 文件（默认为 `tomcat.properties`）

当两者同时存在时，命令行参数的优先级更高。配置文件必须放置在类路径下（通常是 `src/main/resources` 目录），默认名称为 `tomcat.properties`。如需使用自定义配置文件名（如 `test.properties`），需通过命令行参数 `--conf test` 指定。

**配置文件中的路径变量支持：**
- `${root}`：表示当前应用的 `WEB-INF/classes` 所在位置
- `${user.dir}`：表示用户当前工作目录
- `${user.home}`：表示用户主目录


### STEP 1：设置 Tomcat 及依赖包版本

```xml
<properties>
    <tomcat.version>9.0.117</tomcat.version>
    <ymate.module.embed.version>1.0.4</ymate.module.embed.version>
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
    <version>3.3.2</version>
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
                        <version>3.26.0</version>
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


### 常用配置场景

#### 1. 基本 HTTP 服务配置

最简单的配置，仅需设置端口和上下文路径：

```properties
# 服务端口
port=8080
# 上下文路径
contextPath=/myapp
# 主机名
hostName=localhost
```

#### 2. 性能优化配置

针对高并发场景的性能调优：

```properties
# 增加最大连接数
maxConnections=20000
# 增加工作线程数
maxThreads=500
# 调整接受队列长度
acceptCount=200
# 增加 Keep-Alive 请求数
maxKeepAliveRequests=200
# 延长 Keep-Alive 超时时间
keepAliveTimeout=30000
```

#### 3. 安全性增强配置

提高服务器安全性的配置：

```properties
# 禁用 TRACE 方法
allowTrace=false
# 禁用 DNS 查找以提高性能和安全性
enableLookups=false
# 限制请求头数量防止攻击
maxHeaderCount=50
# 限制 Cookie 数量
maxCookieCount=100
# 隐藏服务器信息
server=
```

#### 4. 大文件上传配置

支持大文件上传的配置：

```properties
# 增加 POST 请求体大小限制（100MB）
maxPostSize=104857600
# 增加上传超时时间（30分钟）
connectionUploadTimeout=1800000
# 禁用上传超时
# disableUploadTimeout=true
```

#### 5. 反向代理配置

当 Tomcat 位于 Nginx 或其他反向代理后面时：

```properties
# 设置代理信息
proxyName=www.example.com
proxyPort=443
# 设置 scheme 为 https
scheme=https
secure=true
```


### STEP 4：配置 SSL 证书（可选）

**示例一：** 通过服务参数配置 JKS 格式证书，配置参数项如下：

```properties
# 启用 SSL
SSLEnabled=true
# 密钥库文件路径（支持 ${root}、${user.dir}、${user.home} 变量）
keystoreFile=${root}/keystore.jks
# 密钥库密码
keystorePass=12345678x
# 密钥库类型
keystoreType=JKS
# SSL 协议版本
SSLProtocol=TLSv1.2,TLSv1.3
```

> **注：**
> 请根据实际情况调整以上配置，可以直接通过命令行或者通过 `tomcat.properties` 文件进行设置。
>
> 另外，在配置中与路径相关的参数项均支持 `${root}`、`${user.dir}` 和 `${user.home}` 变量占位，其中 `${root}` 表示当前应用的 `WEB-INF/classes` 所在位置。
>
> 若程序运行加载类路径下 JKS 文件出现 `Invalid keystore format` 异常时，请确认 `maven-resources-plugin` 插件是否存在如下列配置：
> ```xml
> <configuration>
>   <nonFilteredFileExtensions>
>       <nonFilteredFileExtension>jks</nonFilteredFileExtension>
>   </nonFilteredFileExtensions>
> </configuration>
> ```

**示例二：** 通过服务参数配置 PEM 格式证书（适用于 Let's Encrypt 等证书）：

```properties
# 启用 SSL
SSLEnabled=true
# 证书文件路径
certificateFile=${root}/cert.pem
# 证书私钥文件路径
certificateKeyFile=${root}/key.pem
# 证书链文件路径（可选）
certificateChainFile=${root}/chain.pem
# SSL 协议版本
SSLProtocol=TLSv1.2,TLSv1.3
```


**示例三：** 本例以配置 JKS 格式证书为例，演示如何通过 SPI 方式对嵌入式 Tomcat 容器进行自定义配置，代码如下：

```java
package net.ymate.demo;

import net.ymate.module.embed.CommandLineHelper;
import net.ymate.module.embed.Main;
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
            http11NioProtocol.setKeystoreFile(Main.replaceEnvVariable("${root}keystore.jks"));
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

**示例四：** 本例演示如何将 HTTP 请求强制重定向至 HTTPS 以及重置 Cookie 处理器方法。

工程类路径下 `tomcat.properties` 文件内容如下：

```properties
# 默认连接器配置
port=8443
scheme=https
secure=true
SSLEnabled=true
keystoreFile=${root}/keystore.jks
keystorePass=12345678x
keystoreType=JKS

# 自定义连接器配置
httpPort=8080
httpScheme=http
```

Tomcat 自定义 SPI 接口实现类代码如下：

```java
package net.ymate.demo;

import net.ymate.module.embed.CommandLineHelper;
import net.ymate.module.embed.tomcat.ITomcatCustomizer;
import org.apache.catalina.Context;
import org.apache.catalina.Host;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;

public class TomcatCustomizer implements ITomcatCustomizer {

    private static final Log LOG = LogFactory.getLog(TomcatCustomizer.class);

    @Override
    public void customize(CommandLineHelper configs, Tomcat tomcat, Connector connector, Host host, Context context) {
        // 重置 Cookie 处理器避免出现警告
        context.setCookieProcessor(new LegacyCookieProcessor());
        // 设置 HTTP 请求强制重定向至 HTTPS
        SecurityConstraint securityConstraint = new SecurityConstraint();
        securityConstraint.setUserConstraint("CONFIDENTIAL");
        SecurityCollection collection = new SecurityCollection();
        collection.addPattern("/*");
        securityConstraint.addCollection(collection);
        context.addConstraint(securityConstraint);
        // 新增 HTTP 连接器用于请求重定向至默认连接器端口
        Connector connectorNew = new Connector();
        connectorNew.setScheme(configs.getString("httpScheme", "http"));
        connectorNew.setPort(configs.getIntValue("httpPort", 8080));
        connectorNew.setSecure(false);
        connectorNew.setRedirectPort(connector.getPort());
        tomcat.getService().addConnector(connectorNew);
        //
        if (LOG.isInfoEnabled()) {
            LOG.info(">>>>>>>>>>>>>>>>>>> TomcatCustomizer");
        }
    }
}
```


## 故障排查

### 常见问题及解决方案

#### 1. 端口被占用

**问题：** 启动时提示 `Address already in use`

**解决：**
- 检查端口是否已被其他程序占用：`netstat -ano | findstr :8080`
- 更改配置文件中的 `port` 参数为其他可用端口
- 或者通过命令行指定：`java -jar demo.war --port 8088`

#### 2. SSL 证书加载失败

**问题：** 出现 `Invalid keystore format` 异常

**解决：**
- 确认 `maven-resources-plugin` 插件配置了不过滤 `.jks` 文件：
```xml
<configuration>
  <nonFilteredFileExtensions>
      <nonFilteredFileExtension>jks</nonFilteredFileExtension>
  </nonFilteredFileExtensions>
</configuration>
```
- 检查密钥库文件路径是否正确
- 验证密钥库密码是否正确

#### 3. 中文乱码问题

**问题：** URL 或请求参数中的中文显示为乱码

**解决：**
- 确保配置了正确的编码：
```properties
uriEncoding=UTF-8
fileEncoding=UTF-8
useBodyEncodingForURI=true
```

#### 4. 内存不足

**问题：** 出现 `OutOfMemoryError`

**解决：**
- 调整 JVM 内存参数：
```shell
java -Xms512m -Xmx2048m -jar demo.war
```
- 优化 Tomcat 线程池配置，减少 `maxThreads` 值
- 检查应用是否存在内存泄漏

#### 5. 连接超时

**问题：** 客户端请求超时

**解决：**
- 增加连接超时时间：
```properties
connectionTimeout=60000
keepAliveTimeout=60000
```
- 检查网络状况和防火墙设置
- 查看服务器负载情况，必要时增加 `maxThreads`

#### 6. JDK 9+ 反射限制

**问题：** 在 JDK 9 及以上版本运行时出现反射相关异常

**解决：** 添加必要的 JVM 参数：
```shell
java --add-opens=java.base/java.lang=ALL-UNNAMED \
     --add-opens=java.base/java.io=ALL-UNNAMED \
     --add-opens=java.rmi/sun.rmi.transport=ALL-UNNAMED \
     -jar demo.war
```


## One More Thing

YMP 不仅提供便捷的 Web 及其它 Java 项目的快速开发体验，也将不断提供更多丰富的项目实践经验。

感兴趣的小伙伴儿们可以加入官方 QQ 群：[480374360](https://qm.qq.com/cgi-bin/qm/qr?k=3KSXbRoridGeFxTVA8HZzyhwU_btZQJ2)，一起交流学习，帮助 YMP 成长！

如果喜欢 YMP，希望得到你的支持和鼓励！

![Donation Code](https://ymate.net/img/donation_code.png)

了解更多有关 YMP 框架的内容，请访问官网：[https://ymate.net](https://ymate.net)

