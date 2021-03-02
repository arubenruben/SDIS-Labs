package server;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;


public class Server implements RemoteStubs {
    private static HashMap<String, String> dnsTable;

    public static void main(String[] args) {

        dnsTable = new HashMap<>();

        if (args.length != 1) {
            System.out.println("Incorrect Number arguments");
            return;
        }

        try {
            Server server = new Server();
            RemoteStubs remoteStubs = (RemoteStubs) UnicastRemoteObject.exportObject(server, 0);
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.bind(args[0], remoteStubs);
            System.out.println("Server Running");
        } catch (RemoteException | AlreadyBoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String register(String dnsName, String ipAddress) {
        dnsTable.put(dnsName, ipAddress);
        String response = dnsTable.size() - 1 + " " + dnsName + " " + ipAddress;

        System.out.println("register" + " " + dnsName + " " + ipAddress + "::" + response);

        return response;
    }

    @Override
    public String lookup(String dnsName) {

        String IPToReturn = dnsTable.get(dnsName);
        String response = dnsTable.size() + " " + dnsName + " " + IPToReturn;

        System.out.println("register" + " " + dnsName + " " + IPToReturn + "::" + response);
        return response;
    }
}
