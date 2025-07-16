package com.votingsystem.rmi.server;

import com.votingsystem.rmi.interfaces.VotingService;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class VotingServerMain {
    public static void main(String[] args) {
        try {
            // Define hostname para evitar problemas de binding no container
            System.setProperty("java.rmi.server.hostname", "voting-server");

            // Cria o registry no próprio processo na porta 1099
            Registry registry = LocateRegistry.createRegistry(1099);

            VotingService votingService = new VotingServiceImpl();

            // Registra o serviço
            registry.rebind("VotingService1", votingService);

            System.out.println("Servidor de votação ativo como 'VotingService1' na porta 1099.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
