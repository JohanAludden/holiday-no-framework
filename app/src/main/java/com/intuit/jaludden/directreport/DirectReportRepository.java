package com.intuit.jaludden.directreport;

public interface DirectReportRepository {
    DirectReports getDirectReportsFor(String manager);

    void addDirectReportFor(DirectReport directReport);
}
