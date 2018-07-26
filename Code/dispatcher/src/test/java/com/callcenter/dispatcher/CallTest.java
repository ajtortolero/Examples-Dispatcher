package com.callcenter.dispatcher;

import org.junit.Test;

import com.callcenter.dispatcher.models.Call;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CallTest {

    @Test(expected = IllegalArgumentException.class)
    public void testCallCreationWithInvalidParameter() {
        new Call(-1);
    }

    @Test(expected = NullPointerException.class)
    public void testCallCreationWithNullParameter() {
        new Call(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomCallCreationWithInvalidFirstParameter() {
        Call.doCreateRandomCall(-1, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomCallCreationWithInvalidSecondParameter() {
        Call.doCreateRandomCall(1, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomCallCreationWithInvalidParameterOrder() {
        Call.doCreateRandomCall(2, 1);
    }

    @Test
    public void testRandomCallCreationWithValidParameters() {
        Integer varMin = 5;
        Integer varMax = 10;
        Call varCall = Call.doCreateRandomCall(varMin, varMax);

        assertNotNull(varCall);
        assertTrue(varMin <= varCall.getDuration());
        assertTrue(varCall.getDuration() <= varMax);
    }
}
