# YMATE-EMBED

本项目为可执行嵌入式Web容器，在原始WAR包文件结构的基础上为其指定引导程序及相关依赖文件，并通过命令行方式直接启动Web服务，从而达到简化Web工程部署流程的目的。

目前支持以下Web容器：

- Apache-tomcat-embed-8.5.x



## 命令行格式

```shell
java -jar <WAR包文件> [--参数项 [参数值]]
```

### 示例：

```shell
java -jar demo.war --port 8088 --contextPath /demo --redeploy
```



### 参数项及默认值:

| 参数项                  | 说明 | 默认值    |
| ----------------------- | ---- | --------- |
| --redeploy              | 重新部署，服务启动时将重新将WAR包中的相关资源文件得新解压并覆盖 |      |
| --port                  | 服务监听的端口号 | `8080`      |
| --protocol              | 协议名称 | `HTTP/1.1`  |
| --contextPath           | 上下文映射路径 | `""` |
| --hostName              | 服务的主机名称 | `localhost` |
| --uriEncoding           | URI请求的编码格式 | `UTF-8`     |
| --fileEncoding          | 文件编码格式 | `UTF-8`     |
| --asyncTimeout          | 异步请求的默认超时时间（毫秒） | `30000`     |
| --connectionTimeout     | 连接的默认超时时间（毫秒） | `20000`     |
| --maxConnections        | 最大连接数 | `10000`     |
| --maxThreads            | 最大工作线程数 | `200`       |
| --useBodyEncodingForURI | 请求参数的编码方式采用请求体的编码方式 | `true`      |



## 配置说明


### 设置Tomcat版本

```xml
<properties>
    <tomcat.version>8.5.64</tomcat.version>
</properties>
```


### 添加必要依赖包

```xml
<dependency>
    <groupId>net.ymate.module</groupId>
    <artifactId>ymate-module-embed</artifactId>
    <version>1.0-SNAPSHOT</version>
    <scope>provided</scope>
</dependency>
```



### 配置相关插件



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
                        <version>1.0-SNAPSHOT</version>
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



