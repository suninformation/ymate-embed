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
package net.ymate.module.embed;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.nio.file.Files;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;

/**
 * YMP Embedded Bootstrap!
 *
 * @author YMP (<a href="https://www.ymate.net/">www.ymate.net</a>) on 2021/03/23 20:11
 * @since 1.0.0
 */
public class Main {

    public static final int MIN_JAVA_CLASS_VERSION = 52;

    private static volatile boolean running = true;

    public static void main(String[] args) throws Exception {
        checkVersion();
        //
        JarURLConnection urlConnection = ownerJarUrlConnection();
        if (urlConnection != null) {
            try (JarFile jarFile = urlConnection.getJarFile()) {
                if (jarFile != null) {
                    String jarFileName = jarFile.getName();
                    int beginIdx = jarFileName.lastIndexOf(File.separator);
                    if (beginIdx <= 0) {
                        beginIdx = 0;
                    }
                    int endIdx = jarFileName.lastIndexOf(".");
                    if (endIdx <= 0) {
                        endIdx = jarFileName.length() - 1;
                    }
                    CommandLineHelper configs = CommandLineHelper.bind(args);
                    String targetDirPath = configs.getString("targetDir");
                    if (targetDirPath == null || targetDirPath.trim().isEmpty()) {
                        targetDirPath = new File(System.getProperty("user.home"), ".embeddedWorks/").getPath();
                    } else if (targetDirPath.trim().equals(".")) {
                        targetDirPath = new File(urlConnection.getJarFileURL().getFile()).getParent();
                    }
                    File homeDir = new File(targetDirPath, jarFileName.substring(beginIdx, endIdx) + "/ROOT");
                    //
                    if (configs.has("cleanup")) {
                        File cleanupDir = homeDir.getParentFile();
                        if (cleanupDir.exists()) {
                            if (!deleteFiles(cleanupDir)) {
                                System.out.printf("Failed to clean up directory: %s%n", cleanupDir);
                            } else {
                                System.out.printf("Successfully cleaned up directory: %s%n", cleanupDir);
                            }
                        }
                    }
                    boolean redeploy = configs.has("redeploy");
                    //
                    if (!homeDir.exists() || redeploy) {
                        Enumeration<JarEntry> entriesEnum = jarFile.entries();
                        while (entriesEnum.hasMoreElements()) {
                            JarEntry entry = entriesEnum.nextElement();
                            if (!entry.isDirectory()) {
                                if (entry.getName().startsWith("net/ymate/module/embed/")) {
                                    continue;
                                }
                                File distFile = new File(homeDir, entry.getName());
                                File distFileParent = distFile.getParentFile();
                                if (!distFileParent.exists() && !distFileParent.mkdirs()) {
                                    throw new IOException(String.format("Unable to create directory: %s", distFileParent.getPath()));
                                }
                                if (!distFile.exists() || redeploy) {
                                    try (InputStream inputStream = jarFile.getInputStream(entry);
                                         OutputStream outputStream = Files.newOutputStream(distFile.toPath())) {
                                        System.out.printf("%s %s...%n", distFile.exists() && redeploy ? "Redeploying" : "Unpacking", entry.getName());
                                        copyStream(inputStream, outputStream);
                                    }
                                }
                            }
                        }
                    }
                    //
                    Collection<URL> urls = new HashSet<>();
                    File webClassesDir = new File(homeDir, "WEB-INF/classes");
                    if (webClassesDir.exists() && webClassesDir.isDirectory()) {
                        URL fileUrl = webClassesDir.toURI().toURL();
                        urls.add(fileUrl);
                        System.out.printf("Loading %s%n", fileUrl);
                    }
                    File deptLibDir = new File(homeDir, "META-INF/dependencies");
                    if (deptLibDir.exists() && deptLibDir.isDirectory()) {
                        parseLibDir(deptLibDir, urls);
                    }
                    new Main().run(homeDir, args, new URLClassLoader(urls.toArray(new URL[0])));
                }
            }
        }
    }

