package com.lt.nio.channel.chatroom.client;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2022/9/25 13:46
 */
public class ClientThread implements Runnable {

    private Selector selector;

    public ClientThread(Selector selector) {
        this.selector = selector;
    }

    @Override
    public void run() {
        try {
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
                    if (selectionKey.isReadable()) {
                        // 6.2 如果 readable 状态
                        readOperator(selector, selectionKey);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        if (message.length() > 0) {
            System.out.println(message);
        }
    }
}

