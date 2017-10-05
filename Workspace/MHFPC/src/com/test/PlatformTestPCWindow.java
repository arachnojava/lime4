package com.test;

//package com.botz;


import javax.swing.JFrame;

import com.mhframework.MHScreen;
import com.mhframework.MHVideoSettings;
import com.mhframework.platform.pc.MHPCPlatform;
import com.test.TestScreen;


public class PlatformTestPCWindow
{
    public static void main(String[] args)
    {
        // The only requirements for a programmer to use MHFramework
        // are these:
        
        // 1.  One screen (minimum).  If you do not provide one, the
        //     Screen manager will create a placeholder. Of course,
        //     your app will have no UI in this case.
        MHScreen startingScreen = new TestScreen();
        TestScreen.IMAGES_DIR = "assets/images/";

        
        // 2.  Video settings to determine screen resolution, etc.
        MHVideoSettings displaySettings = new MHVideoSettings();
        displaySettings.displayWidth = 800;
        displaySettings.displayHeight = 480;

        // 3.  Pass the above two things into the engine along with
        //     the window the app will run in.
        MHPCPlatform.run(new JFrame(), startingScreen, displaySettings);
    }
}

