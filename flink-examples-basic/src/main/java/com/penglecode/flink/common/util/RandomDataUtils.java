package com.penglecode.flink.common.util;

import com.penglecode.flink.common.model.StudentScore;

import java.util.*;

/**
 * @author pengpeng
 * @version 1.0
 * @since 2021/11/21 21:38
 */
public class RandomDataUtils {

    private static final Random RANDOM = new Random();

    public static final List<String> COURSES = Arrays.asList("语文", "数学", "英语");

    public static final List<String> SEXES = Arrays.asList("男", "女");

    public static final Map<String,Double> METRICS = new HashMap<>();

    static {
        METRICS.put("temperature", 30.0);
        METRICS.put("humidity", 50.0);
    }

    public static List<StudentScore> getRandomStudentScores(int size) {
        List<StudentScore> dataList = new ArrayList<>();
        int sexSize = SEXES.size();
        int courseSize = COURSES.size();
        for(int i = 1; i <= size; i++) {
            dataList.add(new StudentScore(String.valueOf(i), "学生" + i, SEXES.get(RANDOM.nextInt(sexSize)), COURSES.get(RANDOM.nextInt(courseSize)), RANDOM.nextInt(100)));
        }
        return dataList;
    }

    public static List<StudentScore> getRandomScoresOfStudent(int studentSize) {
        List<StudentScore> dataList = new ArrayList<>();
        int sexSize = SEXES.size();
        int courseSize = COURSES.size();
        for(int i = 1; i <= studentSize; i++) {
            String studentId = String.valueOf(i);
            String studentName = "学生" + i;
            String studentSex = SEXES.get(RANDOM.nextInt(sexSize));
            for(String courseName : COURSES) {
                dataList.add(new StudentScore(studentId, studentName, studentSex, courseName, 40 + RANDOM.nextInt(60)));
            }
        }
        return dataList;
    }

    /**
     * 随机生产n个设备温度上数
     */
    public static List<String> getRandomSensorReadings() {
        List<String> readings = new ArrayList<>();
        String[] metrics = METRICS.keySet().toArray(new String[0]);
        for(int i = 0; i < 20; i++) {
            String deviceId = "sensor-" + RANDOM.nextInt(5);
            String metricCode = metrics[RANDOM.nextInt(2)];
            Double metricValue = METRICS.get(metricCode) + 10 * RANDOM.nextGaussian();
            readings.add(String.format("%s,%s,%.2f", deviceId, metricCode, metricValue));
        }
        return readings;
    }

}
