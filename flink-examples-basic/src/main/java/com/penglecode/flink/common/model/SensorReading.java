package com.penglecode.flink.common.model;

/**
 * 传感器上数
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/11/18 23:03
 */
public class SensorReading {

    /**
     * 设备ID
     */
    private String deviceId;

    /**
     * 测点代码
     */
    private String metricCode;

    /**
     * 测点值
     */
    private Double metricValue;

    /**
     * 时间戳
     */
    private Long timestamp;

    public SensorReading() {
    }

    public SensorReading(String deviceId, String metricCode, Double metricValue) {
        this.deviceId = deviceId;
        this.metricCode = metricCode;
        this.metricValue = metricValue;
        this.timestamp = System.currentTimeMillis();
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getMetricCode() {
        return metricCode;
    }

    public void setMetricCode(String metricCode) {
        this.metricCode = metricCode;
    }

    public Double getMetricValue() {
        return metricValue;
    }

    public void setMetricValue(Double metricValue) {
        this.metricValue = metricValue;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public static SensorReading valueOf(String line) {
        try {
            if(line != null) {
                String[] values = line.split(",");
                if(values.length == 3) {
                    return new SensorReading(values[0], values[1], Double.valueOf(values[2]));
                }
            }
        } catch (NumberFormatException e) {
            //ignore
        }
        return null;
    }

    @Override
    public String toString() {
        return "SensorReading{" +
                "deviceId='" + deviceId + '\'' +
                ", metricCode='" + metricCode + '\'' +
                ", metricValue='" + metricValue + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
