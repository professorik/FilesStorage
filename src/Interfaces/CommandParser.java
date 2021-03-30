package Interfaces;
/**
 * @author professorik
 * @created 30/03/2021 - 10:39
 * @project Server
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class CommandParser {

    protected enum COM{
        CD("CD"), COPY("COPY"), DEL("DEL"), DIR("DIR"), EXIT("EXIT"), FIND("FIND"),
        HELP("HELP"), MKDIR("MKDIR"), MOVE("MOVE"), REN("REN"), REPLACE("REPLACE"),
        RMDIR("RMDIR"), REG("REG"), LOG("LOG");
        private final String label;

        COM(String cmndName) {
            this.label = cmndName;
        }
    }

    protected DataInputStream inputStream;
    protected DataOutputStream outputStream;

    protected abstract void parseRequest(String com) throws IOException;

    protected abstract void parseResponse(String com);

    public CommandParser(DataInputStream inputStream, DataOutputStream outputStream) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }
}
