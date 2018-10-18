package com.app;

import org.apache.commons.cli.*;

/**
 * doc: http://commons.apache.org/proper/commons-cli/
 * api: http://commons.apache.org/proper/commons-cli/javadocs/api-release/index.html
 *
 * Todo：OptionGroup
 */
public class Command {

    static void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        /**
         * autoUsage: 打印 命令完整语法，包括可选、必选等
         */
        formatter.printHelp( "client", options);
    }

    public static void main(String[] args) {
        String test = "-c 1000 -outstanding 50 --worker=90 --building ss -Dskip=now -D ss=pp";
        Options options = new Options();

        /**
         * 简单设置
         *      option的名字会显示在前边
         *      longopt需要用 -- 前缀
         */
        options .addOption("h", "help", false, "help message")
                .addOption("c", true, "count")
                .addOption("outstanding", true, "outstanding message")
                .addOption("w", "worker", true, "worker count");

        options.addOption(Option.builder("buildfile")
                .desc("building file").longOpt("building")
                /**
                 * 设置hasArg才有参数
                 * argName在显示帮助信息时有用
                 */
                .hasArg().argName("file")
                /**
                 * 长命令就是使用--的命令
                 */
                .required().build());

        /**
         * java 风格的参数 -DKey=Value
         */
        options.addOption(Option.builder("D")
                .desc("use value for given property")
                .numberOfArgs(2).valueSeparator()
                .argName("property=value")
                .build());

        options.addOption(Option.builder()
                /**
                 * 只有长参数中，名称中才能使用 -
                 */
                .longOpt("block-size")
                .desc("use SIZE-byte blocks")
                .hasArg().argName("SIZE")
                .build());

        CommandLineParser parser = new DefaultParser();
        CommandLine command = null;

        try {
            command = parser.parse(options, test.split(" "));

        } catch (ParseException e) {
            //e.printStackTrace();
            printHelp(options);
            return;
        }

        if (command.hasOption("h")) {
            System.out.println("help message");
            printHelp(options);
            return;
        }

        if (command.hasOption('c')) {
            System.out.printf("count: %s, properties: %s, worker: %s \n",
                    command.getOptionValue('c'),
                    /**
                     * 返回得到 map
                     */
                    command.getOptionProperties("D"),
                    command.getOptionValue("worker"));
        }
        printHelp(options);
    }
}
