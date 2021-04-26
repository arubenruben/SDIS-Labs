import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.HashMap;

public class SSLServer {


    final private static int messageSize = 512;
    private static int port = -1;
    private static SSLServerSocket serverSocket;
    private static HashMap<String, String> dnsTable;
    private static Socket socket;

    public static void main(String[] args) {

        dnsTable = new HashMap<>();

        try {
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException exception) {
            System.out.println("Argument 1 of input not valid");
        }

        String[] cypherSuites = Arrays.copyOfRange(args, 1, args.length);

        try {
            SSLServerSocketFactory factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            serverSocket = (SSLServerSocket) factory.createServerSocket(port);

            serverSocket.setNeedClientAuth(true);

            //serverSocket.setEnabledProtocols(serverSocket.getEnabledProtocols());
            //serverSocket.setEnabledCipherSuites(cypherSuites);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error opening server socket");
        }

        try {
            run();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error While Running");
        }

        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error close server socket");
        }

    }

    public static void run() throws IOException {

        System.out.println("Server Running");

        while (true) {

            socket = serverSocket.accept();
            BufferedReader inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            reply(inputStream.readLine());

            socket.close();

        }
    }

    public static void reply(String request) {

        String[] tokenParsedRequest = request.split(" ");

        System.out.println("Server:" + request);

        if (tokenParsedRequest.length < 2) {
            System.out.println("Not well formatted Request");
            return;
        }
        if (tokenParsedRequest[0].equals("register"))
            handleRegisterRequest(tokenParsedRequest);
        else if (tokenParsedRequest[0].equals("lookup"))
            handleLookupRequest(tokenParsedRequest);

    }

    public static void handleRegisterRequest(String[] tokenParsedRequest) {
        if (tokenParsedRequest.length != 3) {
            System.out.println("Register Request MalFormed");
            return;
        }
        dnsTable.put(tokenParsedRequest[1], tokenParsedRequest[2]);
        String toReply = dnsTable.size() - 1 + " " + tokenParsedRequest[1] + " " + tokenParsedRequest[2];

        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(toReply);
        } catch (IOException e) {
            System.out.println("Error sending Reply");
        }

    }

    public static void handleLookupRequest(String[] tokenParsedRequest) {
        if (tokenParsedRequest.length != 2) {
            System.out.println("Lookup Request MalFormed");
            return;
        }

        String IPToReturn = dnsTable.get(tokenParsedRequest[1].trim());
        String toReply = dnsTable.size() + " " + tokenParsedRequest[1].trim() + " " + IPToReturn;

        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(toReply);
        } catch (IOException e) {
            System.out.println("Error sending Reply");
        }
    }

}

