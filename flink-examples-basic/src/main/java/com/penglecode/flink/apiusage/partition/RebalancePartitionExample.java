package com.penglecode.flink.apiusage.partition;

import com.penglecode.flink.apiusage.function.JokeSourceFunction;
import com.penglecode.flink.common.model.Joke;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * rebalance()轮训(默认的)重分区示例
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/11/26 23:21
 */
public class RebalancePartitionExample {

    public static void main(String[] args) throws Exception {
        //1、创建执行上下文环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(4); //设置全局并行度(默认分区数)
        //2、从SourceFunction中源源不断的读取数据源
        DataStream<Joke> dataStreamSource = env.addSource(new JokeSourceFunction());
        //3、输出数据流处理结果
        //在有无rebalance()情况下打印结果的分区情况几乎没有区别，都是轮训
        dataStreamSource.rebalance().print();
        //4、执行流处理任务
        env.execute();
    }

}
