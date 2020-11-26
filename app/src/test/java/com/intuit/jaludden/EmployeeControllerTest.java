package com.intuit.jaludden;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmployeeControllerTest {

    private EmployeeController controller = new EmployeeController();

    @Test
    public void testNoEmployees() {
        List<Employee> employees = controller.getEmplyeesFor("Johan");
        assertEquals(0, employees.size());
    }
}
