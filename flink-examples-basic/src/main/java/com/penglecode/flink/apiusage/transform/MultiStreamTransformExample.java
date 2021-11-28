package com.penglecode.flink.apiusage.transform;


import com.penglecode.flink.common.model.*;
import com.penglecode.flink.common.util.RandomDataUtils;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.streaming.api.functions.co.CoMapFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

import java.util.ArrayList;
import java.util.List;

/**
 * 多流转换 API示例
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/11/21 21:20
 */
public class MultiStreamTransformExample {

    public static void main(String[] args) throws Exception {
        //splitStreamTest();
        //connectStreamTest();
        unionStreamTest();
    }

    /**
     * 一条流切分成多条流的示例
     */
    public static void splitStreamTest() throws Exception {
        //1、创建执行上下文环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(4); //设置全局并行度(默认分区数)
        //2、从集合中构造数据源并做Map转换
        DataStream<StudentScore> inputStream = env.fromCollection(RandomDataUtils.getRandomStudentScores(20));
        //3、定义Tag标签
        TypeInformation<StudentScore> typeInformation = TypeInformation.of(StudentScore.class);
        OutputTag<StudentScore> passedScoreTag = new OutputTag<>("passed-scores", typeInformation);
        OutputTag<StudentScore> unpassedScoreTag = new OutputTag<>("unpassed-scores", typeInformation);
        //4、在ProcessFunction中进行打标签
        SingleOutputStreamOperator<StudentScore> mainStream = inputStream.process(new ProcessFunction<StudentScore, StudentScore>() {
            @Override
            public void processElement(StudentScore value, ProcessFunction<StudentScore, StudentScore>.Context ctx, Collector<StudentScore> out) throws Exception {
                out.collect(value);
                if(value.getCourseScore() >= 60) {
                    ctx.output(passedScoreTag, value); //大于等于60分的标记为及格
                } else {
                    ctx.output(unpassedScoreTag, value); //小于60分的标记为不及格
                }
            }
        });
        mainStream.getSideOutput(passedScoreTag).print("passed");
        mainStream.getSideOutput(unpassedScoreTag).print("unpassed");
        //5、执行流处理任务
        env.execute();
    }

    /**
     * 两条不同数据类型的流合并成同一条流的示例
     */
    public static void connectStreamTest() throws Exception {
        //1、创建执行上下文环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1); //设置全局并行度
        //2、从集合中构造数据源
        DataStream<GoogleAccount> inputStream1 = env.fromCollection(getGoogleAccounts());
        inputStream1.print("Google");

        DataStream<GithubAccount> inputStream2 = env.fromCollection(getGithubAccounts());
        inputStream2.print("Github");
        //3、两流合并后的数据统一化转换
        DataStream<FederalAccount> connectedStream = inputStream1.connect(inputStream2).map(new CoMapFunction<GoogleAccount, GithubAccount, FederalAccount>() {
            @Override
            public FederalAccount map1(GoogleAccount value) {
                return new FederalAccount(value.getGmail(), value.getClientId(), value.getClientSecret(), value.getLoginUrl(), "Google");
            }

            @Override
            public FederalAccount map2(GithubAccount value) {
                return new FederalAccount(value.getUserName(), value.getAppId(), value.getAppSecret(), value.getLoginUrl(), "Github");
            }
        });
        connectedStream.print("Federal");
        //4、执行流处理任务
        env.execute();
    }

    /**
     * 多条数据类型相同的流合并成同一条流的示例
     * 注意：union操作只能用于相同数据类型的流
     */
    public static void unionStreamTest() throws Exception {
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

    private static List<GoogleAccount> getGoogleAccounts() {
        List<GoogleAccount> accounts = new ArrayList<>();
        accounts.add(new GoogleAccount("grubby@gmail.com", "grubby001", "123456", "https://accounts.google.com/"));
        accounts.add(new GoogleAccount("lucas@gmail.com", "lucas001", "123456", "https://accounts.google.com/"));
        accounts.add(new GoogleAccount("zebediah@gmail.com", "zebediah001", "123456", "https://accounts.google.com/"));
        accounts.add(new GoogleAccount("dudley@gmail.com", "dudley001", "123456", "https://accounts.google.com/"));
        accounts.add(new GoogleAccount("lombard@gmail.com", "lombard001", "123456", "https://accounts.google.com/"));
        accounts.add(new GoogleAccount("eldon@gmail.com", "eldon001", "123456", "https://accounts.google.com/"));
        return accounts;
    }

    private static List<GithubAccount> getGithubAccounts() {
        List<GithubAccount> accounts = new ArrayList<>();
        accounts.add(new GithubAccount("joanna", "joanna001", "123456", "https://github.com/login"));
        accounts.add(new GithubAccount("vanessa", "vanessa001", "123456", "https://github.com/login"));
        accounts.add(new GithubAccount("ritamea", "ritamea001", "123456", "https://github.com/login"));
        accounts.add(new GithubAccount("lorelei", "lorelei001", "123456", "https://github.com/login"));
        accounts.add(new GithubAccount("nessia", "nessia001", "123456", "https://github.com/login"));
        accounts.add(new GithubAccount("georgia", "georgia001", "123456", "https://github.com/login"));
        return accounts;
    }

}
