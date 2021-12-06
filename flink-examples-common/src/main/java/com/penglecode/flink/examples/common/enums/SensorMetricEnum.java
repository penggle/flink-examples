package com.penglecode.flink.examples.common.enums;

import java.util.Random;

/**
 * @author pengpeng
 * @version 1.0
 * @since 2021/12/5 22:19
 */
public enum SensorMetricEnum {

    TEMPERATURE("temperature") {
        @Override
        public Double getRandomValue() {
            return 30 + (10 * RANDOM.nextGaussian());
        }
    },
    HUMIDITY("humidity") {
        @Override
        public Double getRandomValue() {
            return 50 + (10 * RANDOM.nextGaussian());
        }
    };

    private static final Random RANDOM = new Random();

    private final String code;

    SensorMetricEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public abstract Double getRandomValue();

}
