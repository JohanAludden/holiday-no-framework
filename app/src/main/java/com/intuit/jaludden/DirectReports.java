package com.intuit.jaludden;

import java.util.List;
import java.util.stream.Collectors;

public class DirectReports {

    private final String manager;
    private List<DirectReport> directReports;

    public DirectReports(String manager, List<DirectReport> directReports) {
        this.manager = manager;
        this.directReports = directReports;
    }

    public String toJson() {
        return String.format("{\"manager\": \"%s\", \"direct_reports\": [%s]}",
                manager,
                directReports.stream()
                        .map(DirectReport::toJson)
                        .collect(Collectors.joining(",")));
    }

    public int size() {
        return directReports.size();
    }

    public DirectReport get(int index) {
        return directReports.get(index);
    }
}
