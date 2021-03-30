package Interfaces;

import Warnings.CallbackGenerator;

import java.io.*;

/**
 * @author professorik
 * @created 30/03/2021 - 14:13
 * @project Server
 */
public abstract class FileManager {

    protected String currentPath;

    public CallbackGenerator.Messages receiveFile(DataInputStream inputStream) {
        try {
            System.out.println("Start receiving...");
            int bytes = 0;
            String fileName = inputStream.readUTF();
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

    public CallbackGenerator.Messages sendFile(String path, DataOutputStream outputStream) {
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
            return CallbackGenerator.Messages.SYS_ERR;
        }
        return CallbackGenerator.Messages.SUC;
    }
}
