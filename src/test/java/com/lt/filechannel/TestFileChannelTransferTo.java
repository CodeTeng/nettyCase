package com.lt.filechannel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.nio.file.*;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2022/9/24 0:20
 */
public class TestFileChannelTransferTo {
    public static void main(String[] args) {
        String FROM = "data.txt";
        String TO = "out.txt";
        long start = System.currentTimeMillis();
        try (FileChannel from = new FileInputStream(FROM).getChannel();
             FileChannel to = new FileOutputStream(TO).getChannel()) {
            // 效率高，底层会利用操作系统的零拷贝进行优化 传输上限大小为2G
            // 传输超过2G数据进行分批传输
            long size = from.size();
            for (long leave = size; leave > 0; ) {
                leave -= from.transferTo((size - leave), leave, to);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        long end = System.currentTimeMillis();
        System.out.println("用时：" + (end - start) + "ms");
    }
}
