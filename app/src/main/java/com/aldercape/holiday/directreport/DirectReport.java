package com.aldercape.holiday.directreport;

public class DirectReport {

    private String name;
    private String managerName;

    public DirectReport(String name, String managerName) {
        this.name = name;
        this.managerName = managerName;
    }

    public String getManagerName() {
        return managerName;
    }

    public String getName() {
        return name;
    }

    public String toJson() {
        return String.format("{\"manager_name\": \"%s\", \"employee_name\": \"%s\"}", managerName, name);
    }
}
