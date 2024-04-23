package com.manil.dailywise;

import com.manil.dailywise.config.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableConfigurationProperties({LoginProperty.class})
@EnableAsync
public class DailywiseApplication {

    public static void main(String[] args) {
        SpringApplication.run(DailywiseApplication.class, args);
    }

}
