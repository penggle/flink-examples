package com.penglecode.flink.apiusage.source;

import com.penglecode.flink.common.model.Joke;
import com.penglecode.flink.common.serialization.AbstractJsonDeserializationSchema;
import com.penglecode.flink.common.serialization.AbstractJsonKafkaDeserializationSchema;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Properties;

/**
 * 数据来自Kafka的Source API示例
 * 配合SinkToKafkaExample一起使用
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/11/27 23:10
 */
public class SourceFromKafkaExample {

    /**
     * 1、先创建topic
     * 2、再运行SourceFromKafkaExample等待数据的到来
     * 3、最后运行SinkToKafkaExample为上一步提供输入数据
     */
    public static void main(String[] args) throws Exception {
        //1、创建执行上下文环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(4); //设置全局并行度(默认分区数)
        //2、从Kafka中订阅数据
        String topic = "joke";
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "192.168.56.2:9092");
        properties.put("group.id", "joke-consumer-group");
        properties.put("auto.offset.reset", "latest");
        KafkaSource<Joke> kafkaSource = KafkaSource.<Joke>builder()
                .setProperties(properties)
                .setTopics(topic)
                //key-value共用的反序列化
                //.setDeserializer(KafkaRecordDeserializationSchema.of(new JokeJsonKafkaDeserializationSchema()))
                //只关心value(消息体)的话，则只需设置setValueOnlyDeserializer()
                .setValueOnlyDeserializer(new JokeJsonDeserializationSchema())
                .build();
        DataStream<Joke> dataStream = env
                .fromSource(kafkaSource, WatermarkStrategy.noWatermarks(), "joke-kafka-source");
        //3、输出数据流处理结果
        dataStream.print();
        //4、执行流处理任务
        env.execute();
    }

    public static class JokeJsonDeserializationSchema extends AbstractJsonDeserializationSchema<Joke> {

    }

    public static class JokeJsonKafkaDeserializationSchema extends AbstractJsonKafkaDeserializationSchema<Joke> {

    }

}
