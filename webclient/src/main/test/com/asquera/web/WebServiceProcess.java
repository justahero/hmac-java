package com.asquera.web;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses( { ClientTest.class } )
public class WebServiceProcess {

    @BeforeClass
    public static void setUp() {
    }
    
    @AfterClass
    public static void tearDown() {
    }

}
