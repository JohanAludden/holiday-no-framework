package com.intuit.jaludden;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DirectReportsController {

    private Map<String, List<DirectReport>> orgStructure = new HashMap<>();

    public DirectReports getDirectReportsFor(String manager) {
        return new DirectReports(manager, orgStructure.getOrDefault(manager, Collections.emptyList()));
    }

    public void addDirectReportFor(String manager, String employee) {
        orgStructure.computeIfAbsent(manager, key -> new LinkedList<>()).add(new DirectReport(employee));
    }
}
