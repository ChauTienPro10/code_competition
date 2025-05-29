package myself.programing.coding.utils;

public class HandleStringUtils {
    /**
     *
     * @param input
     * @return String
     */
    public static String getRightPartAfterFirstBracket(String input) {
        int index = input.indexOf("]");
        if (index != -1 && index < input.length() - 1) {
            return input.substring(index + 1).trim();
        } else {
            return input;
        }
    }
}
