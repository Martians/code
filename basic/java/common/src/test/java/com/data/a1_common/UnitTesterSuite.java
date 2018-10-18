package com.data.a1_common;

/*
public class UnitTesterSuite {
    public static void main(String[] a) {
        // add the test's in the suite
        TestSuite suite = new TestSuite(UnitTesterTest1.class, UnitTesterTest2.class);
        TestResult result = new TestResult();
        suite.run(result);
        System.out.println("Number of test cases = " + result.runCount());
    }
}
*/

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
@RunWith(Suite.class)
@Suite.SuiteClasses({
        UnitTesterTest1.class,
        UnitTesterTest2.class,
        UnitTesterTest3.class,
        UnitTesterTest4.class
})

public class UnitTesterSuite {
}