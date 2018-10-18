package com.data.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MarkerFactory;

import java.lang.invoke.MethodHandles;

public class SLF4J {
	/** use static */
	final static Logger logger1 = LoggerFactory.getLogger(SLF4J.class);
	
	/** use instance, no static */
	//https://www.slf4j.org/faq.html   - Should Logger members of a class be declared as static?
	final Logger logger2 = LoggerFactory.getLogger(SLF4J.class);

	/** user object to get class name */
	final Logger logger3 = LoggerFactory.getLogger(this.getClass());

	/** auto load current class name */
	final static Logger logger4 = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	static void common() {
		String data = "<< work >> ";
		Logger logger = LoggerFactory.getLogger(SLF4J.class);

		logger.info("it is why {}", data);
		logger.info(MarkerFactory.getMarker("FATAL"), "use logback marker: {}", data);

		/**
		 * log exception
		 */
		logger.error("Failed to format {}", data, new Exception("that is a exception"));

		logger.debug("working");
	}

	/**
	 * https://www.slf4j.org/extensions.html
	 */
	static void profiler() {

	}
	
    public static void main(String[] args) {
		common();

		profiler();
    }
}
