package com.aldercape.holiday.directreport;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class DirectReports {

    private final String manager;
    private List<DirectReport> directReports;

    public DirectReports(String manager, List<DirectReport> directReports) {
        this.manager = manager;
        this.directReports = directReports;
    }

    public DirectReports(String manager) {
        this.manager = manager;
        this.directReports = new LinkedList<>();
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

    public void add(DirectReport directReport) {
        directReports.add(directReport);
    }

    public String manager() {
        return manager;
    }

    public List<DirectReport> directReports() {
        return Collections.unmodifiableList(directReports);
    }
}
