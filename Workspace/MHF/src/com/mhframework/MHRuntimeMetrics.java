package com.mhframework;

/********************************************************************
 * This class borrows logic and code from the book <i>Killer Game
 * Programming in Java</i> by Andrew Davison.
 * 
 * @author Michael Henson
 */
public class MHRuntimeMetrics
{
    private static final int  ONE_SECOND_IN_MILLI = 1000; 
    private static final long ONE_MILLI_IN_NANO = 1000000L; 
    private static final long ONE_SECOND_IN_NANO  = ONE_SECOND_IN_MILLI * ONE_MILLI_IN_NANO;
    
    /** The frame rate we're trying to achieve. */
    public static final short TARGET_FPS = 28*2; // 28 = average FPS for Japanese anime
    
    /** Average period in milliseconds required to achieve the target frame rate. */
    public static final long PERIOD = ONE_SECOND_IN_MILLI/TARGET_FPS;

    /** Maximum number of renders to skip when compensating for long loop iterations. */
    static final short MAX_FRAME_SKIPS = 4;
    
    /** Nanoseconds between metrics calculations. */
    private static final long MAX_STATS_INTERVAL = ONE_SECOND_IN_MILLI;
    
    /** Number of measurements to track for calculating an average. */
    private static final int SAMPLE_SIZE = 5;

    private int frameCount = 0;        // Number of frames elapsed.
    private int statsInterval;         // Time since last metrics calculation.
    private long gameStartTime;        // Approximate time that game started.
    private long prevStatsTime = 0;    // Time that metrics were last calculated.
    private long totalElapsedTime = 0; // Total amount of time elapsed in game loop.
    private int totalFramesSkipped;    // Total number of renders skipped due to long loop iterations.
    private int framesSkipped = 0;     // Number of renders skipped since last metrics calculation.
    private int statsCount = 0;        // Index into fpsStore and upsStore arrays.
    private double averageFPS;         // Calculated average frames per second.
    private double averageUPS;         // Calculated average updates per second.
    private int[] fpsStore, upsStore;  // Arrays for storing calculation results.
    private long startTime, endTime;   // Starting and ending times for current loop iteration.
    private long excess = 0;           // Amount of extra time attained through short loop iterations.
    
    /**************************************************************************
     * Constructor.
     */
    public MHRuntimeMetrics()
    {
        gameStartTime = System.currentTimeMillis();
        prevStatsTime = gameStartTime;
        
        fpsStore = new int[SAMPLE_SIZE];
        upsStore = new int[SAMPLE_SIZE];
    }
    
       
    /**************************************************************************
     * Populates the start time value with the current system time in 
     * nanoseconds.
     */
    public void recordStartTime()
    {
        startTime = System.currentTimeMillis();
    }

    
    /**************************************************************************
     * Populates the end time value with the current system time in nanoseconds
     * and then updates and stores the stats tracked by this class.
     */
    public void recordEndTime()
    {
        endTime = System.currentTimeMillis();
        storeStats(endTime - startTime);  // Update the stored metrics.
        //Toolkit.getDefaultToolkit().sync(); // Sync the display (for Linux users).
    }
    
    
    /**************************************************************************
     * Utility method for converting seconds to nanoseconds.
     * 
     * @param seconds The value in seconds to convert to nanoseconds.
     * @return The nanosecond value that is equivalent to the input parameter.
     */
    public static long secToNano(double seconds)
    {
        return (long)(seconds * ONE_SECOND_IN_NANO);
    }

    
    /**************************************************************************
     * Utility method for converting nanoseconds to seconds.
     * 
     * @param nano The value in nanoseconds to convert to seconds.
     * @return The value in seconds that is equivalent to the input parameter.
     */
    public static int nanoToSec(long nano)
    {
        return (int)(nano / ONE_SECOND_IN_NANO);
    }
    
    
    
    public static int milliToSec(long milli)
    {
        return (int)(milli / ONE_SECOND_IN_MILLI);
    }
    
