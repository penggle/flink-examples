package com.penglecode.flink.apiusage.sink;

import com.penglecode.flink.apiusage.function.JokeSourceFunction;
import com.penglecode.flink.apiusage.function.JokeTuple2MapFunction;
import com.penglecode.flink.common.model.Joke;
import com.penglecode.flink.common.util.JsonUtils;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.redis.RedisSink;
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisPoolConfig;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommand;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommandDescription;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisMapper;

/**
 * 数据输出到Redis的Sink API示例
 *
 * 官方示例文档：http://bahir.apache.org/docs/flink/current/flink-streaming-redis/
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/11/28 22:09
 */
public class SinkToRedisExample {

    public static void main(String[] args) throws Exception {
        //1、创建执行上下文环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(4); //设置全局并行度(默认分区数)
        //2、从SourceFunction中读取的输入数据流
        DataStream<Joke> inputStream = env.addSource(new JokeSourceFunction());
        //3、打印输入数据流
        inputStream.print();
        //4、输出到redis
        FlinkJedisPoolConfig jedisPoolConfig = new FlinkJedisPoolConfig.Builder()
                .setHost("127.0.0.1")
                .setPort(6379)
                .build();

        inputStream.map(new JokeTuple2MapFunction())
                .addSink(new RedisSink<>(jedisPoolConfig, new SimpleRedisMapper()));
        //5、执行流处理任务
        env.execute();
    }

    public static class SimpleRedisMapper implements RedisMapper<Tuple2<String,Joke>> {

        private static final String KEY_PREFIX = "joke:";

        @Override
        public RedisCommandDescription getCommandDescription() {
            return new RedisCommandDescription(RedisCommand.SET);
        }

        @Override
        public String getKeyFromData(Tuple2<String,Joke> tuple2) {
            return KEY_PREFIX + tuple2.f0;
        }

        @Override
        public String getValueFromData(Tuple2<String,Joke> tuple2) {
            return tuple2.f1 == null ? null : JsonUtils.object2Json(tuple2.f1);
        }

    }

}
