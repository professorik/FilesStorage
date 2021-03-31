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
        String comName = com.split(" ")[0].toUpperCase();
        com = com.substring(comName.length()).trim();
        CallbackGenerator.Messages message = switch (COM.valueOf(comName)){
            case CD -> manager.setPath(com);
            case MKDIR -> manager.createFolder(com);
            case REG ->  manager.register(com);
            case UPLOAD -> manager.receiveFile(inputStream);
            case DOWNLOAD -> manager.sendFile(com, outputStream);
            case DIR -> manager.showDirectory(com, outputStream);
            case DEL -> manager.deleteFile(com);
            case RMDIR -> manager.deleteDir(com);
            default -> CallbackGenerator.Messages.UNKNOWN;
        };
        parseResponse(CallbackGenerator.createMessage(message));
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
