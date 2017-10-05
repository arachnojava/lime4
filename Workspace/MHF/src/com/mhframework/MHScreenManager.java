package com.mhframework;

import com.mhframework.core.math.MHVector;
import com.mhframework.platform.MHGameApplication;
import com.mhframework.platform.MHPlatform;
import com.mhframework.platform.event.MHKeyEvent;
import com.mhframework.platform.event.MHKeyListener;
import com.mhframework.platform.event.MHMouseTouchEvent;
import com.mhframework.platform.event.MHMouseTouchListener;
import com.mhframework.platform.graphics.MHBitmapImage;
import com.mhframework.platform.graphics.MHColor;
import com.mhframework.platform.graphics.MHGraphicsCanvas;
import com.mhframework.resources.MHResourceManager;
import com.mhframework.ui.MHButton;
import com.mhframework.ui.event.MHButtonListener;


/******************************************************************************
 * An object of this class holds a reference to the screen that is currently
 * visible.
 * 
 * @author Michael Henson
 */
public class MHScreenManager implements MHKeyListener, MHMouseTouchListener, MHButtonListener
{
    private final MHScreen NULL_SCREEN = new NullScreen();
    private static MHScreenManager instance;
    private static MHGameApplication app;
    private MHScreen currentScreen = null;
    //private Map<String, MHScreen> screens = Collections.synchronizedMap(new HashMap<String, MHScreen>());

    /**************************************************************************
     * Constructor.
     */
    private MHScreenManager()
    {
        // Init currentScreen to null screen.
        currentScreen = NULL_SCREEN;
        MHPlatform.addKeyListener(this);
        MHPlatform.addMouseTouchListener(this);
    }
    
    
    public void initSplashScreen(MHScreen nextScreen)
    {
    	setStartScreen(new SplashScreen(nextScreen));
    }


    /**************************************************************************
     * Sets an initial screen to be displayed when the application begins.
     * 
     * @param screen The first screen to be displayed when the app begins.
     */
    public void setStartScreen(MHScreen screen)
    {
        if (screen == null)
            screen = NULL_SCREEN;

        currentScreen = screen;
    }


    /**************************************************************************
     * Returns a reference to the current screen.
     * 
     * @return A reference to the screen currently being displayed
     */
    public MHScreen getCurrentScreen()
    {
        return currentScreen;
    }


    /**************************************************************************
     * Assigns a reference to the enclosing application that is currently
     * using this screen manager object.
     * 
     * @param app The MHGameApplication object that is using this screen
     *            manager.
     */
    public static void setContext(MHGameApplication app)
    {
        MHScreenManager.app = app;
    }


    /**************************************************************************
     * Returns a reference to the enclosing application that is currently
     * using this screen manager object.
     * 
     * @return The MHGameApplication object that is using this screen
     *          manager.
     */
    public static MHGameApplication getApplicationObject()
    {
        return app;
    }


    /**************************************************************************
     * Returns the singleton instance of the screen manager.
     * 
     * @return A reference to this screen manager object.
     */
    public static MHScreenManager getInstance()
    {
        if (instance == null)
            instance = new MHScreenManager();

        return instance;
    }


    /**************************************************************************
     * Updates the current screen by delegation.
     * 
     * @param elapsedTime The time elapsed in nanoseconds since the last call
     *                    to this method.
     */
    public void update(long elapsedTime)
    {
        currentScreen.update(elapsedTime);
    }


    /**************************************************************************
     * Renders the current screen by delegation.
     * 
     * @param backBuffer The buffer in memory on which to render the screen.
     */
    public void render(MHGraphicsCanvas backBuffer)
    {
        currentScreen.render(backBuffer);

        // DEBUG:
        if (MHGame.isDebugMode())
        {
        String fps = "FPS: " + MHGame.getFramesPerSecond();
        String ups = "UPS: " + MHGame.getUpdatesPerSecond();
        int x = 10;//backBuffer.getWidth() - MHFont.getDefaultFont().stringWidth(fps) - 10;
        int y = 20;//backBuffer.getHeight() - 10;
        showDebugText(fps, x, y, backBuffer);
        showDebugText(ups, x, y+16, backBuffer);
        // backBuffer.drawString(currentScreen.toString(), 50, 400);
        }
    }

    
    private void showDebugText(String text, int x, int y, MHGraphicsCanvas g)
    {
        g.setColor(MHColor.BLACK);
//        g.drawString(text, x-1, y-1);
//        g.drawString(text, x+1, y-1);
//        g.drawString(text, x-1, y+1);
//        g.drawString(text, x+1, y+1);
//        g.setColor(MHColor.WHITE);
        g.drawString(text, x, y);
    }
    

