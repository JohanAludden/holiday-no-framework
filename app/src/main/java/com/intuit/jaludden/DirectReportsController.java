package com.intuit.jaludden;

import java.util.HashMap;
import java.util.Map;

public class DirectReportsController {

    private Map<String, DirectReports> orgStructure = new HashMap<>();

    public DirectReports getDirectReportsFor(String manager) {
        return orgStructure.getOrDefault(manager, new DirectReports(manager));
    }

    public DirectReport addDirectReportFor(String manager, String employee) {
        DirectReport directReport = new DirectReport(employee, manager);
        orgStructure.computeIfAbsent(manager, key -> new DirectReports(manager)).add(directReport);
        return directReport;
    }
}
