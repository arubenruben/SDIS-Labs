import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.Arrays;

public class SSLClient {
    private static SSLSocket socket;
    private static InetAddress address;
    private static int port;
    private static String operation;

    public static void main(String[] args) {


        if (args.length < 3) {
            System.out.println("Minimal number of arguments not reached");
            return;
        }

        String[] cypherSuites;

        if (args[2].equals("lookup")) {
            cypherSuites = Arrays.copyOfRange(args, 4, args.length);
        } else if (args[2].equals("register")) {
            cypherSuites = Arrays.copyOfRange(args, 5, args.length);
        } else {
            System.err.println("Invalid arguments passed");
            return;
        }

        try {
            socket = (SSLSocket) SSLSocketFactory.getDefault().createSocket(args[0], Integer.parseInt(args[1]));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error opening socket");
        }

        socket.setEnabledCipherSuites(cypherSuites);

        String stringToSend;
        operation = args[2];

        if (operation.equals("register"))
            stringToSend = handleRegisterRequestString(args);
        else if (operation.equals("lookup"))
            stringToSend = handleLookupRequestString(args);
        else {
            System.out.println("Invalid Operation");
            return;
        }

        if (stringToSend == null)
            return;

        sendMessage(stringToSend);

        processReply(receiveReply());


        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error closing the socket");
        }

    }

    public static void sendMessage(String stringToSend) {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(stringToSend);
        } catch (IOException e) {
            System.out.println("Error Sending register attempt");
        }
    }

    public static String receiveReply() {

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            return reader.readLine();
        } catch (IOException e) {
            System.out.println("Error while receiving reply from register");
            return null;
        }

    }

    public static void processReply(String reply) {
        System.out.println(reply);
        String[] tokenReplies = reply.split(" ");

        String genericClientMessage = "Client: " + operation + " " + tokenReplies[1] + " " + tokenReplies[2];

        if (tokenReplies[0].equals("-1"))
            System.out.println(genericClientMessage + " : " + "ERROR");
        else
            System.out.println(genericClientMessage + " : " + tokenReplies[0]);

    }

    public static String handleRegisterRequestString(String[] args) {


        String dnsName = args[3];
        String IPAddress = args[4];

        return ("register " + dnsName + " " + IPAddress);
    }

    public static String handleLookupRequestString(String[] args) {

        String dnsName = args[3];

        return "lookup " + dnsName;
    }
}
