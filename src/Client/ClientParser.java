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
        String com = command.substring(comName.length()+1);
        switch (COM.valueOf(comName)){
            case REPLACE -> manager.sendFile(com);
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
