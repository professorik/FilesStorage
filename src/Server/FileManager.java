package Server;

import java.io.*;

public class FileManager {

    private String currentPath = "D:\\IdeaProjects\\ServerProject\\src\\folder1";

    protected void setPath(String currentPath) {
        this.currentPath = currentPath;
    }

    protected boolean createFolder(String path) {
        File theDir = new File(path);
        if (!theDir.exists()) {
            theDir.mkdirs();
            return true;
        }
        return false;
    }

    protected boolean receiveFile(String fileName, DataInputStream inputStream) {
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
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            return true;
        }
    }

    protected boolean register(String nickname){
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
