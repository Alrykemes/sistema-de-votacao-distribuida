package com.votingsystem.rmi.server;

import com.votingsystem.rmi.interfaces.VotingService;
import lombok.Getter;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VotingServiceImpl extends UnicastRemoteObject implements VotingService {
    private Map<String, Map<String, Integer>> pollVotes;
    private Map<String, Map<String, String>> userVotes;
    private final Map<String, List<String>> pollOptions;


    protected VotingServiceImpl() throws RemoteException {
        pollVotes = new HashMap<>();
        userVotes = new HashMap<>();
        pollOptions = new HashMap<>();
    }

    @Override
    public synchronized void vote(String userId, String pollId, String option) throws RemoteException {
        userVotes.putIfAbsent(userId, new HashMap<>());

        if (userVotes.get(userId).containsKey(pollId)) {
            System.out.println("User " + userId + " has already voted in poll " + pollId);
            return;
        }

        List<String> validOptions = pollOptions.get(pollId);
        if (validOptions == null || !validOptions.contains(option)) {
            throw new RemoteException("Opção inválida para a enquete: " + option);
        }

        // Register user vote
        userVotes.get(userId).put(pollId, option);

        // Register vote count
        pollVotes.putIfAbsent(pollId, new HashMap<>());
        Map<String, Integer> options = pollVotes.get(pollId);
        options.put(option, options.getOrDefault(option, 0) + 1);

        System.out.println("Vote registered: user " + userId + " -> " + pollId + " -> " + option);
    }

    @Override
    public Map<String, Integer> getVotes(String pollId) throws RemoteException {
        return pollVotes.getOrDefault(pollId, new HashMap<>());
    }

    @Override
    public List<String> getOptions(String pollId) throws RemoteException {
        return pollOptions.getOrDefault(pollId, new ArrayList<>());
    }

}