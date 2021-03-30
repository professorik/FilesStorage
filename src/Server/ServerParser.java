package Server;

import Interfaces.CommandParser;
import Warnings.CallbackGenerator;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ServerParser extends CommandParser {

    private ServerFileManager manager;

    public ServerParser(DataInputStream inputStream, DataOutputStream outputStream) {
        super(inputStream, outputStream);
        manager = new ServerFileManager();
    }

    @Override
    protected void parseRequest(String com) {
        if (com.startsWith("cd ")) {
            com = com.substring(3);
            parseResponse(CallbackGenerator.createMessage(manager.setPath(com)));
        } else if (com.startsWith("mkdir ")) {
            com = com.substring(6);
            parseResponse(CallbackGenerator.createMessage(manager.createFolder(com)));
        } else if (com.startsWith("reg")) {
            parseResponse(CallbackGenerator.createMessage(manager.register(com.split(" ")[1])));
        } else {
            parseResponse(CallbackGenerator.createMessage(manager.receiveFile(com, inputStream)));
        }
    }

    @Override
    protected void parseResponse(String com) {
        try {
            outputStream.writeUTF(com);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
