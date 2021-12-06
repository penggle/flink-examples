package com.penglecode.flink.streaming.examples.function;

import com.penglecode.flink.examples.FlinkExample;
import com.penglecode.flink.examples.common.enums.SensorMetricEnum;
import com.penglecode.flink.examples.common.model.SensorReading;
import com.penglecode.flink.examples.common.source.SensorReadingSourceFunction;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

/**
 * 通过ProcessFunction来实现数据流分割的示例
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/12/5 23:12
 */
@Component
public class ProcessFunctionExample extends FlinkExample {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //1、创建执行上下文环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(4); //设置全局并行度(默认分区数)
        //2、从集合中构造数据源并做Map转换
        DataStream<SensorReading> inputStream = env.addSource(new SensorReadingSourceFunction(5, 20, SensorMetricEnum.values()));
        //3、定义Tag标签
        TypeInformation<SensorReading> typeInformation = TypeInformation.of(SensorReading.class);
        OutputTag<SensorReading> temperatureTag = new OutputTag<>(SensorMetricEnum.TEMPERATURE.getCode(), typeInformation);
        OutputTag<SensorReading> humidityTag = new OutputTag<>(SensorMetricEnum.HUMIDITY.getCode(), typeInformation);
        //4、在ProcessFunction中进行打标签
        SingleOutputStreamOperator<SensorReading> mainStream = inputStream.process(new ProcessFunction<SensorReading, SensorReading>() {
            @Override
            public void processElement(SensorReading value, ProcessFunction<SensorReading, SensorReading>.Context ctx, Collector<SensorReading> out) {
                out.collect(value);
                if(value.getMetricCode().equals(SensorMetricEnum.TEMPERATURE.getCode())) {
                    ctx.output(temperatureTag, value); //温度数据流
                } else {
                    ctx.output(humidityTag, value); //湿度数据流
                }
            }
        });
        mainStream.getSideOutput(temperatureTag).print(SensorMetricEnum.TEMPERATURE.getCode());
        mainStream.getSideOutput(humidityTag).print(SensorMetricEnum.HUMIDITY.getCode());
        //5、执行流处理任务
        env.execute();
    }

}
