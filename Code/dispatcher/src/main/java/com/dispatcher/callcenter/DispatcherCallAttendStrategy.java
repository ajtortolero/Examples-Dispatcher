package com.dispatcher.callcenter;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.callcenter.dispatcher.enums.EmployeeState;
import com.callcenter.dispatcher.enums.EmployeeType;
import com.callcenter.dispatcher.interfaces.CallAttendStrategy;
import com.callcenter.dispatcher.models.Employee;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * DefaultCallAttendStrategy
 * <p>
 * This strategy returns the first available operator employee.
 * If all operator employees are busy, it returns the first available supervisor employee.
 * If all supervisor employees are busy, it returns the first available director employee.
 * If all employees are busy, it returns null.
 */
public class DispatcherCallAttendStrategy implements CallAttendStrategy {

    private static final Logger logger = LoggerFactory.getLogger(DispatcherCallAttendStrategy.class);
    
    /**
     * Find Employee into a Collection<Employee>
     */
    @Override
    public Employee fnFindEmployee(Collection<Employee> pEmployee) {
        Validate.notNull(pEmployee);
        List<Employee> varEmployeeAvailable = pEmployee.stream().filter(e -> e.fnGetEmployeeState() == EmployeeState.AVAILABLE).collect(Collectors.toList());
        logger.info("Available operators: " + varEmployeeAvailable.size());
        Optional<Employee> varEmployee = varEmployeeAvailable.stream().filter(e -> e.fnGetEmployeeType() == EmployeeType.OPERATOR).findAny();
        if (!varEmployee.isPresent()) {
            logger.info("No available operators found");
            varEmployee = varEmployeeAvailable.stream().filter(e -> e.fnGetEmployeeType() == EmployeeType.SUPERVISOR).findAny();
            if (!varEmployee.isPresent()) {
                logger.info("No available supervisors found");
                varEmployee = varEmployeeAvailable.stream().filter(e -> e.fnGetEmployeeType() == EmployeeType.DIRECTOR).findAny();
                if (!varEmployee.isPresent()) {
                    logger.info("No available directors found");
                    return null;
                }
            }
        }
        logger.info("Employee of type " + varEmployee.get().fnGetEmployeeType() + " found");
        return varEmployee.get();
    }
}
