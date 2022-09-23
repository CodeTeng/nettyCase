package com.lt.filechannel;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description: 遍历目录文件
 * @author: ~Teng~
 * @date: 2022/9/24 0:38
 */
public class TestWalkFileTree {
    public static void main(String[] args) throws IOException {
        Path path = Paths.get("D:\\java_config\\Java\\jdk-17.0.4");
        AtomicInteger dirCount = new AtomicInteger();
        AtomicInteger fileCount = new AtomicInteger();
        AtomicInteger jarCount = new AtomicInteger();
        Files.walkFileTree(path, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                dirCount.getAndIncrement();
                return super.preVisitDirectory(dir, attrs);
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                System.out.println(file);
                if (file.toFile().getName().endsWith(".jar")) {
                    jarCount.getAndIncrement();
                }
                fileCount.getAndIncrement();
                // 删除多级目录
                // Files.delete(path);
                return super.visitFile(file, attrs);
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                // 删除多级目录
                // Files.delete(dir);
                return super.postVisitDirectory(dir, exc);
            }
        });
        System.out.println("文件夹个数：" + dirCount);
        System.out.println("文件个数：" + fileCount);
        System.out.println("jar包个数：" + jarCount);
    }
}
