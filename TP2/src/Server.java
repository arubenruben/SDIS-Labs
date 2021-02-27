import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

//224.0.0.0 to 239.255.255.255 Range UDP Multicast
//Multicast protocol: multicast,<mcast_addr>,<mcast_port>

public class Server {
    private static HashMap<String, String> dnsTable;
    private static DatagramSocket unicastSocket;
    private static MulticastSocket multicastSocket;
    private static int unicastPort;
    private static int multicastPort;
    private static InetAddress multicastAddress;

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


        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(5);

        try {
            executor.scheduleAtFixedRate(multicastBroadCast(), 0, 1, TimeUnit.SECONDS);
        } catch (UnknownHostException e) {
            System.out.println("Error in thread executing");
        }

        unicastProcessing();

        executor.shutdown();
        unicastSocket.close();
        multicastSocket.close();

    }

    private static boolean processInput(String[] args) {

        try {
            unicastPort = Integer.parseInt(args[0]);
        } catch (NumberFormatException exception) {
            System.out.println("Argument 1 of input not valid");
            return false;
        }

        try {
            multicastAddress = InetAddress.getByName(args[1].trim());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

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
        return true;
    }

    private static Runnable multicastBroadCast() throws UnknownHostException {

        String stringToSend = "multicast," + InetAddress.getLocalHost().getHostAddress() + "," + unicastPort;

        byte[] sendingBuf = stringToSend.getBytes();
        return new Runnable() {
            @Override

            public void run() {
                try {
                    DatagramPacket datagramPacket = new DatagramPacket(sendingBuf, sendingBuf.length, multicastAddress, multicastPort);
                    System.out.println("multicast:" + datagramPacket.getAddress() + " " + datagramPacket.getPort() + ":" + InetAddress.getLocalHost().getHostAddress() + " " + unicastPort);

                    multicastSocket.setTimeToLive(1);
                    multicastSocket.send(datagramPacket);

                } catch (IOException e) {
                    System.out.println("Error Multicasting");
                }
            }
        };
    }

    private static void unicastProcessing() {
        while (true) {

        }

    }
}