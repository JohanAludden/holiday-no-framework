package com.aldercape.holiday.directreport;

import com.aldercape.holiday.HolidayDatabase;

public class DatabaseDirectReportRepository implements DirectReportRepository {
    private HolidayDatabase database;

    public DatabaseDirectReportRepository(HolidayDatabase database) {
        this.database = database;
    }

    @Override
    public DirectReports getDirectReportsFor(String manager) {
        DirectReports result = new DirectReports(manager);
        database.executeQuery(
                "select manager, employee from direct_reports where manager = ?",
                rs -> {
                    while (rs.next()) {
                        result.add(new DirectReport(rs.getString("manager"), rs.getString("employee")));
                    }
                });
        return result;
    }

    @Override
    public void addDirectReportFor(DirectReport directReport) {
        database.executeInsert("insert into direct_reports (manager, employee) values (?, ?)", directReport.getManagerName(), directReport.getName());
    }
}
