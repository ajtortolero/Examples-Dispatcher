package com.callcenter.dispatcher;

import org.junit.Test;

import com.callcenter.dispatcher.enums.EmployeeState;
import com.callcenter.dispatcher.enums.EmployeeType;

import com.callcenter.dispatcher.interfaces.CallAttendStrategy;

import com.callcenter.dispatcher.models.Employee;
import com.dispatcher.callcenter.DispatcherCallAttendStrategy;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DispatcherCallAttendStrategyTest {

    private CallAttendStrategy callAttendStrategy;

    public DispatcherCallAttendStrategyTest() {
        this.callAttendStrategy = new DispatcherCallAttendStrategy();
    }

    @Test
    public void testAssignToOperator() {
        Employee varEmployeeOperator = Employee.doCreateEmployee(EmployeeType.OPERATOR);
        Employee varEmployeeSupervisor = Employee.doCreateEmployee(EmployeeType.SUPERVISOR);
        Employee varEmployeeDirector = Employee.doCreateEmployee(EmployeeType.DIRECTOR);
        List<Employee> lstEmployee = Arrays.asList(varEmployeeOperator, varEmployeeSupervisor, varEmployeeDirector);

        Employee varEmployee = this.callAttendStrategy.fnFindEmployee(lstEmployee);

        assertNotNull(varEmployee);
        assertEquals(EmployeeType.OPERATOR, varEmployee.fnGetEmployeeType());
    }

    @Test
    public void doTestAssignToSupervisor() {
        Employee varEmployeeOperator = mock(Employee.class);
        when(varEmployeeOperator.fnGetEmployeeState()).thenReturn(EmployeeState.BUSY);
        Employee varEmployeeSupervisor = Employee.doCreateEmployee(EmployeeType.SUPERVISOR);
        Employee varEmployeeDirector =  Employee.doCreateEmployee(EmployeeType.DIRECTOR);
        List<Employee> lstEmployee = Arrays.asList(varEmployeeOperator, varEmployeeSupervisor, varEmployeeDirector);

        Employee varEmployee = this.callAttendStrategy.fnFindEmployee(lstEmployee);

        assertNotNull(varEmployee);
        assertEquals(EmployeeType.SUPERVISOR, varEmployee.fnGetEmployeeType());
    }

    @Test
    public void doTestAssignToDirector() {
        Employee varEmployeeOperator = mockBusyEmployee(EmployeeType.OPERATOR);
        Employee varEmployeeSupervisor = mockBusyEmployee(EmployeeType.SUPERVISOR);
        Employee varEmployeeDirector = Employee.doCreateEmployee(EmployeeType.DIRECTOR);
        List<Employee> employeeList = Arrays.asList(varEmployeeOperator, varEmployeeSupervisor, varEmployeeDirector);

        Employee varEmployee = this.callAttendStrategy.fnFindEmployee(employeeList);

        assertNotNull(varEmployee);
        assertEquals(EmployeeType.DIRECTOR, varEmployee.fnGetEmployeeType());
    }

    @Test
    public void doTestAssignToNone() {
        Employee operator = mockBusyEmployee(EmployeeType.OPERATOR);
        Employee supervisor = mockBusyEmployee(EmployeeType.SUPERVISOR);
        Employee director = mockBusyEmployee(EmployeeType.DIRECTOR);
        List<Employee> employeeList = Arrays.asList(operator, supervisor, director);

        Employee employee = this.callAttendStrategy.fnFindEmployee(employeeList);

        assertNull(employee);
    }

    private static Employee mockBusyEmployee(EmployeeType pEmployeeType) {
        Employee varEmployee = mock(Employee.class);
        when(varEmployee.fnGetEmployeeType()).thenReturn(pEmployeeType);
        when(varEmployee.fnGetEmployeeState()).thenReturn(EmployeeState.BUSY);
        return varEmployee;
    }
}
