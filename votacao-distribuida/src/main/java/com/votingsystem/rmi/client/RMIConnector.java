package com.votingsystem.rmi.client;

import com.votingsystem.rmi.interfaces.CentralService;
import com.votingsystem.rmi.interfaces.VotingService;

import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Logger;

public class RMIConnector {

    public static VotingService connectVotingService(Logger log) throws Exception {
        Registry votingRegistry = LocateRegistry.getRegistry("voting-server-1", 1099);
        VotingService service = null;
        log.info("🔁 Aguardando VotingService1...");
        while (service == null) {
            try {
                service = (VotingService) votingRegistry.lookup("VotingService1");
            } catch (NotBoundException e) {
                Thread.sleep(1000);
            }
        }
        log.info("✅ Conectado ao VotingService1.");
        return service;
    }

    public static CentralService connectCentralService(Logger log) throws Exception {
        Registry centralRegistry = LocateRegistry.getRegistry("central-server", 1099);
        CentralService service = null;
        log.info("🔁 Aguardando CentralService...");
        while (service == null) {
            try {
                service = (CentralService) centralRegistry.lookup("CentralService");
            } catch (NotBoundException e) {
                Thread.sleep(1000);
            }
        }
        log.info("✅ Conectado ao CentralService.");
        return service;
    }
}
