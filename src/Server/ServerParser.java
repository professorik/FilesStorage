package Server;
/**
 * @author professorik
 * @created 30/03/2021 - 10:39
 * @project Server
 */
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
        String comName = com.split(" ")[0];
        com = com.substring(comName.length()+1);
        switch (COM.valueOf(comName)){
            case CD -> parseResponse(CallbackGenerator.createMessage(manager.setPath(com)));
            case MKDIR -> parseResponse(CallbackGenerator.createMessage(manager.createFolder(com)));
            case REG ->  parseResponse(CallbackGenerator.createMessage(manager.register(com)));
            case REPLACE -> parseResponse(CallbackGenerator.createMessage(manager.receiveFile(com, inputStream)));
            default -> parseRequest(CallbackGenerator.createMessage(CallbackGenerator.Messages.UNKNOWN));
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
