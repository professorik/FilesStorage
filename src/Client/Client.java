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
    private static boolean isSignedIn;
    private static String currentFolder;

    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(System.in);
        isSignedIn = false;
        CommandParser parser = new CommandParser();
        try {
            while (true) {
                System.out.println("Client is running...");
                socket = new Socket(host, port);
                outputStream = new DataOutputStream(socket.getOutputStream());
                inputStream = new DataInputStream(socket.getInputStream());
                String command = in.nextLine();
                if (command.equals("exit")){
                    exit();
                    break;
                }
                parser.parse(command);
                if (inputStream.readBoolean()) {
                    System.out.println("Status: OK");
                } else {
                    System.out.println("Status: SE");
                }
                inputStream.close();
                outputStream.close();
                socket.close();
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

    protected static void register(String nickname) throws IOException {
        outputStream.writeUTF(nickname);
    }

    protected static void setFolder(String path) throws IOException {
        outputStream.writeUTF(path);
    }

    protected static void sendFile(String path) throws IOException {
        int bytes = 0;
        File file = new File(path);
        outputStream.writeUTF(file.getName());
        FileInputStream fileInputStream = new FileInputStream(file);
        // send file size
        outputStream.writeLong(file.length());
        // break file into chunks
        byte[] buffer = new byte[4*1024];
        while ((bytes=fileInputStream.read(buffer))!=-1){
            outputStream.write(buffer,0,bytes);
            outputStream.flush();
        }
        fileInputStream.close();
    }

    //D:\IdeaProjects\ServerProject\src\folder1
    //C:\Users\Tony\Desktop\logo512_512.png
    //C:\Users\Tony\Desktop\14.png
}
