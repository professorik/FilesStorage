package Warnings;

import org.json.simple.JSONObject;

public class CallbackGenerator {

    public enum Status {SUC,ERR}

    public enum Messages {
        SUC("Success", Status.SUC),
        DIR_EXST("The directory is exist.", Status.ERR),
        NO_DIR("This directory not found.", Status.ERR),
        SYS_ERR("Some error.", Status.ERR),
        NO_MEM("There is no free memory.", Status.ERR),
        USR_EXST("This nickname is exist.", Status.ERR),
        FNFE("File not found.", Status.ERR);
        private final String label;
        private final Status type;

        Messages(String label, Status type) {
            this.label = label;
            this.type = type;
        }
    }

    public static String createMessage(Messages mes) {
        JSONObject res = new JSONObject();
        res.put(mes.type, mes.toString());
        return res.toString();
    }

    public static void displayMessage(Messages message) {
        if (message.type == Status.SUC){
            System.out.println(ConsoleColors.GREEN_BOLD + message.label + ConsoleColors.RESET);
        }else{
            System.out.println(ConsoleColors.RED + message.label + ConsoleColors.RESET);
        }
    }
}
