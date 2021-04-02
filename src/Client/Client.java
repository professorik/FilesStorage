package Client;
/**
 * @author professorik
 * @created 30/03/2021 - 10:39
 * @project Server
 */

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private final static String host = "localhost";//"192.168.0.228";
    private static int port = 3333;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Socket socket;
        DataOutputStream outputStream;
        DataInputStream inputStream;
        try {
            System.out.println("Client is running...");
            socket = new Socket(host, port);
            outputStream = new DataOutputStream(socket.getOutputStream());
            inputStream = new DataInputStream(socket.getInputStream());
            ClientParser parser = new ClientParser(inputStream, outputStream, socket);
            while (!socket.isClosed()) {
                if (parser.parseRequest(in.nextLine())) {
                    parser.parseResponse(inputStream.readUTF());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}