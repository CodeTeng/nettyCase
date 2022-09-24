package com.lt.nio.channel.filechannel;


import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @description: 从 FileChannel 读取数据
 * @author: ~Teng~
 * @date: 2022/9/24 20:53
 */
public class ReadTest {
    public static void main(String[] args) {
        try (FileChannel channel = new RandomAccessFile("data.txt", "rw").getChannel()) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024 * 1024);
            channel.read(byteBuffer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
