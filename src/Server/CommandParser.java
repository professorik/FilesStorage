package Server;

import Warnings.CallbackGenerator;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class CommandParser {

    private FileManager manager;

    protected CommandParser() {
        manager = new FileManager();
    }

    protected void parse(String com, DataInputStream inputStream, DataOutputStream outputStream) throws IOException {
        if (com.startsWith("cd ")) {
            com = com.substring(3);
            outputStream.writeUTF(CallbackGenerator.createMessage(manager.setPath(com)));
        } else if (com.startsWith("mkdir ")){
            com = com.substring(6);
            outputStream.writeUTF(CallbackGenerator.createMessage(manager.createFolder(com)));
        } else if (com.startsWith("reg")){
            outputStream.writeUTF(CallbackGenerator.createMessage(manager.register(com.split(" ")[1])));
        } else if (com.equals("exit")){
            System.out.println("User has disconnected");
        }else {
            outputStream.writeUTF(CallbackGenerator.createMessage(manager.receiveFile(com, inputStream)));
        }
    }
}
