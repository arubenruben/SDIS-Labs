import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Client {
    private static DatagramSocket unicastSocket;
    private static MulticastSocket multicastSocket;

    public static void main(String[] args) {
        if (args.length == 5 && !processInputRegister(args)) {
            System.out.println("Error");
            return;
        } else if (args.length == 4 && !processInputLookup(args)) {
            System.out.println("Error");
            return;
        } else {
            System.out.println("Number of parameters invalid");
            return;
        }
    }

    private static boolean processInputLookup(String[] args) {

        if (!multiCastInitializer(args[0], args[1]))
            return false;

        if (!uniCastInitializer())
            return false;

        return true;
    }

    private static boolean processInputRegister(String[] args) {

        if (!multiCastInitializer(args[0], args[1]))
            return false;
        if (!uniCastInitializer())
            return false;
        return true;
    }


    private static boolean multiCastInitializer(String multiCastAddress, String strPort) {

        int multicastPort;

        try {
            multicastPort = Integer.parseInt(strPort);
        } catch (NumberFormatException exception) {
            System.out.println("Argument 1 of input not valid");
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

    private static boolean uniCastInitializer() {


        int unicastPort;

        try {
            //unicastPort = Integer.parseInt();
        } catch (NumberFormatException exception) {
            System.out.println("Argument 1 of input not valid");
            return false;
        }

        return true;
    }
        /*
    public static boolean processInput(String[] args) {



        try {
            unicastPort = Integer.parseInt(args[0]);
        } catch (NumberFormatException exception) {
            System.out.println("Argument 1 of input not valid");
            return false;
        }
        int unicastPort;


        try {
            unicastSocket = new DatagramSocket(unicastPort);
        } catch (SocketException e) {
            System.out.println("Error opening Socket");
            return false;
        }

        return true;
    }
         */
}
