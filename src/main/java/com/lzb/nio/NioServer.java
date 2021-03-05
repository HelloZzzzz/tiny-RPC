package com.lzb.nio;

import com.lzb.utils.ResponseUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @Author : LZB
 * @Date : 2021/2/4
 * @Description :
 */
public class NioServer extends Thread {

    private Selector selector;

    private ByteBuffer readBuffer = ByteBuffer.allocate(1024);
    private ByteBuffer writeBuffer = ByteBuffer.allocate(1024);

    private static final Integer PORT = 8889;

    public NioServer() {
        init();
    }

    private void init() {

        try {
            System.out.println("starting ...... listening port：" + PORT);
            //1)开启多路复用器
            this.selector = Selector.open();
            //2) 开启服务通道
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            //3)设置为非阻塞
            serverSocketChannel.configureBlocking(false);
            //4)绑定端口
            serverSocketChannel.bind(new InetSocketAddress(PORT));
            /*
             * SelectionKey.OP_ACCEPT   —— 接收连接继续事件，表示服务器监听到了客户连接，服务器可以接收这个连接了
             * SelectionKey.OP_CONNECT  —— 连接就绪事件，表示客户与服务器的连接已经建立成功
             * SelectionKey.OP_READ     —— 读就绪事件，表示通道中已经有了可读的数据，可以执行读操作了（通道目前有数据，可以进行读操作了）
             * SelectionKey.OP_WRITE    —— 写就绪事件，表示已经可以向通道写数据了（通道目前可以用于写操作）
             */
            //5)注册,标记服务连接状态为ACCEPT状态
            serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);
            System.out.println("init over");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                //1、 当有至少一个通道被选中,执行此方法
                int select = this.selector.select();
                //2、 获取选中的通道编号集合
                Iterator<SelectionKey> keys = this.selector.selectedKeys().iterator();
                //3、 遍历keys
                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    //4、 当前key需要从动刀集合中移出,如果不移出,下次循环会执行对应的逻辑,造成业务错乱
                    keys.remove();
                    //5、 判断通道是否有效
                    if (key.isValid()) {
                        try {
                            //6、 判断是否可以连接
                            if (key.isAcceptable()) {
                                accept(key);
                            }
                        } catch (CancelledKeyException e) {
                            //出现异常断开连接
                            key.cancel();
                        }

                        try {
                            //7、判断是否可读
                            if (key.isReadable()) {
                                read(key);
                            }
                        } catch (CancelledKeyException e) {
                            key.cancel();
                        }

                        try {
                            //8、判断是否可写
                            if (key.isWritable()) {
                                write(key);
                            }
                        } catch (CancelledKeyException e) {
                            key.cancel();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void accept(SelectionKey key) {
        try {
            //1、 当前通道在init方法中注册到了selector中的ServerSocketChannel
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
            //2、 阻塞方法, 客户端发起后请求返回.
            SocketChannel channel = serverSocketChannel.accept();
            ///3、 serverSocketChannel设置为非阻塞
            channel.configureBlocking(false);
            //4、 设置对应客户端的通道标记,设置次通道为可读时使用
            channel.register(this.selector, SelectionKey.OP_READ);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void read(SelectionKey key) {
        try {
            this.readBuffer.clear();
            SocketChannel channel = (SocketChannel) key.channel();
            //将通道的数据(客户发送的data)读到缓存中.
            int readLen = channel.read(readBuffer);
            if (readLen == -1) {
                key.channel().close();
                key.cancel();
                return;
            }
            //重置游标
            this.readBuffer.flip();
            byte[] bytes = new byte[readBuffer.remaining()];
            readBuffer.get(bytes);
            System.out.println("receive from  " + channel.getRemoteAddress() + " :  " + new String(bytes, StandardCharsets.UTF_8));
            //注册通道,标记为写操作
            channel.register(this.selector, SelectionKey.OP_WRITE);

        } catch (Exception ignored) {

        }
    }


    private void write(SelectionKey key) {
        this.writeBuffer.clear();

        SocketChannel channel = (SocketChannel) key.channel();

        try {
            System.out.println("send to client ..");

            writeBuffer.put(ResponseUtil.getHttpHeader200("hello this is nio server").getBytes(StandardCharsets.UTF_8));

            writeBuffer.flip();
            channel.write(writeBuffer);
            channel.register(this.selector, SelectionKey.OP_READ);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        new Thread(new NioServer()).start();
    }
}
