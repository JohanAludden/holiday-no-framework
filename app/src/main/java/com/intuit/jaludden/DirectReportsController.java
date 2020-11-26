package com.intuit.jaludden;

import java.util.HashMap;
import java.util.Map;

public class DirectReportsController {

    private Map<String, DirectReports> orgStructure = new HashMap<>();

    public DirectReports getDirectReportsFor(String manager) {
        return orgStructure.getOrDefault(manager, new DirectReports(manager));
    }

    public void addDirectReportFor(String manager, String employee) {
        orgStructure.computeIfAbsent(manager, key -> new DirectReports(manager)).add(new DirectReport(employee));
    }
}
