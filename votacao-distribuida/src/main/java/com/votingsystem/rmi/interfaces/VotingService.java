package com.votingsystem.rmi.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface VotingService extends Remote {
    void vote(String username, String pollId, String option) throws RemoteException;
    Map<String, Integer> getVotes(String pollId) throws RemoteException;
}
