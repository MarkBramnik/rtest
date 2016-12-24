package org.rtest.testapp.webapp.spring.beans;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by Mark Bramnik on 23/09/2016.
 */
@Configuration
@EnableScheduling
public class AppConfig {
    @Bean
    public Calculator calculator() {
        return new CalculatorImpl();
    }

    @Bean
    public ScheduledExecutionTask scheduledExecutionTask() {
        return new ScheduledExecutionTaskImpl(calculator());
    }

}
