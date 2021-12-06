package com.penglecode.flink.streaming.examples.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author pengpeng
 * @version 1.0
 * @since 2021/12/6 15:28
 */
@Configuration
public class FlinkExampleConfiguration {

    @Bean
    @ConfigurationProperties(prefix="spring.flink.examples")
    public FlinkSinkProperties flinkSinkProperties() {
        return new FlinkSinkProperties();
    }

    @Bean
    @ConfigurationProperties(prefix="spring.flink.examples")
    public FlinkSourceProperties flinkSourceProperties() {
        return new FlinkSourceProperties();
    }

}
