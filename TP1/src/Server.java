import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.HashMap;

public class Server {
    final private static int messageSize = 512;
    private static int port = -1;
    private static DatagramSocket socket;
    private static HashMap<String, String> dnsTable;

    public static void main(String[] args) {

        dnsTable = new HashMap<>();

        if (args.length != 1) {
            System.out.println("Input Arguments not valid");
            return;
        }
        try {
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException exception) {
            System.out.println("Argument 1 of input not valid");
        }

        try {
            socket = new DatagramSocket(port);
        } catch (SocketException e) {
            System.out.println("Error opening Socket");
        }


        try {
            run();
        } catch (IOException e) {
            System.out.println("Error While Running");
        }

        socket.close();

    }

    public static void run() throws IOException {

        System.out.println("Server Running");

        while (true) {

            byte[] buf = new byte[messageSize];
            DatagramPacket packet = new DatagramPacket(buf, messageSize);
            socket.receive(packet);
            reply(packet);

        }
    }

    public static void handleRegisterRequest(DatagramPacket packet, String[] tokenParsedRequest) {
        if (tokenParsedRequest.length != 3) {
            System.out.println("Register Request MalFormed");
            return;
        }
        dnsTable.put(tokenParsedRequest[1], tokenParsedRequest[2]);
        String toReply = dnsTable.size() - 1 + " " + tokenParsedRequest[1] + " " + tokenParsedRequest[2];

        packet.setData(toReply.getBytes());
        try {
            socket.send(packet);
        } catch (IOException e) {
            System.out.println("Error sending Reply");
        }

    }

    public static void handleLookupRequest(DatagramPacket packet, String[] tokenParsedRequest) {
        if (tokenParsedRequest.length != 2) {
            System.out.println("Lookup Request MalFormed");
            return;
        }

        String IPToReturn = dnsTable.get(tokenParsedRequest[1].trim());
        String toReply = dnsTable.size() + " " + tokenParsedRequest[1].trim() + " " + IPToReturn;

        packet.setData(toReply.getBytes());

        try {
            socket.send(packet);
        } catch (IOException e) {
            System.out.println("Error sending Reply");
        }
    }

    public static void reply(DatagramPacket packet) {

        String request = new String(packet.getData());

        String[] tokenParsedRequest = request.split(" ");

        System.out.println("Server:" + request);

        if (tokenParsedRequest.length < 2) {
            System.out.println("Not well formatted Request");
            return;
        }
        if (tokenParsedRequest[0].equals("register"))
            handleRegisterRequest(packet,tokenParsedRequest);
        else if (tokenParsedRequest[0].equals("lookup"))
            handleLookupRequest(packet,tokenParsedRequest);

    }

}
