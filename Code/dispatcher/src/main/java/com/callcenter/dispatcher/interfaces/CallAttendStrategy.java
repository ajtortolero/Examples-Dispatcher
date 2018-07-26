package com.callcenter.dispatcher.interfaces;

import java.util.Collection;

import com.callcenter.dispatcher.models.Employee;

/**
 * Models different strategies on which is the next Employee available to work
 */
public interface CallAttendStrategy {

    /**
     * Find available employees
     *
     * @param pEemployee List of employees
     * @return Next available employee to take on a task, or null if all employees are busy
     */
    Employee fnFindEmployee(Collection<Employee> pEemployee);
}
