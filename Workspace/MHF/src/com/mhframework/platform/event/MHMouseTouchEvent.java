package com.mhframework.platform.event;

import com.mhframework.MHScreen;
import com.mhframework.MHScreenManager;
import com.mhframework.core.math.MHVector;

public class MHMouseTouchEvent
{
    private int x;
    private int y;
    private boolean rightClick = false;
    private boolean handled = false;
    private MHScreen screen;
    
    public MHMouseTouchEvent(int x, int y)
    {
    	this(x, y, false, MHScreenManager.getInstance().getCurrentScreen());
    }
    
    
    public MHMouseTouchEvent(int x, int y, boolean rightClick, MHScreen eventSite)
    {
        this.x = x;
        this.y = y;
        this.rightClick = rightClick;
        this.screen = eventSite;
    }
    
    
    public int getX()
    {
        return x;
    }
    
    
    public int getY()
    {
        return y;
    }


    public MHVector getPoint()
    {
        return new MHVector(x, y);
    }


	public boolean isRightClick() 
	{
		return rightClick;
	}


	public boolean isHandled() 
	{
		return handled;
	}


	public void setHandled(boolean handled) 
	{
		this.handled = handled;
	}
	
	
	public MHScreen getEventSite()
	{
		return screen;
	}
}
