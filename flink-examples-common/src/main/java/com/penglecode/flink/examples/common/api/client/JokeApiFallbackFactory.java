package com.penglecode.flink.examples.common.api.client;

import com.penglecode.flink.examples.common.api.dto.OpenApiResult;
import com.penglecode.flink.examples.common.model.Joke;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * {@link JokeApiClient}的FallbackFactory
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/12/31 11:31
 */
@Component
public class JokeApiFallbackFactory implements FallbackFactory<JokeApiClient> {

    @Override
    public JokeApiClient create(Throwable cause) {
        return new JokeApiFallbackImpl(cause);
    }

    public static class JokeApiFallbackImpl extends OpenApiFallback implements JokeApiClient {

        public JokeApiFallbackImpl(Throwable cause) {
            super(cause);
        }

        @Override
        public OpenApiResult<Joke> getJokeById(String sid) {
            return commonFallbackResult();
        }

        @Override
        public OpenApiResult<List<Joke>> getJokeList(String type, Integer page, Integer count) {
            return commonFallbackResult();
        }

    }

}
