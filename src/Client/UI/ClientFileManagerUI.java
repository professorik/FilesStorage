package Client.UI;
/**
 * @author professorik
 * @created 30/03/2021 - 10:39
 * @project Server
 */

import Abstracts.FileManager;
import Warnings.CallbackGenerator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;
import java.util.Iterator;

public class ClientFileManagerUI extends FileManager {

    private Socket socket;
    private DataOutputStream outputStream;
    private DataInputStream inputStream;

    public ClientFileManagerUI(DataInputStream inputStream, DataOutputStream outputStream, Socket socket) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.socket = socket;
        currentPath = "D:\\IdeaProjects\\ServerProject\\src\\downloads";//"D:\\Downloads";
    }

    protected boolean sendCommand(String command) throws IOException {
        outputStream.writeUTF(command);
        return true;
    }

    protected JSONArray lookDirInfo() throws IOException {
        sendCommand("DIR");
        String res;
        boolean fl = false;
        try {
            Object tempObj;
            do {
                fl = !fl;
                res = inputStream.readUTF();
                tempObj = new JSONParser().parse(res);
                if (tempObj instanceof JSONObject){
                    JSONObject jsonObject = (JSONObject) tempObj;
                    String key = jsonObject.keySet().iterator().next().toString();
                    if (!CallbackGenerator.displayMessage(CallbackGenerator.Messages.valueOf((String) jsonObject.get(key)))){
                        return null;
                    }
                }
            } while (!(tempObj instanceof JSONArray));
            return (JSONArray) tempObj;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean sendFileToServer(String path, DataOutputStream outputStream) {
        try {
            outputStream.writeUTF("UPLOAD");
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.sendFile(path, outputStream);
        return true;
    }

    public boolean receiveFile(String path, DataInputStream inputStream) {
        try {
            outputStream.writeUTF("DOWNLOAD " + path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.receiveFile(inputStream);
        return true;
    }

    public boolean printHelp(String res) {
        System.out.println(res);
        return false;
    }

    protected boolean exit() {
        try {
            inputStream.close();
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
