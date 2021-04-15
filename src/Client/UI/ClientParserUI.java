package Client.UI;
/**
 * @author professorik
 * @created 30/03/2021 - 10:39
 * @project Server
 */

import Warnings.CallbackGenerator;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.DataInputStream;
import java.io.IOException;

public class ClientParserUI {

    private DataInputStream inputStream;

    public ClientParserUI(DataInputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String parseResp() {
        try {
            String resp = inputStream.readUTF();
            System.out.println(resp);
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(resp);
            String key = jsonObject.keySet().iterator().next().toString();
            return CallbackGenerator.getErrorMessage(CallbackGenerator.Messages.valueOf((String) jsonObject.get(key)));
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}