    /**************************************************************************
     * Utility method for converting nanoseconds to milliseconds.
     * 
     * @param nano The value in nanoseconds to convert to milliseconds.
     * @return The value in milliseconds that is equivalent to the input 
     *         parameter.
     */
    public static long nanoToMilli(long nano)
    {
        return nano / ONE_MILLI_IN_NANO;
    }
    
    
    private void storeStats(long elapsedTime)
    { 
      frameCount++;
      statsInterval += elapsedTime;

      if (statsInterval > MAX_STATS_INTERVAL) 
      {
        long timeNow = System.currentTimeMillis();

        long realElapsedTime = timeNow - prevStatsTime;   // time since last stats collection
        totalElapsedTime += realElapsedTime;

        totalFramesSkipped += framesSkipped;
        
        int actualFPS = TARGET_FPS;     // calculate the latest FPS and UPS
        int actualUPS = TARGET_FPS;
        if (totalElapsedTime > 0) 
        {
            int seconds = milliToSec(totalElapsedTime);
            seconds = (seconds > 0 ? seconds : 1);
            actualFPS = (frameCount / seconds);
            actualUPS = ((frameCount + totalFramesSkipped) / seconds);
        }

        // store the latest FPS and UPS
        fpsStore[ statsCount%SAMPLE_SIZE ] = actualFPS;
        upsStore[ statsCount%SAMPLE_SIZE ] = actualUPS;
        statsCount += 1;

        double totalFPS = 0.0;     // total the stored FPSs and UPSs
        double totalUPS = 0.0;
        for (int i=0; i < SAMPLE_SIZE; i++) 
        {
          totalFPS += fpsStore[i];
          totalUPS += upsStore[i];
        }

        if (statsCount < SAMPLE_SIZE) // obtain the average FPS and UPS
        { 
          averageFPS = totalFPS/statsCount;
          averageUPS = totalUPS/statsCount;
        }
        else 
        {
          averageFPS = totalFPS/SAMPLE_SIZE;
          averageUPS = totalUPS/SAMPLE_SIZE;
        }

        framesSkipped = 0;
        prevStatsTime = timeNow;
        statsInterval = 0;   // reset
      }
    }  // end of storeStats()

    
    /****************************************************************
     * Calculate how long the application thread should sleep based 
     * on the time it took to run the game loop.
     */
    public void sleep()
    {
        long sleepTime = PERIOD - (endTime - startTime);

        // NOTE:  Android has rules regarding the UI thread that discourages
        // sleeping.  Therefore, I'm trying a new platform-neutral approach.
        
        if (sleepTime > 0)
        {
            do {
                Thread.yield();
            } while (System.currentTimeMillis() - endTime < sleepTime);
            
//            try
//            {
//                Thread.sleep(sleepTime);
//            } 
//            catch (final InterruptedException e)
//            {
//            }
        } 
        else
        {
            excess -= sleepTime; // store excess time value
            Thread.yield(); // give another thread a chance to run
        }
    }
       
    
    /**************************************************************************
     * Returns the current average number of frames rendered per second.
     * 
     * @return The average frames per second at the time this method is
     *         invoked.
     */
    public int getFramesPerSecond()
    {
        return (int) averageFPS;
    }


    /**************************************************************************
     * Returns the current average number of updates per second.
     * 
     * @return The average updates per second at the time this method is
     *         invoked.
     */
    public int getUpdatesPerSecond()
    {
        return (int) averageUPS;
    }

    
    /**************************************************************************
     * Get the total number of seconds elapsed since the game began.
     * 
     * @return The game timer value in seconds.
     */
    public int getTimeSpentInGame()
    {
        return milliToSec(getGameTimerValue());
    }
    
    
    /**************************************************************************
     * Get the total number of milliseconds elapsed since the game began.
     * 
     * @return The game timer value in milliseconds.
     */
    public long getGameTimerValue()
    {
        return System.currentTimeMillis() - gameStartTime;
    }


    /**************************************************************************
     * Predicate method for determining whether an additional update should be 
     * performed without rendering.  This method is intended to aid in
     * maintaining a consistent frame rate even when game loop iterations take
     * longer than expected.
     * 
     * @return A boolean indicating whether an additional update should be
     *         performed based on the current timer values.
     */
    public boolean shouldUpdate()
    {
        boolean updateNeeded = (excess > PERIOD) && (framesSkipped < MAX_FRAME_SKIPS);
        
        if (updateNeeded)
        {
            excess -= MHRuntimeMetrics.PERIOD;
            framesSkipped++;
        }
        
        return updateNeeded;
    }


    /**************************************************************************
     * Calculate and return the total time elapsed in milliseconds between the
     * recorded start and end times.
     * 
     * @return Time elapsed in milliseconds.
     */
    public long getElapsedTime()
    {
        //long time = Math.abs(endTime - startTime);
        return Math.abs(endTime - startTime);
    }
}
