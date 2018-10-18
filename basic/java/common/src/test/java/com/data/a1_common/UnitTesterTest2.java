package com.data.a1_common;

import com.data.a6_test.UnitTester;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

// 继承该类会有问题，Ignore和 expected无法通过
//public class UnitTesterTest2 extends TestCase {

public class UnitTesterTest2 {
    String message = "Hello World-2";
    UnitTester tester = new UnitTester(message);

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testPrintMessage1() {
        assertEquals(message, tester.printMessage());
        assertFalse(1 > 6);
        assertNotNull(message);
    }

    @Test(timeout=1000)
    public void testPrintMessage2() {
        System.out.println("with time check");
        assertEquals(message, message);
    }

    @Test(expected = ArithmeticException.class)
    public void testPrintMessage3() {
        System.out.println("throw exception");
        tester.willThrowException();
    }

    @Test
    @Ignore("ignore: not implement")
    public void testPrintMessag4() {
        System.out.println("will never run");
        fail();
    }
}