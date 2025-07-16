package com.votingsystem.rmi.central;

import com.votingsystem.rmi.interfaces.CentralService;
import com.votingsystem.rmi.interfaces.VotingService;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class CentralServiceImpl extends UnicastRemoteObject implements CentralService {
    private List<VotingService> votingServers;

    protected CentralServiceImpl(List<VotingService> servers) throws RemoteException {
        this.votingServers = servers;
    }

    @Override
    public Map<String, Map<String, Integer>> aggregateResults() throws RemoteException {
        Map<String, Map<String, Integer>> aggregated = new HashMap<>();
        for (VotingService server : votingServers) {

            Map<String, Integer> results = server.getVotes("poll1");
            if (results != null) {
                aggregated.put("poll1", results);
            }
            // Repetir para outras enquetes / servidores
        }
        return aggregated;
    }
}
