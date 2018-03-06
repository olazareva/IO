package ua.lazareva.loganalyzer.entity;

import ua.lazareva.loganalyzer.entity.HttpMethod;

import java.time.LocalDateTime;

public class LogToken {
    private LocalDateTime time;
    private HttpMethod method;
    private String message;

    public LogToken(LocalDateTime time, HttpMethod method, String message) {
        this.time = time;
        this.method = method;
        this.message = message;
    }

    public LocalDateTime getDateTime() {
        return time;
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("{time=")
                .append(time)
                .append(", method=")
                .append(method)
                .append(", message='")
                .append(message)
                .append("\'}");
        return buffer.toString();
    }
}
