package com.lt.network;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;

import static com.lt.bytebuffer.ByteBufferUtil.debugAll;
import static com.lt.bytebuffer.ByteBufferUtil.debugRead;

/**
 * @description: Selector模式下的服务器
 * @author: ~Teng~
 * @date: 2022/9/24 13:29
 */
@Slf4j
public class SelectorServer {
    public static void main(String[] args) throws IOException {
        // 1. 创建服务器
        ServerSocketChannel ssc = ServerSocketChannel.open();
        // 设置非阻塞模式
        ssc.configureBlocking(false);
        // 2. 创建 Selector 管理多个 channel
        Selector selector = Selector.open();
        // 3. 建立 selector 与 channel 的联系（注册）
        // SelectionKey 就是将来事件发生后，通过它可以知道事件和哪了Channel的事件有联系
        SelectionKey sscKey = ssc.register(selector, 0, null);
        // key 只关注 accept 事件
        sscKey.interestOps(SelectionKey.OP_ACCEPT);
        log.debug("register key:{}", sscKey);
        ssc.bind(new InetSocketAddress(8080));
        while (true) {
            // 4. select 方法 没有事件发生，线程阻塞，有事件发生，线程才恢复运行
            // select 在事件未处理时，不会阻塞，事件发生后，要么处理，要么取消，不能置之不理
            selector.select();
            // 5. 处理事件 selectedKeys 内部包含了所有发生的事件
            Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                iter.remove();  // 必须将事件进行移除
                log.debug("key:{}", key);
                // 6. 区分事件类型
                if (key.isAcceptable()) {
                    ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                    SocketChannel sc = channel.accept();
                    sc.configureBlocking(false);
                    ByteBuffer byteBuffer = ByteBuffer.allocate(16);    // attachment
                    // 将 ByteBuffer 作为附件关联到 SelectionKey 上
                    SelectionKey scKey = sc.register(selector, 0, byteBuffer);
                    scKey.interestOps(SelectionKey.OP_READ);
                    log.debug("{}", sc);
                } else if (key.isReadable()) {
                    try {
                        SocketChannel channel = (SocketChannel) key.channel();  // 拿到触发事件的 channel
                        ByteBuffer byteBuffer = (ByteBuffer) key.attachment();   // 获取 SelectionKey 上关联的附件
                        int read = channel.read(byteBuffer);    // 如果时正常断开 read 方法的返回值是-1
                        if (read == -1) {
                            key.cancel();
                        } else {
                            split(byteBuffer);
                            if (byteBuffer.position() == byteBuffer.limit()) {
                                // 扩容
                                ByteBuffer newBuffer = ByteBuffer.allocate(byteBuffer.capacity() * 2);
                                byteBuffer.flip();
                                newBuffer.put(byteBuffer);
                                key.attach(newBuffer);
                            }
                        }
                    } catch (IOException e) {
                        key.cancel();   // 客户端断开了，因此需要将key取消（从selector 的 keys 集合中真正删除）
                    }
                }
                // key.cancel();
            }
        }
    }

    private static void split(ByteBuffer byteBuffer) {
        byteBuffer.flip();
        for (int i = 0; i < byteBuffer.limit(); i++) {
            if (byteBuffer.get(i) == '\n') {
                int length = i + 1 - byteBuffer.position();
                ByteBuffer target = ByteBuffer.allocate(length);
                for (int j = 0; j < length; j++) {
                    target.put(byteBuffer.get());
                }
                debugAll(target);
            }
        }
        byteBuffer.compact();
    }
}
