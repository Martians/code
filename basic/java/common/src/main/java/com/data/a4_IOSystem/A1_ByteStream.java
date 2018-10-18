package com.data.a4_IOSystem;

import java.io.*;
import java.net.URL;

/**
 * 相应的类，以及实现的接口
 *
 * 字节流：310 P、650 P
 * InputStream、OutputStream
 *          -- Flushable
 *          read(byte buf[])、read(int)
 *
 *      BufferInputStream、BufferOutputStream
 *      FileInputStream、FileOutputStream
 *      PrintStream - print、println、format;可以将数据写入文件
 *          -- Flushable、Appendable
 *      RandomAccessFile
 *          -- DataInput、DataOutput
 *
 * 字符流：671 P
 * Reader、Writer
 *          --Readable
 *          -- Appendable, Flushable
 *          read(char buf[])、read(CharBuffer buf)、read(int)
 *          write(char buf[])、append(charSequence chars)、write(String)
 *
 *      BufferReader、BufferWriter
 *      FileReader、FileWriter
 *      PrintWriter - print、println、format;可以将数据写入文件
 *
 *      InputStreamReader - 字节转换为字符的输入流
 */
public class A1_ByteStream {
    static final URL path = ClassLoader.getSystemResource("config.properties");

    /**
     * 预定义的 public、static、final的
     *
     * System.in  - InputStream
     * System.out - PrintStream - OutputStream
     *      PrintStream：print、println、format
     *                   可以将数据写入文件
     * System.err -
     */
    static void common() {
        System.out.println("\n common:");
        System.out.write('b');
        System.out.write('\n');

        /**
         * InputStream
         *      read(byte buffer[])
         *      reset
         *
         * OutputStream
         *      write
         *      flush
         */
    }

    /**
     * BufferedInputStream:
     *      设置缓冲区大小
     *      可以mark、reset
     * BufferedOutputStream：设置缓冲区大小
     *
     * SequeceInputStream
     *      可以连接多个Stream
     */
    static void bufferStream() {
    }

    /**
     * ByteArrayInputStream: 将字节数组变成了输入流
     * ByteArrayOutputStream：
     *      f.write();
     *      f.toByteArray();
     *      f.toString();
     */
    static void byteArrayStream() {
    }

    /**
     * 直接写入读取基本类型
     *  DataInputStream、DataOutputStream
     *      writeInt、WriteBoolean、ReadLine
     *
     *  实现了 DataInput、DataOutput 接口
     */
    static void dataStream() {
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 面向字符的流的输入,对于商业程序，读取控制台更好的方法，是使用面向字符的流
     *
     * BufferedReader：支持缓冲
     * InputStreamReader：将字节转换成字符
     *
     * System.in 是按照行缓冲的，因此必须输入回车才能起效；因此这种方法输入不是很有价值
     */
    static void inputStream() throws IOException {
        System.out.println("\n input char:");

        BufferedReader br = new BufferedReader(
                new InputStreamReader(System.in));
        System.out.println("input char");
        char c = (char)br.read();

        System.out.println("input string");
        String str = br.readLine();

        System.out.println("get char: " + c + ", get string: " + str);
    }

    /**
     * 基于字符流的输出，国际化
     */
    static void outputStream() throws IOException {
        System.out.println("\n output char:");

        /**
         * autoflush用于控制，每次写入\n、字符数组、调用println时，是否自动刷新缓冲区
         */
        PrintWriter pw = new PrintWriter(System.out, true);
        pw.println("do you work now");
    }

	/**
	 * 472, System.setErr
	 */
	public static void main(String args[]) throws IOException {

	    if (false) {
            common();

            inputStream();

            outputStream();
        }
	}
}
   