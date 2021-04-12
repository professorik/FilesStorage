package Client;
/**
 * @author professorik
 * @created 30/03/2021 - 10:39
 * @project Server
 */

import Abstracts.CommandParser;
import Warnings.CallbackGenerator;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientParser extends CommandParser {

    private ClientFileManager manager;
    private Socket socket;

    public ClientParser(DataInputStream inputStream, DataOutputStream outputStream, Socket socket) {
        super(inputStream, outputStream);
        this.socket = socket;
        manager = new ClientFileManager(super.outputStream);
    }

    @Override
    public boolean parseRequest(String command) throws IOException {
        String comName = command.split(" ")[0].toUpperCase();
        String com = command.substring(comName.length()).trim();
        try {
            return switch (COM.valueOf(comName)) {
                case UPLOAD -> manager.sendFileToServer(com, outputStream);
                case DOWNLOAD -> manager.receiveFile(com, inputStream);
                case DIR -> manager.lookDirInfo(command, inputStream);
                case HELP -> manager.printHelp(showCommands());
                case EXIT -> manager.exit(inputStream, outputStream, socket);
                default -> manager.sendCommand(command);
            };
        }catch (IllegalArgumentException e){
            CallbackGenerator.displayMessage(CallbackGenerator.Messages.UNKNOWN);
            return false;
        }
    }

    @Override
    public boolean parseResponse(String resp) {
        try {
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(resp);
            String key = jsonObject.keySet().iterator().next().toString();
            return CallbackGenerator.displayMessage(CallbackGenerator.Messages.valueOf((String) jsonObject.get(key)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
}
/*
CD D:\IdeaProjects\ServerProject\src\folder1
DEL D:\IdeaProjects\ServerProject\src\folder1\folder2\14.png
RMDIR D:\IdeaProjects\ServerProject\src\folder1\folder2
MKDIR folder10
UPLOAD C:\Users\Tony\Desktop\logo512_512.png
DOWNLOAD D:\IdeaProjects\ServerProject\src\folder1\logo512_512.png
 */