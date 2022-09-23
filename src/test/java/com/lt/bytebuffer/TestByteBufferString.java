package com.lt.bytebuffer;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static com.lt.bytebuffer.ByteBufferUtil.debugAll;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2022/9/23 21:02
 */
public class TestByteBufferString {
    public static void main(String[] args) {
        // 1. 字符串转为 ByteBuffer
        ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.put("hello".getBytes(StandardCharsets.UTF_8));
        debugAll(buffer);

        // 2. Charset
        ByteBuffer buffer2 = StandardCharsets.UTF_8.encode("hello");    // 自动切换为读模式
        debugAll(buffer2);

        // 3. wrap
        ByteBuffer buffer3 = ByteBuffer.wrap("hello".getBytes(StandardCharsets.UTF_8)); // 自动切换为读模式
        debugAll(buffer3);

        // ByteBuffer 转为字符串
        // 要 ByteBuffer 为读模式才可以
        buffer.flip();
        String str1 = StandardCharsets.UTF_8.decode(buffer).toString();
        System.out.println(str1);
        String str2 = StandardCharsets.UTF_8.decode(buffer2).toString();
        System.out.println(str2);
    }
}
