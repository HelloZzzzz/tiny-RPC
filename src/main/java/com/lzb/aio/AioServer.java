package com.lzb.aio;


import com.lzb.utils.ResponseUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @Author : LZB
 * @Date : 2021/2/9
 * @Description : JDK 1.7 AIO
 */
public class AioServer {
    private static final Integer PORT = 8888;
    private static final String IP = "127.0.0.1";
    private static final Integer TIMEOUT = 5;
    private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;

    private ByteBuffer byteBuffer = null;
    private AsynchronousServerSocketChannel asynchronousServerSocketChannel = null;

    public AioServer() {
        try {
            //1、 打开一个ServerSocket通道并获取AsynchronousServerSocketChannel实例：
            //2、 绑定需要监听的端口到serverSocketChannel:
            asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(IP, PORT));
        } catch (IOException e) {
            e.printStackTrace();
        }
        byteBuffer = ByteBuffer.allocate(1024);
    }

    public void start() {
        System.out.println("开始监听端口：" + PORT);
        //3、 实现一个CompletionHandler回调接口handler，
        //4、 之后需要在handler的实现中处理连接请求和监听下一个连接、数据收发，以及通信异常。
        CompletionHandler<AsynchronousSocketChannel, Object> handler = new CompletionHandler<AsynchronousSocketChannel, Object>() {

            @Override
            public void completed(AsynchronousSocketChannel result, Object attachment) {
                try {
                    System.out.println("Thread.currentThread().getName() = " + Thread.currentThread().getName());
                    //4.1、 继续监听下一个连接请求
                    asynchronousServerSocketChannel.accept(attachment, this);
                    byteBuffer.clear();
                    //使用 ByteBuffer.wrap 效率更高
                    String toClient = "welcome , this is server";
                    byteBuffer.put(ResponseUtil.getHttpHeader200(toClient).getBytes(StandardCharsets.UTF_8));
                    byteBuffer.flip();
                    //4.2、 发送消息
                    result.write(byteBuffer).get(TIMEOUT, TIME_UNIT);
                    byteBuffer.clear();
                    //4.3、 接收消息
                    Integer receivedCount = result.read(byteBuffer).get(TIMEOUT, TIME_UNIT);
                    System.out.println("收到客户端字节数：" + receivedCount);
                    System.out.println("收到客户端信息：" + new String(byteBuffer.array(), 0, receivedCount, StandardCharsets.UTF_8));
                } catch (InterruptedException | ExecutionException | TimeoutException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void failed(Throwable exc, Object attachment) {
                System.out.println("Throwable：" + exc.getMessage());
            }
        };
        asynchronousServerSocketChannel.accept(null, handler);

    }

    public static void main(String[] args) throws InterruptedException {
        AioServer server = new AioServer();
        server.start();
        //由于serverSocketChannel.accept(null, handler);是一个异步方法，调用会直接返回，所以这里让线程一直阻塞
        Thread.currentThread().join();
    }


}
