package com.penglecode.flink.streaming.examples.transform;


import com.penglecode.flink.examples.FlinkExample;
import com.penglecode.flink.examples.common.model.SensorReading;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 多条数据类型相同的流合并成同一条流的示例
 * 注意：union操作只能用于相同数据类型的流
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/11/21 21:20
 */
@Component
public class UnionStreamTransformExample extends FlinkExample {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //1、创建执行上下文环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1); //设置全局并行度
        //2、从集合中构造数据源
        DataStream<SensorReading> inputStream1 = env.fromCollection(getTempSensorReadings());
        inputStream1.print("temperature");
        DataStream<SensorReading> inputStream2 = env.fromCollection(getHumSensorReadings());
        inputStream2.print("humidity");
        DataStream<SensorReading> inputStream3 = env.fromCollection(getLightSensorReadings());
        inputStream3.print("light_intensity");
        //3、合并多个流
        DataStream<SensorReading> unionStream = inputStream1.union(inputStream2).union(inputStream3);
        unionStream.print("union");
        //4、执行流处理任务
        env.execute();
    }

    private static List<SensorReading> getTempSensorReadings() {
        List<SensorReading> readings = new ArrayList<>();
        readings.add(new SensorReading("sensor001", "temperature", 34.56));
        readings.add(new SensorReading("sensor001", "temperature", 28.32));
        readings.add(new SensorReading("sensor001", "temperature", 42.47));
        readings.add(new SensorReading("sensor001", "temperature", 38.89));
        readings.add(new SensorReading("sensor001", "temperature", 36.52));
        return readings;
    }

    private static List<SensorReading> getHumSensorReadings() {
        List<SensorReading> readings = new ArrayList<>();
        readings.add(new SensorReading("sensor002", "humidity", 56.21));
        readings.add(new SensorReading("sensor002", "humidity", 49.30));
        readings.add(new SensorReading("sensor002", "humidity", 62.87));
        readings.add(new SensorReading("sensor002", "humidity", 59.63));
        readings.add(new SensorReading("sensor002", "humidity", 60.51));
        return readings;
    }

    private static List<SensorReading> getLightSensorReadings() {
        List<SensorReading> readings = new ArrayList<>();
        readings.add(new SensorReading("sensor003", "light_intensity", 980.0));
        readings.add(new SensorReading("sensor003", "light_intensity", 865.0));
        readings.add(new SensorReading("sensor003", "light_intensity", 692.0));
        readings.add(new SensorReading("sensor003", "light_intensity", 1127.0));
        readings.add(new SensorReading("sensor003", "light_intensity", 1023.0));
        return readings;
    }

}
