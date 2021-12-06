package com.penglecode.flink.streaming.examples.source;

import com.penglecode.flink.examples.FlinkExample;
import com.penglecode.flink.examples.common.model.SensorReading;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * 数据来自SourceFunction的Source API示例
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/11/19 0:03
 */
@Component
public class SourceFromFunctionExample extends FlinkExample {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //1、创建执行上下文环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(4); //设置全局并行度(默认分区数)
        //2、从SourceFunction中源源不断的读取数据源
        DataStream<SensorReading> dataStreamSource = env.addSource(new SensorReadingSourceFunction());
        //3、输出数据流处理结果
        dataStreamSource.print();
        //4、执行流处理任务
        env.execute();
    }

    public static class SensorReadingSourceFunction implements SourceFunction<SensorReading> {

        private final Random random = new Random();

        private volatile boolean running = true;

        @Override
        public void run(SourceContext<SensorReading> ctx) throws Exception {
            while(running) {
                double temperature = 30 + (random.nextDouble() * 10);
                SensorReading sensorReading = new SensorReading("sensor001", "temperature", Double.valueOf(String.format("%.2f", temperature)));
                ctx.collect(sensorReading);
                LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(1));
            }
        }

        @Override
        public void cancel() {
            running = false;
        }

    }

}
