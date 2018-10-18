package com.data.a1_common;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class UnitTesterRunner {

    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(UnitTesterTest1.class);
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println("Test runer: " + result.wasSuccessful());
    }
}
