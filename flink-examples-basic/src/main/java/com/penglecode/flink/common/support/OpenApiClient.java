package com.penglecode.flink.common.support;

import com.fasterxml.jackson.core.type.TypeReference;
import com.penglecode.flink.common.model.Joke;
import com.penglecode.flink.common.util.JsonUtils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

/**
 * @author pengpeng
 * @version 1.0
 * @since 2021/11/26 22:52
 */
public class OpenApiClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenApiClient.class);

    private static final String API_OPEN_URL = "https://api.apiopen.top";

    private static final OkHttpClient OK_HTTP_CLIENT = new OkHttpClient();

    /**
     * 获取笑话列表
     * https://api.apiopen.top/api.html#924fc6c4b2ca4e5a8ed0e9a3ad86d48d
     *
     * @param type  - 指定查询类型，可选参数(all/video/image/gif/text)
     * @param page  - 页码(传0或者不传会随机推荐)
     * @param count - 每页返回数量
     * @return
     */
    public static List<Joke> getJokeList(String type, Integer page, Integer count) {
        if(page == null || page < 0) {
            page = 1;
        }
        if(count == null || count < 1) {
            count = 10;
        }
        Request request = new Request.Builder()
                .url(API_OPEN_URL + "/getJoke?page=" + page + "&count=" + count + (type != null ? "&type=" + type : ""))
                .get()
                .build();
        try (Response response = OK_HTTP_CLIENT.newCall(request).execute()) {
            if(response.isSuccessful()) {
                ResponseBody body = response.body();
                if(body != null) {
                    OpenApiResult<List<Joke>> apiResult = JsonUtils.json2Object(body.string(), new TypeReference<OpenApiResult<List<Joke>>>(){});
                    return apiResult.getResult();
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return Collections.emptyList();
    }

    public static void main(String[] args) {
        List<Joke> jokeList = OpenApiClient.getJokeList(null, 1, 100);
        jokeList.forEach(System.out::println);
    }

}
