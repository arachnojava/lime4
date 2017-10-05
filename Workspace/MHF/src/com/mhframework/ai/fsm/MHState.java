package com.mhframework.ai.fsm;

/********************************************************************
 * Interface to be implemented in state-based AI agents.  This system
 * is based on the one presented in "Programming Game AI by Example"
 * by Mat Buckland. (Wordware Publishing, 2005)
 * 
 * @author Michael Henson
 */
public interface MHState
{
    public void enter(MHStateContext context);
    public void execute(MHStateContext context);
    public void exit(MHStateContext context);
}
