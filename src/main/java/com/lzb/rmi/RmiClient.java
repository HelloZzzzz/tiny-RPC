package com.lzb.rmi;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * @Author : LZB
 * @Date : 2021/2/7
 * @Description :
 */
public class RmiClient {
    public static void main(String[] args) throws NotBoundException, RemoteException {
        // 连接到服务器localhost，端口1099:
        Registry registry = LocateRegistry.getRegistry("localhost", 1099);
        // 查找名称为"HumanHelloWorld"的服务并强制转型为HelloWorld接口:
        HelloWorld helloWorld = (HelloWorld) registry.lookup("HumanHelloWorld");
        // 正常调用接口方法:
        System.out.println("helloWorld.sayHello() = " + helloWorld.sayHello());
    }
}
