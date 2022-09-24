package com.lt.network;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import static com.lt.bytebuffer.ByteBufferUtil.debugRead;

/**
 * @description: 使用 nio 来理解阻塞模式与非阻塞模式, 单线程
 * @author: ~Teng~
 * @date: 2022/9/24 12:19
 */
@Slf4j
public class Server {
    public static void main(String[] args) throws IOException {
        // 1. 创建服务器
        ServerSocketChannel ssc = ServerSocketChannel.open();
        // 非阻塞模式
        ssc.configureBlocking(false);
        ByteBuffer byteBuffer = ByteBuffer.allocate(16);
        // 2. 绑定监听端口
        ssc.bind(new InetSocketAddress(8080));
        List<SocketChannel> channels = new ArrayList<>();
        while (true) {
            // 3. accept 建立与客户端连接， SocketChannel 用来与客户端之间通信
            SocketChannel socketChannel = ssc.accept(); // 阻塞模式 线程停止运行 ，如果在非阻塞模式下 未接受连接 返回 null
            if (socketChannel != null) {
                log.debug("connected...{}", socketChannel);
                socketChannel.configureBlocking(false); // 非阻塞模式
                channels.add(socketChannel);
            }
            for (SocketChannel channel : channels) {
                // 4. 接收客户端发送的数据
                int read = channel.read(byteBuffer);// 阻塞模式 线程停止运行 如果在非阻塞模式下，如果没有接受到数据，read返回0
                if (read > 0) {
                    byteBuffer.flip();
                    debugRead(byteBuffer);
                    byteBuffer.clear();
                    log.debug("after read...{}", channel);
                }
            }
        }
    }
}
