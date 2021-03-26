package Client;

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
}
