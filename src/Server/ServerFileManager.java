package Server;
/**
 * @author professorik
 * @created 30/03/2021 - 10:39
 * @project Server
 */
import Interfaces.FileManager;
import Warnings.CallbackGenerator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.*;
import java.sql.Date;
import java.sql.Time;

public class ServerFileManager extends FileManager {

    public ServerFileManager() {
        currentPath = "D:\\IdeaProjects\\ServerProject\\src\\folder1";
    }

    protected CallbackGenerator.Messages setPath(String currentPath) {
        File theDir = new File(currentPath);
        if (theDir.exists()) {
            this.currentPath = currentPath;
            return CallbackGenerator.Messages.SUC;
        }
        return CallbackGenerator.Messages.NO_DIR;
    }

    protected CallbackGenerator.Messages createFolder(String dirName) {
        File theDir = new File(currentPath + "/" + dirName);
        if (!theDir.exists()) {
            theDir.mkdirs();
            return CallbackGenerator.Messages.SUC;
        }
        return CallbackGenerator.Messages.DIR_EXST;
    }

    protected CallbackGenerator.Messages showDirectory(String path, DataOutputStream outputStream){
        File theDir = new File(path);
        if (theDir.exists() && theDir.isDirectory()) {
            JSONArray res = new JSONArray();
            JSONObject fileInfo;
            for (File i: theDir.listFiles()){
                fileInfo = new JSONObject();
                fileInfo.put("isDir", i.isDirectory());
                fileInfo.put("name", i.getName());
                fileInfo.put("time", i.lastModified());
                res.add(fileInfo);
            }
            try {
                outputStream.writeUTF(res.toString());
            } catch (IOException e) {
                e.printStackTrace();
                return CallbackGenerator.Messages.SYS_ERR;
            }
            return CallbackGenerator.Messages.SUC;
        }
        return CallbackGenerator.Messages.NO_DIR;
    }

    //FIXME
    protected CallbackGenerator.Messages register(String nickname){
        String folder = null;
        for (char i = 'E'; i < 'Z'; ++i) {
            if (new File(i+":").exists()){
                if (new File(i+":\\"+nickname).exists()){
                    return CallbackGenerator.Messages.USR_EXST;
                }else if (new File(i+":\\").getFreeSpace() > 1024*1024*1024f) {
                    folder = i + ":\\" + nickname;
                }
            }
        }
        if (folder != null){
            File file = new File(folder);
            file.mkdirs();
            currentPath = folder;
            return CallbackGenerator.Messages.SUC;
        }
        return CallbackGenerator.Messages.NO_MEM;
    }
}
