package Client;
/**
 * @author professorik
 * @created 30/03/2021 - 10:39
 * @project Server
 */
import Interfaces.FileManager;
import Warnings.CallbackGenerator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.sql.Time;
import java.util.Date;
import java.util.Iterator;

public class ClientFileManager extends FileManager {

    private DataOutputStream outputStream;

    public ClientFileManager(DataOutputStream outputStream) {
        this.outputStream = outputStream;
        currentPath = "D:\\IdeaProjects\\ServerProject\\src\\downloads";//"D:\\Downloads";
    }

    protected void sendCommand(String command) throws IOException{
        outputStream.writeUTF(command);
    }

    protected void lookDirInfo(String com, DataInputStream inputStream) throws IOException {
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
