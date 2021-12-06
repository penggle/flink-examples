package com.penglecode.flink.examples.common.serialization;

import com.penglecode.flink.examples.common.util.JsonUtils;
import org.apache.flink.api.common.serialization.SerializationSchema;

import java.nio.charset.Charset;

/**
 * 默认的JSON序列化Schema
 *
 * 注意：自定义的序列化Schema中不要出现没有实现java.io.Serializable的成员变量，
 * 因为DeserializationSchema/SerializationSchema都是需要能被JDK序列化的，那就需要要求其自定义类也要求能够被JDK序列化
 * 否则会出现java.io.NotSerializableException异常
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/11/27 20:56
 */
public class DefaultJsonSerializationSchema<T> implements SerializationSchema<T> {

    private final String charset;

    public DefaultJsonSerializationSchema() {
        this("UTF-8");
    }

    public DefaultJsonSerializationSchema(String charset) {
        this.charset = charset;
    }

    /**
     * 同理，取而代之的是使用JsonUtils工具类而不是定义一个ObjectMapper成员变量
     * 来规避objectMapper.registerModule(new JavaTimeModule());出现的java.io.NotSerializableException异常
     *
     * @param element
     * @return
     */
    @Override
    public byte[] serialize(T element) {
        if(element == null) {
            return null;
        }
        return JsonUtils.object2Json(element).getBytes(Charset.forName(charset));
    }

    protected String getCharset() {
        return charset;
    }

}
