package Client;
/**
 * @author professorik
 * @created 30/03/2021 - 10:39
 * @project Server
 */
import Interfaces.FileManager;
import Warnings.CallbackGenerator;

import java.io.*;

public class ClientFileManager extends FileManager {

    private DataOutputStream outputStream;

    public ClientFileManager(DataOutputStream outputStream) {
        this.outputStream = outputStream;
        currentPath = "D:\\IdeaProjects\\ServerProject\\src\\downloads";//"D:\\Downloads";
    }

    protected void sendCommand(String command) throws IOException{
        outputStream.writeUTF(command);
    }

    public CallbackGenerator.Messages sendFile(String path, DataOutputStream outputStream) {
        try {
            outputStream.writeUTF("UPLOAD");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.sendFile(path, outputStream);
    }

    public CallbackGenerator.Messages receiveFile(String path, DataInputStream inputStream) {
        try {
            outputStream.writeUTF("DOWNLOAD " + path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.receiveFile(inputStream);
    }
}
