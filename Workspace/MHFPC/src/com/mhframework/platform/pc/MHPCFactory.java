package com.mhframework.platform.pc;

import javax.swing.JFrame;

import com.mhframework.MHScreen;
import com.mhframework.MHScreenManager;
import com.mhframework.MHVideoSettings;
import com.mhframework.platform.MHGameApplication;
import com.mhframework.platform.MHPlatformFactory;
import com.mhframework.platform.MHSoundManager;
import com.mhframework.platform.event.MHKeyCodes;
import com.mhframework.platform.graphics.MHBitmapImage;
import com.mhframework.platform.graphics.MHColor;
import com.mhframework.platform.graphics.MHFont;
import com.mhframework.platform.graphics.MHGraphicsCanvas;
import com.mhframework.platform.pc.graphics.MHPCColor;
import com.mhframework.platform.pc.graphics.MHPCFont;
import com.mhframework.platform.pc.graphics.MHPCGraphics;
import com.mhframework.platform.pc.graphics.MHPCImage;

public class MHPCFactory implements MHPlatformFactory
{
    private static MHPlatformFactory INSTANCE;
	private MHSoundManager soundManager;
	private MHPCKeyCodes keyCodes;
    
    
    private MHPCFactory()
    {
        
    }
    
    
    public static MHPlatformFactory getInstance()
    {
        if (INSTANCE == null)
            INSTANCE = new MHPCFactory();
        
        return INSTANCE;
    }
    

    public MHBitmapImage createImage(int width, int height)
    {
        return MHPCImage.create(width, height);
    }


    public MHBitmapImage createImage(String filename)
    {
        return MHPCImage.create(filename);
    }


    public MHColor createColor(int r, int g, int b, int a)
    {
        return MHPCColor.create(r, g, b, a);
    }


    public MHGraphicsCanvas createGraphicsCanvas(int width, int height)
    {
        return MHPCGraphics.create(width, height);
    }

    
	@Override
	public MHSoundManager getSoundManager() 
	{
		if (soundManager == null)
			soundManager = new MHPCSoundManager();
		
		return soundManager;
	}


    @Override
    public MHKeyCodes getKeyCodes()
    {
        if (keyCodes == null)
            keyCodes = new MHPCKeyCodes();
        
        return keyCodes;
    }


    @Override
    public MHFont createFont(String fontName)
    {
        return new MHPCFont(fontName);
    }
    


    
    public static MHGameApplication createApplication(JFrame frame, final MHScreen startingScreen, final MHVideoSettings displaySettings)
    {
        //factory = MHPCFactory.getInstance();
        MHScreenManager.getInstance().setStartScreen(startingScreen);
        return MHPCApplication.create(frame, displaySettings);
    }
    
}