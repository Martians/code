package com.data.logging;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.util.StatusPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * logback 自动包含了 slf4j
 *
 * 官网：https://logback.qos.ch/manual/groovy.html
 * 中文：http://justcode.ikeepstudying.com/wp-content/uploads/2017/04/logback-%E4%B8%AD%E6%96%87%E6%89%8B%E5%86%8C.pdf
 * 		http://www.importnew.com/22290.html
 * 		https://www.cnblogs.com/warking/p/5710303.html
 *
 * logback-test.xml、logback.xml
 *
 * MDCFIlter、SiftingAppender
 *
 * 0. 配置方式
 * 		自动定期读取配置，动态更新选项
 * 		修改配置文件位置，通过修改系统属性，java -Dlogback.configurationFile
 * 		配置文件可以访问系统同变量（java -D），用于生成动态文件名？
 * 		可以指定一个property/file属性，将所有的变量放在该文件中
 * 		如果输出很多内部信息，就是配置存在错误
 * 		可以通过JMX进行配置， 159P
 *
 * 1. 继承和分离：appender、level等
 * 		日志属性的继承：
 * 		日志属性的分离：可用此来实现，将不同的内容写入到不同的日志文案中去
 *
 * 2. 多个日志文件
 * 		每次启动生成一个日志文件
 *		按照时间、大小触发生成新的文件
 *
 * 4. 多种appender：发送邮件、写入数据库、syslog 中文手册-84P
 *
 * 5. Logback Access
 * 		包含很多appender，与Logback-classic类似，但是不尽相同；
 * 		主要用于web访问时更方便 90P
 *
 * 6. Layout
 * 		可以输出异常信息
 * 		其他技巧：日志级别只输出一个字符：%.-1level
 * 		可以对整个前缀部分进行编组，输出指定一个固定大小；109P
 * 		可以计算表达式，在特定情况下执行特定请求
 * 		HTML格式的Layout 118P
 *
 * 7. 运行时
 *
 *
 *
 *
 */
public class Logback {
	final static Logger logger1 = LoggerFactory.getLogger(Logback.class);

	static void common() {
		Logger root = LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);

		/**
		 * 手动配置 https://logback.qos.ch/manual/configuration.html#joranDirectly
		 * 		example: https://www.programcreek.com/java-api-examples/?api=ch.qos.logback.classic.joran.JoranConfigurator
		 *	Joran是一个通用配置狂阿基，168P
		 */

//		JoranConfigurator configurator = new JoranConfigurator();
//		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
//		configurator.setContext(lc);
	}

	static void startup() {
		Logger startup = LoggerFactory.getLogger("startup");
		startup.info("new log");
	}

	/**
	 * 似乎未生效
	 */
	static void time_roll() {
		Logger time = LoggerFactory.getLogger("time_roll");
		for(int i = 0; i < 1000; i++) {
			time.trace("1111");
			time.debug("debug level1");
			time.info("info level1");
			time.warn("warn level1");
			time.error("error level1");
		}
		try {
			Thread.sleep(61000);
		} catch (Exception e) {

		}
		for(int i = 0; i < 1000; i++) {
			time.trace("11111");
			time.debug("1debug level1");
			time.info("1info level1");
			time.warn("1warn level1");
			time.error("1error level1");
		}
	}

	static void index_roll() {
		Logger index = LoggerFactory.getLogger("index_roll");
		for(int i = 0; i < 1000; i++) {
			index.trace("1111");
			index.debug("debug level");
			index.info("info level");
			index.warn("warn level");
			index.error("error level");
		}
	}

	static void size_time_roll() {
		Logger roll = LoggerFactory.getLogger("size_time_roll");
		for(int i = 0; i < 500; i++) {
			roll.trace("111");
			roll.debug("debug level");
			roll.info("info level");
			roll.warn("warn level");
			roll.error("error level");
		}


	}
	static void filter() {
		final Logger file = LoggerFactory.getLogger("file");
		file.trace("111");
		file.debug("111");
		file.info("111");
		file.warn("111");
	}

	static void async() {
		Logger async = LoggerFactory.getLogger("async");
		async.debug("111111111111");
	}

	/**
	 * MDC为“Mapped Diagnostic Context”（映射诊断上下文) 139 P
	 *		目的是，不需要为了每个client建立不同的日志，增加了管理开销；通常用于网站中，多个client的访问记录
	 *		相当于一个线程本地变量，设置了很多的值，可以用于在日志中显示、使用
	 */
	static void sift() {
		Logger sift = LoggerFactory.getLogger("sift");
		sift.debug("111111111111");

		MDC.put("a", "1");
		MDC.put("last", "good");

		MDC.put("userid", "u1");
		sift.debug("111111111111");

		MDC.put("userid", "u2");
		sift.debug("111111111111");

		MDC.clear();
	}


	static void output() {
		/**
		 * 这个跟在配置文件中，设置 debug=true是一致的
		 */
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		StatusPrinter.print(lc);
	}

    public static void main(String[] args) {

		if (false) {
			common();

			startup();

			time_roll();
			index_roll();
			size_time_roll();

			filter();

			async();

			sift();

			output();
		}
    }
}
