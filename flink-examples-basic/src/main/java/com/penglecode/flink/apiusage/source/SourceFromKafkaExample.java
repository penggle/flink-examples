package com.penglecode.flink.apiusage.source;

import com.penglecode.flink.common.model.SensorReading;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import java.util.Objects;
import java.util.Properties;

/**
 * 数据来自Kafka的Source API示例
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/11/20 13:11
 */
public class SourceFromKafkaExample {

    public static void main(String[] args) throws Exception {
        //1、创建执行上下文环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(4); //设置全局并行度
        //2、从Kafka中订阅数据
        String topic = "sensor-readings";
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "192.168.56.2:9092");
        properties.put("group.id", "sensor-consumer-group");
        properties.put("auto.offset.reset", "latest");
        SourceFunction<String> kafkaSourceFunction = new FlinkKafkaConsumer(topic, new SimpleStringSchema(), properties);
        DataStream<SensorReading> dataStreamSource = env.addSource(kafkaSourceFunction).map(new SensorReadingMapFunction()).filter(Objects::nonNull);
        //3、输出数据流处理结果
        dataStreamSource.print();
        //4、执行流处理任务
        env.execute();
    }

    /**
     * 生产端发送：sensor007,temperature,35.21
     */
    public static class SensorReadingMapFunction implements MapFunction<String,SensorReading> {
        @Override
        public SensorReading map(String value) throws Exception {
            String[] values = value.split(",");
            if(values.length == 3) {
                return new SensorReading(values[0], values[1], Double.valueOf(values[2]));
            }
            return null;
        }
    }

}
