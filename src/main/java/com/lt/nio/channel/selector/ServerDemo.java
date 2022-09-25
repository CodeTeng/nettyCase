package com.lt.nio.channel.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2022/9/25 13:14
 */
public class ServerDemo {
    public static void main(String[] args) {
        try {
            ServerSocketChannel ssc = ServerSocketChannel.open();
            ssc.bind(new InetSocketAddress(8000));
            ssc.configureBlocking(false);

            Selector selector = Selector.open();
            ssc.register(selector, SelectionKey.OP_ACCEPT);

            ByteBuffer readBuffer = ByteBuffer.allocate(1024);
            ByteBuffer writeBuffer = ByteBuffer.allocate(128);
            writeBuffer.put("received".getBytes(StandardCharsets.UTF_8));
            writeBuffer.flip();

            while (selector.select() > 0) {
                Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
                while (iter.hasNext()) {
                    // 获取就绪操作
                    SelectionKey key = iter.next();
                    iter.remove();

                    if (key.isAcceptable()) {
                        // 创建新的连接，并且把连接注册到 selector 上，而且
                        // 声明这个 channel 只对读操作感兴趣。
                        SocketChannel sc = ssc.accept();
                        sc.configureBlocking(false);
                        sc.register(selector, SelectionKey.OP_READ);
                    } else if (key.isReadable()) {
                        SocketChannel sc = (SocketChannel) key.channel();
                        // 读取数据
                        int length = 0;
                        while ((length = sc.read(readBuffer)) > 0) {
                            readBuffer.flip();
                            System.out.println("received:" + new String(readBuffer.array(), 0, length));
                            readBuffer.clear();
                        }
                        key.interestOps(SelectionKey.OP_WRITE);
                    } else if (key.isWritable()) {
                        writeBuffer.rewind();
                        SocketChannel sc = (SocketChannel) key.channel();
                        sc.write(writeBuffer);
                        key.interestOps(SelectionKey.OP_READ);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
