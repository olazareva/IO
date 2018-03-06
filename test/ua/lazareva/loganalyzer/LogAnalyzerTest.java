package ua.lazareva.loganalyzer;

import org.junit.Test;
import ua.lazareva.loganalyzer.service.LogAnalyzer;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class LogAnalyzerTest {
    private LogAnalyzer inst = new LogAnalyzer();
    private String path = "/home/olga/IdeaProjects/ThirdStep/resources/loganalyzer/log _04032004.log";

    @Test
    public void testGetLogWithFilter() {
        assertEquals (25, inst.getLogWithFilter(path,
                LocalDateTime.of(2004, 3, 7, 16, 10, 0),
                LocalDateTime.of(2004, 3, 7, 17, 0, 0)).size());

        assertEquals (50, inst.getLogWithFilter(path,
                LocalDateTime.of(2004, 3, 7, 0, 0, 0),
                LocalDateTime.of(2004, 3, 8, 0, 0, 0)).size());

        assertEquals (0, inst.getLogWithFilter(path,
                LocalDateTime.of(2004, 3, 1, 0, 0, 0),
                LocalDateTime.of(2004, 3, 7, 0, 0, 0)).size());

            }
}