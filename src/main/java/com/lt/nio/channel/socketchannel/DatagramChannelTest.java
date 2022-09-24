package com.lt.nio.channel.socketchannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.StandardCharsets;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2022/9/24 22:05
 */
public class DatagramChannelTest {
    public static void main(String[] args) throws IOException {
        DatagramChannel server = DatagramChannel.open();
        server.socket().bind(new InetSocketAddress(8898));  // 绑定端口
        System.out.println("waiting...");
        ByteBuffer buffer = ByteBuffer.allocate(32);

        // SocketAddress 可以获得发包的 ip、端口等信息
        SocketAddress socketAddress = server.receive(buffer);// 接收数据

        ByteBuffer sendBuffer = ByteBuffer.wrap("Hello".getBytes(StandardCharsets.UTF_8));
        server.send(sendBuffer, new InetSocketAddress("localhost", 8898));  // 发送数据

        // UDP 不存在真正意义上的连接，这里的连接是向特定服务地址用 read 和 write 接收发送数据包
        // read()和 write()只有在 connect()后才能使用，不然会抛NotYetConnectedException 异常
        server.connect(new InetSocketAddress("localhost", 8080));
        server.write(sendBuffer);
    }
}
