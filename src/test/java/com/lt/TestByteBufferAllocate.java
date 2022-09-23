package com.lt;

import java.nio.ByteBuffer;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2022/9/23 20:38
 */
public class TestByteBufferAllocate {
    public static void main(String[] args) {
        System.out.println(ByteBuffer.allocate(16).getClass());
        System.out.println(ByteBuffer.allocateDirect(16).getClass());
        /**
         * class java.nio.HeapByteBuffer    --- Java 堆内存 读写效率较低,收到 GC 的影响
         * class java.nio.DirectByteBuffer  --- 直接内存(系统内存) 读写效率高(少一次拷贝),不会受 GC 影响,但分配的效率低，可能会内存泄露
         */
    }
}
