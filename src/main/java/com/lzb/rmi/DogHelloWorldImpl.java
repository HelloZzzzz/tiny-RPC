package com.lzb.rmi;

import java.rmi.RemoteException;

/**
 * @Author : LZB
 * @Date : 2021/2/7
 * @Description :
 */
public class DogHelloWorldImpl implements HelloWorld {
    @Override
    public String sayHello() throws RemoteException {
        return "wang wang wang  this is dog rmi";
    }
}
