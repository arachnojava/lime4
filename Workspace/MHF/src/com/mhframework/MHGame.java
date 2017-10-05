package com.mhframework;

import com.mhframework.platform.MHGameApplication;
import com.mhframework.platform.MHPlatform;
import com.mhframework.platform.graphics.MHBitmapImage;
import com.mhframework.platform.graphics.MHGraphicsCanvas;

/********************************************************************
 * This class drives the entire game process.  As the hub of the
 * runtime architecture, it coordinates the rendering objects,
 * runtime metrics, and threading concerns.
 * 
 * @author Michael Henson
 */
public final class MHGame implements Runnable
{
    private static MHGame instance;
    private static MHGameApplication context;
    private static MHBitmapImage backBuffer;
    private static MHRuntimeMetrics   timer = new MHRuntimeMetrics();

    private static Thread gameLoop;
    
    private static boolean programOver=false, running=true, debugMode=false;

    /****************************************************************
     * Constructor.
     */
    public MHGame(final MHGameApplication context)
    {
        MHGame.context = context;
        MHGame.instance = this;
        Runtime.getRuntime().addShutdownHook(new Thread()
            {
                public void run()
                {
                    programOver = true;
                    MHGame.context.shutdown();
                }
            });
        
        new Thread(this).start();
    }


    /****************************************************************
     * Executes the game loop.
     * 
     * The entire game loop consists of the following steps:
     * <ul>
     * <li>While the program is not finished:
     * <ol>
     * <li>Record the start time of the current iteration
     * <li>Tell the screen manager to update.
     * <li>Tell the screen manager to render.
     * <li>Present the back buffer to the screen.
     * <li>Record the ending time of the current iteration
     * <li>Calculate how long we should "sleep" before continuing, and then
     * sleep for that amount of time.
     * <li>Resynchronize the frame rate by updating without rendering, if
     * necessary.
     * </ol>
     * </ul>
     */
    public void run()
    {
        backBuffer = MHPlatform.createImage((int)context.getDisplaySize().getX(), (int)context.getDisplaySize().getY());
        MHGraphicsCanvas g = backBuffer.getGraphicsCanvas(); 

        MHScreenManager screenManager = MHScreenManager.getInstance();
        MHScreenManager.setContext(context);
        screenManager.changeScreen(screenManager.getCurrentScreen());
        
        long lastUpdateTime = System.currentTimeMillis();
        
        // Loop until the program is over
        while (!programOver)
        {
            if (running)
            {
            // Record the starting time of the loop
            timer.recordStartTime();

            // Update the screen data
            screenManager.update(System.currentTimeMillis() - lastUpdateTime);
            lastUpdateTime = System.currentTimeMillis();
            
            // Draw the updated screen
            screenManager.render(g);
            context.present(g);
            
            // Record the ending time of the loop
            timer.recordEndTime();

            // Separate UPS from FPS to maintain a  better frame rate.
            while (timer.shouldUpdate())
            {
                screenManager.update(System.currentTimeMillis() - lastUpdateTime);
                lastUpdateTime = System.currentTimeMillis();
            }
            
            // Calculate how long it took to run this loop and
            // use that value to see how long we should wait
            // (or "sleep") before starting the next iteration
            timer.sleep();
            
            } // if (running)
        } // while (!programOver) . . .

        context.shutdown();
    } // run()

    
    /****************************************************************
     * Resumes the game thread after it has been paused.
     */
    public static void resume() 
    {
        running = true;
        gameLoop = new Thread(instance);
        gameLoop.start();
    }

    
    /****************************************************************
     * Pauses the game thread.
     */
    public static void pause() 
    {
        running = false;
        while (true) 
        {
            try 
            {
                //just keep doing this until app has focus
                gameLoop.join();
            } 
            catch (InterruptedException e) { }
        }
    }
    
    
    /****************************************************************
     * Return the back buffer used in rendering.
     */
    public static MHGraphicsCanvas getBackBuffer()
    {
        return backBuffer.getGraphicsCanvas();
    }

    
    /****************************************************************
     * Return the value of the <code>programOver</code> flag.
     */
    public static boolean isProgramOver()
    {
        return programOver;
    }


    /****************************************************************
     * Set the value of the <code>programOver</code> flag.  When this
     * value is set to true, the program terminates.
     */
    public static void setProgramOver(final boolean isOver)
    {
        programOver = isOver;
    }


    /****************************************************************
     * Return the result of the most recent frames-per-second
     * calculation.
     */
    public static int getFramesPerSecond()
    {
        return timer.getFramesPerSecond();
    }


    /****************************************************************
     * Return the result of the most recent updates-per-second
     * calculation.
     */
    public static int getUpdatesPerSecond()
    {
        return timer.getUpdatesPerSecond();
    }

    
    /****************************************************************
     * Return the total time in seconds that the game has been in
     * progress.
     */
    public static long getTimeSpentInGame()
    {
        return timer.getTimeSpentInGame();
    }
    
    
    /****************************************************************
     *  Return the value of the game timer in milliseconds. 
     */
    public static long getGameTimerValue()
    {
        return timer.getGameTimerValue();
    }


    /**
     * @return the debugMode
     */
    public static boolean isDebugMode()
    {
        return debugMode;
    }


    /**
     * @param debugMode the debugMode to set
     */
    public static void setDebugMode(boolean debugMode)
    {
        MHGame.debugMode = debugMode;
    }
}
