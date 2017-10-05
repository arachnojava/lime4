package com.mhframework.platform.android;

import com.mhframework.platform.MHPlatformFactory;
import com.mhframework.platform.MHSoundManager;
import com.mhframework.platform.android.graphics.MHAndroidBitmap;
import com.mhframework.platform.android.graphics.MHAndroidCanvas;
import com.mhframework.platform.android.graphics.MHAndroidFont;
import com.mhframework.platform.android.graphics.MHAndroidPaint;
import com.mhframework.platform.event.MHKeyCodes;
import com.mhframework.platform.graphics.MHBitmapImage;
import com.mhframework.platform.graphics.MHColor;
import com.mhframework.platform.graphics.MHFont;
import com.mhframework.platform.graphics.MHGraphicsCanvas;

public class MHAndroidFactory implements MHPlatformFactory
{

    private static MHAndroidFactory INSTANCE;
	private MHSoundManager soundManager;
	private MHKeyCodes keyCodes;

    private MHAndroidFactory()
    {
        
    }

    public MHBitmapImage createImage(int width, int height)
    {
    	return MHAndroidBitmap.create(width, height);
    }


    public MHBitmapImage createImage(String filename)
    {
    	return MHAndroidBitmap.create(filename);
    }


    public MHColor createColor(int r, int g, int b, int a)
    {
        return MHAndroidPaint.create(r, g, b, a);
    }


    public MHGraphicsCanvas createGraphicsCanvas(int width, int height)
    {
        return MHAndroidCanvas.create(width, height);
    }


    public static MHPlatformFactory getInstance()
    {
        if (INSTANCE == null)
            INSTANCE = new MHAndroidFactory();
        
        return INSTANCE;
    }

    
	@Override
	public MHSoundManager getSoundManager() 
	{
		if (soundManager == null)
			soundManager = new MHAndroidSoundManager();
		
		return soundManager;
	}

	
    @Override
    public MHKeyCodes getKeyCodes()
    {
        if (keyCodes == null)
            keyCodes = new MHAndroidKeyCodes();
        
        return keyCodes;
    }

    
    /**************************************************************************
     * TODO:  Consider calling this from MHResourceManager.
     */
    @Override
    public MHFont createFont(String fontName)
    {
        return new MHAndroidFont(fontName);
    }
}
