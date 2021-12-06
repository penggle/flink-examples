package com.penglecode.flink.streaming.examples.config;


/**
 * @author pengpeng
 * @version 1.0
 * @since 2021/12/6 15:21
 */
public class FlinkSinkProperties {

    private SinkKafka sinkKafka;

    private SinkRedis sinkRedis;

    public SinkKafka getSinkKafka() {
        return sinkKafka;
    }

    public void setSinkKafka(SinkKafka sinkKafka) {
        this.sinkKafka = sinkKafka;
    }

    public SinkRedis getSinkRedis() {
        return sinkRedis;
    }

    public void setSinkRedis(SinkRedis sinkRedis) {
        this.sinkRedis = sinkRedis;
    }

    public static class SinkKafka {

        private String bootstrapServers;

        public String getBootstrapServers() {
            return bootstrapServers;
        }

        public void setBootstrapServers(String bootstrapServers) {
            this.bootstrapServers = bootstrapServers;
        }
    }

    public static class SinkRedis {

        private String host;

        private int port = 6379;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }
    }

}
