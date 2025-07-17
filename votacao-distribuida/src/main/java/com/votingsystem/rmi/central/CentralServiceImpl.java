package com.votingsystem.rmi.central;

import com.votingsystem.rmi.domain.poll.Poll;
import com.votingsystem.rmi.domain.poll.PollOption;
import com.votingsystem.rmi.domain.user.User;
import com.votingsystem.rmi.domain.user.UserDto;
import com.votingsystem.rmi.exception.UserAlreadyExistsException;
import com.votingsystem.rmi.interfaces.CentralService;
import com.votingsystem.rmi.interfaces.VotingService;
import com.votingsystem.rmi.repository.UserRepository;
import com.votingsystem.rmi.repository.PollRepository;
import com.votingsystem.rmi.security.PasswordEncoder;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.*;
import java.util.logging.Logger;

public class CentralServiceImpl extends UnicastRemoteObject implements CentralService {
    private List<VotingService> votingServers;
    private UserRepository userRepository;
    private PollRepository pollRepository;
    private PasswordEncoder passwordEncoder;
    private Logger log;

    protected CentralServiceImpl(List<VotingService> servers) throws RemoteException {
        this.votingServers = servers;
        this.userRepository = new UserRepository();
        this.pollRepository = new PollRepository();
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

    @Override
    public synchronized void createPoll(String title, List<String> options) throws RemoteException {
        Poll existingPoll = pollRepository.findByTitle(title);
        if (existingPoll != null) {
            throw new RemoteException("Já existe uma enquete com esse título.");
        }

        // Cria lista de opções
        List<PollOption> pollOptions = new ArrayList<>();
        for (String option : options) {
            String optionId = UUID.randomUUID().toString(); // ID único para cada opção
            pollOptions.add(new PollOption(optionId, option, 0));
        }

        // Cria nova enquete
        Poll newPoll = new Poll(UUID.randomUUID().toString(), title, pollOptions);

        // Salva no banco
        pollRepository.save(newPoll);
    }

    @Override
    public Map<String, Map<String, Integer>> listPolls() throws RemoteException {
        List<Poll> polls = pollRepository.findAll();
        Map<String, Map<String, Integer>> result = new HashMap<>();

        for (Poll poll : polls) {
            Map<String, Integer> options = new LinkedHashMap<>();
            for (PollOption option : poll.getPollOptionsList()) {
                options.put(option.getText(), option.getVotes());
            }
            result.put(poll.getTitle(), options);
        }

        return result;
    }
}
