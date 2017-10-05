package com.mhframework.platform.pc;

import javax.swing.JFrame;

import com.mhframework.MHGame;
import com.mhframework.MHScreen;
import com.mhframework.MHScreenManager;
import com.mhframework.MHVideoSettings;
import com.mhframework.platform.MHGameApplication;
import com.mhframework.platform.MHPlatform;

public abstract class MHPCPlatform extends MHPlatform
{

    public static MHGameApplication createApplication(JFrame frame, final MHScreen startingScreen, final MHVideoSettings displaySettings)
    {
        factory = MHPCFactory.getInstance();
        if (displaySettings.showSplashScreen)
        	MHScreenManager.getInstance().initSplashScreen(startingScreen);
        else
        	MHScreenManager.getInstance().setStartScreen(startingScreen);
        return MHPCApplication.create(frame, displaySettings);
    }
    
    
    /**************************************************************************
     * Launch an MHFramework application in a JFrame using the 
     * supplied screen.  Video settings are defaulted to 720p.
     */
    public static void run(JFrame frame, final MHScreen startingScreen)
    {
        run(frame, startingScreen, new MHVideoSettings());
    }
    
    
    /**************************************************************************
     * Launch an MHFramework application in a JFrame using the 
     * supplied screen and video settings.
     */
    public static void run(JFrame frame, final MHScreen startingScreen, final MHVideoSettings displaySettings)
    {
        app = MHPCPlatform.createApplication(frame, startingScreen, displaySettings);
        new MHGame(app);
        //System.exit(0);
    }
}
