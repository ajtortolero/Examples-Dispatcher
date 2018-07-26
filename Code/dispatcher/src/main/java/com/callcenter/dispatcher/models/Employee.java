package com.callcenter.dispatcher.models;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.callcenter.dispatcher.enums.EmployeeState;
import com.callcenter.dispatcher.enums.EmployeeType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;

import java.util.UUID;

/**
 * Models the Employee Domain Objects
 */
public class Employee implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(Employee.class);

    /**
     * ID of Employee
     */     
    private UUID _Id;
    
    /**
     * Type of Employee
     */     
    private EmployeeType _Type;

    /**
     * State of Employee
     */     
    private EmployeeState _State;

    /**
     * Incoming Calls of Employee
     */              
    private ConcurrentLinkedDeque<Call> _IncomingCalls;

    /**
     * Attended Calls of Employee
     */          
    private ConcurrentLinkedDeque<Call> _AttendedCalls;

    /**
     * Builder
     */      
    public Employee(EmployeeType pEmployeeType) {
        Validate.notNull(pEmployeeType);
        this._Id = UUID.randomUUID();         
        this._Type = pEmployeeType;
        this._State = EmployeeState.AVAILABLE;
        this._IncomingCalls = new ConcurrentLinkedDeque<Call>();
        this._AttendedCalls = new ConcurrentLinkedDeque<Call>();
    }

    /**
     * Obtains EmployeeId of the Employee
     */         
    public UUID fnGetEmployeeId() {
        return _Id;
    }    
    
    /**
     * Obtains EmployeeType of the Employee
     */         
    public EmployeeType fnGetEmployeeType() {
        return _Type;
    }

    /**
     * Obtains EmployeeState of the Employee
     */     
    public synchronized EmployeeState fnGetEmployeeState() {
        return _State;
    }

    /**
     * Setting State to Employee
     */      
    private synchronized void doSetEmployeeState(EmployeeState employeeState) {
        logger.info("Employee " + Thread.currentThread().getName() + " changes its state to " + employeeState);
        this._State = employeeState;
    }

    /**
     * Obtains List of Call Attended by the Employee
     */      
    public synchronized List<Call> fnGetAttendedCall() {
        return new ArrayList<>(_AttendedCalls);
    }

    /**
     * Queues a call to be attended by the employee
     *
     * @param pCall call to be attended
     */
    public synchronized void doAttend(Call pCall) {
        logger.info("Employee " + Thread.currentThread().getName() + " queues a call of " + pCall.getDuration() + " seconds");
        this._IncomingCalls.add(pCall);
    }
    
    /**
     * Create an Employee
     *
     * @param pEmployeeType Type of Employee
     */    
    public static Employee doCreateEmployee(EmployeeType pEmployeeType) {
        return new Employee(pEmployeeType);
    }    

    /**
     * This is the method that runs on the thread.
     * If the incoming calls queue is not empty, then it changes its state from AVAILABLE to BUSY, takes the call
     * and when it finishes it changes its state from BUSY back to AVAILABLE. This allows a Thread Pool to decide
     * to dispatch another call to another employee.
     */
    @Override
    public void run() {
        logger.info("Employee " + Thread.currentThread().getName() + " starts to work");
        while (true) {
            if (!this._IncomingCalls.isEmpty()) {
                Call varCall = this._IncomingCalls.poll();
                this.doSetEmployeeState(EmployeeState.BUSY);
                logger.info("Employee " + Thread.currentThread().getName() + " starts working on a call of " + varCall.getDuration() + " seconds");
                try {
                    TimeUnit.SECONDS.sleep(varCall.getDuration());
                } catch (InterruptedException e) {
                    logger.error("Employee " + Thread.currentThread().getName() + " was interrupted and could not finish call of " + varCall.getDuration() + " seconds");
                } finally {
                    this.doSetEmployeeState(EmployeeState.AVAILABLE);
                }
                this._AttendedCalls.add(varCall);
                logger.info("Employee " + Thread.currentThread().getName() + " finishes a call of " + varCall.getDuration() + " seconds");
            }
        }
    }
}