package com.votingsystem.centralserver;

import com.votingsystem.centralserver.interfaces.CentralService;
import com.votingsystem.centralserver.repository.UserRepository;
import com.votingsystem.centralserver.security.PasswordEncoder;
import com.votingsystem.common.interfaces.VotingService;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class CentralServiceMain {

    private static VotingService connectVotingService(Registry votingRegistry, String serviceName, int maxRetries, long retryIntervalMs) throws InterruptedException {
        int attempts = 0;

        while (attempts < maxRetries) {
            try {
                return (VotingService) votingRegistry.lookup(serviceName);
            } catch (NotBoundException | RemoteException e) {
                System.out.println("🔁 " + serviceName + " não encontrado ainda. Tentando novamente em " + (retryIntervalMs / 1000) + "s...");
                Thread.sleep(retryIntervalMs);
                attempts++;
            }
        }
        throw new RuntimeException(serviceName + " não pôde ser conectado após " + maxRetries + " tentativas");
    }

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.createRegistry(1099);
            Registry votingRegistry = LocateRegistry.getRegistry("voting-server", 1099);

            VotingService votingService = connectVotingService(votingRegistry, "VotingService1", 30, 1000); // tenta por 30s

            System.out.println("✅ Conectado ao VotingService1 com sucesso.");

            List<VotingService> servers = List.of(votingService);

            UserRepository userRepository = new UserRepository();
            PasswordEncoder passwordEncoder = new PasswordEncoder();

            CentralService centralService = new CentralServerImpl(servers, userRepository, passwordEncoder);

            registry.rebind("CentralService", centralService);

            System.out.println("Servidor central ativo na porta 1099.");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
