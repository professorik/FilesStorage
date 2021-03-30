package Client;

import java.io.*;

public class ClientFileManager {

    private DataOutputStream outputStream;

    public ClientFileManager(DataOutputStream outputStream) {
        this.outputStream = outputStream;
    }

    protected void sendCommand(String command) throws IOException{
        outputStream.writeUTF(command);
    }


    protected void sendFile(String path) throws IOException {
        int bytes;
        File file = new File(path);
        outputStream.writeUTF(file.getName());
        FileInputStream fileInputStream = new FileInputStream(file);
        outputStream.writeLong(file.length());
        byte[] buffer = new byte[4 * 1024];
        while ((bytes = fileInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytes);
            outputStream.flush();
        }
        fileInputStream.close();
    }
}
