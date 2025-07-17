package com.votingsystem.rmi.client;

import com.votingsystem.rmi.domain.user.User;
import com.votingsystem.rmi.exception.UserAlreadyExistsException;
import com.votingsystem.rmi.interfaces.CentralService;

import java.util.Scanner;

public class AuthHandler {
    private final CentralService centralService;
    private final Scanner sc;

    public AuthHandler(CentralService centralService, Scanner sc) {
        this.centralService = centralService;
        this.sc = sc;
    }

    public void handleAuth() {
        int option;
        do {
            System.out.println("\n Opções: \n\n 1- fazer login \n 2- fazer cadastro");
            option = sc.nextInt();

            switch (option) {
                case 1 -> login();
                case 2 -> register();
                default -> System.out.println("Escolha inválida. Tente novamente.");
            }
        } while (option != 1 && option != 2);
    }

    private void login() {
        System.out.print("Digite seu usuario: ");
        String username = sc.next();
        System.out.print("Digite sua senha: ");
        String password = sc.next();
        try {
            if (centralService.loginUser(username, password)) {
                System.out.println("Login realizado com sucesso!");
            } else {
                System.out.println("Credenciais inválidas! Tente novamente!");
                login(); // tenta de novo
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void register() {
        System.out.print("Digite seu usuario: ");
        String username = sc.next();
        System.out.print("Digite sua senha: ");
        String password = sc.next();
        try {
            centralService.registerUser(new User(username, password));
            System.out.println("Cadastro realizado com sucesso!");
        } catch (UserAlreadyExistsException e) {
            System.out.println("Erro: " + e.getMessage());
            register();
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}
