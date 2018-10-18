package com.data.a4_IOSystem;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

import static com.data.a0_util.format.Display.*;

/**
 *
 * 692 P
 * NIO 面向缓冲区、基于通道的IO（打开到IO设 备的连接）
 *      通道必须与缓冲区关联
 *
 * 接口：
 *      Path
 *
*  类：
 *      File s：
 */
public class A6_NIO {
    static final URL origin = ClassLoader.getSystemResource("config.properties");
    /**
     * 打开channel需要先获取Path，Path是一个接口，继承自
     *     Watchable、Iterable<Path>
     *     不能直接实例化，必须通过静态函数 get(String)、或者 get(URI)
     *
     *  https://stackoverflow.com/questions/9834776/java-nio-file-path-issue
     *     String osAppropriatePath = System.getProperty( "os.name" ).contains( "indow" ) ? filePath.substring(1) : filePath;
     */
    static final Path path = Paths.get(origin.getPath().substring(1));

    /**
     * Files 类
     *      readAttributes
     *      newByteChannel
     *      newInputStream
     *      newDirectoryStream
     *      createFile、createDirectory、copy
     *      isDirectory、isReadable
     */
    static void common() throws IOException {

        out("pareent: " + path.getParent());
        out(Files.exists(path) + ", " + Files.isHidden(path));

        BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class);
        out(attributes.isDirectory() + ", " + attributes.lastAccessTime());

        /**
         * 简单遍历一层目录
         */
        try (DirectoryStream<Path> stream
                     = Files.newDirectoryStream(path.getParent()))
        {
            out(path.getParent() + " is directory: " + Files.isDirectory(path.getParent()));

            for (Path entry : stream) {
                out(entry.getFileName());
            }
        } catch(IOException e) {
            System.out.println("I/O Error: " + e);
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 递归遍历目录
     *
     *      MyFileVisitor 只用来对获得的每一项单独处理，还可以重载
     *          preVisitFile、postVisitFile
     *
     * 更进一步，WatchService可以监控目录的变化
     */
    static class MyFileVisitor extends SimpleFileVisitor<Path> {
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            out("file: " + file);
            /**
             * SKIP_SUBTREE、SKIP_SIBLINGS、Terminate
             */
            return FileVisitResult.CONTINUE;
            //return super.visitFile(file, attrs);
        }
    }
    static void listDirectory() {
        try {
            Files.walkFileTree(path.getParent(), new MyFileVisitor());

        } catch(IOException e) {
            System.out.println("I/O Error: " + e);
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 基于通道的IO 使用NIO
     */

    /**
     * 获得通道的方式
     *      Files.newByetChannel
     *
     *      FileInputStream.getChannel
     *
     * 接口：
     *      SeekableByteChannel
     *
     * 实现：
     *      FileChannel
     *
     */
    static void channelRead() throws IOException, URISyntaxException {
        /**
         * 打开文件，并建立连接到文件的通道
         *
         * SeekableByteChannel 是接口，实际类型是 Filechannel
         */
        try (SeekableByteChannel channel =
                     Files.newByteChannel(path))
        {
            ByteBuffer buf = ByteBuffer.allocate(256);
            int count = 0;

            do {
                count = channel.read(buf);
                if (count != -1) {
                    /**
                     * 调整缓冲区的位置，以便可读
                     */
                    buf.rewind();
                    //System.out.print(buf.asCharBuffer());
                    for (int i = 0; i < count; i++) {
                        System.out.print((char) buf.get());
                    }
                }
            } while (count != -1);

        } catch(IOException e) {
            System.out.println("I/O Error: " + e);
        }
    }

    static void channelMap() {
        /**
         * 这里直接转换为 FileChannel
         */
        try (FileChannel channel = (FileChannel)Files.newByteChannel(path,
                    StandardOpenOption.WRITE, StandardOpenOption.READ, StandardOpenOption.CREATE))
        {
            long size = channel.size();
            /**
             * 直接将文件映射到缓冲区
             */
            MappedByteBuffer buf = channel.map(FileChannel.MapMode.READ_WRITE, 0, size);

            for (int i = 0; i < size; i++) {
                System.out.print((char) buf.get());
            }

        } catch(IOException e) {
            System.out.println("I/O Error: " + e);
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 基于流的IO 使用NIO
     */
    static void streamRead() {

        try (InputStream fin = new BufferedInputStream(
                     Files.newInputStream(path)))
        {
            int i;
            do {
                i = fin.read();
                /**
                 * 这里必须转换为 char，否则输出是整数
                 */
                if (i != -1) System.out.print((char)i);
            } while(i != -1);

        } catch(IOException e) {
            System.out.println("I/O Error: " + e);
        }
    }
    //////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 基于路径、文件系统操作 使用NIO
     */
    public static void main(String args[]) throws IOException, URISyntaxException {
        if (false) {
            channelRead();

            channelMap();

            streamRead();
        }
        common();

        listDirectory();
    }
}
