package com.lt.nio.channel.filechannel;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2022/9/24 21:12
 */
public class ScatteringReadsTest {
    public static void main(String[] args) {
        ByteBuffer byteBuffer1 = ByteBuffer.allocate(128);
        ByteBuffer byteBuffer2 = ByteBuffer.allocate(1024);
        ByteBuffer[] byteBuffers = {byteBuffer1, byteBuffer2};
        try (FileChannel channel = new RandomAccessFile("data.txt", "rw").getChannel()) {
            channel.read(byteBuffers);
        } catch (Exception e) {
            e.printStackTrace();
        }
        byteBuffer1.flip();
        byteBuffer2.flip();
        System.out.println(Charset.defaultCharset().decode(byteBuffer1));
        System.out.println("==========");
        System.out.println(Charset.defaultCharset().decode(byteBuffer2));
    }
}
