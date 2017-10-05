package com.mhframework.platform.android;

import android.app.Activity;

import com.mhframework.MHGame;
import com.mhframework.MHScreen;
import com.mhframework.MHScreenManager;
import com.mhframework.MHVideoSettings;
import com.mhframework.platform.MHGameApplication;
import com.mhframework.platform.MHPlatform;

public class MHAndroidPlatform extends MHPlatform
{
    public static MHGameApplication createApplication(Activity activity, final MHScreen startingScreen, final MHVideoSettings displaySettings)
    {
        factory = MHAndroidFactory.getInstance();
        MHScreenManager.getInstance().setStartScreen(startingScreen);
        return MHAndroidApplication.create(activity, displaySettings);
    }
    
    
    /**************************************************************************
     * Launch an MHFramework application as an Android activity using
     * the supplied screen. Video settings are defaulted to 720p.
     */
    public static void run(Activity activity, final MHScreen startingScreen)
    {
        run(activity, startingScreen, new MHVideoSettings());
    }
    
    


    /**************************************************************************
     * Launch an MHFramework application as an Android activity using
     * the supplied screen and video settings.
     */
    public static void run(Activity activity, MHScreen startingScreen,
            MHVideoSettings displaySettings)
    {
        app = createApplication(activity, startingScreen, displaySettings);
        //MHFramework.launchGame(app);
        new MHGame(app);
    }
    
    
    public static MHAndroidApplication getApp()
    {
    	return (MHAndroidApplication) app;
    }
}
