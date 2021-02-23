import java.io.IOException;
import java.net.*;
import java.util.Arrays;

public class Client {
    private static DatagramSocket socket;
    private static InetAddress address;
    private static int port;
    private static String operation;

    public static void main(String[] args) {

        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            System.out.println("Error opening Socket");
        }

        if (args.length < 3) {
            System.out.println("Minimal number of arguments not reached");
            return;
        }

        try {
            address = InetAddress.getByName(args[0]);
        } catch (UnknownHostException e) {
            System.out.println("Host not reachable");
        }
        try {
            port = Integer.parseInt(args[1]);
        } catch (NumberFormatException exception) {
            System.out.println("Argument 1 of input not valid");
        }

        String stringToSend;
        operation=args[2];

        if (operation.equals("register"))
            stringToSend=handleRegisterRequestString(args);
        else if (operation.equals("lookup"))
            stringToSend=handleLookupRequestString(args);
        else{
            System.out.println("Invalid Operation");
            return;
        }

        if(stringToSend==null)
            return;

        sendMessage(stringToSend);

        processReply(receiveReply());


        socket.close();

    }

    public static void sendMessage(String stringToSend){

        byte[] sendingBuf = stringToSend.getBytes();

        try {
            socket.send(new DatagramPacket(sendingBuf, sendingBuf.length, address, port));
        } catch (IOException e) {
            System.out.println("Error Sending register attempt");
        }
    }
    public static String receiveReply(){

        byte[] receivingBuf = new byte[512];

        DatagramPacket response = new DatagramPacket(receivingBuf, receivingBuf.length, address, port);

        try {
            socket.receive(response);
        } catch (IOException e) {
            System.out.println("Error while receiving reply from register");
        }

        return new String(response.getData());

    }

    public  static void processReply(String reply){

        String[] tokenReplies = reply.split(" ");

        String genericClientMessage = "Client: "+ operation+" " + tokenReplies[1] + " " + tokenReplies[2];

        if (tokenReplies[0].equals("-1"))
            System.out.println(genericClientMessage + " : " + "ERROR");
        else
            System.out.println(genericClientMessage + " : " + tokenReplies[0]);

    }

    public static String handleRegisterRequestString(String[] args) {

        if (args.length != 5) {
            System.out.println("Invalid number of arguments of a register request");
            return null;
        }
        String dnsName = args[3];
        String IPAddress = args[4];

        return ("register " + dnsName + " " + IPAddress);
    }

    public static String handleLookupRequestString(String[] args) {

        if (args.length != 4) {
            System.out.println("Invalid number of arguments lookup request");
            return null;
        }
        String dnsName = args[3];

        return "lookup " + dnsName;
    }
}
