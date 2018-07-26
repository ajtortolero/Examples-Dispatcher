package com.callcenter.dispatcher.models;

import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import java.util.UUID;

/**
 * Models the Call Domain Objects
 */
public class Call {

    /**
     * ID of Call
     */     
    private UUID _Id;
    
    
	/**
     * Duration in seconds of the Call
     */	
    private Integer _Duration;

    /**
     * Creates a new Call with duration measured in seconds
     *
     * @param pDurationInSeconds duration in seconds must be equal or greater than zero
     */
    public Call(Integer pDurationInSeconds) {
        Validate.notNull(pDurationInSeconds);
        Validate.isTrue(pDurationInSeconds >= 0);
        
        this._Id = UUID.randomUUID();          
        this._Duration = pDurationInSeconds;
    }
    
    /**
     * Obtains Id of the Call
     */    
    public UUID getId() {
        return _Id;
    }    
   
    /**
     * Obtains duration in seconds of the Call
     */    
    public Integer getDuration() {
        return _Duration;
    }

    /**
     * Builds a new random call
     *
     * @param pMinDuration minimum duration in seconds must be equal or greater than zero
     * @param pMaxDuration maximum duration in seconds must be equal or greater than minDurationInSeconds
     * @return A new random call with a random duration value between minimum and maximum duration
     */
    public static Call doCreateRandomCall(Integer pMinDuration, Integer pMaxDuration) {
        Validate.isTrue(pMaxDuration >= pMinDuration && pMinDuration >= 0);
        return new Call(ThreadLocalRandom.current().nextInt(pMinDuration, pMaxDuration + 1));
    }

    /**
     * Builds a new random call list
     *
     * @param pListSize amount of random calls to be created
     * @param pMinDuration minimum duration in seconds of each call must be equal or greater than zero
     * @param pMaxDuration maximum duration in seconds of each call must be equal or greater than minDurationInSeconds
     * @return A new list of random calls each with a random duration value between minimum and maximum duration
     */
    public static List<Call> doCreateRandomCall(Integer pListSize, Integer pMinDuration, Integer pMaxDuration) {
        Validate.isTrue(pListSize >= 0);
        List<Call> lstCall = new ArrayList<>();
        for (int i = 0; i < pListSize; i++) {
            lstCall.add(doCreateRandomCall(pMinDuration, pMaxDuration));
        }
        return lstCall;
    }
}
