package com.penglecode.flink.examples.common.api.client;

import com.penglecode.flink.examples.common.api.dto.OpenApiResult;
import com.penglecode.flink.examples.common.model.Joke;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 基于https://api.apiopen.top/api.html上的开放API接口
 *
 * 开启完整HTTP报文日志：
 * <Logger name="com.penglecode.flink.examples.common.api.client.JokeApiClient" level="DEBUG"/>
 * 
 * @author 	pengpeng
 * @date 	2020年1月18日 下午5:18:17
 */
@FeignClient(name="openapi", qualifier="jokeApiClient", contextId="jokeApiClient", url="${feign.client.openapi.url}", fallbackFactory=JokeApiFallbackFactory.class)
public interface JokeApiClient {

	/**
	 * 根据id获取段子详情
	 * @param sid
	 * @return
	 */
	@GetMapping(value="/getSingleJoke", produces=MediaType.APPLICATION_JSON_VALUE)
	OpenApiResult<Joke> getJokeById(@RequestParam(name="sid") String sid);

	/**
	 * 根据条件查询段子列表
	 * @param type
	 * @param page
	 * @param count
	 * @return
	 */
	@GetMapping(value="/getJoke", produces=MediaType.APPLICATION_JSON_VALUE)
	OpenApiResult<List<Joke>> getJokeList(@RequestParam(name = "type", required = false) String type,
			@RequestParam(name = "page", defaultValue = "1") Integer page,
			@RequestParam(name = "count", defaultValue = "10") Integer count);
	
}
