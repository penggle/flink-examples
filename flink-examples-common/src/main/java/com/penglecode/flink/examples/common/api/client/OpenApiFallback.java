package com.penglecode.flink.examples.common.api.client;

import com.penglecode.flink.examples.common.api.dto.OpenApiResult;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/12/31 11:34
 */
public abstract class OpenApiFallback {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenApiFallback.class);

    protected final Throwable cause;

    public OpenApiFallback(Throwable cause) {
        this.cause = cause;
    }

    protected <T> OpenApiResult<T> commonFallbackResult() {
        LOGGER.error(String.format("fallback for exception: %s", cause.getMessage()), cause);
        if(cause instanceof FeignException) {
            FeignException feignException = (FeignException) cause;
            return new OpenApiResult<>(feignException.status(), feignException.getMessage(), null);
        }
        return defaultFallbackResult();
    }

    protected <T> OpenApiResult<T> defaultFallbackResult() {
        return new OpenApiResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), cause.getMessage(), null);
    }

}
