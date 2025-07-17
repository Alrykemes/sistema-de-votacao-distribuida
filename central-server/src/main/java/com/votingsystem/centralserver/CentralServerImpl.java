package com.votingsystem.centralserver;

import com.votingsystem.common.domain.User;
import com.votingsystem.common.exception.UserAlreadyExistsException;
import com.votingsystem.centralserver.interfaces.CentralService;
import com.votingsystem.common.interfaces.VotingService;
import com.votingsystem.centralserver.repository.UserRepository;
import com.votingsystem.centralserver.security.PasswordEncoder;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class CentralServerImpl extends UnicastRemoteObject implements CentralService {
    private final List<VotingService> votingServers;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public CentralServerImpl(List<VotingService> servers, UserRepository userRepository, PasswordEncoder passwordEncoder) throws RemoteException {
        this.votingServers = servers;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Map<String, Map<String, Integer>> aggregateResults() throws RemoteException {
        Map<String, Map<String, Integer>> aggregated = new HashMap<>();

        for (VotingService server : votingServers) {
            Map<String, Integer> results = server.getVotes("poll1");

            if (results != null) {
                aggregated.merge("poll1", results, (oldMap, newMap) -> {
                    newMap.forEach((option, count) -> oldMap.merge(option, count, Integer::sum));
                    return oldMap;
                });
            }
        }

        return aggregated;
    }

    @Override
    public User registerUser(User user) throws RemoteException, UserAlreadyExistsException {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new UserAlreadyExistsException(user.getUsername());
        }

        String hashedPassword = passwordEncoder.hashPassword(user.getPassword());
        User userToSave = new User(user.getId(), user.getUsername(), hashedPassword);

        try {
            userRepository.save(userToSave);
        } catch (Exception e) {
            throw new RemoteException("Erro ao salvar usuário", e);
        }

        return userToSave;
    }

    @Override
    public boolean loginUser(String username, String password) throws RemoteException {
        User user = userRepository.findByUsername(username);
        return user != null && passwordEncoder.verifyPassword(password, user.getPassword());
    }
}