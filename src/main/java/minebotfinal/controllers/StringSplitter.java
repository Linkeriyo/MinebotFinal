package minebotfinal.controllers;

import java.util.ArrayList;
import java.util.List;

public class StringSplitter {
    char[] chars;
    boolean insideQuote;
    List<String> stringList = new ArrayList<>();
    StringBuilder wordbuffer = new StringBuilder();

    public StringSplitter(String input) {
        this.chars = input.toCharArray();
        this.insideQuote = false;
    }

    public List<String> split() {
        for (char aChar : chars) {
            if (aChar == '"') {
                insideQuote = !insideQuote;
                if (wordbuffer.length() > 0) {
                    stringList.add(wordbuffer.toString());
                    wordbuffer.setLength(0);
                }
            } else if (insideQuote) {
                wordbuffer.append(aChar);
            } else {
                if (aChar == ' ') {
                    if (wordbuffer.length() > 0) {
                        stringList.add(wordbuffer.toString());
                        wordbuffer.setLength(0);
                    }
                } else {
                    wordbuffer.append(aChar);
                }
            }
        }
        stringList.add(wordbuffer.toString());
        return stringList;
    }

}
