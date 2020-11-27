package com.intuit.jaludden.directreport;

public class DirectReportsController {

    private DirectReportRepository repository;

    public DirectReportsController(DirectReportRepository repository) {
        this.repository = repository;
    }

    public DirectReports getDirectReportsFor(String manager) {
        return repository.getDirectReportsFor(manager);
    }

    public DirectReport addDirectReportFor(String manager, String employee) {
        var directReport = new DirectReport(employee, manager);
        repository.addDirectReportFor(directReport);
        return directReport;
    }
}
