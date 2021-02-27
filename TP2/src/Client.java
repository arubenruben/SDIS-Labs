import java.io.IOException;
import java.net.*;
import java.util.Arrays;

//Multicast protocol: multicast,<mcast_addr>,<mcast_port>

public class Client {
    private static DatagramSocket unicastSocket;
    private static MulticastSocket multicastSocket;
    private static int multicastPort;
    private static InetAddress multicastAddress;
    private static InetAddress unicastAddress;
    private static int unicastPort;
    private static String operation;


    public static void main(String[] args) {

        if (args.length == 5) {
            try {
                handleRegister(args);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        } else if (args.length == 4) {
            try {
                handleLookup(args);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Number of parameters invalid");
        }
    }


    private static void handleRegister(String[] args) throws UnknownHostException {

        if (args[2].equals("register")) {
            operation = args[2];
        } else {
            return;
        }


        if (multicastInitializer(args))
            return;

        if (unicastInitializer((receiveUnicastInformation()))) {
            System.out.println("Error Initializing Unicast Socket");
            return;
        }

        sendMessage("register " + args[3] + " " + args[4]);
        processReply(receiveReply());
        return;
    }

    private static void handleLookup(String[] args) throws UnknownHostException {

        if (args[2].equals("lookup")) {
            operation = args[2];
        } else {
            return;
        }

        if (multicastInitializer(args))
            return;

        if (unicastInitializer((receiveUnicastInformation()))) {
            System.out.println("Error Initializing Unicast Socket");
            return;
        }

        sendMessage("lookup " + args[3]);
        processReply(receiveReply());

    }

    private static boolean multicastInitializer(String[] args) {

        try {
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

        try {
            multicastSocket.receive(response);
        } catch (IOException e) {
            System.out.println("Error while receiving reply from register");
            return null;
        }

        return new String(response.getData());
    }

    private static boolean unicastInitializer(String response) throws UnknownHostException {

        if (response == null)
            return true;

        String[] tokenReplies = response.split(",");

        unicastAddress = InetAddress.getByName(tokenReplies[1].trim());

        try {
            unicastPort = Integer.parseInt(tokenReplies[2].trim());
        } catch (NumberFormatException exception) {
            System.out.println("Error in unicast port");
            return false;
        }

        try {
            unicastSocket = new DatagramSocket();
        } catch (SocketException e) {
            System.out.println("Error opening Socket");
        }

        System.out.println("multicast:" + multicastAddress.getHostAddress() + " " + multicastPort + " : " + unicastAddress.getHostAddress() + " " + unicastPort);

        return false;
    }

    public static void sendMessage(String stringToSend) {
        byte[] sendingBuf = stringToSend.getBytes();

        try {
            unicastSocket.send(new DatagramPacket(sendingBuf, sendingBuf.length, unicastAddress, unicastPort));
        } catch (IOException e) {
            System.out.println("Error Sending register attempt");
        }
    }

    public static String receiveReply() {

        byte[] receivingBuf = new byte[512];

        DatagramPacket response = new DatagramPacket(receivingBuf, receivingBuf.length, unicastAddress, unicastPort);

        try {
            unicastSocket.receive(response);
        } catch (IOException e) {
            System.out.println("Error while receiving reply from register");
        }

        return new String(response.getData());

    }

    public static void processReply(String reply) {
        if (reply == null) {
            System.out.println("Null reply");
            return;
        }
        System.out.println(reply);
        String[] tokenReplies = reply.split(" ");

        String genericClientMessage = "Client: " + operation + " " + tokenReplies[1] + " " + tokenReplies[2];

        if (tokenReplies[0].equals("-1"))
            System.out.println(genericClientMessage + " : " + "ERROR");
        else
            System.out.println(genericClientMessage + " : " + tokenReplies[0]);

    }
}
