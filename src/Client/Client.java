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
    private static String host = "localhost";//"192.168.0.228";
    private static int port = 3333;
    private static Socket socket;
    private static DataOutputStream outputStream;
    private static DataInputStream inputStream;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        try {
            System.out.println("Client is running...");
            socket = new Socket(host, port);
            outputStream = new DataOutputStream(socket.getOutputStream());
            inputStream = new DataInputStream(socket.getInputStream());
            ClientParser parser = new ClientParser(inputStream, outputStream);
            while (true) {
                String command = in.nextLine();
                if (command.equals("exit")) {
                    exit();
                    break;
                }
                parser.parseRequest(command);
                //FIXME
                if (!command.toLowerCase().equals("help")) {
                    parser.parseResponse(inputStream.readUTF());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected static void exit() throws IOException {
        outputStream.writeUTF("exit");
        inputStream.close();
        outputStream.close();
        socket.close();
    }
}