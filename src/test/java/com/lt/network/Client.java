package com.lt.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2022/9/24 12:50
 */
public class Client {
    public static void main(String[] args) throws IOException {
        SocketChannel sc = SocketChannel.open();
        sc.connect(new InetSocketAddress("localhost", 8080));
        sc.write(Charset.defaultCharset().encode("0123456789abcdef3333\n"));
    }
}
