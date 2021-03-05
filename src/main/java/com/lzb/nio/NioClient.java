package com.lzb.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @Author : LZB
 * @Date : 2021/2/4
 * @Description :
 */
public class NioClient {
    private static final String EXIT = "q!";
    private static final String IP = "127.0.0.1";
    private static final Integer PORT = 8889;
    ByteBuffer buffer = ByteBuffer.allocate(1024);

    public void start() {
        InetSocketAddress address = new InetSocketAddress(IP, PORT);
        try (SocketChannel socketChannel = SocketChannel.open()) {
            socketChannel.connect(address);
            String toServe = "hello Nio";
            buffer.put(toServe.getBytes(StandardCharsets.UTF_8));
            buffer.flip();

            socketChannel.write(buffer);

            buffer.clear();

            //读取服务器返回的数据
            int readLen = socketChannel.read(buffer);
            if (readLen == -1) {
                return;
            }
            //重置buffer游标
            buffer.flip();
            byte[] bytes = new byte[buffer.remaining()];
            //读取数据到字节数组
            buffer.get(bytes);
            System.out.println("receive from server : " + new String(bytes, StandardCharsets.UTF_8));
            buffer.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        NioClient nioClient = new NioClient();
        nioClient.start();
    }
}
