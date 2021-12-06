package com.penglecode.flink.streaming.examples.transform;

import com.penglecode.flink.examples.FlinkExample;
import com.penglecode.flink.examples.common.model.SensorReading;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 数据滚动聚合转换 API示例
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/11/20 22:54
 */
@Component
public class AggregationTransformExample extends FlinkExample {

    private static final Random RANDOM = new Random();

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //1、创建执行上下文环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1); //设置全局并行度(默认分区数)
        //2、从集合中构造数据源并做Map转换
        DataStream<SensorReading> dataStreamSource = env.fromCollection(getStaticSensorReadings())
                .map(SensorReading::valueOf)
                .filter(Objects::nonNull);

        //3、使用KeySelector来进行分组，即通过Lambda表达式指定分组key
        KeyedStream<SensorReading,String> keyedStream1 = dataStreamSource.keyBy(SensorReading::getMetricCode);
        //4、执行滚动聚合操作(注意观察某个deviceId对应的temperature/humidity值会被修改成最大的那个值)
        DataStream<SensorReading> resultStream = keyedStream1.max("metricValue");
        //5、打印输出结果
        resultStream.print();
        //6、执行流处理任务
        env.execute();
    }

    private static List<String> getStaticSensorReadings() {
        List<String> readings = new ArrayList<>();
        readings.add("sensor-01,temperature,31.34");
        readings.add("sensor-02,temperature,26.45");
        readings.add("sensor-03,temperature,36.82");
        readings.add("sensor-04,temperature,34.21");
        readings.add("sensor-05,temperature,38.36");
        readings.add("sensor-06,humidity,54.96");
        readings.add("sensor-07,humidity,59.76");
        readings.add("sensor-08,humidity,62.76");
        readings.add("sensor-09,humidity,49.15");
        readings.add("sensor-10,humidity,59.55");
        return readings;
    }

}
