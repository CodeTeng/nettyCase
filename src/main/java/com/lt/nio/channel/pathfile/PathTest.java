package com.lt.nio.channel.pathfile;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2022/9/24 22:54
 */
public class PathTest {

    @Test
    public void test1() {
        // 1. 创建绝对路径
        Path path1 = Paths.get("D:\\java_config\\Java\\jdk-17.0.4");
        System.out.println(path1);  // D:\java_config\Java\jdk-17.0.4
        // 2. 创建相对路径
        Path path2 = Paths.get("D:\\", "java_config\\Java");
        System.out.println(path2);  // D:\java_config\Java

        // Path 接口的 normalize()方法可以使路径标准化。
        // 标准化意味着它将移除所有在路径字符串的中间的.和..代码，并解析路径字符串所引用的路径
        String originalPath = "D:\\java_config\\..\\jdk-17.0.4";
        Path path3 = Paths.get(originalPath);
        System.out.println(path3);  // D:\java_config\..\jdk-17.0.4
        Path path4 = path3.normalize();
        System.out.println(path4);  // D:\jdk-17.0.4
    }
}
