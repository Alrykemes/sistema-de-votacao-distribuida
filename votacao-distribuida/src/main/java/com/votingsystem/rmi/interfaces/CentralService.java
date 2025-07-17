package com.votingsystem.rmi.interfaces;

import com.votingsystem.rmi.domain.user.User;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface CentralService extends Remote {
    Map<String, Map<String, Integer>> aggregateResults() throws RemoteException;
    User registerUser(User user) throws RemoteException;
    boolean loginUser(String username, String password) throws RemoteException;
    void createPoll(String title, List<String> options) throws RemoteException;
    Map<String, List<String>> listPolls() throws RemoteException;
}