    public void run(File homeDir, String[] args, ClassLoader classLoader) {
        String tmpDirPath = new File(homeDir.getParentFile(), "/temp").getPath();
        System.setProperty("embedded.home", homeDir.getPath());
        System.setProperty("java.io.tmpdir", tmpDirPath);
        //
        System.out.printf("Working directory: %s%n", homeDir.getPath());
        System.out.printf("Temporary directory: %s%n", tmpDirPath);
        //
        Thread.currentThread().setContextClassLoader(classLoader);
        ServiceLoader<IContainer> containers = ServiceLoader.load(IContainer.class, classLoader);
        if (containers.iterator().hasNext()) {
            try {
                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    for (IContainer container : containers) {
                        try {
                            container.stop();
                            System.out.printf("Container [%s] stopped.%n", container.getClass().getName());
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                        synchronized (Main.class) {
                            running = false;
                            Main.class.notify();
                        }
                    }
                }));
                for (IContainer container : containers) {
                    container.start(args);
                    System.out.printf("Container [%s] started.%n", container.getClass().getName());
                }
            } catch (Throwable e) {
                e.printStackTrace();
                System.exit(1);
            }
            synchronized (Main.class) {
                while (running) {
                    try {
                        Main.class.wait();
                    } catch (Throwable ignored) {
                    }
                }
            }
        } else {
            System.out.println("Warning: No container class was found.");
        }
    }

    private static void checkVersion() {
        String versionStr = System.getProperty("java.class.version");
        if (versionStr != null) {
            String classVersionStr = versionStr.split("\\.")[0];
            try {
                int javaVersion = Integer.parseInt(classVersionStr);
                if (javaVersion < MIN_JAVA_CLASS_VERSION) {
                    throw new UnsupportedClassVersionError(String.format("%d.0", javaVersion));
                }
                System.out.printf("Java class version: %s%n", String.format("%d.0", javaVersion));
            } catch (NumberFormatException e) {
                System.out.printf("Failed to parse java.class.version: %s.%n", versionStr);
            }
        }
    }

    public static void parseLibDir(File libsDir, Collection<URL> urls, String... startWiths) throws MalformedURLException {
        if (libsDir.exists() && libsDir.isDirectory()) {
            File[] libFiles = libsDir.listFiles();
            if (libFiles != null) {
                for (File libFile : libFiles) {
                    boolean flag;
                    if (startWiths != null && startWiths.length > 0) {
                        flag = Arrays.stream(startWiths).anyMatch(startWith -> libFile.getName().startsWith(startWith));
                    } else {
                        flag = true;
                    }
                    if (flag) {
                        URL fileUrl = libFile.toURI().toURL();
                        urls.add(fileUrl);
                        System.out.printf("Loading %s%n", fileUrl);
                    }
                }
            }
        }
    }

    public static JarURLConnection ownerJarUrlConnection() throws IOException {
        URL mainClass = Main.class.getClassLoader().getResource("net/ymate/module/embed/Main.class");
        if (mainClass != null) {
            URLConnection connection = mainClass.openConnection();
            if (connection instanceof JarURLConnection) {
                return ((JarURLConnection) connection);
            }
        }
        return null;
    }

    public static void copyStream(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[9182];
        int n;
        if (input != null) {
            while (-1 != (n = input.read(buffer))) {
                output.write(buffer, 0, n);
            }
        }
    }

    public static boolean deleteFiles(File distFile) {
        if (distFile == null || !distFile.exists()) {
            return false;
        }
        if (distFile.isDirectory()) {
            File[] files = distFile.listFiles();
            if (files != null) {
                Arrays.stream(files).forEachOrdered(Main::deleteFiles);
            }
        }
        return distFile.delete();
    }

    /**
     * @return 返回当前嵌入式应用文件释放的主目录
     * @since 1.0.2
     */
    public static String getEmbeddedHome() {
        return System.getProperty("embedded.home");
    }

    /**
     * @return 返回当前应用根路径
     * @since 1.0.2
     */
    public static String getRootPath() {
        File rootDir = new File(getEmbeddedHome(), "WEB-INF/classes");
        if (rootDir.exists() && rootDir.isDirectory()) {
            return rootDir.getPath();
        }
        return System.getProperty("user.dir").trim();
    }

    /**
     * @param origin 原始路径值
     * @return 返回替换后的路径值
     * @since 1.0.2
     */
    public static String replaceEnvVariable(String origin) {
        if (origin != null && !origin.trim().isEmpty()) {
            String rootPath = getRootPath();
            if (origin.contains("${root}")) {
                origin = origin.replaceAll("\\$\\{root}", Matcher.quoteReplacement(rootPath));
            } else if (origin.contains("${user.dir}")) {
                origin = origin.replaceAll("\\$\\{user.dir}", Matcher.quoteReplacement(System.getProperty("user.dir", rootPath)));
            } else if (origin.contains("${user.home}")) {
                origin = origin.replaceAll("\\$\\{user.home}", Matcher.quoteReplacement(System.getProperty("user.home", rootPath)));
            }
        }
        return origin;
    }
}
