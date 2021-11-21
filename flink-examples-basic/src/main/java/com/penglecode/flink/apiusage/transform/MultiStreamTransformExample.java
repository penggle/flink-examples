package com.penglecode.flink.apiusage.transform;


import com.penglecode.flink.common.model.SensorReading;
import com.penglecode.flink.common.model.StudentScore;
import com.penglecode.flink.common.util.RandomDataUtils;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

/**
 * 多流转换 API示例
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/11/21 21:20
 */
public class MultiStreamTransformExample {

    public static void main(String[] args) throws Exception {

    }

    public static void splitStreamTest() throws Exception {
        //1、创建执行上下文环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(4); //设置全局并行度
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

}
