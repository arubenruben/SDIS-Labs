import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Client {
    private static DatagramSocket socket;
    private static InetAddress address;
    private static int port;
    private static int returnCode;

    public static void main(String[] args) {

        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            System.out.println("Error opening Socket");
        }


        if(args.length<3){
            System.out.println("Minimal number of arguments not reached");
            return;
        }


        try {
            address=InetAddress.getByName(args[0]);
        } catch (UnknownHostException e) {
            System.out.println("Host not reachable");
            return;
        }
        try {
            port=Integer.parseInt(args[1]);
        }catch (NumberFormatException exception){
            System.out.println("Argument 1 of input not valid");
        }

        if(args[2].equals("register"))
            handleRegister(args);
        else if(args[2].equals("lookup"))
            handleLookup(args);
        else
            System.out.println("Invalid Operation");


        socket.close();

    }

    public static void handleRegister(String[] args){

        if(args.length!=5){
            System.out.println("Invalid number of arguments of a register request");
            return;
        }
        String dnsName=new String(args[3]);
        String IPAddress=new String(args[4]);

        String string="register "+dnsName+" "+IPAddress;
        byte[] sendingBuf=string.getBytes();

        try {
            socket.send(new DatagramPacket(sendingBuf,sendingBuf.length,address,port));
        } catch (IOException e) {
            System.out.println("Error Sending register attempt");
        }

        byte[] receivingBuf=new byte[512];
        DatagramPacket response=new DatagramPacket(receivingBuf,receivingBuf.length,address,port);

        try {
            socket.receive(response);
        } catch (IOException e) {
            System.out.println("Error while receiving reply from register");
        }

        String reply=new String(response.getData());
        String[] tokenReplies=reply.split(" ");

        String genericClientMessage="Client: register " + dnsName+" "+ IPAddress;

        if(tokenReplies[0].equals("-1"))
            System.out.println(genericClientMessage+" : " +"ERROR");
        else
            System.out.println(genericClientMessage+" : " + returnCode);
    }

    public static void handleLookup(String[] args){

        if(args.length!=4){
            System.out.println("Invalid number of arguments lookup request");
            return;
        }
        String dnsName=new String(args[3]);

        String string="lookup "+dnsName;
        byte[] sendingBuf=string.getBytes();

        try {
            socket.send(new DatagramPacket(sendingBuf,sendingBuf.length,address,port));
        } catch (IOException e) {
            System.out.println("Error Sending register attempt");
        }

        byte[] receivingBuf=new byte[512];
        DatagramPacket response=new DatagramPacket(receivingBuf,receivingBuf.length,address,port);

        try {
            socket.receive(response);
        } catch (IOException e) {
            System.out.println("Error while receiving reply from register");
        }

        String reply=new String(response.getData());
        String[] tokenReplies=reply.split(" ");

        String genericClientMessage="Client: lookup " + dnsName;

        if(tokenReplies[0].equals("-1"))
            System.out.println(genericClientMessage+" : " +"ERROR");
        else
            System.out.println(genericClientMessage+" : " + returnCode);
    }
}
