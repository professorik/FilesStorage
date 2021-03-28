package Client;


import org.json.simple.JSONObject;

import java.io.IOException;

public class CommandParser {
    protected void parse(String command) throws IOException {
        if (command.startsWith("cd ")){
            Client.setFolder(command);
        }else if (command.startsWith("reg")){
            Client.register(command);
        }else{
            Client.sendFile(command);
        }
    }

    protected String getCommand(String command){
        JSONObject jsonObject = new JSONObject();
        //jsonObject.put("client", new JSONObject().put("register"));
        return jsonObject.toString();
    }
}
