package Server;
/**
 * @author professorik
 * @created 30/03/2021 - 10:39
 * @project Server
 */

import Abstracts.FileManager;
import Warnings.CallbackGenerator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.*;
import java.util.Arrays;
import java.util.stream.Collectors;

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

    protected CallbackGenerator.Messages deleteFile(String path) {
        File fileTD = new File(path);
        if (fileTD.exists() && fileTD.isFile()) {
            fileTD.delete();
            return CallbackGenerator.Messages.SUC;
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
        if (fileTD.exists() && fileTD.isDirectory()) {
            deleteDirectory(path);
            return CallbackGenerator.Messages.SUC;
        }
        return CallbackGenerator.Messages.FNFE;
    }

    protected CallbackGenerator.Messages showDirectory(String path, DataOutputStream outputStream) {
        File theDir = new File(path);
        if (theDir.exists() && theDir.isDirectory()) {
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
        return CallbackGenerator.Messages.NO_DIR;
    }

    //TODO: test
    protected CallbackGenerator.Messages register(String nickname) {
        String folder = null;
        double minFreeSpace = 1024 * 1024 * 1024d; //1Gb
        for (File root: Arrays.stream(File.listRoots()).filter(file -> file.getFreeSpace()>minFreeSpace).collect(Collectors.toList())){
            System.out.println(root.toString());
            if (Arrays.stream(root.listFiles()).filter(file -> file.getName().equals(nickname)).count() > 0) {
                return CallbackGenerator.Messages.USR_EXST;
            }
            folder = root.toString() + nickname;
        }
        if (folder != null) {
            File file = new File(folder);
            file.mkdirs();
            currentPath = folder;
            return CallbackGenerator.Messages.SUC;
        }
        return CallbackGenerator.Messages.NO_MEM;
    }
}
