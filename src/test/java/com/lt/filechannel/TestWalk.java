package com.lt.filechannel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @description: 拷贝多级目录
 * @author: ~Teng~
 * @date: 2022/9/24 0:51
 */
public class TestWalk {
    public static void main(String[] args) throws IOException {
        String source = "D:\\java_config\\Java\\jdk-17.0.4";
        String target = "D:\\java_config\\Java\\jdk-17.0.4-demo";
        long start = System.currentTimeMillis();
        Files.walk(Paths.get(source)).forEach(path -> {
            String targetName = path.toString().replace(source, target);
            try {
                // 是否是目录
                Path dir = Paths.get(targetName);
                if (Files.isDirectory(path)) {
                    Files.createDirectory(dir);
                } else if (Files.isRegularFile(path)) {
                    // 普通文件
                    Files.copy(path, dir);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }
}
