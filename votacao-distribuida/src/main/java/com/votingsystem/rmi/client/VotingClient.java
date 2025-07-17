package com.votingsystem.rmi.client;


import com.votingsystem.rmi.domain.user.User;
import com.votingsystem.rmi.exception.UserAlreadyExistsException;
import com.votingsystem.rmi.interfaces.VotingService;
import com.votingsystem.rmi.interfaces.CentralService;
import java.util.logging.Logger;

import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class VotingClient {
    public static void main(String[] args) {
        VotingService votingService = null;
        CentralService centralService = null;
        Logger log = Logger.getLogger("global");

        Scanner sc = new Scanner(System.in);

        try {
            // Conecta ao registry do VotingServer para pegar VotingService
            Registry votingRegistry = LocateRegistry.getRegistry("voting-server-1", 1099);
            // Conecta ao servidor central para pegar o CentralService
            Registry centralRegistry = LocateRegistry.getRegistry("central-server", 1099);

            log.info("🔁 Cliente aguardando VotingService1 aparecer no registry...");

            while (votingService == null) {
                try {
                    votingService = (VotingService) votingRegistry.lookup("VotingService1");
                } catch (NotBoundException e) {
                    Thread.sleep(1000);
                }
            }
            log.info("✅ Cliente conectado ao VotingService1 com sucesso.");

            log.info("🔁 Cliente aguardando CentralService aparecer no registry...");
            while (centralService == null) {
                try {
                    centralService = (CentralService) centralRegistry.lookup("CentralService");
                } catch (NotBoundException e) {
                    Thread.sleep(1000);
                }
            }
            log.info("✅ Cliente conectado ao CentralService com sucesso. \n");


            System.out.println("\nOlá Seja bem-vindo ao sistema de votação!! \n");
            int option = 0;
            do {
                System.out.println("\n Opções: \n\n 1- fazer login \n 2- fazer cadastro");
                option = sc.nextInt();
                switch (option) {
                    case 1:
                        System.out.println("Digite seu usuario: ");
                        String usernameL = sc.next();
                        System.out.println("Digite sua senha: ");
                        String passwordL = sc.next();
                        try {
                            if(centralService.loginUser(usernameL, passwordL)) {
                                System.out.println("Login realizado com sucesso!");
                                break;
                            } else {
                                System.out.println("Credenciais invalidas! Tente novamente!");
                                continue;
                            }
                        } catch (Exception exceptionRegister) {
                            option = 9999999;
                            System.out.println(exceptionRegister.getMessage());
                            continue;
                        }
                    case 2:
                        System.out.println("Digite seu usuario: ");
                        String usernameR = sc.next();
                        System.out.println("Digite sua senha: ");
                        String passwordR = sc.next();
                        try {
                            centralService.registerUser(new User(usernameR, passwordR));
                            System.out.println("Cadastro realizado com sucesso!");
                            break;
                        } catch (UserAlreadyExistsException exceptionRegister) {
                            option = 9999999;
                            System.out.println(exceptionRegister.getMessage());
                            continue;
                        }
                    default:
                        System.out.println("Escolha inválida tente novamente! \n");
                }
            } while (option != 1 && option != 2);

            do {
                System.out.println("\n Enquetes abertas a votação: \n\n");
                centralService.aggregateResults().forEach((op, count) -> System.out.println(op + ": " + count));
                option = sc.nextInt();
            } while (option != 1 && option != 2);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
