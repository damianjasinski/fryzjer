package client;

public class FormatValidator {
    static boolean validateFormatTime(String input) throws NumberFormatException {
        int num = Integer.parseInt(input);
        return num >= 10 && num <= 18;
    }
}
