package com.lt.nio.channel.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2022/9/25 13:21
 */
public class ClientDemo {
    public static void main(String[] args) {
        try {
            SocketChannel sc = SocketChannel.open();
            sc.connect(new InetSocketAddress("localhost", 8000));
            ByteBuffer writeBuffer = ByteBuffer.allocate(32);
            ByteBuffer readBuffer = ByteBuffer.allocate(32);
            writeBuffer.put("hello".getBytes(StandardCharsets.UTF_8));
            writeBuffer.flip();

            while (true) {
                writeBuffer.rewind();
                sc.write(writeBuffer);
                readBuffer.clear();
                sc.read(readBuffer);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
