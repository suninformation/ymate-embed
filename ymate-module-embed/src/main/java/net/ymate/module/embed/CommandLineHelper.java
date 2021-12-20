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

import java.util.*;

/**
 * @author 刘镇 (suninformation@163.com) on 2021/03/24 19:13
 * @since 1.0.0
 */
public class CommandLineHelper {

    public static final String EMPTY = "";

    public static final String SPACE = " ";

    private final Map<String, String> arguments = new HashMap<>();

    public static CommandLineHelper bind(String... args) {
        return new CommandLineHelper(args);
    }

    private CommandLineHelper(String... args) {
        String argumentsStr = EMPTY;
        if (args != null && args.length > 0) {
            argumentsStr = String.join(SPACE, args);
        }
        String[] argumentsArr = argumentsStr.trim().split("--");
        if (argumentsArr.length > 0) {
            for (String argument : argumentsArr) {
                if (argument != null && !argument.trim().isEmpty()) {
                    String[] tmpArr = argument.trim().split(SPACE);
                    switch (tmpArr.length) {
                        case 1:
                            arguments.put(tmpArr[0], EMPTY);
                            break;
                        case 2:
                            arguments.put(tmpArr[0], tmpArr[1]);
                            break;
                        default:
                            List<String> tmpList = new ArrayList<>();
                            for (String tmpStr : tmpArr) {
                                if (tmpStr != null && !tmpStr.trim().isEmpty()) {
                                    tmpList.add(tmpStr.trim());
                                }
                            }
                            if (tmpList.size() > 1) {
                                arguments.put(tmpList.remove(0), String.join(SPACE, tmpList.toArray(new String[0])));
                            }
                    }
                }
            }
        }
    }

    public void appendIfNotExist(Map<String, String> source) {
        if (source != null && !source.isEmpty()) {
            source.forEach(arguments::putIfAbsent);
        }
    }

    public void appendIfNotExist(Properties source) {
        if (source != null && !source.isEmpty()) {
            source.forEach((k, v) -> arguments.putIfAbsent(k.toString(), v.toString()));
        }
    }

    public Map<String, String> getArguments() {
        return Collections.unmodifiableMap(arguments);
    }

    private String fixOption(String option) {
        if (option != null && option.length() > 2 && option.startsWith("--")) {
            return option.substring(2);
        }
        return option;
    }

    public boolean has(String option) {
        return arguments.containsKey(fixOption(option));
    }

    public boolean has(String option, boolean defaultValue) {
        return arguments.containsKey(fixOption(option)) || defaultValue;
    }

    public String getString(String option) {
        return arguments.get(fixOption(option));
    }

    public String getString(String option, String defaultValue) {
        String value = arguments.get(fixOption(option));
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        return value;
    }

    public boolean getBooleanValue(String option) {
        return getBooleanValue(option, false);
    }

    public boolean getBooleanValue(String option, boolean defaultValue) {
        String value = getString(option, String.valueOf(defaultValue));
        return Boolean.parseBoolean(value);
    }

    public Boolean getBoolean(String option) {
        return getBoolean(option, null);
    }

    public Boolean getBoolean(String option, Boolean defaultValue) {
        String value = getString(option);
        if (value != null) {
            return Boolean.parseBoolean(value);
        }
        return defaultValue;
    }

    public int getIntValue(String option) {
        return getIntValue(option, 0);
    }

    public int getIntValue(String option, int defaultValue) {
        String value = getString(option, String.valueOf(defaultValue));
        return Integer.parseInt(value);
    }

    public Integer getInt(String option) {
        return getInt(option, null);
    }

    public Integer getInt(String option, Integer defaultValue) {
        String value = getString(option);
        if (value != null) {
            return Integer.parseInt(value);
        }
        return defaultValue;
    }

    public long getLongValue(String option) {
        return getLongValue(option, 0);
    }

    public long getLongValue(String option, long defaultValue) {
        String value = getString(option, String.valueOf(defaultValue));
        return Long.parseLong(value);
    }

    public Long getLong(String option) {
        return getLong(option, null);
    }

    public Long getLong(String option, Long defaultValue) {
        String value = getString(option);
        if (value != null) {
            return Long.parseLong(value);
        }
        return defaultValue;
    }
}
