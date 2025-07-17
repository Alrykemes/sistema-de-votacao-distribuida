package com.votingsystem.common.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface VotingService extends Remote {
    void vote(String pollId, String option) throws RemoteException;
    Map<String, Integer> getVotes(String pollId) throws RemoteException;
}
