package com.votingsystem.rmi.client;

import com.votingsystem.rmi.interfaces.CentralService;
import com.votingsystem.rmi.interfaces.VotingService;

import java.rmi.RemoteException;
import java.util.*;

public class VotingHandler {
    private final CentralService centralService;
    private final VotingService votingService;
    private final Scanner scanner;
    private final String username;

    public VotingHandler(CentralService centralService, VotingService votingService, Scanner scanner) {
        this.centralService = centralService;
        this.votingService = votingService;
        this.scanner = scanner;

        System.out.print("Digite seu nome de usuário: ");
        this.username = scanner.nextLine();
    }

    public void showMainMenu() {
        while (true) {
            System.out.println("\nMain Menu:\n");
            System.out.println("1 - Listar enquetes");
            System.out.println("2 - Criar uma nova enquete");
            System.out.println("3 - Votar em uma enquete");
            System.out.println("0 - Sair");

            int option = scanner.nextInt();
            scanner.nextLine(); // clear buffer

            switch (option) {
                case 1 -> listPolls();
                case 2 -> createPoll();
                case 3 -> vote();
                case 0 -> {
                    System.out.println("Saindo...");
                    return;
                }
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    private void listPolls() {
        try {
            Map<String, Map<String, Integer>> polls = centralService.listPolls();
            if (polls.isEmpty()) {
                System.out.println("Nenhuma enquete encontrada.");
                return;
            }
            System.out.println("Enquetes disponíveis:\n");
            for (var entry : polls.entrySet()) {
                System.out.println("Enquete: " + entry.getKey());
                for (Map.Entry<String, Integer> optionEntry : entry.getValue().entrySet()) {
                    System.out.println("  - " + optionEntry.getKey() + ": " + optionEntry.getValue() + " votos");
                }
            }
        } catch (RemoteException e) {
            System.out.println("Erro listando enquetes: " + e.getMessage());
        }
    }

    private void createPoll() {
        try {
            System.out.print("Digite o titulo da enquete: ");
            String title = scanner.nextLine();

            System.out.print("Quantas opções a enquete deve ter? (Max=2) ");
            int optionCount = scanner.nextInt();
            scanner.nextLine(); // clear buffer

            List<String> options = new ArrayList<>();
            for (int i = 0; i < optionCount; i++) {
                System.out.print("Digite a opção " + (i + 1) + ": ");
                options.add(scanner.nextLine());
            }

            centralService.createPoll(title, options);
            System.out.println("Enquete criada com sucesso!");
        } catch (RemoteException e) {
            System.out.println("Erro criando enquete: " + e.getMessage());
        }
    }

    private void vote() {
        try {
            Map<String, Map<String, Integer>> polls = centralService.listPolls();
            if (polls.isEmpty()) {
                System.out.println("Nenhuma enquete disponível para votar.");
                return;
            }

            System.out.println("Escolha uma enquete para votar:");
            List<String> pollTitles = new ArrayList<>(polls.keySet());
            for (int i = 0; i < pollTitles.size(); i++) {
                System.out.println((i + 1) + " - " + pollTitles.get(i));
            }

            int pollChoice = scanner.nextInt();
            scanner.nextLine(); // clear buffer

            if (pollChoice < 1 || pollChoice > pollTitles.size()) {
                System.out.println("Escolha inválida.");
                return;
            }

            String selectedPoll = pollTitles.get(pollChoice - 1);
            Map<String, Integer> options = polls.get(selectedPoll);

            System.out.println("Opções para a enquete '" + selectedPoll + "':");
            List<String> optionList = new ArrayList<>(options.keySet());
            for (int i = 0; i < optionList.size(); i++) {
                System.out.println((i + 1) + " - " + optionList.get(i));
            }

            int optionChoice = scanner.nextInt();
            scanner.nextLine(); // clear buffer

            if (optionChoice < 1 || optionChoice > optionList.size()) {
                System.out.println("Opção inválida.");
                return;
            }

            String selectedOption = optionList.get(optionChoice - 1);

            // Chamada correta ao método vote
            votingService.vote(username, selectedPoll, selectedOption);

            System.out.println("Voto registrado com sucesso!");

        } catch (RemoteException e) {
            System.out.println("Erro ao votar: " + e.getMessage());
        }
    }
}
