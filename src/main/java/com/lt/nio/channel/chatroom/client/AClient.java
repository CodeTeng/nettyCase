package com.lt.nio.channel.chatroom.client;

import java.io.IOException;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2022/9/25 13:46
 */
public class AClient {
    public static void main(String[] args) {
        try {
            new ChatClient().startClient("AClient");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
