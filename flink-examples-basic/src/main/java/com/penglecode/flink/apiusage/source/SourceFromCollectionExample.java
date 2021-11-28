package com.penglecode.flink.apiusage.source;

import com.penglecode.flink.common.model.SensorReading;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.graph.StreamGraphGenerator;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * 数据来自集合的Source API示例
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/11/18 23:10
 */
public class SourceFromCollectionExample {

    public static void main(String[] args) throws Exception {
        //1、创建执行上下文环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(4); //设置全局并行度(默认分区数)
        //2、从集合中读取数据源
        DataStream<SensorReading> dataStreamSource1 = env.fromCollection(getTempSensorReadings(), TypeInformation.of(SensorReading.class));

        //3、从自增序列中读取数据源
        DataStream<Long> dataStreamSource2 = env.fromSequence(1, 100);

        //4、输出数据流处理结果
        dataStreamSource1.print("source1");
        dataStreamSource2.print("source2");

        //5、执行流处理任务
        env.execute(StreamGraphGenerator.DEFAULT_STREAMING_JOB_NAME);
    }

    /**
     * 随机生产n个设备温度上数
     */
    private static List<SensorReading> getTempSensorReadings() {
        return new Random().doubles(30,40)
                .limit(100)
                .mapToObj(temp -> new SensorReading("sensor001", "temperature", Double.valueOf(String.format("%.2f", temp))))
                .collect(Collectors.toList());
    }

}
