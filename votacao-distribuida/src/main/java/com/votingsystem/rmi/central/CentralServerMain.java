package com.votingsystem.rmi.central;

import com.votingsystem.rmi.interfaces.CentralService;
import com.votingsystem.rmi.interfaces.VotingService;

import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.List;

public class CentralServerMain {
    public static void main(String[] args) {

        VotingService votingService = null;

        try {
            // Cria o registry local na porta 1099 para o servidor central
            Registry registry = LocateRegistry.createRegistry(1099);

            // Tenta conectar ao VotingService1 no servidor VotingServer
            Registry votingRegistry = LocateRegistry.getRegistry("voting-server-1", 1099);

            while (votingService == null) {
                try {
                    votingService = (VotingService) votingRegistry.lookup("VotingService1");
                } catch (NotBoundException e) {
                    System.out.println("🔁 VotingService1 não encontrado ainda. Tentando novamente em 1s...");
                    Thread.sleep(1000);
                }
            }

            System.out.println("✅ Conectado ao VotingService1 com sucesso.");

            // Se quiser adicionar mais servidores, pode colocar aqui
            List<VotingService> servers = Arrays.asList(votingService);

            CentralService centralService = new CentralServiceImpl(servers);

            // Registra o CentralService no registry local
            registry.rebind("CentralService", centralService);

            System.out.println("Servidor central ativo na porta 1099.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
