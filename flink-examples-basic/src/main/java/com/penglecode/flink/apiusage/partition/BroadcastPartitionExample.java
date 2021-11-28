package com.penglecode.flink.apiusage.partition;

import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Arrays;
import java.util.List;

/**
 * broadcast()广播重分区示例
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/11/25 22:35
 */
public class BroadcastPartitionExample {

    public static void main(String[] args) throws Exception {
        //1、创建执行上下文环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(2); //设置全局并行度(默认分区数)
        //2、从集合中构造数据源并做Map转换
        env.fromCollection(getKeyword())
                .map(new RichMapFunction<String,String>() {
                    @Override
                    public String map(String value) {
                        System.out.println("Map1【" + (getRuntimeContext().getIndexOfThisSubtask() + 1) + "】 > " + value);
                        return "hello " + value;
                    }
                }).broadcast() //在多个分区的情况下，看看有无broadcast()的区别
                .map(new RichMapFunction<String,String>() {
                    @Override
                    public String map(String value) {
                        System.out.println("Map2【" + (getRuntimeContext().getIndexOfThisSubtask() + 1) + "】 > " + value);
                        return value;
                    }
                });
        //4、执行流处理任务
        env.execute();
    }

    public static List<String> getKeyword() {
        return Arrays.asList("spark", "flink", "hadoop", "hbase", "hive", "storm", "presto", "clickhouse", "kafka", "flume");
    }

}
