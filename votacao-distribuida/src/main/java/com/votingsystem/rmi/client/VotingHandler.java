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
            System.out.println("Enquete criada com sucesso.!");

        } catch (RemoteException e) {
            System.out.println("Erro criando enquete: " + e.getMessage());
        }
    }

    public void vote() throws RemoteException {
        List<String> polls = votingService.getPolls();

        if (polls.isEmpty()) {
            System.out.println("Não há enquetes disponíveis.");
            return;
        }

        System.out.println("Enquetes disponíveis:");
        for (int i = 0; i < polls.size(); i++) {
            System.out.println((i + 1) + ". " + polls.get(i));
        }

        System.out.print("Escolha o número da enquete: ");
        int pollIndex = scanner.nextInt();
        scanner.nextLine(); // Limpa o \n deixado pelo nextInt()

        if (pollIndex < 1 || pollIndex > polls.size()) {
            System.out.println("Número de enquete inválido.");
            return;
        }

        String selectedPoll = polls.get(pollIndex - 1);

        List<String> options = votingService.getOptions(selectedPoll);

        if (options.isEmpty()) {
            System.out.println("Essa enquete não possui opções.");
            return;
        }

        System.out.println("Opções disponíveis:");
        for (int i = 0; i < options.size(); i++) {
            System.out.println((i + 1) + ". " + options.get(i));
        }

        System.out.print("Escolha o número da opção: ");
        int optionIndex = scanner.nextInt();
        scanner.nextLine(); // Limpa o \n

        if (optionIndex < 1 || optionIndex > options.size()) {
            System.out.println("Número de opção inválido.");
            return;
        }

        String selectedOption = options.get(optionIndex - 1);

        votingService.vote(username, selectedPoll, selectedOption);

        System.out.println("Voto registrado com sucesso!");
    }
}
