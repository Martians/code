package com.data.a1_common;

import com.data.a6_test.UnitTester;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;


@RunWith(Parameterized.class)
public class UnitTesterTest3 {

    private Integer inputNumber;
    private Boolean expectedResult;
    private UnitTester instance;

    String message = "Hello World-3";

    //将参数化测试内容，记录在构造函数中
    public UnitTesterTest3(Integer inputNumber, Boolean expectedResult) {
        this.inputNumber = inputNumber;
        this.expectedResult = expectedResult;
    }

    @Before
    public void initialize() {
        // 每次生成一个新的实例，也可以直接在checkTest中new一个对象出来
        instance = new UnitTester(message);
    }

    @Parameterized.Parameters
    public static Collection primeNumbers() {
        return Arrays.asList(new Object[][] {
            { 2, true },
            { 6, true },
            { 19, false },
            { 22, true },
            { 23, false }
        });
    }

    // 针对每个参数，调用一次测试用例，UnitTesterTest3只生成一次
    @Test
    public void checkTest() {
        System.out.println("Parameterized Number is : " + inputNumber);
        assertEquals(expectedResult, instance.check(inputNumber));
    }
}