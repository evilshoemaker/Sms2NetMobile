package ru.digios.sms2net.core;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;

public class Utils {
    public static String concatStrings(Iterable<String> strings, String separator) {
        StringBuilder sb = new StringBuilder();
        String sep = "";
        for(String s: strings) {
            sb.append(sep).append(s);
            sep = separator;
        }
        return sb.toString();
    }

    public static String concatStringsIds(Iterable<Message> messages) {
        StringBuilder sb = new StringBuilder();
        String sep = "";
        for(Message message: messages) {
            sb.append(sep).append(String.valueOf(message.getId()));
            sep = ",";
        }
        return sb.toString();
    }

    public static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        }
        catch (Exception ex) {

        }
    }
}
