package Client;


import Warnings.CallbackGenerator;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class CommandParser {
    protected void parseRequest(String command) throws IOException {
        if (command.startsWith("cd ")){
            Client.setFolder(command);
        }else if (command.startsWith("mkdir ")){
            Client.mkFolder(command);
        }else if (command.startsWith("reg")){
            Client.register(command);
        }else{
            Client.sendFile(command);
        }
    }

    protected void parseResponse(String resp) throws ParseException {
        JSONObject jsonObject = (JSONObject) new JSONParser().parse(resp);
        String key = jsonObject.keySet().iterator().next().toString();
        CallbackGenerator.displayMessage(CallbackGenerator.Messages.valueOf((String) jsonObject.get(key)));
    }
}
