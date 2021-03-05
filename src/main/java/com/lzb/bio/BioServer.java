package com.lzb.bio;

import com.lzb.utils.ResponseUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @Author : LZB
 * @Date : 2021/2/1
 * @Description :
 */
public class BioServer {

    private static final Integer PORT = 18080;
    private static final String IP = "127.0.0.1";

    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(IP, PORT));
        while (true) {
            //同步阻塞
            Socket socket = serverSocket.accept();
            new Thread(() -> {
                try {
                    byte[] bytes = new byte[1024];
                    int len = socket.getInputStream().read(bytes);
                    System.out.println("Server receive " + new String(bytes, 0, len, StandardCharsets.UTF_8));
                    String serverResponse = ResponseUtil.getHttpHeader200("hello bio server");
                    socket.getOutputStream().write(serverResponse.getBytes(StandardCharsets.UTF_8));
                    socket.getOutputStream().flush();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }


    public static void main(String[] args) throws Exception {
        BioServer server = new BioServer();
        server.start();

    }


}
