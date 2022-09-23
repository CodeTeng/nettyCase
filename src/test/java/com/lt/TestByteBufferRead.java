package com.lt;

import java.nio.ByteBuffer;

import static com.lt.ByteBufferUtil.debugAll;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2022/9/23 20:51
 */
public class TestByteBufferRead {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put(new byte[]{'a', 'b', 'c', 'd'});
        buffer.flip();

        // rewind 从头开始读
        /*buffer.get(new byte[4]);
        debugAll(buffer);
        buffer.rewind();
        debugAll(buffer);
        System.out.println((char) buffer.get());*/    // a

        // mark & reset
        // mark 做一个标记 记录 position 的位置， reset 是将 position 重置到 mark 标记的位置
        /*System.out.println((char) buffer.get());
        System.out.println((char) buffer.get());
        buffer.mark();  // 索引为2
        System.out.println((char) buffer.get());
        System.out.println((char) buffer.get());
        debugAll(buffer);
        buffer.reset();
        debugAll(buffer);
        System.out.println((char) buffer.get());
        System.out.println((char) buffer.get());*/

        // get(i)  不会改变读索引 position
        System.out.println((char) buffer.get(3));
        debugAll(buffer);
    }
}
