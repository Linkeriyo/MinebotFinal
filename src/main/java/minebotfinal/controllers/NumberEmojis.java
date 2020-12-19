package minebotfinal.controllers;

import java.util.ArrayList;
import java.util.List;

public class NumberEmojis {

    public static final String ZERO = ":zero:";
    public static final String ONE = ":one:";
    public static final String TWO = ":two:";
    public static final String THREE = ":three:";
    public static final String FOUR = ":four:";
    public static final String FIVE = ":five:";
    public static final String SIX = ":six:";
    public static final String SEVEN = ":seven:";
    public static final String EIGHT = ":eight:";
    public static final String NINE = ":nine:";

    public static String getDigitEmoji(char n) {
        switch (n) {
            case '0':
                return ZERO;
            case '1':
                return ONE;
            case '2':
                return TWO;
            case '3':
                return THREE;
            case '4':
                return FOUR;
            case '5':
                return FIVE;
            case '6':
                return SIX;
            case '7':
                return SEVEN;
            case '8':
                return EIGHT;
            case '9':
                return NINE;
            default:
                return "";
        }
    }

    public static List<String> getNumberEmoji(int n) {
        char[] num = String.valueOf(n).toCharArray();
        List<String> emojis = new ArrayList<>();
        for (char c : num) {
            emojis.add(getDigitEmoji(c));
        }
        return emojis;
    }
}
