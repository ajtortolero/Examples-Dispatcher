package com.callcenter.dispatcher;

import org.junit.Test;

import com.callcenter.dispatcher.enums.EmployeeState;
import com.callcenter.dispatcher.enums.EmployeeType;
import com.callcenter.dispatcher.models.Call;
import com.callcenter.dispatcher.models.Employee;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class EmployeeTest {

    @Test(expected = NullPointerException.class)
    public void testEmployeeInvalidCreation() {
        new Employee(null);
    }

    @Test
    public void testEmployeeCreation() {
        Employee varEmployee = Employee.doCreateEmployee(EmployeeType.OPERATOR);

        assertNotNull(varEmployee);
        assertEquals(EmployeeType.OPERATOR, varEmployee.fnGetEmployeeType());
        assertEquals(EmployeeState.AVAILABLE, varEmployee.fnGetEmployeeState());
    }

    @Test
    public void testEmployeeAttendWhileAvailable() throws InterruptedException {
        Employee varEmployee = Employee.doCreateEmployee(EmployeeType.OPERATOR);
        ExecutorService varExecutorService = Executors.newSingleThreadExecutor();

        varExecutorService.execute(varEmployee);
        varEmployee.doAttend(Call.doCreateRandomCall(0, 1));

        varExecutorService.awaitTermination(5, TimeUnit.SECONDS);
        assertEquals(1, varEmployee.fnGetAttendedCall().size());
    }

    @Test
    public void testEmployeeStatesWhileAttend() throws InterruptedException {
        Employee varEmployee = Employee.doCreateEmployee(EmployeeType.OPERATOR);
        ExecutorService varExecutorService = Executors.newSingleThreadExecutor();

        varExecutorService.execute(varEmployee);
        assertEquals(EmployeeState.AVAILABLE, varEmployee.fnGetEmployeeState());
        
        TimeUnit.SECONDS.sleep(1);
        
        varEmployee.doAttend(Call.doCreateRandomCall(2, 3));
        varEmployee.doAttend(Call.doCreateRandomCall(0, 1));
        
        TimeUnit.SECONDS.sleep(1);
        
        assertEquals(EmployeeState.BUSY, varEmployee.fnGetEmployeeState());

        varExecutorService.awaitTermination(5, TimeUnit.SECONDS);
        assertEquals(2, varEmployee.fnGetAttendedCall().size());
    }
}
