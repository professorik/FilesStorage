package Server;

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
            outputStream.writeBoolean(manager.createFolder(com));
            manager.setPath(com);
        } else if (com.startsWith("reg")){
            outputStream.writeBoolean(manager.register(com.split(" ")[1]));
        } else if (com.equals("exit")){
            System.out.println("User has disconnected");
        }else {
            outputStream.writeBoolean(manager.receiveFile(com, inputStream));
        }
    }
}
