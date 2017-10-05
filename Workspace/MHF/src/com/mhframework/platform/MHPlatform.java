package com.mhframework.platform;

import com.mhframework.core.math.MHVector;
import com.mhframework.platform.event.MHInputEventHandler;
import com.mhframework.platform.event.MHKeyCodes;
import com.mhframework.platform.event.MHKeyListener;
import com.mhframework.platform.event.MHMouseTouchListener;
import com.mhframework.platform.graphics.MHBitmapImage;
import com.mhframework.platform.graphics.MHColor;
import com.mhframework.platform.graphics.MHFont;


public abstract class MHPlatform
{
    protected static MHPlatformFactory factory;
    protected static MHGameApplication app;

    
    public static MHVector getDisplayOrigin()
    {
        if (app == null)
            return new MHVector(0,0);
        
        return app.getDisplayOrigin();
    }


    public static MHBitmapImage createImage(int width, int height)
    {
        return factory.createImage(width, height);
    }


    public static MHBitmapImage createImage(String filename)
    {
        return factory.createImage(filename);
    }


    public static MHColor createColor(int r, int g, int b, int a)
    {
        return factory.createColor(r, g, b, a);
    }

    
    public static MHColor createColor(int r, int g, int b)
    {
        return createColor(r, g, b, 255);
    }


    public static void addMouseTouchListener(MHMouseTouchListener handler)
    {
    	MHInputEventHandler.getInstance().addMouseTouchListener(handler);
    }

    
    public static void removeMouseTouchListener(MHMouseTouchListener handler)
    {
    	MHInputEventHandler.getInstance().removeMouseTouchListener(handler);
    }

    
    public static void addKeyListener(MHKeyListener handler)
    {
    	MHInputEventHandler.getInstance().addKeyListener(handler);
    }
    
    
    public static void removeKeyListener(MHKeyListener handler)
    {
    	MHInputEventHandler.getInstance().removeKeyListener(handler);
    }
    
    
    public static MHSoundManager getSoundManager()
    {
    	return factory.getSoundManager();
    }


    public static MHKeyCodes getKeyCodes()
    {
        return factory.getKeyCodes();
    }


    public static MHFont createFont(String ttfFile)
    {
        return factory.createFont(ttfFile);
    }
}
