package Server;
/**
 * @author professorik
 * @created 30/03/2021 - 10:39
 * @project Server
 */
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
        private ServerParser parser;

        public ServerConnectionProcessor(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                inputStream = new DataInputStream(socket.getInputStream());
                outputStream = new DataOutputStream(socket.getOutputStream());
                parser = new ServerParser(inputStream, outputStream);
                String s = inputStream.readUTF();
                while (true) {
                    parser.parseRequest(s);
                    try {
                        s = inputStream.readUTF();
                    }catch (EOFException e){
                        break;
                    }
                }
                System.out.println("User has disconnected");
                inputStream.close();
                outputStream.close();
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
