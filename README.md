# YMATE-EMBED

本项目为可执行嵌入式 Web 容器，在原始 WAR 包文件结构的基础上为其指定引导程序及相关依赖文件，并通过命令行方式直接启动 Web 服务，从而达到简化 Web 工程部署流程的目的。

目前支持以下 Web 容器：

- [Apache Tomcat® - 8.5.x](https://tomcat.apache.org/) 



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



## 参数项及默认值

| 参数项                  | 说明 | 默认值    |
| ----------------------- | ---- | --------- |
| --redeploy              | 重新部署<br />*服务启动时将 WAR 包中的相关资源文件重新解压并覆盖* |      |
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



## 配置说明



### STEP 1：设置 Tomcat 版本

```xml
<properties>
    <tomcat.version>8.5.73</tomcat.version>
</properties>
```



### STEP 2：添加必要依赖包

```xml
<dependency>
    <groupId>net.ymate.module</groupId>
    <artifactId>ymate-module-embed</artifactId>
    <version>1.0.0</version>
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
                        <version>1.0.0</version>
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



## One More Thing

YMP 不仅提供便捷的 Web 及其它 Java 项目的快速开发体验，也将不断提供更多丰富的项目实践经验。

感兴趣的小伙伴儿们可以加入官方 QQ 群：480374360，一起交流学习，帮助 YMP 成长！

如果喜欢 YMP，希望得到你的支持和鼓励！

![Donation Code](https://ymate.net/img/donation_code.png)

了解更多有关 YMP 框架的内容，请访问官网：[https://ymate.net](https://ymate.net)

