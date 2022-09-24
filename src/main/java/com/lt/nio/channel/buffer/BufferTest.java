package com.lt.nio.channel.buffer;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2022/9/24 22:35
 */
public class BufferTest {

    @Test
    public void testSlice() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        // 缓冲区的数据为 0-9
        for (int i = 0; i < byteBuffer.capacity(); i++) {
            byteBuffer.put((byte) i);
        }
        // 创建子缓冲区
        byteBuffer.position(3);
        byteBuffer.limit(7);
        ByteBuffer slice = byteBuffer.slice();

        // 改变子缓冲区的大小
        for (int i = 0; i < slice.capacity(); i++) {
            byte b = slice.get(i);
            b *= 10;
            slice.put(i, b);
        }

        byteBuffer.position(0);
        byteBuffer.limit(byteBuffer.capacity());
        while (byteBuffer.hasRemaining()) {
            System.out.println(byteBuffer.get());
        }
    }
}
