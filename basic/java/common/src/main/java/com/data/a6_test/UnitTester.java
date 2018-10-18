package com.data.a6_test;


public class UnitTester {
    private String message;
    protected int number = 10;

    public UnitTester(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String printMessage() {
        System.out.println(message);
        return message;
    }

    public void willThrowException() {
        System.out.println(message);
        int a = 0;
        int b = 1/a;
    }

    public boolean check(Integer inputNumber) {
        return inputNumber % 2 == 0;
    }
}
