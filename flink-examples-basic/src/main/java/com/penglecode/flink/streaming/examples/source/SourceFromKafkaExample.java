package com.penglecode.flink.streaming.examples.source;

import com.penglecode.flink.examples.FlinkExample;
import com.penglecode.flink.examples.common.model.Joke;
import com.penglecode.flink.examples.common.serialization.AbstractJsonDeserializationSchema;
import com.penglecode.flink.examples.common.serialization.AbstractJsonKafkaDeserializationSchema;
import com.penglecode.flink.streaming.examples.config.FlinkSourceProperties;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

/**
 * 数据来自Kafka的Source API示例
 * 配合SinkToKafkaExample一起使用
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/11/27 23:10
 */
@Component
public class SourceFromKafkaExample extends FlinkExample {

    private final FlinkSourceProperties flinkSourceProperties;

    public SourceFromKafkaExample(FlinkSourceProperties flinkSourceProperties) {
        this.flinkSourceProperties = flinkSourceProperties;
    }

    /**
     * 1、先创建topic
     * 2、再运行SourceFromKafkaExample等待数据的到来
     * 3、最后运行SinkToKafkaExample为上一步提供输入数据
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        //1、创建执行上下文环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(4); //设置全局并行度(默认分区数)
        //2、从Kafka中订阅数据
        String topic = "joke";
        KafkaSource<Joke> kafkaSource = KafkaSource.<Joke>builder()
                .setBootstrapServers(flinkSourceProperties.getSourceKafka().getBootstrapServers())
                .setGroupId("joke-consumer-group")
                .setStartingOffsets(OffsetsInitializer.latest())
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
