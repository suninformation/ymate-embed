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

import net.ymate.platform.core.util.DateTimeUtils;
import net.ymate.platform.core.util.RuntimeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author 刘镇 (suninformation@163.com) on 2018/3/15 下午2:41
 * @version 1.0
 */
public final class Main {

    private static final Log _LOG = LogFactory.getLog(Main.class);

    private static volatile boolean __running = true;

    private static void __doWriteLog(String content) {
        System.out.println("[" + DateTimeUtils.formatTime(System.currentTimeMillis(), DateTimeUtils.YYYY_MM_DD_HH_MM_SS) + "] " + StringUtils.trimToEmpty(content));
    }

    private static void __doStopContainer(IContainer container) {
        try {
            container.stop();
            __doWriteLog("Container[" + container.getName() + "] stopped.");
        } catch (Throwable e) {
            e = RuntimeUtils.unwrapThrow(e);
            __doWriteLog("Container[" + container.getName() + "] stops exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void __doStartContainer(IContainer container) throws Exception {
        container.start();
        __doWriteLog("Container[" + container.getName() + "] started.");
    }

    /**
     * 主程序入口
     *
     * @param args 参数集合
     */
    public static void main(String[] args) {
        try {
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    for (IContainer _container : IContainer.Manager.getContainers()) {
                        __doStopContainer(_container);
                    }
                    __doStopContainer(IContainer.Manager.getDefaultContainer());
                    synchronized (Main.class) {
                        __running = false;
                        Main.class.notify();
                    }
                }
            });
            //
            StopWatch _watch = new StopWatch();
            _watch.start();
            __doStartContainer(IContainer.Manager.getDefaultContainer());
            for (IContainer _container : IContainer.Manager.getContainers()) {
                __doStartContainer(_container);
            }
            _watch.stop();
            __doWriteLog("All containers started. Total execution time: " + _watch.getTime() + "ms.");
        } catch (Throwable e) {
            e = RuntimeUtils.unwrapThrow(e);
            __doWriteLog("Exception occurred during service startup: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        synchronized (Main.class) {
            while (__running) {
                try {
                    Main.class.wait();
                } catch (Throwable ignored) {
                }
            }
        }
    }
}
