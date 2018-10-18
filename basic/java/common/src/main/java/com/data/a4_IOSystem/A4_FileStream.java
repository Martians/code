package com.data.a4_IOSystem;

import java.io.*;
import java.net.URL;

import static com.data.a0_util.format.Display.*;

/**
 * 647 P
 */
public class A4_FileStream {
    static final URL path = ClassLoader.getSystemResource("config.properties");
    /**
     * Path
     * URL
     * URI
     */
    static void common() {
        //Path
    }

    /**
     * 用于此处的调试目的, 显示文件内容
     */
    static void outputFile(String path) {
        try (FileReader  fin = new FileReader (path)) {
            BufferedReader bs = new BufferedReader(fin);
            String s;
            do {
                s = bs.readLine();
                if (s != null) out(s);
            } while(s != null && !s.equals(""));

        } catch(IOException e) {
            System.out.println("I/O Error: " + e);
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * File类只用来进行元数据操作
     *      https://blog.csdn.net/u011532367/article/details/50095345
     *      createTempFile
     */
    static void fileAttribute() {
        out("\nfileAttribute");
        File f = new File(path.getPath());

        out("file parent: %s, \npath: %s\n, name: %s\n, is dir: %b, last modify %d, length: %d",
                f.getParent(),
                f.getPath(),
                f.getName(),
                f.isDirectory(),
                f.lastModified(),
                f.length());

        out("partition info, total: %d, free: %d", f.getTotalSpace(), f.getFreeSpace());

        try {
            File n = new File(f.getParent() + "\\11");
            n.createNewFile();

            File s = new File(f.getParent() + "\\22");
            n.renameTo(s);

            s.delete();

        } catch (IOException e) {
            out(e);
        }
    }

    static void directory() {
        out("\nlist directory");
        File p = new File(path.getPath());
        File f = new File(p.getParent());

        if (f.isDirectory()) {
            String[] list = f.list();

            for (String s : list) {
                out("get file: " + s);
            }
        }

        /**
         * 可以使用 FilenameFilter
         */

        /**
         * 返回 File数组
         */
        if (f.isDirectory()) {
            File[] list = f.listFiles();

            for (File t : list) {
                out("get file: " + t);
            }
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 字节流方式访问
     */

    /**
     * FileInputStream 实现了 AutoCloseable接口，指定了close方法
     *
     * 实现了AutoCloseable接口的资源，可以使用 ARM：Automatic Resource Management
     */
    static void fileCopy() {
        System.out.println("\n file copy:");
        File f = new File("outfile.log");

        /**
         * 使用带资源的try语句，不用担心关闭资源时，再次出现的异常
         * 该异常会被抑制 324 P
         */
        try (FileInputStream fin = new FileInputStream(path.getPath());
             /**
              * file stream，可以传入f打开；
              *     用于先检查文件信息，然后进行读取
              */
             FileOutputStream fout = new FileOutputStream(f))
        {
            /**
             * 是否已提前判断所有流的大小？
             */
            out("remain: " + fin.available());

            int i;
            do {
                /**
                 * 这里虽然i是整形，但是实际只有低8位有效
                 */
                if ((i = fin.read()) != -1) {
                    fout.write(i);
                }
            } while (i != -1);

        } catch(IOException e) {
            System.out.println("I/O Error: " + e);
        }

        /**
         * 删除新文件
         */
        f.delete();
    }

    /**
     * append 追加方式，或者是覆盖方式
     */
    static void fileOutput() throws IOException {
        String path = "aa";
        File f = new File(path);

        /**
         * 方式1：FileOutputStream
         */
        try (FileOutputStream fout = new FileOutputStream(f, true)) {
            fout.write('1');

        } catch(IOException e) {
            System.out.println("I/O Error: " + e);
        }

        /**
         * 方式2：PrintStream
         *
         * 这种方式可以支持 print("%s", ) 格式化输出
         * 会自动覆盖已经存在的文件
         */
        PrintStream p = new PrintStream(f);
        p.printf("it is %d", 10000);
        outputFile(path);

        f.delete();
    }

    /**
     * 随机访问接口
     */
    static void randomAccess() {
        /**
         * access mode:
         *      s 表明所有元数据的修改，立即刷新到设备中
         *      d 表明所有数据的修改，立即刷新到设备中
         */
        try (RandomAccessFile f = new RandomAccessFile(path.getPath(), "rw")) {
            f.seek(50);
            out("seek to 50, get: " + f.readLine());

        } catch(IOException e) {
            System.out.println("I/O Error: " + e);
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 字符流方式访问
     */


    public static void main(String args[]) throws IOException {

        if (false) {
            fileAttribute();

            directory();
        }
        fileCopy();

        fileOutput();

         randomAccess();

    }
}
