package ua.lazareva.loganalyzer.service;

import ua.lazareva.loganalyzer.entity.HttpMethod;
import ua.lazareva.loganalyzer.entity.LogToken;

import java.io.*;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogAnalyzer {

    public ArrayList<LogToken> getLogWithFilter(String path, LocalDateTime timeFrom, LocalDateTime timeTo) {
        File file = new File(path);
        validateInput(file);
        ArrayList<LogToken> collection = parseFile(file);
        for (int i = collection.size() - 1; i >= 0; i--) {
            if (collection.get(i).getDateTime().compareTo(timeFrom) < 0 || collection.get(i).getDateTime().compareTo(timeTo) > 0) {
                collection.remove(i);
            }
        }
        return collection;
    }

    private ArrayList<LogToken> parseFile(File file) {
        ArrayList<LogToken> collection = new ArrayList<>();
        try {
            FileInputStream inputStream = new FileInputStream(file);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String newLine;
            while ((newLine = bufferedReader.readLine()) != null) {
                collection.add(parseLine(newLine));
            }
            return collection;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return collection;
    }

    private LogToken parseLine(String str) {
        ZonedDateTime date = null;
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .parseCaseSensitive()
                .appendPattern("dd/MMM/yyyy:HH:mm:ss Z")
                .toFormatter(Locale.ENGLISH);
        Pattern datePattern = Pattern.compile("(?<=\\[).{26}(?=\\])");
        Matcher matcher = datePattern.matcher(str);
        if (matcher.find()) {
            date = ZonedDateTime.parse(matcher.group(0), formatter);
        }

        HttpMethod method = null;
        Pattern methodPattern = Pattern.compile("(" + HttpMethod.GET + ")|(" + HttpMethod.POST + ")");
        matcher = methodPattern.matcher(str);
        if (matcher.find()) {
            method = HttpMethod.valueOf(matcher.group(0));
        }

        String msg = "";
        Pattern messagePattern = Pattern.compile("(?<=\").+(?=\")");
        matcher = messagePattern.matcher(str);
        if (matcher.find()) {
            msg = matcher.group(0);
        }
        return new LogToken(date.toLocalDateTime(), method, msg);
    }

    private void validateInput(File file) {
        if (!file.exists()) {
            throw new IllegalArgumentException("Error! File location " + file + " not exists!!!");
        }
    }
}
