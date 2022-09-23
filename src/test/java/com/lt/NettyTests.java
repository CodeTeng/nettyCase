package com.lt;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2022/9/23 19:58
 */
@Slf4j
public class NettyTests {
    public static void main(String[] args) {
        // 获取FileChannel
        // 1. 输入输出流 2. RandomAccessFile
        try (FileChannel channel = new FileInputStream("data.txt").getChannel()) {
            // 准备缓冲区
            ByteBuffer byteBuffer = ByteBuffer.allocate(10);
            while (true) {
                // 从channel 读取数据，向byteBuffer写入
                int len = channel.read(byteBuffer);
                log.debug("读取到的字节数：{}", len);
                if (len == -1) {
                    // 没有内容了
                    break;
                }
                // 打印byteBuffer的内容
                byteBuffer.flip();  // 切换至读模式
                while (byteBuffer.hasRemaining()) {
                    byte b = byteBuffer.get();
                    log.debug("读取到的字节：{}", (char) b);
                }
                // 切换为写模式
                byteBuffer.clear();
            }

        } catch (Exception e) {
        }
    }
}
