package com.data.a1_common;

import com.data.a6_test.UnitTester;
import org.junit.*;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class UnitTesterTest4 {

    String message = "Hello World-1";
    UnitTester tester = new UnitTester(message);

    @Test
    public void testgetMessage1() {
    //    assertThat(tester.number, anyOf(equalTo(10), equalTo(11)));

        assertThat(tester.getMessage(), is( "Hello World-1" ) );
        assertThat(tester.getMessage(), not( "developerWorks" ) );

        assertThat(tester.getMessage(), containsString( "World"));
        assertThat(tester.getMessage(), startsWith( "Hello" ) );

        //assertThat( testedValue, equalTo( expectedValue ) );
        //assertThat( mapObject, hasEntry( "key", "value" ) );
        //assertThat( mapObject, hasKey ( "key" ) );
    }
}