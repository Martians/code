package com.data.a1_common;

import com.data.a6_test.UnitTester;
import org.junit.*;

import static org.junit.Assert.*;

public class UnitTesterTest1 {

    String message = "Hello World-1";
    UnitTester tester = new UnitTester(message);

    //execute before class
    @BeforeClass
    public static void beforeClass() {
        System.out.println("-- before class");
    }

    //execute after class
    @AfterClass
    public static void  afterClass() {
        System.out.println("-- after class");
    }

    //execute before test
    @Before
    public void before() {
        System.out.println("\nbefore test");
    }

    //execute after test
    @After
    public void after() {
        System.out.println("after test");
    }

    @Test
    public void testPrintMessage1() {
        assertEquals(message, tester.printMessage());
        assertFalse(1 > 6);
        assertNotNull(message);
    }

    @Test
    public void testPrintMessage2() {
        assertEquals(message, tester.printMessage());
    }
}