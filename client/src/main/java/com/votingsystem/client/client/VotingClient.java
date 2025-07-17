package com.votingsystem.client.client;

import com.votingsystem.common.domain.User;
import com.votingsystem.common.exception.UserAlreadyExistsException;
import com.votingsystem.common.interfaces.VotingService;
import com.votingsystem.centralserver.interfaces.CentralService;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;
import java.util.Scanner;

public class VotingClient {

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

    private static CentralService connectCentralService(Registry centralRegistry, String serviceName, int maxRetries, long retryIntervalMs) throws InterruptedException {
        int attempts = 0;
        while (attempts < maxRetries) {
            try {
                return (CentralService) centralRegistry.lookup(serviceName);
            } catch (NotBoundException | RemoteException e) {
                System.out.println("🔁 " + serviceName + " não encontrado ainda. Tentando novamente em " + (retryIntervalMs / 1000) + "s...");
                Thread.sleep(retryIntervalMs);
                attempts++;
            }
        }
        throw new RuntimeException(serviceName + " não pôde ser conectado após " + maxRetries + " tentativas");
    }

    public static void main(String[] args) {
        CentralService centralService;

        Scanner sc = new Scanner(System.in);

        try {
            Registry votingRegistry = LocateRegistry.getRegistry("voting-server", 1099);
            Registry centralRegistry = LocateRegistry.getRegistry("central-server", 1099);

            connectVotingService(votingRegistry, "VotingService1", 30, 1000);
            System.out.println("✅ Cliente conectado ao VotingService1 com sucesso.");

            centralService = connectCentralService(centralRegistry, "CentralService", 30, 1000);
            System.out.println("✅ Cliente conectado ao CentralService com sucesso.\n");

            System.out.println("\nOlá! Seja bem-vindo ao sistema de votação!\n");

            boolean loggedIn = false;
            while (!loggedIn) {
                System.out.println("Opções:\n1 - Fazer login\n2 - Fazer cadastro\nEscolha:");
                String optionStr = sc.nextLine().trim();

                if (!optionStr.matches("[12]")) {
                    System.out.println("Escolha inválida. Tente novamente.\n");
                    continue;
                }

                int option = Integer.parseInt(optionStr);

                switch (option) {
                    case 1 -> {
                        System.out.print("Digite seu usuário: ");
                        String usernameL = sc.nextLine().trim();
                        System.out.print("Digite sua senha: ");
                        String passwordL = sc.nextLine().trim();

                        boolean success = centralService.loginUser(usernameL, passwordL);
                        if (success) {
                            System.out.println("Login realizado com sucesso!\n");
                            loggedIn = true;
                        } else {
                            System.out.println("Usuário ou senha inválidos. Tente novamente.\n");
                        }
                    }
                    case 2 -> {
                        System.out.print("Digite seu usuário: ");
                        String usernameC = sc.nextLine().trim();
                        System.out.print("Digite sua senha: ");
                        String passwordC = sc.nextLine().trim();

                        try {
                            User newUser = new User(null, usernameC, passwordC);
                            centralService.registerUser(newUser);
                            System.out.println("Cadastro realizado com sucesso! Agora faça login.\n");
                        } catch (UserAlreadyExistsException e) {
                            System.out.println("Erro: Usuário já existe. Tente outro.\n");
                        }
                    }
                }
            }

            // Exibir resultados das enquetes
            Map<String, Map<String, Integer>> results = centralService.aggregateResults();
            if (results.isEmpty()) {
                System.out.println("Nenhuma enquete disponível no momento.");
            } else {
                System.out.println("Enquetes abertas a votação:\n");
                for (Map.Entry<String, Map<String, Integer>> pollEntry : results.entrySet()) {
                    System.out.println("Enquete: " + pollEntry.getKey());
                    for (Map.Entry<String, Integer> optionEntry : pollEntry.getValue().entrySet()) {
                        System.out.println("  " + optionEntry.getKey() + ": " + optionEntry.getValue());
                    }
                    System.out.println();
                }
            }

            // Aqui você pode adicionar interação para votar usando votingService, se quiser

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sc.close();
        }
    }
}
