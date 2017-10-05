package com.mhframework;


public interface MHUpdatable
{

    /**************************************************************************
     * Method to be called to perform data updates during the update phase
     * of the game loop.
     * 
     * @param elapsedTime Time elapsed in nanoseconds since the last iteration
     *                    of the game loop.
     */
    public abstract void update(long elapsedTime);

}
