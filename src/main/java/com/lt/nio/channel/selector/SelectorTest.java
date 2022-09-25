package com.lt.nio.channel.selector;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2022/9/25 12:18
 */
public class SelectorTest {

    @Test
    public void test1() throws IOException {
        // 1. 获取 selector
        Selector selector = Selector.open();
        // 2. 获取通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 3. 设置非阻塞
        serverSocketChannel.configureBlocking(false);
        // 4. 绑定连接
        serverSocketChannel.bind(new InetSocketAddress(9999));
        // 5. 进行注册 将通道注册到选择器上,并制定监听事件为：“接收”事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
        while (iter.hasNext()) {
            SelectionKey key = iter.next();
            if (key.isReadable()) {
                // readable
            } else if (key.isAcceptable()) {
                // acceptable
            } else if (key.isWritable()) {
                // writable
            } else if (key.isConnectable()) {
                // connectable
            }
            iter.remove();
        }
    }
}
