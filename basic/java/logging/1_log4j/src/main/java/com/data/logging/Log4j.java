package com.data.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * 详解log4j2：https://blog.csdn.net/autfish/article/details/51203709
 * 官网：http://logging.apache.org/log4j/2.0/manual/configuration.html#Appenders
 *
 * PatternLayout
 * 		http://logging.apache.org/log4j/2.x/manual/layouts.html
 * 		https://blog.csdn.net/zhu19774279/article/details/41577415
 *
 * Appender：目的地
 * 		类型：
 * 		    ConsoleAppender、FileAppender、DailyRollingFileAppender、SyslogAppender
 * 		    JDBCAppender
 *      布局：Layout
 * 		    DateLayout、HTMLLayout、XMLLayout
 *
 * Logger：对外出口
 *      由Logger来定义日志级别
 *      组织多个appender在一起
 *
 *
 * Todo:
 * 	1. 发送邮件
 * 	2. 输出到html
 * 	3. syslog
 * 	4. mongodb
 */
public class Log4j {

	/**
	 * 获得 root logger
	 */
	private static Logger root = LogManager.getRootLogger();

    /**
     * 使用类名作为 logger 的名字
     *
     * 如果没有单独配置，将自动使用 root logger
     */
    private static Logger local = LogManager.getLogger(Log4j.class);

    /**
     * 将logger与appender进行绑定，单独配置logger的日志级别等
     */
	private static Logger test = LogManager.getLogger("test");

    /**
     * 将logger与appender进行绑定，单独配置logger的日志级别等
     */
    private static Logger test_up = LogManager.getLogger("test_up");


	/**
	 * 为了测试目的，定义了很多logger，实际上并不需要
	 */
	private static Logger console = LogManager.getLogger("console");
	private static Logger file = LogManager.getLogger("file");
	private static Logger roll = LogManager.getLogger("roll");
	private static Logger filter = LogManager.getLogger("filter");
	private static Logger async = LogManager.getLogger("async");

    static void config() {
        try {
            File file = new File(Log4j.class.getResource("/none_log4j2_config.xml").getPath());
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
            final ConfigurationSource source = new ConfigurationSource(in);
            Configurator.initialize(null, source);

        } catch (Exception e) {
            System.out.println("open config: " + e);
        }

        Logger logger = LogManager.getLogger("mylog");
        logger.info("new configure");
    }


	static void common() {
        root.info("root log");

        test.debug("1111");

        /**
         * 除了自己输出，还会输出到 error_log
         */
        test_up.warn("test up output");

        local.info("local log");
    }

	static void roll() {
		roll.info("roll log");

		for(int i = 0; i < 500; i++) {
			roll.trace("trace level");
			roll.debug("debug level");
			roll.info("info level");
			roll.warn("warn level");
			roll.error("error level");
			roll.fatal("fatal level");
		}
	}

	static void filter() {
		filter.info("appear in common and error file");

		filter.warn("appear in error file");
		filter.error("appear in error file");

		filter.fatal("appear in common roll and error file");
	}

	static void async() {
		async.debug("async message");
	}


	public static void main(String[] args) {

		if (false) {
            config();

			common();

			roll();

			filter();

			async();
		}

		roll();
	}
}
