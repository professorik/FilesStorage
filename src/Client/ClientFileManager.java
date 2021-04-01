package Client;
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

import java.io.*;
import java.net.Socket;
import java.util.Date;
import java.util.Iterator;

public class ClientFileManager extends FileManager {

    private DataOutputStream outputStream;

    public ClientFileManager(DataOutputStream outputStream) {
        this.outputStream = outputStream;
        currentPath = "D:\\IdeaProjects\\ServerProject\\src\\downloads";//"D:\\Downloads";
    }

    protected boolean sendCommand(String command) throws IOException{
        outputStream.writeUTF(command);
        return true;
    }

    protected boolean lookDirInfo(String com, DataInputStream inputStream) throws IOException {
        sendCommand(com);
        String res = inputStream.readUTF();
        try {
            JSONArray slideContent = (JSONArray) new JSONParser().parse(res);
            Iterator i = slideContent.iterator();
            while (i.hasNext()) {
                JSONObject file = (JSONObject) i.next();
                long lastMod = (long)file.get("time");
                System.out.print(new Date(lastMod));
                if ((boolean)file.get("isDir")){
                    System.out.print("\t<DIR>\t");
                }else{
                    System.out.print("\t\t\t");
                }
                System.out.println(file.get("name"));
            }
        }catch (ParseException e){
            e.printStackTrace();
        }
        return true;
    }

    public boolean sendFile(String path, DataOutputStream outputStream, boolean isClient) { //FIXME
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

    public boolean exit(DataInputStream inputStream, DataOutputStream outputStream, Socket socket) {
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
