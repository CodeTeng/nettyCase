package com.lt;

import java.nio.ByteBuffer;

import static com.lt.ByteBufferUtil.debugAll;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2022/9/23 20:30
 */
public class TestByteBufferReadWrite {
    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        byteBuffer.put((byte) 0x61);    // 'a'
        debugAll(byteBuffer);
        byteBuffer.put(new byte[]{0x62, 0x63, 0x64});   // 写入 b c d
        debugAll(byteBuffer);
//        System.out.println(byteBuffer.get());
        // 切换读取模式
        byteBuffer.flip();
        System.out.println(byteBuffer.get());
        debugAll(byteBuffer);
        byteBuffer.compact();
        debugAll(byteBuffer);
        byteBuffer.put(new byte[]{0x65, 0x66});
        debugAll(byteBuffer);
    }
}
