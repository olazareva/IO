package ua.lazareva.loganalyzer.entity;

public enum HttpMethod {
    GET("GET"),
    POST("POST");

    private String name;

    HttpMethod(String name) {
        this.name = name;
    }
}
