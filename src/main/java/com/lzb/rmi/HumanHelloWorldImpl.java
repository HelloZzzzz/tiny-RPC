package com.lzb.rmi;

/**
 * @Author : LZB
 * @Date : 2021/2/7
 * @Description :
 */
public class HumanHelloWorldImpl /*extends UnicastRemoteObject*/ implements HelloWorld  {
    //使用  HelloWorld remote = (HelloWorld) UnicastRemoteObject.exportObject(helloWorld, 9999);
    // 就可不extends UnicastRemoteObject
    /*protected HumanHelloWorldImpl() throws RemoteException {
    }*/

    @Override
    public String sayHello() {
        return "hello this is human rmi";
    }
}
