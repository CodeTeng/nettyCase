package com.lt.nio.channel.filechannel;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2022/9/24 20:57
 */
public class WriteTest {
    public static void main(String[] args) throws Exception {
        try (FileChannel channel = new RandomAccessFile("data.txt", "rw").getChannel()) {

            ByteBuffer byteBuffer = ByteBuffer.allocate(48);
            String newData = "New String to write to file..." + System.currentTimeMillis();
            byteBuffer.put(newData.getBytes(StandardCharsets.UTF_8));
            byteBuffer.flip();
            while (byteBuffer.hasRemaining()) {
                channel.write(byteBuffer);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
