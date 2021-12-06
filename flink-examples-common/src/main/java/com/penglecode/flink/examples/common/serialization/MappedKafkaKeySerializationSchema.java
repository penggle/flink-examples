package com.penglecode.flink.examples.common.serialization;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SerializationSchema;

import java.nio.charset.Charset;

/**
 * 默认的Kafka Key序列化Schema实现，基于JAVA8的Function来动态指定Key
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/11/27 22:22
 */
public class MappedKafkaKeySerializationSchema<T,K> implements SerializationSchema<T> {

    private final String charset;

    /**
     * 不能使用java.util.Function，因为其不可序列化
     */
    private final MapFunction<T,K> keyMapper;

    public MappedKafkaKeySerializationSchema(MapFunction<T,K> keyMapper) {
        this("UTF-8", keyMapper);
    }

    public MappedKafkaKeySerializationSchema(String charset, MapFunction<T,K> keyMapper) {
        this.keyMapper = keyMapper;
        this.charset = charset;
    }

    @Override
    public byte[] serialize(T element) {
        try {
            K key = keyMapper.map(element);
            return key.toString().getBytes(Charset.forName(charset));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected String getCharset() {
        return charset;
    }

    protected MapFunction<T, K> getKeyMapper() {
        return keyMapper;
    }

}
