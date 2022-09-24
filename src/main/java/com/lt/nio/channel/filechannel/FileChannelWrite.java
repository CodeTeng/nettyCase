package com.lt.nio.channel.filechannel;

import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2022/9/24 21:05
 */
public class FileChannelWrite {
    public static void main(String[] args) {
        try (FileChannel from = new RandomAccessFile("data.txt", "rw").getChannel();
             FileChannel to = new RandomAccessFile("out.txt", "rw").getChannel()) {
            from.transferTo(0, from.size(), to);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
