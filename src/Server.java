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
        private String path = "E:/professorik";

        public ServerConnectionProcessor(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                inputStream = new DataInputStream(socket.getInputStream());
                outputStream = new DataOutputStream(socket.getOutputStream());
                String com = inputStream.readUTF();
                if (com.startsWith("cd ")) {
                    com = com.substring(3);
                    outputStream.writeBoolean(createFolder(com));
                    path = com;
                } else if (com.startsWith("reg")){
                    outputStream.writeBoolean(register(com.split(" ")[1]));
                } else {
                    outputStream.writeBoolean(receiveFile(com));
                }
                sleep(1000);
                inputStream.close();
                outputStream.close();
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private boolean createFolder(String path) {
            File theDir = new File(path);
            if (!theDir.exists()) {
                theDir.mkdirs();
                return true;
            }
            return false;
        }

        private boolean receiveFile(String fileName) {
            try {
                System.out.println("Start receiving...");
                int bytes = 0;
                FileOutputStream fileOutputStream = new FileOutputStream(path + "/" + fileName);
                long size = inputStream.readLong();
                byte[] buffer = new byte[4 * 1024];
                while (size > 0 && (bytes = inputStream.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
                    fileOutputStream.write(buffer, 0, bytes);
                    size -= bytes;
                }
                fileOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            } finally {
                return true;
            }
        }

        private boolean register(String nickname){
            String folder = null;
            for (char i = 'E'; i < 'Z'; ++i) {
                if (new File(i+":").exists()){
                    if (new File(i+":\\"+nickname).exists()){
                        return false;
                    }else if (new File(i+":\\").getFreeSpace() > 1024*1024*1024f) {
                        folder = i + ":\\" + nickname;
                    }
                }
            }
            if (folder != null){
                File file = new File(folder);
                file.mkdirs();
                return true;
            }
            return false;
        }
    }
}
