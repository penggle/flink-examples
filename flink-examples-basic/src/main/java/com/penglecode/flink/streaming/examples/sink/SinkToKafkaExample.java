package com.penglecode.flink.streaming.examples.sink;

import com.penglecode.flink.examples.FlinkExample;
import com.penglecode.flink.examples.common.model.Joke;
import com.penglecode.flink.examples.common.serialization.DefaultJsonSerializationSchema;
import com.penglecode.flink.examples.common.serialization.MappedKafkaKeySerializationSchema;
import com.penglecode.flink.examples.common.source.JokeSourceFunction;
import com.penglecode.flink.streaming.examples.config.FlinkSinkProperties;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

/**
 * 数据输出到Kafka的Sink API示例
 * 配合SourceFromKafkaExample一起使用
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/11/26 23:21
 */
@Component
public class SinkToKafkaExample extends FlinkExample {

    private final FlinkSinkProperties flinkSinkProperties;

    public SinkToKafkaExample(FlinkSinkProperties flinkSinkProperties) {
        this.flinkSinkProperties = flinkSinkProperties;
    }

    /**
     * 1、先创建topic
     * 2、再运行SourceFromKafkaExample等待数据的到来(这步可以在flink上提交运行)
     * 3、最后运行SinkToKafkaExample为上一步提供输入数据
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        //1、创建执行上下文环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(4); //设置全局并行度(默认分区数)
        //2、从SourceFunction中读取的输入数据流
        DataStream<Joke> inputStream = env.addSource(new JokeSourceFunction());
        //3、打印输入数据流
        inputStream.print();
        //4、输出到kafka
        KafkaRecordSerializationSchema<Joke> recordSerializer = KafkaRecordSerializationSchema.<Joke>builder()
                .setTopic("joke")
                .setKeySerializationSchema(new MappedKafkaKeySerializationSchema<>(Joke::getSid))
                .setValueSerializationSchema(new DefaultJsonSerializationSchema<>())
                .build();
        KafkaSink<Joke> kafkaSink = KafkaSink.<Joke>builder()
                .setBootstrapServers(flinkSinkProperties.getSinkKafka().getBootstrapServers())
                .setRecordSerializer(recordSerializer)
                .build();
        inputStream.sinkTo(kafkaSink);
        //5、执行流处理任务
        env.execute();
    }

}
