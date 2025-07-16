package com.votingsystem.rmi.server;

import com.votingsystem.rmi.interfaces.VotingService;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public class VotingServiceImpl extends UnicastRemoteObject implements VotingService {
    private Map<String, Map<String, Integer>> polls;

    protected VotingServiceImpl() throws RemoteException {
        polls = new HashMap<>();
    }

    @Override
    public synchronized void vote(String pollId, String option) throws RemoteException {
        polls.putIfAbsent(pollId, new HashMap<>());
        Map<String, Integer> options = polls.get(pollId);
        options.put(option, options.getOrDefault(option, 0) + 1);
        System.out.println("Voto registrado: " + pollId + " -> " + option);
    }

    @Override
    public Map<String, Integer> getVotes(String pollId) throws RemoteException {
        return polls.getOrDefault(pollId, new HashMap<>());
    }
}