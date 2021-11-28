package com.penglecode.flink.apiusage.transform;

import com.penglecode.flink.common.model.SensorReading;
import com.penglecode.flink.common.util.RandomDataUtils;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.*;

/**
 * 数据滚动聚合转换 API示例
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/11/20 22:54
 */
public class AggregationTransformExample {

    private static final Random RANDOM = new Random();

    public static void main(String[] args) throws Exception {
        //maxRollAggregatingTest1();
        //maxRollAggregatingTest2();
        //maxByRollAggregatingTest();
        avgByReduceRollAggregatingTest();
    }

    public static void maxRollAggregatingTest1() throws Exception {
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

    public static void maxRollAggregatingTest2() throws Exception {
        //1、创建执行上下文环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(4); //设置全局并行度(默认分区数)
        //2、从集合中构造数据源并做Map转换
        DataStream<SensorReading> dataStreamSource = env.fromCollection(RandomDataUtils.getRandomSensorReadings())
                .map(SensorReading::valueOf)
                .filter(Objects::nonNull);

        //3、打印输出结果
        dataStreamSource.print();

        //4、本例需求只关心各个测点的最大值，所以先Map成关心的数据(此处用元组表示)，再对元组进行keyBy
        KeyedStream<Tuple2<String,Double>, Tuple> keyedStream1 = dataStreamSource.map(new MapFunction<SensorReading, Tuple2<String,Double>>() {
            @Override
            public Tuple2<String,Double> map(SensorReading value) throws Exception {
                return new Tuple2<>(value.getMetricCode(), value.getMetricValue());
            }
        }).keyBy(0); //按照metricCode进行分组
        //5、执行滚动聚合操作
        DataStream<Tuple2<String,Double>> resultStream = keyedStream1.max(1); //对分组后的metricValue求滚动最大值
        //6、打印输出结果
        resultStream.print();
        //7、执行流处理任务
        env.execute();
    }

    public static void maxByRollAggregatingTest() throws Exception {
        //1、创建执行上下文环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1); //设置全局并行度(默认分区数)
        //2、从集合中构造数据源并做Map转换
        DataStream<SensorReading> dataStreamSource = env.fromCollection(getStaticSensorReadings())
                .map(SensorReading::valueOf)
                .filter(Objects::nonNull);

        //3、使用KeySelector来进行分组，即通过Lambda表达式指定分组key
        KeyedStream<SensorReading,String> keyedStream1 = dataStreamSource.keyBy(SensorReading::getMetricCode);
        //4、执行滚动聚合操作(使用maxBy不会修改原来记录)
        DataStream<SensorReading> resultStream = keyedStream1.maxBy("metricValue");
        //5、打印输出结果
        resultStream.print();
        //6、执行流处理任务
        env.execute();
    }

    public static void avgByReduceRollAggregatingTest() throws Exception {
        //1、创建执行上下文环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(4); //设置全局并行度(默认分区数)
        //2、从集合中构造数据源并做Map转换
        DataStream<SensorReading> dataStreamSource = env.fromCollection(getStaticSensorReadings())
                .map(SensorReading::valueOf)
                .filter(Objects::nonNull);

        //3、本例需求只关心各个测点的平均值，所以先Map成关心的数据(此处用元组表示)，再对元组进行keyBy
        KeyedStream<Tuple3<String,Double,Integer>,Tuple> keyedStream1 = dataStreamSource.map(new MapFunction<SensorReading, Tuple3<String,Double,Integer>>() {
            @Override
            public Tuple3<String, Double, Integer> map(SensorReading value) throws Exception {
                return new Tuple3<>(value.getMetricCode(), value.getMetricValue(), 1);
            }
        }).keyBy(0);
        //4、执行滚动聚合操作
        DataStream<Tuple2<String,Double>> resultStream = keyedStream1
                //先计算<metricCode,SUM(metricValue),COUNT>
                .reduce((currentState, latestReading) -> new Tuple3<>(currentState.getField(0), (Double) currentState.getField(1) + (Double) latestReading.getField(1), (Integer) currentState.getField(2) + (Integer) latestReading.getField(2)))
                //再对上一步计算的结果求AVG，即SUM(metricValue)/COUNT
                .map(new MapFunction<Tuple3<String, Double, Integer>, Tuple2<String, Double>>() {
                    @Override
                    public Tuple2<String, Double> map(Tuple3<String, Double, Integer> value) throws Exception {
                        return new Tuple2<>(value.getField(0), (Double) value.getField(1) / (Integer) value.getField(2));
                    }
                });
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
