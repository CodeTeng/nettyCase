package com.lt.nio.channel.filechannel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2022/9/24 21:22
 */
public class GatheringWritesTest {
    public static void main(String[] args) {
        ByteBuffer byteBuffer1 = ByteBuffer.allocate(128);
        ByteBuffer byteBuffer2 = ByteBuffer.allocate(128);
        byteBuffer1.put("Hi".getBytes(StandardCharsets.UTF_8));
        byteBuffer2.put("Hello".getBytes(StandardCharsets.UTF_8));
        ByteBuffer[] byteBuffers = {byteBuffer1, byteBuffer2};
        try (FileChannel channel = new FileOutputStream("out.txt").getChannel()) {
            byteBuffer1.flip(); // 切换为读模式
            byteBuffer2.flip();
            channel.write(byteBuffers);
            System.out.println(channel.size()); // 7
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
