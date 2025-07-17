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
        Logger log = Logger.getLogger("global");
        Scanner sc = new Scanner(System.in);

        try {
            VotingService votingService = RMIConnector.connectVotingService(log);
            CentralService centralService = RMIConnector.connectCentralService(log);

            System.out.println("\nOlá! Seja bem-vindo ao sistema de votação!\n");

            AuthHandler authHandler = new AuthHandler(centralService, sc);
            authHandler.handleAuth();

            VotingHandler votingHandler = new VotingHandler(centralService, votingService ,sc);
            votingHandler.showMainMenu();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

