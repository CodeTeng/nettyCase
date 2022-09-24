package com.lt.nio.channel.pipe;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;
import java.nio.charset.StandardCharsets;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2022/9/25 0:09
 */
public class TestPipe {

    @Test
    public void testPipe() throws IOException {
        // 1. 获取通道
        Pipe pipe = Pipe.open();
        // 2. 获取 Sink 通道，用来传输数据
        Pipe.SinkChannel sinkChannel = pipe.sink();
        // 3. 向 sink 通道写入数据
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put("Hello,World".getBytes(StandardCharsets.UTF_8));
        byteBuffer.flip();
        sinkChannel.write(byteBuffer);
        // 4. 获取 source 通道，用来接受 sink 通道的数据
        Pipe.SourceChannel sourceChannel = pipe.source();
        // 5. 接受数据 并保存到缓冲区
        ByteBuffer byteBuffer2 = ByteBuffer.allocate(1024);
        int length = sourceChannel.read(byteBuffer2);
        System.out.println(new String(byteBuffer2.array(), 0, length));

        sourceChannel.close();
        sinkChannel.close();
    }
}
