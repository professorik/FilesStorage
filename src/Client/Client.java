package Client;

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
                parser.parseResponse(inputStream.readUTF());
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
/*
cd D:\IdeaProjects\ServerProject\src\folder1
mkdir folder10
C:\Users\Tony\Desktop\logo512_512.png
C:\Users\Tony\Desktop\14.png
 */