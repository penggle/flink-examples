package com.penglecode.flink.streaming.examples.transform;


import com.penglecode.flink.examples.FlinkExample;
import com.penglecode.flink.examples.common.model.FederalAccount;
import com.penglecode.flink.examples.common.model.GithubAccount;
import com.penglecode.flink.examples.common.model.GoogleAccount;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.CoMapFunction;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 连接多流转换 API示例
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/11/21 21:20
 */
@Component
public class ConnectStreamTransformExample extends FlinkExample {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //1、创建执行上下文环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1); //设置全局并行度
        //2、从集合中构造数据源
        DataStream<GoogleAccount> inputStream1 = env.fromCollection(getGoogleAccounts());
        inputStream1.print("Google");

        DataStream<GithubAccount> inputStream2 = env.fromCollection(getGithubAccounts());
        inputStream2.print("Github");
        //3、两流合并后的数据统一化转换
        DataStream<FederalAccount> connectedStream = inputStream1.connect(inputStream2).map(new CoMapFunction<GoogleAccount, GithubAccount, FederalAccount>() {
            @Override
            public FederalAccount map1(GoogleAccount value) {
                return new FederalAccount(value.getGmail(), value.getClientId(), value.getClientSecret(), value.getLoginUrl(), "Google");
            }

            @Override
            public FederalAccount map2(GithubAccount value) {
                return new FederalAccount(value.getUserName(), value.getAppId(), value.getAppSecret(), value.getLoginUrl(), "Github");
            }
        });
        connectedStream.print("Federal");
        //4、执行流处理任务
        env.execute();
    }

    private static List<GoogleAccount> getGoogleAccounts() {
        List<GoogleAccount> accounts = new ArrayList<>();
        accounts.add(new GoogleAccount("grubby@gmail.com", "grubby001", "123456", "https://accounts.google.com/"));
        accounts.add(new GoogleAccount("lucas@gmail.com", "lucas001", "123456", "https://accounts.google.com/"));
        accounts.add(new GoogleAccount("zebediah@gmail.com", "zebediah001", "123456", "https://accounts.google.com/"));
        accounts.add(new GoogleAccount("dudley@gmail.com", "dudley001", "123456", "https://accounts.google.com/"));
        accounts.add(new GoogleAccount("lombard@gmail.com", "lombard001", "123456", "https://accounts.google.com/"));
        accounts.add(new GoogleAccount("eldon@gmail.com", "eldon001", "123456", "https://accounts.google.com/"));
        return accounts;
    }

    private static List<GithubAccount> getGithubAccounts() {
        List<GithubAccount> accounts = new ArrayList<>();
        accounts.add(new GithubAccount("joanna", "joanna001", "123456", "https://github.com/login"));
        accounts.add(new GithubAccount("vanessa", "vanessa001", "123456", "https://github.com/login"));
        accounts.add(new GithubAccount("ritamea", "ritamea001", "123456", "https://github.com/login"));
        accounts.add(new GithubAccount("lorelei", "lorelei001", "123456", "https://github.com/login"));
        accounts.add(new GithubAccount("nessia", "nessia001", "123456", "https://github.com/login"));
        accounts.add(new GithubAccount("georgia", "georgia001", "123456", "https://github.com/login"));
        return accounts;
    }

}
