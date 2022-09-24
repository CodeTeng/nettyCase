package com.lt.nio.channel.socketchannel;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2022/9/24 22:13
 */
public class UDPTest {

    @Test
    public void sendDatagram() throws Exception {
        DatagramChannel sendChannel = DatagramChannel.open();
        while (true) {
            sendChannel.send(ByteBuffer.wrap("发包".getBytes(StandardCharsets.UTF_8))
                    , new InetSocketAddress("localhost", 9999));
            System.out.println("发包端发包");
            Thread.sleep(1000);
        }
    }

    @Test
    public void receive() throws Exception {
        DatagramChannel receiveChannel = DatagramChannel.open();
        receiveChannel.bind(new InetSocketAddress(9999));
        ByteBuffer byteBuffer = ByteBuffer.allocate(128);
        while (true) {
            SocketAddress socketAddress = receiveChannel.receive(byteBuffer);
            byteBuffer.flip();
            System.out.println(socketAddress + " ");
            System.out.println(Charset.defaultCharset().decode(byteBuffer));
        }
    }
}
