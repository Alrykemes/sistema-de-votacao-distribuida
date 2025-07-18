package com.votingsystem.rmi.central;

import com.votingsystem.rmi.domain.user.User;
import com.votingsystem.rmi.domain.user.UserDto;
import com.votingsystem.rmi.exception.UserAlreadyExistsException;
import com.votingsystem.rmi.interfaces.CentralService;
import com.votingsystem.rmi.interfaces.VotingService;
import com.votingsystem.rmi.repository.UserRepository;
import com.votingsystem.rmi.security.PasswordEncoder;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.logging.Logger;

public class CentralServiceImpl extends UnicastRemoteObject implements CentralService {
    private List<VotingService> votingServers;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private Logger log;

    protected CentralServiceImpl(List<VotingService> servers) throws RemoteException {
        this.votingServers = servers;
        this.userRepository = new UserRepository();
        this.passwordEncoder = new PasswordEncoder();
        this.log = Logger.getLogger("global");
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

    @Override
    public User registerUser(User user) throws RemoteException, UserAlreadyExistsException {
        if (userRepository.findByUsername(user.getUsername()) == null) {
            user.setPassword(passwordEncoder.hashPassword(user.getPassword()));
            userRepository.save(user);
            return user;
        } else {
            throw new UserAlreadyExistsException(user.getUsername());
        }
    }

    @Override
    public boolean loginUser(String username, String password) throws RemoteException {
        UserDto user = userRepository.findByUsername(username);
        if (user != null && user.password() != null) {
            return passwordEncoder.verifyPassword(password, user.password());
        }

        return false;
    }
}
