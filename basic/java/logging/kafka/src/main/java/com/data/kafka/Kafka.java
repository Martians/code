package com.data.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Kafka {

	//https://logging.apache.org/log4j/2.0/manual/appenders.html#KafkaAppender
	final static Logger logger = LoggerFactory.getLogger("kafka");
	
	public static void main(String[] args) {
		
		logger.info("a message form log4j");
		logger.debug("a message form log4j, not send to kafka");
		
		System.out.println("Hello World!");
	}
}
