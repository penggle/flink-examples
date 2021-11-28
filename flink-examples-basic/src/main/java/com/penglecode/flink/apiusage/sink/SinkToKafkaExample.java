package com.penglecode.flink.apiusage.sink;

import com.penglecode.flink.apiusage.function.JokeSourceFunction;
import com.penglecode.flink.common.model.Joke;
import com.penglecode.flink.common.serialization.DefaultJsonSerializationSchema;
import com.penglecode.flink.common.serialization.MappedKafkaKeySerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * 数据输出到Kafka的Sink API示例
 * 配合SourceFromKafkaExample一起使用
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/11/26 23:21
 */
public class SinkToKafkaExample {

    /**
     * 1、先创建topic
     * 2、再运行SourceFromKafkaExample等待数据的到来
     * 3、最后运行SinkToKafkaExample为上一步提供输入数据
     */
    public static void main(String[] args) throws Exception {
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
                .setBootstrapServers("192.168.56.2:9092")
                .setRecordSerializer(recordSerializer)
                .build();
        inputStream.sinkTo(kafkaSink);
        //5、执行流处理任务
        env.execute();
    }

}
