package com.lt.bytebuffer;

import java.nio.ByteBuffer;

import static com.lt.bytebuffer.ByteBufferUtil.debugAll;

/**
 * @description: 处理粘包与半包
 * @author: ~Teng~
 * @date: 2022/9/24 0:00
 */
public class TestBufferExam {
    public static void main(String[] args) {
        ByteBuffer source = ByteBuffer.allocate(32);
        source.put("Hello,world\nI'm zhangsan\nHo".getBytes());
        split(source);
        source.put("w are you?\nhaha!\n".getBytes());
        split(source);
    }

    private static void split(ByteBuffer byteBuffer) {
        // 切换为读模式
        byteBuffer.flip();
        for (int i = 0; i < byteBuffer.limit(); i++) {
            if (byteBuffer.get(i) == '\n') {
                // 用 ByteBuffer 进行存储
                int length = i + 1 - byteBuffer.position();
                ByteBuffer target = ByteBuffer.allocate(length);
                // 开始写入
                for (int j = 0; j < length; j++) {
                    target.put(byteBuffer.get());
                }
                debugAll(target);
            }
        }
        // 切换为写模式
        byteBuffer.compact();
    }
}
