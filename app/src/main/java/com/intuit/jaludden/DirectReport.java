package com.intuit.jaludden;

public class DirectReport {

    private final String name;

    public DirectReport(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String toJson() {
        return String.format("{\"name\": \"%s\"}", name);
    }
}
