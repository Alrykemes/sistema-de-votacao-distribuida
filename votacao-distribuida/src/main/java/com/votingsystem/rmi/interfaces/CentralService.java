package com.votingsystem.rmi.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface CentralService extends Remote {
    Map<String, Map<String, Integer>> aggregateResults() throws RemoteException;
}
