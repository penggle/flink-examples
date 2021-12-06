package com.penglecode.flink.streaming.examples.transform;

import com.penglecode.flink.examples.FlinkExample;
import com.penglecode.flink.examples.common.model.Joke;
import com.penglecode.flink.examples.common.source.JokeSourceFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

/**
 * 富函数转换示例
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/11/24 23:23
 */
@Component
public class RichFunctionTransformExample extends FlinkExample {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //1、创建执行上下文环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(2); //设置全局并行度(默认分区数)
        //2、添加SourceFunction数据源
        DataStream<Joke> inputStream = env.addSource(new JokeSourceFunction());
        //3、使用RichFunction进行转换
        DataStream<Tuple2<String,Integer>> resultStream = inputStream
                .map(new RichMapFunction<Joke,Tuple2<String,Integer>>() {
                    @Override
                    public void open(Configuration parameters) {
                        /*
                         * open/close方法在当前map()的所有分区(并行度)中执行一次
                         * 例如当前map()的并行度为4，则open/close方法会执行4次
                         */
                        System.out.println("【open】打开连接!");
                    }
                    @Override
                    public Tuple2<String,Integer> map(Joke value) {
                        return new Tuple2<>(value.getType(), 1);
                    }
                    @Override
                    public void close() {
                        /*
                         * open/close方法在当前map()的所有分区(并行度)中执行一次
                         * 例如当前map()的并行度为4，则open/close方法会执行4次
                         */
                        System.out.println("【close】关闭连接!");
                    }
                })
                .keyBy(0) //按类型分组
                .sum(1)
                .setParallelism(4);
        //4、输出数据流处理结果
        resultStream.print();
        //5、执行流处理任务
        env.execute();
    }

}
