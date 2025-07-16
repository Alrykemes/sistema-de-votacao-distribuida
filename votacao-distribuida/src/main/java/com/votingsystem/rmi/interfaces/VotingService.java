package com.votingsystem.rmi.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface VotingService extends Remote {
    void vote(String pollId, String optionId) throws RemoteException;
    Map<String, Integer> getVotes(String pollId) throws RemoteException;
}
