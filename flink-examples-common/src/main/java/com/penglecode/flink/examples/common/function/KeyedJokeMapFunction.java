package com.penglecode.flink.examples.common.function;

import com.penglecode.flink.examples.common.model.Joke;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;

/**
 * Joke ==> Tuple2<sid,Joke>
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/11/28 22:31
 */
public class KeyedJokeMapFunction implements MapFunction<Joke, Tuple2<String,Joke>> {

    @Override
    public Tuple2<String,Joke> map(Joke value) {
        return new Tuple2<>(value.getSid(), value);
    }

}
