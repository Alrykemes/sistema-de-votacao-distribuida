package com.votingsystem.centralserver.interfaces;

import com.votingsystem.common.domain.User;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface CentralService extends Remote {
    Map<String, Map<String, Integer>> aggregateResults() throws RemoteException;
    User registerUser(User user) throws RemoteException;
    boolean loginUser(String username, String password) throws RemoteException;
}
