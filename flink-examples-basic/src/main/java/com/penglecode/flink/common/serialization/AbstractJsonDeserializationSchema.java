package com.penglecode.flink.common.serialization;

import com.penglecode.flink.common.util.JsonUtils;
import org.apache.flink.api.common.functions.InvalidTypesException;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.TypeExtractor;
import org.apache.flink.util.FlinkRuntimeException;

import java.nio.charset.Charset;

/**
 * JSON反序列化Schema基类
 *
 * 注意：自定义的反序列化Schema中不要出现没有实现java.io.Serializable的成员变量，
 * 因为DeserializationSchema/SerializationSchema都是需要能被JDK序列化的，那就需要要求其自定义类也要求能够被JDK序列化
 * 否则会出现java.io.NotSerializableException异常
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/11/27 20:59
 */
public abstract class AbstractJsonDeserializationSchema<T> implements DeserializationSchema<T> {

    private final TypeInformation<T> typeInformation;

    /**
     * 取而代之的是String类型的charset而不是java.nio.charset.Charset对象
     * 否则会出现异常：java.io.NotSerializableException: sun.nio.cs.UTF_8
     */
    private final String charset;

    protected AbstractJsonDeserializationSchema() {
        this("UTF-8");
    }

    protected AbstractJsonDeserializationSchema(String charset) {
        try {
            this.charset = charset;
            this.typeInformation =
                    TypeExtractor.createTypeInfo(
                            AbstractJsonDeserializationSchema.class, getClass(), 0, null, null);
        } catch (InvalidTypesException e) {
            throw new FlinkRuntimeException(
                    "The implementation of AbstractDeserializationSchema is using a generic variable. "
                            + "This is not supported, because due to Java's generic type erasure, it will not be possible to "
                            + "determine the full type at runtime. For generic implementations, please pass the TypeInformation "
                            + "or type class explicitly to the constructor.");
        }
    }

    /**
     * 同理，取而代之的是使用JsonUtils工具类而不是定义一个ObjectMapper成员变量
     * 来规避objectMapper.registerModule(new JavaTimeModule());出现的java.io.NotSerializableException异常
     *
     * @param message
     * @return
     */
    @Override
    public T deserialize(byte[] message) {
        return JsonUtils.json2Object(new String(message, Charset.forName(charset)), typeInformation.getTypeClass());
    }

    @Override
    public boolean isEndOfStream(T nextElement) {
        return false;
    }

    @Override
    public TypeInformation<T> getProducedType() {
        return typeInformation;
    }

    protected String getCharset() {
        return charset;
    }

}
