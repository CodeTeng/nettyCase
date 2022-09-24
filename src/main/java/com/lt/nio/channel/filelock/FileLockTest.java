package com.lt.nio.channel.filelock;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2022/9/25 0:23
 */
public class FileLockTest {

    @Test
    public void testFileLock() throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.wrap("Hello,World".getBytes(StandardCharsets.UTF_8));
        String fp = "D:\\MyCode\\JavaProjects\\netty\\data.txt";
        Path path = Paths.get(fp);
        FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.WRITE, StandardOpenOption.APPEND);
//        fileChannel.position(fileChannel.size() - 1);
        FileLock lock = fileChannel.tryLock(0, Long.MAX_VALUE, false);
        System.out.println("共享锁：" + lock.isShared());
        fileChannel.write(byteBuffer);
        fileChannel.close();
        System.out.println("写法操完成");
        // 读取数据
        readPrint(fp);
    }

    private void readPrint(String path) throws IOException {
        FileReader fileReader = new FileReader(path);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String str = bufferedReader.readLine();
        System.out.println("读取内容");
        while (str != null) {
            System.out.println("  " + str);
            str = bufferedReader.readLine();
        }
        fileReader.close();
        bufferedReader.close();
    }
}
