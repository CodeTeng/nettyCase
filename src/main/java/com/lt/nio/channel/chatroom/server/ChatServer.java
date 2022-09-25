package com.lt.nio.channel.chatroom.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * @description: 服务器端
 * @author: ~Teng~
 * @date: 2022/9/25 13:46
 */
public class ChatServer {

    /**
     * 启动服务器
     */
    public void startServer() throws IOException {
        // 1. 创建 Selector 选择器
        Selector selector = Selector.open();
        // 2. 创建 ServerSocketChannel 通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 3. 为 channel 通道绑定监听端口 设置非阻塞
        serverSocketChannel.bind(new InetSocketAddress(8000));
        serverSocketChannel.configureBlocking(false);
        // 4. 把 channel 通道注册选择器中
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("服务器已经启动成功了");
        // 5. 循环，等待是否有新连接接入
        while (true) {
            // 获取 channel 数量
            int readChannel = selector.select();
            if (readChannel == 0) {
                continue;
            }
            // 获取可用的 channel
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            // 遍历集合
            Iterator<SelectionKey> iter = selectionKeys.iterator();
            while (iter.hasNext()) {
                SelectionKey selectionKey = iter.next();
                // 移除 set 集合当前的 selectionKey
                iter.remove();
                // 6. 根据就绪状态。调用对应方法实现具体业务操作
                if (selectionKey.isAcceptable()) {
                    // 6.1 如果 accept 状态
                    acceptOperator(serverSocketChannel, selector);
                } else if (selectionKey.isReadable()) {
                    // 6.2 如果 readable 状态
                    readOperator(selector, selectionKey);
                }
            }
        }
    }

    /**
     * 处理可读状态操作
     *
     * @param selector     selector
     * @param selectionKey selectorKey
     */
    private void readOperator(Selector selector, SelectionKey selectionKey) throws IOException {
        // 1. 从 selectorKey 中获取已经就绪的 channel
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        // 2. 创建 buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        // 3. 循环方式读取客户端消息
        int readLength = socketChannel.read(byteBuffer);
        StringBuilder message = new StringBuilder();
        if (readLength > 0) {
            // 切换读模式
            byteBuffer.flip();
            // 读取内容
            message.append(StandardCharsets.UTF_8.decode(byteBuffer));
        }
        // 4. 将 channel 再次注册到选择器上，监听可读状态
        socketChannel.register(selector, SelectionKey.OP_READ);
        // 5. 把客户端发送消息，广播到其他客户端
        if (message.length() > 0) {
            // 广播给其他客户端
            System.out.println(message);
            castOtherClient(message, selector, socketChannel);
        }
    }

    /**
     * 广播给其他客户端
     *
     * @param message       message
     * @param selector      selector
     * @param socketChannel socketChannel
     */
    private void castOtherClient(StringBuilder message, Selector selector, SocketChannel socketChannel) throws IOException {
        // 1. 获取所有已经接入客户端
        Set<SelectionKey> selectionKeySet = selector.keys();
        // 2. 循环向所有 channel 广播消息
        for (SelectionKey selectionKey : selectionKeySet) {
            // 获取每个 channel
            Channel targetChannel = selectionKey.channel();
            // 不需要给自己发送
            if (targetChannel instanceof SocketChannel target && targetChannel != socketChannel) {
                target.write(StandardCharsets.UTF_8.encode(message.toString()));
            }
        }
    }

    /**
     * 处理接入状态操作
     *
     * @param serverSocketChannel ssc
     * @param selector            selector
     */
    private void acceptOperator(ServerSocketChannel serverSocketChannel, Selector selector) throws IOException {
        // 1. 接入状态，创建 socketChannel
        SocketChannel socketChannel = serverSocketChannel.accept();
        // 2. 设置非阻塞模式
        socketChannel.configureBlocking(false);
        // 3. 把 channel 注册到 selector 选择器上，并且监听可读状态
        socketChannel.register(selector, SelectionKey.OP_READ);
        // 4. 客户端回复信息
        socketChannel.write(StandardCharsets.UTF_8.encode("欢迎进入聊天室，请注意隐私安全"));
    }

    /**
     * 启动主方法
     *
     * @param args args
     */
    public static void main(String[] args) {
        try {
            new ChatServer().startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
