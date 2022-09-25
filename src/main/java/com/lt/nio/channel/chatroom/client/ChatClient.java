package com.lt.nio.channel.chatroom.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @description: 客户端
 * @author: ~Teng~
 * @date: 2022/9/25 13:46
 */
public class ChatClient {

    /**
     * 启动客户端方法
     */
    public void startClient(String name) throws IOException {
        // 1. 连接服务器端
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost", 8000));
        // 接受服务器端响应数据
        Selector selector = Selector.open();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        // 创建线程
        new Thread(new ClientThread(selector)).start();
        // 2. 向服务器端发送消息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String msg = scanner.nextLine();
            if (msg.length() > 0) {
                socketChannel.write(StandardCharsets.UTF_8.encode(name + ":" + msg));
            }
        }
    }
}
