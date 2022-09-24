package com.lt.nio.channel.pathfile;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.Future;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2022/9/24 23:03
 */
public class FileTest {

    @Test
    public void test1() throws IOException {
        Path path = Paths.get("D:\\MyCode\\JavaProjects\\netty\\data.txt");
        // 第二个参数是一个或多个打开选项，它告诉 AsynchronousFileChannel 在文件上执行什么操作
        AsynchronousFileChannel fileChannel
                = AsynchronousFileChannel.open(path, StandardOpenOption.READ);
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        // 通过 Future 进行读取数据
        Future<Integer> future = fileChannel.read(byteBuffer, 0);
        while (!future.isDone()) {
            System.out.println("reading....");
        }
        byteBuffer.flip();
        byte[] data = new byte[byteBuffer.limit()];
        byteBuffer.get(data);
        System.out.println(new String(data));
        byteBuffer.clear();
    }

    @Test
    public void test2() throws IOException {
        Path path = Paths.get("D:\\MyCode\\JavaProjects\\netty\\data.txt");
        // 第二个参数是一个或多个打开选项，它告诉 AsynchronousFileChannel 在文件上执行什么操作
        AsynchronousFileChannel fileChannel
                = AsynchronousFileChannel.open(path, StandardOpenOption.READ);
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        // 调用 read()方法，该方法将一个 CompletionHandler 作为参数
        fileChannel.read(byteBuffer, 0, byteBuffer, new CompletionHandler<Integer, ByteBuffer>() {
            // 读取操作完成，调用该方法
            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                System.out.println("result = " + result);
                attachment.flip();
                byte[] data = new byte[attachment.limit()];
                attachment.get(data);
                System.out.println(new String(data));
                attachment.clear();
            }

            // 读取失败 调用该方法
            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                System.out.println("读取失败！！！");
            }
        });
    }

    @Test
    public void test3() throws IOException {
        Path path = Paths.get("D:\\MyCode\\JavaProjects\\netty\\out.txt");
        AsynchronousFileChannel fileChannel
                = AsynchronousFileChannel.open(path, StandardOpenOption.WRITE);
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put("Hello,World".getBytes(StandardCharsets.UTF_8));
        byteBuffer.flip();
        // 通过 Future 进行写入
        Future<Integer> future = fileChannel.write(byteBuffer, 0);
        while (!future.isDone()) {
            System.out.println("writing...");
        }
        System.out.println("Write Over");
    }

    @Test
    public void test4() throws IOException {
        Path path = Paths.get("D:\\MyCode\\JavaProjects\\netty\\out.txt");
        if (!Files.exists(path)) {
            Files.createDirectory(path);
        }
        AsynchronousFileChannel fileChannel
                = AsynchronousFileChannel.open(path, StandardOpenOption.WRITE);
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put("Hello,World!!!".getBytes(StandardCharsets.UTF_8));
        byteBuffer.flip();
        // 通过 CompletionHandler 进行写入
        fileChannel.write(byteBuffer, 0, byteBuffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                System.out.println("bytes written:" + result);  // bytes written:14
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                System.out.println("Write failed");
            }
        });
    }
}
