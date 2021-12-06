package com.penglecode.flink.examples.common.source;

import com.penglecode.flink.examples.common.api.client.JokeApiClient;
import com.penglecode.flink.examples.common.api.dto.OpenApiResult;
import com.penglecode.flink.examples.common.model.Joke;
import com.penglecode.flink.examples.common.util.SpringUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * 随机生成传感器数据的SourceFunction
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/11/5 22:14
 */
public class JokeSourceFunction extends RichSourceFunction<Joke> {

    private static final Logger LOGGER = LoggerFactory.getLogger(JokeSourceFunction.class);

    private static final int MAX_RETRIES = 3;

    private volatile boolean running;

    private JokeApiClient jokeApiClient;

    @Override
    public void open(Configuration parameters) {
        LOGGER.info(">>> parameters = {}", parameters);
        ClassLoader userCodeClassLoader = getRuntimeContext().getUserCodeClassLoader();
        LOGGER.info(">>> userCodeClassLoader = {}", userCodeClassLoader);
        this.jokeApiClient = SpringUtils.getBean("jokeApiClient", JokeApiClient.class);
        this.running = true;
    }

    @Override
    public void run(SourceContext<Joke> ctx) {
        List<Joke> jokeList;
        int page = 1;
        int count = 10;
        int retries = 0;
        while(running && retries < MAX_RETRIES) {
            jokeList = getJokeList(page++, count);
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

    protected List<Joke> getJokeList(int page, int count) {
        OpenApiResult<List<Joke>> result = jokeApiClient.getJokeList(null, page, count);
        if(result != null && result.isSuccessful()) {
            return result.getResult();
        }
        return Collections.emptyList();
    }

}