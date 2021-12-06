package com.penglecode.flink.examples.common.source;

import com.penglecode.flink.examples.common.enums.SensorMetricEnum;
import com.penglecode.flink.examples.common.model.SensorReading;
import org.springframework.util.Assert;

import java.util.Random;

/**
 * 随机生成传感器数据的SourceFunction
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/11/5 22:14
 */
public class SensorReadingSourceFunction extends CommonSourceFunction<SensorReading> {

    private final SensorMetricEnum[] sensorMetrics;

    /**
     * 随机设备数，用于生成deviceId
     * 例如为5：则会随机生成0~4之间的设备ID
     */
    private final int deviceSize;

    public SensorReadingSourceFunction(int deviceSize, int sourceSize, SensorMetricEnum... sensorMetrics) {
        super(sourceSize);
        Assert.notEmpty(sensorMetrics, "Parameter 'sensorMetrics' can not be empty");
        Assert.isTrue(deviceSize > 0, "Parameter 'deviceSize' must be > 0");
        this.sensorMetrics = sensorMetrics;
        this.deviceSize = deviceSize;
    }

    @Override
    protected SensorReading createSource(Random random) {
        SensorMetricEnum metric = sensorMetrics[random.nextInt(sensorMetrics.length)];
        String deviceId = "sensor-" + random.nextInt(deviceSize);
        String metricCode = metric.getCode();
        Double metricValue = metric.getRandomValue();
        return new SensorReading(deviceId, metricCode, metricValue);
    }

}
