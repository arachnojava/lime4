package com.mhframework;

import com.mhframework.core.math.physics.MHPhysicsCore;
import com.mhframework.platform.event.MHKeyEvent;
import com.mhframework.platform.event.MHMouseTouchEvent;
import com.mhframework.platform.graphics.MHBitmapImage;
import com.mhframework.platform.graphics.MHGraphicsCanvas;
import com.mhframework.ui.MHButton;
import com.mhframework.ui.MHGuiComponent;
import com.mhframework.ui.MHGuiComponentList;
import com.mhframework.ui.MHLabel;
import com.mhframework.ui.MHMouseCursor;

/********************************************************************
 * Abstract class for deriving game screens.  
 */
public abstract class MHScreen implements MHRenderable
{
    private String description;
    private MHGuiComponentList components;
    private MHMouseCursor mouseCursor = new MHMouseCursor(MHMouseCursor.Type.CROSSHAIR);

    // Status bar variables.
    private MHLabel statusBar;
    private int statusBarHeight = 22;
	private MHScreen previousScreen;
    
    /****************************************************************
     * Default constructor.
     */
    public MHScreen()
    {
        this("");
    }
    
    
    /****************************************************************
     * Overloaded constructor taking an optional descriptive string
     * that may be used for a variety of purposes, such as a screen
     * title or a mapping in a key/value pair.
     */
    public MHScreen(String description)
    {
        setDescription(description);
        components = new MHGuiComponentList();
    }
    
 
    /****************************************************************
     * Return the description string of this screen.
     * 
     * @return The description string of this screen
     */
    public String getDescription()
    {
        return description;
    }

    
    /****************************************************************
     * Set the description string of this screen.
     * 
     * @param description A descriptive string for this screen.
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    
    /**************************************************************************
     * Adds a GUI component to this screen.
     * 
     * @param c The component to add.
     */
    public void add(MHGuiComponent c)
    {
        components.add(c);
    }
    
    
    /****************************************************************
     * Called automatically by <code>MHScreenManager</code>, this
     * method is intended to be overridden to perform any 
     * initialization that needs to happen after the screen's 
     * instantiation but prior to the screen being displayed.  The 
     * base implementation in <code>MHScreen</code> is just an empty 
     * hook.
     */
    public void load() {}
    
    
    /****************************************************************
     * Called automatically by <code>MHScreenManager</code>, this
     * method is intended to be overridden to perform any cleanup or
     * finalization required for a screen.  The base implementation 
     * in <code>MHScreen</code> is just an empty hook.
     */ 
    public void unload() {}

    
    /****************************************************************
     * Method for the update phase of the game loop.  It is 
     * called automatically by <code>MHScreenManager</code>.
     * <p>This implementation invokes the updates in the physics core
     * and GUI system.  If your application needs those things, then
     * your screen's <code>update</code> method should call this one.
     */
    public void update(long elapsedTime)
    {
        MHPhysicsCore.getInstance().update(elapsedTime);
        components.update(elapsedTime);
    }

    
    /****************************************************************
     * Method for rendering the screen to the sent graphics
     * canvas.  It is called automatically by 
     * <code>MHScreenManager</code> as part of the game loop.
     * Since the implementation in this class renders the GUI, any
     * screen containing a GUI should call this method in their
     * override, and it should generally be called last.
     */
    public void render(MHGraphicsCanvas g)
    {
        components.render(g);
        mouseCursor.render(g);
    }

    
    /**************************************************************************
     * Render onto the sent graphics canvas at the specified screen 
     * coordinates.
     * 
     * @param g The graphics canvas on which to render.
     * @param x The x coordinate in view space.
     * @param y The y coordinate in view space.
     */
    public void render(MHGraphicsCanvas g, int x, int y)
    {
    	render(g);
    }

    
    protected MHMouseCursor getMouseCursor()
    {
    	return mouseCursor;
    }
    
    
    protected MHLabel getStatusBar()
    {
    	if (statusBar == null)
    	{
    		statusBar = MHLabel.create("");
    		statusBar.setBounds(0, MHScreenManager.getDisplayHeight()-this.statusBarHeight, MHScreenManager.getDisplayWidth()-1, this.statusBarHeight-1);
    		statusBar.setVisible(true);
    		statusBar.setAlignment(MHLabel.ALIGN_LEFT);
    		this.add(statusBar);
    	}
    	
    	return statusBar;
    }
    
    
    public void setStatusBarText(String text)
    {
    	getStatusBar().setText(text);
    }
    
    
    public int getStatusBarHeight()
    {
    	return getStatusBar().getHeight();
    }
    
    
    public void setStatusBarVisible(boolean v)
    {
    	getStatusBar().setVisible(v);
    }

    
    public static void tileImage(final MHGraphicsCanvas g, final MHBitmapImage image, int x, int y)
    {
        while (x > 0) x -= image.getWidth();
        while (y > 0) y -= image.getHeight();

        int cx = x;
        int cy = y;
        final int scrWidth = MHScreenManager.getDisplayWidth();
        final int scrHeight = MHScreenManager.getDisplayHeight();
        final int imgWidth = image.getWidth();
        final int imgHeight = image.getHeight();

        while (cy < scrHeight+imgHeight)
        {
            while (cx < scrWidth+imgWidth)
            {
                g.drawImage(image, cx, cy);
                cx += imgWidth;
            }
            cx = x;
            cy += imgHeight;
        }
    }


	public MHGuiComponentList getGuiComponents() 
	{
		return components;
	}


	
	public void onMouseDown(MHMouseTouchEvent e) 
	{
		components.onMouseDown(e);
	}


	public void onMouseUp(MHMouseTouchEvent e) 
	{
		components.onMouseUp(e);
	}


	public void onMouseMoved(MHMouseTouchEvent e) 
	{
		components.onMouseMoved(e);
	}


	public void onKeyDown(MHKeyEvent e) 
	{
		components.onKeyDown(e);
	}


	public void onKeyUp(MHKeyEvent e) 
	{
		components.onKeyUp(e);
	}


	public void onButtonPressed(MHButton button, MHMouseTouchEvent e) 
	{
	}


	public MHScreen getPreviousScreen() 
	{
		return previousScreen;
	}
	
	public void setPreviousScreen(MHScreen previous)
	{
		previousScreen = previous;
	}
}
