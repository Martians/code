package com.data.a0_util.time;


import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

/**
 * 静态导入，可以不再使用类限定名，来使用类的静态成员，可以直接使用
 */
import static java.lang.System.*;
/**
 * api：https://docs.oracle.com/javase/8/docs/api/index.html
 * http://blog.csdn.net/sf_cyl/article/details/51987088
 */

public class SimpleTime {

    /////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 系统时间
     */
    static void systemTime() throws InterruptedException {
        long start = System.currentTimeMillis();

        Thread.sleep(200);

        long end = System.currentTimeMillis( );
        out.printf("elapse: %d", end - start);
    }

    /**
     * 前提是，系统有能够提供纳秒级精度的计数器
     */
    static void nanoTime() throws InterruptedException {
        long start = System.nanoTime();

        Thread.sleep(200);

        long end = System.nanoTime( );
        out.printf("elapse nano: %d", end - start);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Java8中的新接口
     *
     * LocalDate
     */
    static void localTime() {
        out.println("\nlocalTime:");

        /** 指定日期 */
        LocalDate date = LocalDate.of(2015, 6, 5);
        out.printf("month length: %d, name: %s, prev: %s, next: %s\n",
                date.lengthOfMonth(),
                date.getMonth().name(),
                date.minusMonths(1),
                date.plusMonths(1));

        /**
         * 日志转换
         *
         * 直接进行的日期转换：只支持这一种格式，2014/02/28无效
         * 使用DateTimeFormatter
         */
        LocalDate date1 = LocalDate.parse("2014-02-28");
        out.println("date: " + date1);

        /**
         * 当前日期
         */
        LocalDate now = LocalDate.now();
        out.println("time: " + now);

        /**
         * 方便逻辑
         */
        LocalDate firstDayOfThisMonth = date.with(TemporalAdjusters.firstDayOfMonth());

        /**
         * LocalDate
         * LocalDateTime
         */
        out.println("date: " + LocalDate.now());
        out.println("datetime: " + LocalDateTime.now());

    }

    /////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 日期类型：
     * Date类基本已经 Deprecated
     *
     */
    static void dateTime() {
        out.println("\ndateTime:");
        out.printf("new date: %s", new Date());

        Date date = new Date();
        /** Datetime */
        Timestamp timestamp = new Timestamp(date.getTime()); //2013-01-14 22:45:36.484
    }

    /**
     * https://www.cnblogs.com/blackheartinsunshine/p/6019408.html
     */
    static void calenderTime() {
    }


    /**
     * 日期格式化
     *
     * 1. DateFormat是时间转换接口，DateTimeF.getTimeInstance等，可以获得time、date转换实例
     * 2. SimpleDateFormat 是DateFormat的一个实例，需要每次new一个出来
     */
    static void stringtoDate() throws ParseException {
        out.println("\nstringtoDate:");

        /** 使用基类DateFormat */
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse("2013-01-14");
        out.println(date.getTime());

        /** 使用子类SimpleDateFormat */
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = format1.parse("2013-01-14 11:12:15");
        out.println(date1.getTime());
    }

    /**
     * Date -> 字符串
     */
    static void dateToString() throws ParseException {
        out.println("\ndateToString:");
        Date date = new Date();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        out.println(format.format(date));

        /** 添加其他字符串 'at' */
        SimpleDateFormat format1 = new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
        out.println(format1.format(date));
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////


    public static void main(String[] args) throws Exception {
        if (false) {
            systemTime();
            nanoTime();

            dateTime();
            localTime();

            stringtoDate();
            dateToString();
        }
        localTime();
    }

}
