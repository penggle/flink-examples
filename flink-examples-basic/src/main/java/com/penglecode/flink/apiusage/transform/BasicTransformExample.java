package com.penglecode.flink.apiusage.transform;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * 数据基本转换 API示例
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/11/20 18:57
 */
public class BasicTransformExample {

    public static void main(String[] args) throws Exception {
        LocalDate nowDate = LocalDate.now();
        //1、创建执行上下文环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(4); //设置全局并行度(默认分区数)
        //2、从集合中构造数据源并做Map转换
        DataStream<String> dataStreamSource = env.fromCollection(getBirthdayList())
                .filter(date -> date.getMonth().equals(nowDate.getMonth())) //只取当月生日的
                .map(LocalDate::toString); //LocalDate格式化为String
        //3、输出数据流处理结果
        dataStreamSource.print();
        //4、执行流处理任务
        env.execute();
    }

    protected static List<LocalDate> getBirthdayList() {
        return new Random().ints(-100, 100)
                .limit(50)
                .mapToObj(delta -> LocalDate.now().plusDays(delta))
                .collect(Collectors.toList());
    }

}
