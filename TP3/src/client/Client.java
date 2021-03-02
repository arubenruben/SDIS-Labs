package client;

import server.RemoteStubs;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    public static void main(String[] args) {
        if (args.length < 4) {
            System.out.println("Invalid number of parameters");
            return;
        }

        RemoteStubs remoteStub;
        String hostName = args[0];
        String remoteObjectName = args[1];

        try {
            Registry registry = LocateRegistry.getRegistry(hostName);
            remoteStub = (RemoteStubs) registry.lookup(remoteObjectName);
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
            return;
        }

        String operation = args[2];

        if (operation.equals("register")) {
            try {
                String response = remoteStub.register(args[3], args[4]);
                System.out.println(operation + " " + args[3] + " " + args[4] + "::" + response);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else if (operation.equals("lookup")) {

            try {
                String response = remoteStub.lookup(args[3]);
                System.out.println(operation + " " + args[3] + " " + "::" + response);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Invalid operation");
        }


    }
}
