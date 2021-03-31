package Client;
/**
 * @author professorik
 * @created 30/03/2021 - 10:39
 * @project Server
 */
import Interfaces.CommandParser;
import Warnings.CallbackGenerator;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ClientParser extends CommandParser {

    private ClientFileManager manager;

    public ClientParser(DataInputStream inputStream, DataOutputStream outputStream) {
        super(inputStream, outputStream);
        manager = new ClientFileManager(super.outputStream);
    }

    @Override
    protected void parseRequest(String command) throws IOException {
        String comName = command.split(" ")[0];
        String com = command.substring(comName.length()).trim();
        switch (COM.valueOf(comName)){
            case UPLOAD -> manager.sendFile(com, outputStream);
            case DOWNLOAD -> manager.receiveFile(com, inputStream);
            case DIR -> manager.lookDirInfo(command, inputStream);
            default -> manager.sendCommand(command);
        }
    }

    @Override
    protected void parseResponse(String resp) {
        try {
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(resp);
            String key = jsonObject.keySet().iterator().next().toString();
            CallbackGenerator.displayMessage(CallbackGenerator.Messages.valueOf((String) jsonObject.get(key)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
/*
CD D:\IdeaProjects\ServerProject\src\folder1
MKDIR folder10
UPLOAD C:\Users\Tony\Desktop\logo512_512.png
DOWNLOAD D:\IdeaProjects\ServerProject\src\folder1\logo512_512.png
UPLOAD C:\Users\Tony\Desktop\14.png
 */
