package Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static int port = 3333;

    public static void main(String[] args) {
        try {
            System.out.println("Server is running...");
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                Socket socket = serverSocket.accept();
                ServerConnectionProcessor processor = new ServerConnectionProcessor(socket);
                processor.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class ServerConnectionProcessor extends Thread {
        private Socket socket;
        private DataOutputStream outputStream = null;
        private DataInputStream inputStream = null;
        private CommandParser parser;

        public ServerConnectionProcessor(Socket socket) {
            this.socket = socket;
            parser = new CommandParser();
        }

        @Override
        public void run() {
            try {
                inputStream = new DataInputStream(socket.getInputStream());
                outputStream = new DataOutputStream(socket.getOutputStream());
                parser.parse(inputStream.readUTF(), inputStream, outputStream); //sleep(1000);
                inputStream.close();
                outputStream.close();
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
