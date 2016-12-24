package org.rtest.testapp.webapp.spring.beans;

/**
 * Created by Mark Bramnik on 23/09/2016.
 */
public class CalculatorImpl implements Calculator {
    public CalculatorImpl() {
        System.out.println("Created Calculator bean");
    }

    public int add(int a, int b) {
        return a + b;
    }
}
