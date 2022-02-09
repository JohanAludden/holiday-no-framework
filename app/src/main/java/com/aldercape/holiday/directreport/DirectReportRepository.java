package com.aldercape.holiday.directreport;

public interface DirectReportRepository {
    DirectReports getDirectReportsFor(String manager);

    void addDirectReportFor(DirectReport directReport);
}
