package com.cqrs;

import com.cqrs.command.saga.AccountSagaManager;
import com.cqrs.common.configuration.AxonConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * Created by geonyeong.kim on 2020-01-15
 */
@SpringBootApplication(
        exclude = {
                DataSourceAutoConfiguration.class
        }
)
@Import({AxonConfiguration.class, AccountSagaManager.class})
public class QueryApplication {

    public static void main(String[] args) {
        SpringApplication.run(QueryApplication.class, args);
    }
}
