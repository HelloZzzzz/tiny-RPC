package com.lzb.rmi;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * @Author : LZB
 * @Date : 2021/2/7
 * @Description :
 * Java提供了RMI实现远程方法调用：
 *
 * RMI通过自动生成stub和skeleton实现网络调用，客户端只需要查找服务并获得接口实例，服务器端只需要编写实现类并注册为服务；
 *
 * RMI的序列化和反序列化可能会造成安全漏洞，因此调用双方必须是内网互相信任的机器，不要把1099端口暴露在公网上作为对外服务。
 */
public class RmiHumanServer {
    public static void main(String[] args) throws RemoteException {
        HelloWorld helloWorld = new HumanHelloWorldImpl();
        // 将此服务转换为远程服务接口:
        HelloWorld remote = (HelloWorld) UnicastRemoteObject.exportObject(helloWorld, 9999);
        // 将RMI服务注册到1099端口:
        Registry registry = LocateRegistry.createRegistry(1099);
        // 注册此服务，服务名为"HumanHelloWorld":
        registry.rebind("HumanHelloWorld", remote);

        System.out.println("bind over ....");




    }

}




