package com.penglecode.flink.examples.wordcount;

import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple2;

/**
 * 基于批处理的WordCount
 * DataSet API
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/11/13 21:59
 */
public class WordCountExample1 {

    public static void main(String[] args) throws Exception {
        //1、创建批处理的执行环境
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        //2、从hello.txt文件中读取数据集
        DataSet<String> inputDataSet = env.readTextFile(WordCountExample1.class.getResource("/hello.txt").toString());
        //3、对数据集进行处理，具体来说就是将每行数据进行分词，收集<word,1>这样的二元组(最小粒度的二元组)
        DataSet<Tuple2<String,Integer>> resultSet = inputDataSet.flatMap(new WordTokenizer())
                .groupBy(0)
                .sum(1); //这个字段位置指的就是flink Tuple元组类型的泛型位置(见注释)
        //4、打印结果
        resultSet.print();
    }



}
