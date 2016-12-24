package org.rtest.testapp.webapp.spring.beans;

import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;
import java.util.Random;

/**
 * Created by Mark Bramnik on 23/09/2016.
 */
public class ScheduledExecutionTaskImpl implements ScheduledExecutionTask {
    private Calculator calculator = null;
    private Random random = new Random();
    public ScheduledExecutionTaskImpl(Calculator calculator) {
        this.calculator = calculator;
    }

    @Scheduled(fixedRate = 10000)
    public void execute() {
        int sampleA = random.nextInt(100);
        int sampleB = random.nextInt(100);
        int result = calculator.add(sampleA , sampleB);
        System.out.println(" Date : " + new Date() + "  Done calculation: " + sampleA  + " + " + sampleB + " = " + result );

    }
}
