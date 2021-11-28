package com.penglecode.flink.apiusage.transform;

import com.penglecode.flink.common.model.StudentScore;
import com.penglecode.flink.common.util.RandomDataUtils;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * 富函数转换示例
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/11/24 23:23
 */
public class RichFunctionTransformExample {

    public static void main(String[] args) throws Exception {
        //1、创建执行上下文环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(2); //设置全局并行度(默认分区数)
        //2、从集合中构造数据源
        DataStream<StudentScore> inputStream = env.fromCollection(RandomDataUtils.getRandomScoresOfStudent(10));
        //3、使用RichFunction进行转换
        DataStream<Tuple3<String,String,String>> resultStream = inputStream
                .keyBy(StudentScore::getStudentId) //先按学生分组
                .map(new RichMapFunction<StudentScore, Tuple3<String,String,String>>() {
                    @Override
                    public void open(Configuration parameters) {
                        /**
                         * open/close方法在当前map()的所有分区(并行度)中执行一次
                         * 例如当前map()的并行度为4，则open/close方法会执行4次
                         */
                        System.out.println("【open】打开连接!");
                    }
                    @Override
                    public Tuple3<String,String,String> map(StudentScore value) {
                        return new Tuple3<>(value.getStudentName(), value.getCourseName(), getGradeOfScore(value.getCourseScore()));
                    }
                    @Override
                    public void close() {
                        /**
                         * open/close方法在当前map()的所有分区(并行度)中执行一次
                         * 例如当前map()的并行度为4，则open/close方法会执行4次
                         */
                        System.out.println("【close】关闭连接!");
                    }
                }).setParallelism(4);
        //4、输出数据流处理结果
        resultStream.print();
        //5、执行流处理任务
        env.execute();
    }

    protected static String getGradeOfScore(Integer score) {
        if(score >= 90) {
            return "A";
        } else if(score >= 80) {
            return "B";
        } else if(score >= 70) {
            return "C";
        } else if(score >= 60) {
            return "D";
        } else {
            return "E";
        }
    }

}
