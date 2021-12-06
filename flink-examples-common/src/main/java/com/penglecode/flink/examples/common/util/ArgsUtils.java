package com.penglecode.flink.examples.common.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * SpringBoot程序Program Arguments工具类
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/12/5 21:09
 */
public class ArgsUtils {

    private ArgsUtils() {}

    public static List<String> getOptionValues(ApplicationArguments args, String optionName) {
        return args.getOptionValues(optionName);
    }

    public static String getOptionValue(ApplicationArguments args, String optionName) {
        return getOptionValue(args, optionName, null);
    }

    public static String getOptionValue(ApplicationArguments args, String optionName, String defaultValue) {
        List<String> optionValues = getOptionValues(args, optionName);
        String optionValue = null;
        if(!CollectionUtils.isEmpty(optionValues)) {
            optionValue = optionValues.get(0);
        }
        return StringUtils.defaultIfEmpty(optionValue, defaultValue);
    }

}
