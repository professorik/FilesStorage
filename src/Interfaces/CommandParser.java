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

    protected enum COM {
        CD("Set the current folder with the path."),
        COPY("Copy the selected file into the current folder."),
        DEL("Delete the selected file."),
        DIR("Show all files in the selected folder."),
        EXIT("Hide terminal."),
        FIND("Find file."),
        HELP("Show all permissible commands."),
        MKDIR("Create new folder in the current directory."),
        MOVE("Move file to another folder"),
        REN("Rename the selected folder."),
        UPLOAD("Upload file to the selected folder."),
        RMDIR("Delete the selected folder"),
        REG("Sign up"),
        LOG("Sign in"),
        DOWNLOAD("Download the selected file.");
        private final String descrip;

        COM(String descrip) {
            this.descrip = descrip;
        }

        public void getInfo(StringBuilder str){
            str.append(name()).append(" - ").append(descrip).append("\n");
        }
    }

    protected String showCommands(){
        StringBuilder res = new StringBuilder();
        for (COM i : COM.values()) {
            i.getInfo(res);
        }
        return res.toString();
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
