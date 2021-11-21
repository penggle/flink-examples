package com.penglecode.flink.examples.wordcount;

import org.apache.flink.api.java.tuple.Tuple2;
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
public class WordCountExample2 {

    public static void main(String[] args) throws Exception {
        //1、创建流处理的执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(4); //设置流处理的全局并行度
        //2、从hello.txt文件中读取数据集
        DataStream<String> inputDataStream = env.readTextFile(WordCountExample2.class.getResource("/data/hello.txt").toString());
        //3、对数据流进行处理，具体来说就是将每行数据进行分词，收集<word,1>这样的二元组(最小粒度的二元组)
        DataStream<Tuple2<String,Integer>> resultStream = inputDataStream.flatMap(new WordTokenizer())
                //这里的数字参数指的就是flink Tuple元组类型的泛型位置(见注释)
                .keyBy(0) //区别于DataSet#groupBy(..)方法，因为数据流是一个一个来的，不像数据集那样一下就全部准备好了，所以从语义上讲，称作keyBy()
                .sum(1);
        //4、打印结果：parallelThreadIndex > (word,statedCount)
        //这个parallelThreadIndex指的就是print()并行线程的index下标(此处使用全局并行度4)
        resultStream.print();
        //5、由于当前是流处理，需要手动触发这个任务(把数据流灌进来)，否则上面的print()是不起作用的
        env.execute();
    }

}
