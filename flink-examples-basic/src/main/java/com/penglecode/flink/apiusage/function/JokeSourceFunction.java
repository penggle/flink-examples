package com.penglecode.flink.apiusage.function;

import com.penglecode.flink.common.model.Joke;
import com.penglecode.flink.common.support.OpenApiClient;
import org.apache.commons.collections.CollectionUtils;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class JokeSourceFunction implements SourceFunction<Joke> {

    private static final int MAX_RETRIES = 3;

    private volatile boolean running = true;

    @Override
    public void run(SourceContext<Joke> ctx) {
        List<Joke> jokeList;
        int page = 1;
        int count = 10;
        int retries = 0;
        while(running && retries < MAX_RETRIES) {
            jokeList = OpenApiClient.getJokeList(null, page++, count);
            if(CollectionUtils.isEmpty(jokeList)) {
                retries++;
            } else {
                for(int i = 0, size = jokeList.size(); i < size; i++) {
                    if(i > 0) {
                        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(1));
                    }
                    ctx.collect(jokeList.get(i));
                }
            }
        }
    }

    @Override
    public void cancel() {
        running = false;
    }

}