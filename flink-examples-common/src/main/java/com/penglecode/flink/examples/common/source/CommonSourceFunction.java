package com.penglecode.flink.examples.common.source;

import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * 随机生成数据源的基类
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/11/5 23:00
 */
public abstract class CommonSourceFunction<T> implements SourceFunction<T> {

    private final Random random = new Random();

    /**
     * 如果小于等于0，则意味着是无界流，并且伴随着停顿时间(1秒1个数据)
     * 如果大于0，则意味着生成sourceSize个数据
     */
    private final int sourceSize;

    private volatile boolean running;

    public CommonSourceFunction(int sourceSize) {
        this.sourceSize = sourceSize;
        this.running = true;
    }

    @Override
    public void run(SourceContext<T> ctx) throws Exception {
        int sourceCount = 0;
        while (running) {
            ctx.collect(createSource(random));
            if(sourceSize <= 0) {
                LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(1));
            } else if(sourceSize == ++sourceCount) {
                break;
            }
        }
    }

    @Override
    public void cancel() {
        this.running = false;
    }

    protected abstract T createSource(Random random);

}
