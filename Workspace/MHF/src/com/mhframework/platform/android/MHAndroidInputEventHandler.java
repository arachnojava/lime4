package com.mhframework.platform.android;

import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;

import com.mhframework.core.math.MHVector;
import com.mhframework.platform.MHPlatform;
import com.mhframework.platform.event.MHInputEventHandler;
import com.mhframework.platform.event.MHKeyEvent;
import com.mhframework.platform.event.MHMouseTouchEvent;

public class MHAndroidInputEventHandler implements OnKeyListener, OnTouchListener
{
    public MHAndroidInputEventHandler(View appView)
    {
    	appView.setOnTouchListener(this);
    	appView.setOnKeyListener(this);
    }

    
    private MHKeyEvent parseKeyEvent(KeyEvent e)
    {
        int code = e.getKeyCode();
        boolean shift = e.isShiftPressed();
        boolean control = e.isCtrlPressed();
        boolean alt = e.isAltPressed();
        
        return new MHKeyEvent(code, shift, control, alt);
    }
    
    
    private MHMouseTouchEvent parseMotionEvent(MotionEvent e)
    {
        MHVector v = MHPlatform.getDisplayOrigin();
        int x = (int)(e.getX()-v.getX());
        int y = (int)(e.getY()-v.getY());
    	return new MHMouseTouchEvent(x, y);
    }
    
    
	@Override
	public boolean onTouch(View v, MotionEvent e) 
	{
		 switch (e.getAction()) 
		 {
	        case MotionEvent.ACTION_DOWN:
	        	MHInputEventHandler.getInstance().onMouseDown(parseMotionEvent(e));
	            break;
	        case MotionEvent.ACTION_MOVE:
	        	MHInputEventHandler.getInstance().onMouseMoved(parseMotionEvent(e));
	            break;
	        case MotionEvent.ACTION_CANCEL:
	        	// Do we need to handle this?
	            break;
	        case MotionEvent.ACTION_UP:
	        	MHInputEventHandler.getInstance().onMouseUp(parseMotionEvent(e));
	            break;	
	            
		 }
		 return true;
	}


	@Override
	public boolean onKey(View v, int i, KeyEvent e) 
	{
		 switch (e.getAction()) 
		 {
	        case KeyEvent.ACTION_DOWN:
	            MHInputEventHandler.getInstance().onKeyDown(parseKeyEvent(e));
	            break;
	        case KeyEvent.ACTION_UP:
	            MHInputEventHandler.getInstance().onKeyUp(parseKeyEvent(e));
	            break;
	     }
         return true;
     }
}
