package com.penglecode.flink.streaming.examples.boot;

import com.penglecode.flink.BasePackage;
import com.penglecode.flink.examples.FlinkExample;
import com.penglecode.flink.examples.FlinkExampleFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * FLink实时流示例应用SpringBoot启动入口
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/12/5 18:08
 */
@EnableFeignClients(basePackageClasses=BasePackage.class)
@SpringBootApplication(scanBasePackageClasses= BasePackage.class)
public class FlinkStreamingExampleApplication implements ApplicationRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlinkStreamingExampleApplication.class);

    /**
     * 运行示例之前需要设置本次运行的示例类
     * IDEA: Run/Debug Configurations[FlinkStreamingExampleApplication] -> Configuration -> Program Arguments :
     * --flink.example.class=com.penglecode.flink.streaming.examples.xxx.XxxExample
     */
    public static void main(String[] args) {
        //本例以非web方式(ServletWebServer,ReactiveWebServer)启动
        new SpringApplicationBuilder(FlinkStreamingExampleApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }

    /**
     * SpringBoot完全启动完毕后执行ApplicationRunner#run(args)方法，
     * 该方法执行完毕，JVM正常退出(因为上面是以非web方式启动的，当前线程即是main线程(看日志便知))
     * @param args
     * @throws Exception
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        LOGGER.info("【ApplicationRunner】==> args = {}", (Object) args.getSourceArgs());
        FlinkExample flinkExample = FlinkExampleFactory.getFlinkExample(args);
        if(flinkExample != null) {
            flinkExample.run(args);
        }
    }

}