    /**************************************************************************
     * Unloads the current screen, replaces it with a new screen, and loads the
     * new screen.
     * 
     * @param newScreen The next screen to display.
     */
    public void changeScreen(MHScreen newScreen)
    {
        if (newScreen == null)
            newScreen = NULL_SCREEN;

        currentScreen.unload();
        currentScreen = newScreen;
        currentScreen.load();
    }


    public static int getDisplayWidth()
    {
        return (int) app.getDisplaySize().getX();
    }


    public static int getDisplayHeight()
    {
        return (int) app.getDisplaySize().getY();
    }


    public static MHVector getDisplayOrigin()
    {
        return app.getDisplayOrigin();
    }

    /**************************************************************************
     * Dummy screen inspired by the Null Object pattern to prevent exceptions
     * in the event that a developer fails to provide a valid screen
     * implementation upon instantiating the screen manager.
     * 
     * @author Michael Henson
     */
    private class NullScreen extends MHScreen
    {
        private MHColor textColor = MHPlatform.createColor(0, 255, 0, 255);
        private MHColor bgColor = MHPlatform.createColor(0, 0, 0, 255);

        public NullScreen()
        {
            super("No screens loaded.");
        }

        @Override
        public void update(long elapsedTime)
        {
        }

        @Override
        public void render(MHGraphicsCanvas g)
        {
            render(g, 10, 25);
        }


        public void render(MHGraphicsCanvas g, int x, int y)
        {
            g.fill(bgColor);
            g.setColor(textColor);
            g.drawString("No screen loaded.", x, y);
        }
    }

    
    private class SplashScreen extends MHScreen
    {
    	private MHScreen nextScreen = NULL_SCREEN;
        private MHColor textColor = MHPlatform.createColor(255, 255, 255, 255);
        private MHColor bgColor = MHPlatform.createColor(0, 0, 0, 255);
        private MHColor fadeColor = MHPlatform.createColor(0, 0, 0, 255);
        private int fadeValue = 255;
        private int fadeRate = 10;
        private int fadeState = 0;
        private MHBitmapImage logo;
        private int lx, ly;
        private long time;
        
    	public SplashScreen(MHScreen next)
    	{
    		super("MHFramework Splash Screen");
    		logo = MHResourceManager.getInstance().getImage("assets\\engine\\images\\MHFLogo-GlowWhite.png");
    		nextScreen = next;
    	}
    	
    	
        @Override
        public void update(long elapsedTime)
        {
        	switch (fadeState)
        	{
        	case 0:
        		fadeValue -= fadeRate;
        		if (fadeValue < 0)
        		{
        			fadeValue = 0;
        			fadeState = 1;
        			//nextScreen.load();
        			time = MHGame.getGameTimerValue();
        		}
        		break;
        	case 1:
        		nextScreen.load();
        		if (MHGame.getGameTimerValue() - time > 2000)
        			fadeState = 2;
        		break;
        	case 2:
        		fadeValue += fadeRate * 2;
        		if (fadeValue > 255)
        		{
        			fadeValue = 255;
        		}
        		break;
        	}
        	
        	fadeColor.setAlpha(fadeValue);
        	lx = MHScreenManager.getDisplayWidth()/2 - logo.getWidth()/2;
        	ly = MHScreenManager.getDisplayHeight()/2 - logo.getHeight()/2;
        }

        
        @Override
        public void render(MHGraphicsCanvas g)
        {
        	g.fill(bgColor);
        	g.setColor(textColor);
        	g.drawString("Powered By", lx, ly);
        	g.drawImage(logo, lx, ly);
        	g.fill(fadeColor);
        	
        	if (fadeValue >= 255)
        		MHScreenManager.getInstance().changeScreen(nextScreen);
        }
    }


	@Override
	public void onMouseDown(MHMouseTouchEvent e) 
	{
		currentScreen.onMouseDown(e);
	}


	@Override
	public void onMouseUp(MHMouseTouchEvent e) 
	{
		currentScreen.onMouseUp(e);
	}


	@Override
	public void onMouseMoved(MHMouseTouchEvent e) 
	{
		currentScreen.onMouseMoved(e);
	}


	@Override
	public void onKeyDown(MHKeyEvent e) 
	{
	    if (e.getKeyCode() == MHGlobalConstants.KEY_DEBUG())
	        MHGame.setDebugMode(!MHGame.isDebugMode());
	        
		currentScreen.onKeyDown(e);
	}


	@Override
	public void onKeyUp(MHKeyEvent e) 
	{
		currentScreen.onKeyUp(e);
	}


	@Override
	public void onButtonPressed(MHButton button, MHMouseTouchEvent e) 
	{
		currentScreen.onButtonPressed(button, e);
	}
}
