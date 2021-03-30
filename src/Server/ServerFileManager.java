package Server;
/**
 * @author professorik
 * @created 30/03/2021 - 10:39
 * @project Server
 */
import Warnings.CallbackGenerator;

import java.io.*;

public class ServerFileManager {

    private String currentPath = "D:\\IdeaProjects\\ServerProject\\src\\folder1";

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

    protected CallbackGenerator.Messages receiveFile(String fileName, DataInputStream inputStream) {
        try {
            System.out.println("Start receiving...");
            int bytes = 0;
            FileOutputStream fileOutputStream = new FileOutputStream(currentPath + "/" + fileName);
            long size = inputStream.readLong();
            byte[] buffer = new byte[4 * 1024];
            while (size > 0 && (bytes = inputStream.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
                fileOutputStream.write(buffer, 0, bytes);
                size -= bytes;
            }
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return CallbackGenerator.Messages.FNFE;
        } catch (IOException e) {
            e.printStackTrace();
            return CallbackGenerator.Messages.SYS_ERR;
        }
        return CallbackGenerator.Messages.SUC;
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
