package com.lt.nio.channel.socketchannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2022/9/24 21:48
 */
public class SocketChannelTest {
    public static void main(String[] args) throws IOException {
        SocketChannel sc = SocketChannel.open();
        sc.connect(new InetSocketAddress("localhost", 8080));
        sc.configureBlocking(false);    // 设置未非阻塞模式
        ByteBuffer byteBuffer = ByteBuffer.allocate(16);
        // SocketChannel 收到数据写入 Buffer中
        sc.read(byteBuffer);  // 当为阻塞模式时，read 执行时会导致线程阻塞
        sc.close();
        System.out.println("Over");
    }
}
