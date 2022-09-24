package com.lt.network;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import static com.lt.bytebuffer.ByteBufferUtil.debugAll;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2022/9/24 17:28
 */
@Slf4j
public class MultiThreadServer {
    public static void main(String[] args) throws IOException {
        Thread.currentThread().setName("boss");
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        Selector boss = Selector.open();
        SelectionKey bossKey = ssc.register(boss, 0, null);
        bossKey.interestOps(SelectionKey.OP_ACCEPT);
        ssc.bind(new InetSocketAddress(8080));
        // 1. 创建固定数量的 worker 并初始化
        Worker[] workers = new Worker[Runtime.getRuntime().availableProcessors()];
        for (int i = 0; i < workers.length; i++) {
            workers[i] = new Worker("worker-" + i);
        }
        AtomicInteger index = new AtomicInteger();
        while (true) {
            boss.select();
            Iterator<SelectionKey> iter = boss.selectedKeys().iterator();
            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                iter.remove();
                if (key.isAcceptable()) {
                    SocketChannel sc = ssc.accept();
                    sc.configureBlocking(false);
                    log.debug("connected:{}", sc.getRemoteAddress());
                    // 2. 关联 selector
                    log.debug(" before register...{}", sc.getRemoteAddress());
                    // round robin 轮询
                    workers[index.getAndIncrement() % workers.length].register(sc);  // boss 调用 初始化 selector 启动 worker
                    log.debug(" after register...{}", sc.getRemoteAddress());
                }
            }
        }
    }

    static class Worker implements Runnable {
        private Thread thread;

        private Selector selector;

        private String name;

        private volatile boolean start = false;  // 还未初始化

        private ConcurrentLinkedQueue<Runnable> queue = new ConcurrentLinkedQueue<>();

        public Worker(String name) {
            this.name = name;
        }

        /**
         * 初始化线程和 selector
         */
        public void register(SocketChannel sc) throws IOException {
            if (!start) {
                thread = new Thread(this, name);
                thread.start();
                selector = Selector.open();
                start = true;
            }
            // 向队列添加了任务 但这个任务并没有立即执行 boss
            queue.add(() -> {
                try {
                    sc.register(selector, SelectionKey.OP_READ, null); // boss
                } catch (ClosedChannelException e) {
                    throw new RuntimeException(e);
                }
            });
            selector.wakeup();  // 唤醒 select() 方法
        }

        @Override
        public void run() {
            while (true) {
                try {
                    selector.select(); // worker-0 阻塞 wakeup
                    Runnable task = queue.poll();
                    if (task != null) {
                        task.run(); // 执行了 sc.register(selector, SelectionKey.OP_READ, null);
                    }
                    Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
                    while (iter.hasNext()) {
                        SelectionKey key = iter.next();
                        iter.remove();
                        if (key.isReadable()) {
                            ByteBuffer buffer = ByteBuffer.allocate(16);
                            SocketChannel channel = (SocketChannel) key.channel();
                            log.debug("red...{}", channel.getRemoteAddress());
                            channel.read(buffer);
                            buffer.flip();
                            debugAll(buffer);
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
