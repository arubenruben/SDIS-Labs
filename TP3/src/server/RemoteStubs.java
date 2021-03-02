package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteStubs extends Remote {
    String register(String dnsName, String ipAddress) throws RemoteException;

    String lookup(String dnsName) throws RemoteException;
}
