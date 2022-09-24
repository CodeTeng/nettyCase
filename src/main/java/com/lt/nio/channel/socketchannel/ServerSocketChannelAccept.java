package com.lt.nio.channel.socketchannel;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2022/9/24 21:34
 */
public class ServerSocketChannelAccept {
    public static void main(String[] args) throws Exception {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        ssc.bind(new InetSocketAddress(8080));
        ByteBuffer byteBuffer = ByteBuffer.wrap("Hello".getBytes(StandardCharsets.UTF_8));
        while (true) {
            SocketChannel sc = ssc.accept();
            if (sc == null) {
                System.out.println("null.....");
                Thread.sleep(2000);
            } else {
                System.out.println("Incoming connection from:" + sc.getRemoteAddress());
                sc.write(byteBuffer);
                sc.close();
            }
        }
    }
}
