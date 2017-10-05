package com.mhframework.ai.fsm;

/******************************************************************************
 * Interface to be implemented by objects whose behaviors are controlled by
 * states.
 * 
 * @author Michael Henson
 */
public interface MHStateContext
{
    public void changeState(MHState newState);
}
