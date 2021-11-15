package com.penglecode.flink.examples.wordcount;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * 基于流处理的WordCount
 * DataStream API
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/11/14 12:53
 */
public class WordCountExample3 {

    /**
     * parallelism(并行度)的优先级：代码中指定的 > Submit-Job时指定的 > flink全局配置(parallelism.default)
     */
    public static void main(String[] args) throws Exception {
        //1、创建流处理的执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //env.setParallelism(4); //设置流处理的并行度
        /*
         * 2、此处监听文本socket来获取事件流数据，在Linux系统下通过netcat来启动一个文本socket服务端，
         * 本机在WSL2-Ubuntu子系统中使用命令：nc -lk 7777启动一个文本socket服务端
         * 其中-l：listen监听的意思，-k：keep保持着不断开
         * (注意得先启动文本socket服务端！！！否则此运行程序会报错)
         */
        ParameterTool parameterTool = ParameterTool.fromArgs(args);
        String ncHost = parameterTool.get("nc.host", "localhost");
        int ncPort = parameterTool.getInt("nc.port", 7777);
        DataStream<String> inputDataStream = env.socketTextStream(ncHost, ncPort);
        //3、对数据流进行处理，具体来说就是将每行数据进行分词，收集<word,1>这样的二元组(最小粒度的二元组)
        DataStream<Tuple2<String,Integer>> resultStream = inputDataStream.flatMap(new WordTokenizer())
                //这里的数字参数指的就是flink Tuple元组类型的泛型位置(见注释)
                .keyBy(0) //区别于DataSet#groupBy(..)方法，因为数据流是一个一个来的，不像数据集那样一下就全部准备好了，所以从语义上讲，称作keyBy()
                .sum(1)
                //.setParallelism(4) //每个步骤都可以设置并行度,言外之意每个步骤都可以多线程执行
                ;
        //4、打印结果：parallelThreadIndex > (word,statedCount)
        //这个parallelThreadIndex指的就是并行线程的index下标，仔细观察结果你可以看见：同一个word都由同一个线程处理
        resultStream.print().setParallelism(1); //每个步骤都可以设置并行度,言外之意每个步骤都可以多线程执行
        //5、由于当前是流处理，需要手动触发这个任务(把数据流灌进来)，否则上面的print()是不起作用的
        env.execute();
    }

}
