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
    protected boolean parseRequest(String com) {
        String[] tempArr = com.split(" "); //FIXME: files can contain spaces
        String comName = tempArr[0].toUpperCase();
        com = com.substring(comName.length()).trim();
        CallbackGenerator.Messages message;
        if (COM.valueOf(comName).equals(COM.LOG) || COM.valueOf(comName).equals(COM.REG) || manager.isSignedIn){
            message = switch (COM.valueOf(comName)) {
                case CD -> manager.setPath(com);
                case MKDIR -> manager.createFolder(com);
                case REG -> manager.register(tempArr[1], tempArr[2], tempArr[3]);
                case UPLOAD -> manager.receiveFile(inputStream);
                case DOWNLOAD -> manager.sendFile(com, outputStream);
                case DIR -> manager.showDirectory(com, outputStream);
                case DEL -> manager.deleteFile(com);
                case RMDIR -> manager.deleteDir(com);
                case LOG -> manager.signIn(tempArr[1], tempArr[2]);
                case REN -> manager.renameFile(tempArr[1], tempArr[2]);
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
