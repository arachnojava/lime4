package com.mhframework.platform.event;

import java.util.ArrayList;

import com.mhframework.core.math.MHVector;

/******************************************************************************
 * Observable singleton that consolidates low-level user input events.
 * 
 * <p>In MHFramework 3 and up, any object can be an event listener.  It only
 * needs to implement the appropriate listener interface (<code>MHKeyListener</code>,
 * <code>MHMouseTouchListener</code>, etc.) and then register
 * itself with this class by calling <code>MHInputEventHandler.addKeyListener()</code>,
 * <code>MHInputEventHandler.addMouseTouchListener()</code>, etc.
 * 
 * @since 3.0
 * @author Michael Henson
 *
 */
public class MHInputEventHandler implements MHMouseTouchListener, MHKeyListener
{
    private static MHInputEventHandler instance;
    private ArrayList<MHMouseTouchListener> mouseListeners;
    private ArrayList<MHKeyListener> keyListeners;
    
    private MHVector mousePosition = new MHVector();
    
    private MHInputEventHandler()
    {
        mouseListeners = new ArrayList<MHMouseTouchListener>();
        keyListeners = new ArrayList<MHKeyListener>();
    }
    
    
    public static MHInputEventHandler getInstance()
    {
        if (instance == null)
            instance = new MHInputEventHandler();
        
        return instance;
    }
    
    
    public void addKeyListener(MHKeyListener observer)
    {
        keyListeners.add(observer);
    }
    

    public void removeKeyListener(MHKeyListener observer)
    {
        keyListeners.remove(observer);
    }
    

    public void addMouseTouchListener(MHMouseTouchListener observer)
    {
        mouseListeners.add(observer);
    }
    

    public void removeMouseTouchListener(MHMouseTouchListener observer)
    {
        mouseListeners.remove(observer);
    }
    

    public void onKeyDown(MHKeyEvent e)
    {
        for (MHKeyListener observer : keyListeners)
            observer.onKeyDown(e);
    }


    public void onKeyUp(MHKeyEvent e)
    {
        for (MHKeyListener observer : keyListeners)
            observer.onKeyUp(e);
    }


    public void onMouseDown(MHMouseTouchEvent e)
    {
    	mousePosition = e.getPoint();
    	
        for (MHMouseTouchListener observer : mouseListeners)
        {
            if (e.isHandled())
            	return;
            observer.onMouseDown(e);
        }
    }


    public void onMouseUp(MHMouseTouchEvent e)
    {
    	mousePosition = e.getPoint();
    	
    	for (int i = 0; i < mouseListeners.size(); i++)
    	{
            if (e.isHandled())
            	return;
            mouseListeners.get(i).onMouseUp(e);
    	}
    }


    public void onMouseMoved(MHMouseTouchEvent e)
    {
    	mousePosition = e.getPoint();
    	
        for (MHMouseTouchListener observer : mouseListeners)
            observer.onMouseMoved(e);
    }


	public MHVector getMousePosition() 
	{
		return mousePosition;
	}
}
