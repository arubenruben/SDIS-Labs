import java.io.IOException;
import java.net.*;
import java.util.Arrays;

//Multicast protocol: multicast,<mcast_addr>,<mcast_port>

public class Client {
    private static DatagramSocket unicastSocket;
    private static MulticastSocket multicastSocket;
    private static int multicastPort;
    private static InetAddress multicastAddress;

    public static void main(String[] args) {

        if (args.length == 5)
            handleRegister(args);
        else if (args.length == 4)
            handleLookup(args);
        else {
            System.out.println("Number of parameters invalid");
        }
    }


    private static void handleRegister(String[] args) {


        if (multicastInitializer(args))
            return;
        System.out.println("Initialized Multicast");
        if (!unicastInitializer((receiveUnicastInformation()))) {
            System.out.println("Error Initializing Unicast Socket");
            return;
        }

        return;
    }

    private static void handleLookup(String[] args) {


        if (multicastInitializer(args))
            return;

        if (!unicastInitializer((receiveUnicastInformation()))) {
            System.out.println("Error Initializing Unicast Socket");
            return;
        }


        return;
    }

    private static boolean multicastInitializer(String[] args) {

        try {
            System.out.println(args[0]);
            multicastAddress = InetAddress.getByName(args[0]);
        } catch (UnknownHostException e) {
            System.out.println("Error Parsing Multicast Address");
            return true;
        }

        try {
            multicastPort = Integer.parseInt(args[1]);
        } catch (NumberFormatException exception) {
            System.out.println("Argument 1 of input not valid");
            return true;
        }

        try {
            System.out.println(multicastPort);
            multicastSocket = new MulticastSocket(multicastPort);
        } catch (IOException e) {
            System.out.println("Error opening Socket");
            return true;
        }
        try {
            multicastSocket.joinGroup(multicastAddress);
        } catch (IOException e) {
            System.out.println("Error joining the Group");
            return true;
        }
        return false;
    }

    private static String receiveUnicastInformation() {
        byte[] receivingBuf = new byte[512];

        DatagramPacket response = new DatagramPacket(receivingBuf, receivingBuf.length);
        System.out.println("Parado");
        try {
            multicastSocket.receive(response);
            System.out.println(response);
        } catch (IOException e) {
            System.out.println("Error while receiving reply from register");
            return null;
        }

        return new String(response.getData());
    }

    private static boolean unicastInitializer(String response) {

        if (response == null)
            return false;

        String[] tokenReplies = response.split(",");

        System.out.println(Arrays.toString(tokenReplies));

        return true;
    }
}
