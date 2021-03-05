package com.lzb.bio;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @Author : LZB
 * @Date : 2021/2/1
 * @Description : BIO模型
 */
public class BioClient {

    private static final Integer PORT = 18080;
    private static final String IP = "127.0.0.1";
    private byte[] bytes = new byte[1024];

    public void start() throws IOException {
        Socket socket = new Socket(IP, PORT);
        socket.getOutputStream().write("hello BIO".getBytes(StandardCharsets.UTF_8));
        socket.getOutputStream().flush();
        System.out.println("Client send hello BIO");

        int len = socket.getInputStream().read(bytes);
        System.out.println("Client receive " + new String(bytes, 0, len, StandardCharsets.UTF_8));
        socket.close();
    }


    public static void main(String[] args) throws IOException {
        BioClient client = new BioClient();
        client.start();
    }
}