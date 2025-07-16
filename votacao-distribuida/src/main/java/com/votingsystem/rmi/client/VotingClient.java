package com.votingsystem.rmi.client;

import com.votingsystem.rmi.interfaces.VotingService;
import com.votingsystem.rmi.interfaces.CentralService;

import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;

public class VotingClient {
    public static void main(String[] args) {
        VotingService votingService = null;

        try {
            // Conecta ao registry do VotingServer para pegar VotingService
            Registry votingRegistry = LocateRegistry.getRegistry("voting-server", 1099);

            System.out.println("🔁 Cliente aguardando VotingService1 aparecer no registry...");

            while (votingService == null) {
                try {
                    votingService = (VotingService) votingRegistry.lookup("VotingService1");
                } catch (NotBoundException e) {
                    Thread.sleep(1000);
                }
            }

            System.out.println("✅ Cliente conectado ao VotingService1 com sucesso.");

            // Vota em algumas opções
            votingService.vote("poll1", "opcaoA");
            votingService.vote("poll1", "opcaoB");
            votingService.vote("poll1", "opcaoA");

            System.out.println("Votos registrados!");

            Map<String, Integer> parcial = votingService.getVotes("poll1");
            System.out.println("Resultado parcial da poll1:");
            parcial.forEach((op, count) -> System.out.println(op + ": " + count));

            // Agora conecta ao servidor central para pegar os resultados agregados
            Registry centralRegistry = LocateRegistry.getRegistry("central-server", 1099);
            CentralService centralService = null;

            System.out.println("🔁 Cliente aguardando CentralService aparecer no registry...");

            while (centralService == null) {
                try {
                    centralService = (CentralService) centralRegistry.lookup("CentralService");
                } catch (NotBoundException e) {
                    Thread.sleep(1000);
                }
            }

            System.out.println("✅ Cliente conectado ao CentralService com sucesso.");

            Map<String, Map<String, Integer>> total = centralService.aggregateResults();

            System.out.println("Resultado agregado do servidor central:");
            total.forEach((poll, options) -> {
                System.out.println("Enquete: " + poll);
                options.forEach((op, count) -> System.out.println("  " + op + ": " + count));
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
