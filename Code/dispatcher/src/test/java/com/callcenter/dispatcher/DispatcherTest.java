package com.callcenter.dispatcher;

import org.junit.Test;

import com.callcenter.dispatcher.enums.EmployeeType;
import com.callcenter.dispatcher.models.Call;
import com.callcenter.dispatcher.models.Employee;
import com.dispatcher.callcenter.Dispatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class DispatcherTest {

    private static final int CALL_AMOUNT = 10;

    private static final int MIN_CALL_DURATION = 5;

    private static final int MAX_CALL_DURATION = 10;

    @Test(expected = NullPointerException.class)
    public void doTestDispatcherCreationWithNullEmployees() {
        new Dispatcher(null);
    }

    @Test(expected = NullPointerException.class)
    public void doTestDispatcherCreationWithNullStrategy() {
        new Dispatcher(new ArrayList<>(), null);
    }

    @Test
    public void doTestDispatchCallToEmployee() throws InterruptedException {
        List<Employee> lstEmployee = doCreateEmployeeList();
        Dispatcher varDispatcher = new Dispatcher(lstEmployee);
        varDispatcher.start();
        
        TimeUnit.SECONDS.sleep(1);
        
        ExecutorService varExecutorService = Executors.newSingleThreadExecutor();
        
        varExecutorService.execute(varDispatcher);
        TimeUnit.SECONDS.sleep(1);

        doCreateCallList().stream().forEach(call -> {
            varDispatcher.dispatch(call);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                fail();
            }
        });

        varExecutorService.awaitTermination(MAX_CALL_DURATION * 2, TimeUnit.SECONDS);
        assertEquals(CALL_AMOUNT, lstEmployee.stream().mapToInt(employee -> employee.fnGetAttendedCall().size()).sum());
    }

    private static List<Employee> doCreateEmployeeList() {
        Employee varEmployee1 = Employee.doCreateEmployee(EmployeeType.OPERATOR);
        Employee varEmployee2 = Employee.doCreateEmployee(EmployeeType.OPERATOR);
        Employee varEmployee3 = Employee.doCreateEmployee(EmployeeType.OPERATOR);
        Employee varEmployee4 = Employee.doCreateEmployee(EmployeeType.OPERATOR);
        Employee varEmployee5 = Employee.doCreateEmployee(EmployeeType.OPERATOR);
        Employee varEmployee6 = Employee.doCreateEmployee(EmployeeType.OPERATOR);
        Employee varEmployee7 = Employee.doCreateEmployee(EmployeeType.SUPERVISOR);
        Employee varEmployee8 = Employee.doCreateEmployee(EmployeeType.SUPERVISOR);
        Employee varEmployee9 = Employee.doCreateEmployee(EmployeeType.SUPERVISOR);
        Employee varEmployee10 = Employee.doCreateEmployee(EmployeeType.DIRECTOR);
        return Arrays.asList(varEmployee1, varEmployee2, varEmployee3, varEmployee4, varEmployee5, varEmployee6,
                varEmployee7, varEmployee8, varEmployee9, varEmployee10);
    }

    private static List<Call> doCreateCallList() {
        return Call.doCreateRandomCall(CALL_AMOUNT, MIN_CALL_DURATION, MAX_CALL_DURATION);
    }
}
