package com.penglecode.flink.apiusage.function;

import com.penglecode.flink.common.model.Joke;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;

/**
 * @author pengpeng
 * @version 1.0
 * @since 2021/11/28 22:31
 */
public class JokeTuple2MapFunction implements MapFunction<Joke, Tuple2<String,Joke>> {

    @Override
    public Tuple2<String,Joke> map(Joke value) throws Exception {
        return new Tuple2<>(value.getSid(), value);
    }

}
