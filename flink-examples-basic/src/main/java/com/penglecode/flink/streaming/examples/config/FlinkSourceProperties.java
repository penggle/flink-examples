package com.penglecode.flink.streaming.examples.config;

/**
 * @author pengpeng
 * @version 1.0
 * @since 2021/12/6 15:21
 */
public class FlinkSourceProperties {

    private SourceKafka sourceKafka;

    public SourceKafka getSourceKafka() {
        return sourceKafka;
    }

    public void setSourceKafka(SourceKafka sourceKafka) {
        this.sourceKafka = sourceKafka;
    }

    public static class SourceKafka {

        private String bootstrapServers;

        public String getBootstrapServers() {
            return bootstrapServers;
        }

        public void setBootstrapServers(String bootstrapServers) {
            this.bootstrapServers = bootstrapServers;
        }
    }

}
