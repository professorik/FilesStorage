package Server;
/**
 * @author professorik
 * @created 30/03/2021 - 10:39
 * @project Server
 */
import Abstracts.CommandParser;
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
    protected boolean parseRequest(String com) throws IOException {
        String comName = com.split(" ")[0].toUpperCase();
        com = com.substring(comName.length()).trim();
        CallbackGenerator.Messages message;
        if (COM.valueOf(comName).equals(COM.LOG) || COM.valueOf(comName).equals(COM.REG) || manager.isSignedIn){
            message = switch (COM.valueOf(comName)) {
                case CD -> manager.setPath(com);
                case MKDIR -> manager.createFolder(com);
                case REG -> manager.register(com.split(" ")[0], com.split(" ")[1], com.split(" ")[2]);
                case UPLOAD -> manager.receiveFile(inputStream);
                case DOWNLOAD -> manager.sendFile(com, outputStream);
                case DIR -> manager.showDirectory(com, outputStream);
                case DEL -> manager.deleteFile(com);
                case RMDIR -> manager.deleteDir(com);
                case LOG -> manager.signIn(com.split(" ")[0], com.split(" ")[1]);
                default -> CallbackGenerator.Messages.UNKNOWN;
            };
        }else{
            message = CallbackGenerator.Messages.SYS_ERR;
        }
        parseResponse(CallbackGenerator.createMessage(message));
        return true;
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
