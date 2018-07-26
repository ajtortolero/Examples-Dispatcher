package com.dispatcher.callcenter;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.callcenter.dispatcher.interfaces.CallAttendStrategy;
import com.callcenter.dispatcher.models.Call;
import com.callcenter.dispatcher.models.Employee;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Dispatcher implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(Dispatcher.class);

    public static final Integer ThreadsMax = 10;

    private Boolean _Active;

    private ExecutorService _ExecutorService;

    private ConcurrentLinkedDeque<Employee> _Employee;

    private ConcurrentLinkedDeque<Call> _Call;

    private CallAttendStrategy _CallAttendStrategy;

    public Dispatcher(List<Employee> pEmployee) {
        this(pEmployee, new DispatcherCallAttendStrategy());
    }

    public Dispatcher(List<Employee> pEmployee, CallAttendStrategy pCallAttendStrategy) {
    	
        Validate.notNull(pEmployee);
        Validate.notNull(pCallAttendStrategy);
        
        this._Call = new ConcurrentLinkedDeque<Call>();
        this._Employee = new ConcurrentLinkedDeque<Employee>(pEmployee);
        
        this._CallAttendStrategy = pCallAttendStrategy;
        this._ExecutorService = Executors.newFixedThreadPool(ThreadsMax);
    }

    public synchronized void dispatch(Call call) {
        logger.info("Dispatch new call of " + call.getDuration() + " seconds");
        this._Call.add(call);
    }

    /**
     * Starts the employee threads and allows the dispatcher run method to execute
     */
    public synchronized void start() {
        this._Active = true;
        for (Employee iteEmployee : this._Employee) {
            this._ExecutorService.execute(iteEmployee);
        }
    }

    /**
     * Stops the employee threads and the dispatcher run method immediately
     */
    public synchronized void stop() {
        this._Active = false;
        this._ExecutorService.shutdown();
    }

    public synchronized Boolean getActive() {
        return _Active;
    }

    /**
     * This is the method that runs on the thread.
     * If the incoming calls queue is not empty, then it searches for and available employee to take the call.
     * Calls will queue up until some workers becomes available.
     */
    @Override
    public void run() {
        while (getActive()) {
            if (this._Call.isEmpty()) {
                continue;
            } else {
                Employee varEmployee = this._CallAttendStrategy.fnFindEmployee(this._Employee);
                if (varEmployee == null) {
                    continue;
                }
                Call varCall = this._Call.poll();
                try {
                    varEmployee.doAttend(varCall);
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    this._Call.addFirst(varCall);
                }
            }
        }
    }
}
