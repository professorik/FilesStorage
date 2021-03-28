import java.awt.*;

public class CallbackGenerator {

    public enum Status {
        SUC("Success"), ERR("Error");
        private final String type;

        Status(String type) {
            this.type = type;
        }

        private String getDisplayableType() {
            return type;
        }
    }

    public enum Messages {
        SUC("Success", Status.SUC),
        DIR_EXST("The directory is exist.", Status.ERR),
        SYS_ERR("Some error.", Status.ERR),
        FNFE("File not found.", Status.ERR);
        private final String label;
        private final Status type;

        Messages(String label, Status type) {
            this.label = label;
            this.type = type;
        }

        private String getDisplayableType() {
            return type.getDisplayableType();
        }

        public Status getType() {
            return type;
        }

        public String getLabel() {
            return label;
        }
    }

    public static void main(String[] args) {
        for (Messages mes : Messages.values()) {
            displayMessage(mes);
        }
    }

    private static void displayMessage(Messages message) {
        if (message.getType() == Status.SUC){
            System.out.println(ConsoleColors.GREEN_BOLD + message.getLabel() + ConsoleColors.RESET);
        }else{
            System.out.println(ConsoleColors.RED + message.getLabel() + ConsoleColors.RESET);
        }
    }
}
