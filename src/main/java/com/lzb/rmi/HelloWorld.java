package com.lzb.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @Author : LZB
 * @Date : 2021/2/7
 * @Description :
 */
public interface HelloWorld extends Remote {

    String sayHello() throws RemoteException;


}
