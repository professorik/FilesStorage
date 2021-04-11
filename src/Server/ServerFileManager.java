package Server;
/**
 * @author professorik
 * @created 30/03/2021 - 10:39
 * @project Server
 */

import Abstracts.FileManager;
import Server.DB.DBController;
import Warnings.CallbackGenerator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.*;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ServerFileManager extends FileManager {

    private DBController dbCon;
    private String userName;
    protected boolean isSignedIn;

    public ServerFileManager() {
        dbCon = new DBController();
        isSignedIn = false;
    }

    protected CallbackGenerator.Messages signIn(String name, String password){
        isSignedIn = dbCon.userExist(name, password);
        if (isSignedIn){
            userName = name;
            currentPath = dbCon.getPath(userName);
        }else{
            userName = null;
            currentPath = null;
            return CallbackGenerator.Messages.NO_USR;
        }
        return CallbackGenerator.Messages.SUC;
    }

    //TODO: check if the user is trying to navigate to another user's folder
    //      - parse a query more flexible
    //      - move back
    protected CallbackGenerator.Messages setPath(String currentPath) {
        if (currentPath.endsWith("\\")){
            currentPath = currentPath.substring(0, currentPath.length()-1);
        }
        File theDir = new File(currentPath);
        if (theDir.exists()) {
            this.currentPath = currentPath;
            dbCon.updatePath(userName, currentPath);
            return CallbackGenerator.Messages.SUC;
        }else if (!currentPath.startsWith(this.currentPath)) {
            return setPath(this.currentPath + "\\" + currentPath);
        }
        return CallbackGenerator.Messages.NO_DIR;
    }

    protected CallbackGenerator.Messages createFolder(String dirName) {
        File theDir = new File(currentPath + "\\" + dirName);
        if (!theDir.exists()) {
            theDir.mkdirs();
            return CallbackGenerator.Messages.SUC;
        }
        return CallbackGenerator.Messages.DIR_EXST;
    }

    protected CallbackGenerator.Messages deleteFile(String path) {
        File fileTD = new File(path);
        if (fileTD.exists()) {
            if (fileTD.isFile()) {
                fileTD.delete();
                return CallbackGenerator.Messages.SUC;
            }
        }else if (!path.startsWith(this.currentPath)) {
            return deleteFile(this.currentPath + "\\" + path);
        }
        return CallbackGenerator.Messages.FNFE;
    }

    private void deleteDirectory(String path){
        File dir = new File(path);
        for (File file: dir.listFiles()){
            if (file.isDirectory()){
                deleteDirectory(file.getAbsolutePath());
            }
            file.delete();
        }
        dir.delete();
    }

    protected CallbackGenerator.Messages deleteDir(String path) {
        File fileTD = new File(path);
        if (fileTD.exists()){
            if (fileTD.isDirectory()) {
                deleteDirectory(path);
                return CallbackGenerator.Messages.SUC;
            }
        }else if (!path.startsWith(this.currentPath)) {
            return deleteDir(this.currentPath + "\\" + path);
        }
        return CallbackGenerator.Messages.FNFE;
    }

    protected CallbackGenerator.Messages showDirectory(String path, DataOutputStream outputStream) {
        File theDir = new File(path);
        if (theDir.exists()){
            if (theDir.isDirectory()) {
                JSONArray res = new JSONArray();
                JSONObject fileInfo;
                for (File i : theDir.listFiles()) {
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
        }else if (!path.startsWith(this.currentPath)) {
            return showDirectory(this.currentPath + "\\" + path, outputStream);
        }
        return CallbackGenerator.Messages.NO_DIR;
    }

    private boolean isPasswordsValid(String pass1, String pass2){
        //\\w+ - symbols, numbers and underline
        if (pass1.equals(pass2) && pass1.length() > 3 && pass1.matches("\\w+")){
            return true;
        }
        return false;
    }

    protected CallbackGenerator.Messages changePassword(String pass1, String pass2){
        if (isPasswordsValid(pass1, pass2)){
            dbCon.updatePassword(userName, pass1);
        }
        return CallbackGenerator.Messages.BAD_PAS;
    }

    protected CallbackGenerator.Messages register(String nickname, String pass1, String pass2){
        if (isPasswordsValid(pass1, pass2)) {
            return register(nickname, pass1);
        }
        return CallbackGenerator.Messages.BAD_PAS;
    }

    private CallbackGenerator.Messages register(String nickname, String password) {
        if (dbCon.userExist(nickname)){
            return CallbackGenerator.Messages.USR_EXST;
        }
        String folder = null;
        double minFreeSpace = 1024 * 1024 * 1024d; //1Gb
        for (File root: Arrays.stream(File.listRoots()).filter(file -> file.getFreeSpace()>minFreeSpace).collect(Collectors.toList())){
            if (Arrays.stream(root.listFiles()).filter(file -> file.getName().equals(nickname)).count() > 0) {
                return CallbackGenerator.Messages.SYS_ERR; //protecting from another users folders
            }
            folder = root.toString() + nickname;
        }
        if (folder != null) {
            File file = new File(folder);
            file.mkdirs();
            this.currentPath = folder;
            this.userName = nickname;
            isSignedIn = true;
            dbCon.registerUser(nickname, password, currentPath);
            return CallbackGenerator.Messages.SUC;
        }
        return CallbackGenerator.Messages.NO_MEM;
    }

    protected CallbackGenerator.Messages delete(String nickname, String password){
        if (dbCon.userExist(nickname, password)){
            dbCon.delete(nickname);
            return CallbackGenerator.Messages.SUC;
        }
        return CallbackGenerator.Messages.NLG;
    }
}
