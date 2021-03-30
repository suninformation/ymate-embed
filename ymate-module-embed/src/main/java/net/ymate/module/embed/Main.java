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

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * YMP Embedded Bootstrap!
 *
 * @author YMP (https://www.ymate.net/) on 2021/03/23 20:11
 * @since 1.0.0
 */
public class Main {

    public static final int MIN_JAVA_CLASS_VERSION = 52;

    private static volatile boolean running = true;

    public static void main(String[] args) throws Exception {
        checkVersion();
        //
        JarFile jarFile = ownerJarFile();
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
            File targetFile = new File(System.getProperty("user.home"), ".embeddedWorks/" + jarFileName.substring(beginIdx, endIdx) + "/ROOT");
            String tmpDirPath = new File(targetFile.getParentFile(), "/temp").getPath();
            System.setProperty("embedded.home", targetFile.getPath());
            System.setProperty("java.io.tmpdir", tmpDirPath);
            //
            System.out.println("Working directory: " + targetFile.getPath());
            System.out.println("Temporary directory: " + tmpDirPath);
            //
            CommandLineHelper configs = CommandLineHelper.bind(args);
            boolean redeploy = configs.has("redeploy");
            //
            Enumeration<JarEntry> entriesEnum = jarFile.entries();
            while (entriesEnum.hasMoreElements()) {
                JarEntry entry = entriesEnum.nextElement();
                if (!entry.isDirectory()) {
                    if (entry.getName().startsWith("net/ymate/module/embed/")) {
                        continue;
                    }
                    File distFile = new File(targetFile, entry.getName());
                    File distFileParent = distFile.getParentFile();
                    if (!distFileParent.exists() && !distFileParent.mkdirs()) {
                        throw new IOException(String.format("Unable to create directory: %s", distFileParent.getPath()));
                    }
                    if (!distFile.exists() || redeploy) {
                        try (InputStream inputStream = jarFile.getInputStream(entry);
                             OutputStream outputStream = new FileOutputStream(distFile)) {
                            System.out.printf("%s %s...%n", distFile.exists() && redeploy ? "Redeploying" : "Unpacking", entry.getName());
                            copyStream(inputStream, outputStream);
                        }
                    }
                }
            }
            Set<URL> urls = new HashSet<>();
            File deptLibDir = new File(targetFile, "META-INF/dependencies");
            if (deptLibDir.exists() && deptLibDir.isDirectory()) {
                parseLibDir(deptLibDir, urls);
            }
            File webLibDir = new File(targetFile, "WEB-INF/lib");
            if (webLibDir.exists() && webLibDir.isDirectory()) {
                parseLibDir(webLibDir, urls, "tomcat-", "ecj-", "ymate-module-embed-");
            }
            URLClassLoader classLoader = new URLClassLoader(urls.toArray(new URL[0]));
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
                System.out.println("Java class version: " + String.format("%d.0", javaVersion));
            } catch (NumberFormatException e) {
                System.out.printf("Failed to parse java.class.version: %s.%n", versionStr);
            }
        }
    }

    public static void parseLibDir(File libsDir, Set<URL> urls, String... startWiths) throws MalformedURLException {
        if (libsDir.exists() && libsDir.isDirectory()) {
            File[] libFiles = libsDir.listFiles();
            if (libFiles != null && libFiles.length > 0) {
                for (File libFile : libFiles) {
                    boolean flag;
                    if (startWiths != null && startWiths.length > 0) {
                        flag = Arrays.stream(startWiths).anyMatch(startWith -> libFile.getName().startsWith(startWith));
                    } else {
                        flag = true;
                    }
                    if (flag) {
                        urls.add(libFile.toURI().toURL());
                        System.out.println("Loading " + libFile.toURI().toURL());
                    }
                }
            }
        }
    }

    public static JarFile ownerJarFile() throws IOException {
        URL mainClass = Main.class.getClassLoader().getResource("net/ymate/module/embed/Main.class");
        if (mainClass != null) {
            URLConnection connection = mainClass.openConnection();
            if (connection instanceof JarURLConnection) {
                return ((JarURLConnection) mainClass.openConnection()).getJarFile();
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
}
