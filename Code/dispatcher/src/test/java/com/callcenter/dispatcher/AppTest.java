package com.callcenter.dispatcher;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class AppTest {

	public static void main(String[] args) {
		Result varResult;
		
		/* Test to Call */
		System.out.println("Init Test " + CallTest.class.toString());
		varResult = JUnitCore.runClasses(CallTest.class);
		for (Failure iteFailure : varResult.getFailures()) {
			System.out.println(iteFailure.toString());
		}
		
		/* Test to DispatcherCallAttendStrategy */
		System.out.println("Init Test " + DispatcherCallAttendStrategyTest.class.toString());
		varResult = JUnitCore.runClasses(DispatcherCallAttendStrategyTest.class);
		for (Failure iteFailure : varResult.getFailures()) {
			System.out.println(iteFailure.toString());
		}
		
		/* Test to Dispatcher */
		System.out.println("Init Test " + DispatcherTest.class.toString());
		varResult = JUnitCore.runClasses(DispatcherTest.class);
		for (Failure iteFailure : varResult.getFailures()) {
			System.out.println(iteFailure.toString());
		}
		
		/* Test to Employee */
		System.out.println("Init Test " + EmployeeTest.class.toString());
		varResult = JUnitCore.runClasses(EmployeeTest.class);
		for (Failure iteFailure : varResult.getFailures()) {
			System.out.println(iteFailure.toString());
		}		
	}
}