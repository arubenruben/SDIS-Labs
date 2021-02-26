import java.io.IOException;
import java.net.*;
import java.util.HashMap;

//224.0.0.0 to 239.255.255.255 Range UDP Multicast
public class Server {
    private static HashMap<String, String> dnsTable;
    private static DatagramSocket unicastSocket;
    private static MulticastSocket multicastSocket;

    public static void main(String[] args) {
        dnsTable = new HashMap<>();

        if (args.length != 3) {
            System.out.println("Incorrect Number of arguments");
            return;
        }
        if (!processInput(args)) {
            System.out.println("Error processing Input");
            return;
        }


    }

    public static boolean processInput(String[] args) {
        int unicastPort;
        int multicastPort;
        String multiCastAddress;

        try {
            unicastPort = Integer.parseInt(args[0]);
        } catch (NumberFormatException exception) {
            System.out.println("Argument 1 of input not valid");
            return false;
        }

        multiCastAddress = args[1];

        try {
            multicastPort = Integer.parseInt(args[2]);
        } catch (NumberFormatException exception) {
            System.out.println("Argument 1 of input not valid");
            return false;
        }

        try {
            unicastSocket = new DatagramSocket(unicastPort);
        } catch (SocketException e) {
            System.out.println("Error opening Socket");
            return false;
        }
        try {
            multicastSocket = new MulticastSocket(multicastPort);
        } catch (IOException e) {
            System.out.println("Error opening Socket");
            return false;
        }

        try {
            multicastSocket.joinGroup(InetAddress.getByName(multiCastAddress));
        } catch (IOException e) {
            System.out.println("Error joining the Group");
            return false;
        }

        return true;
    }
}
