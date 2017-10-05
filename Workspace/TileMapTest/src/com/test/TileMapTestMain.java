package com.test;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import com.mhframework.MHVideoSettings;
import com.mhframework.platform.pc.MHPCPlatform;

public class TileMapTestMain
{
    public static void main(String[] args)
    {
        MHVideoSettings settings = new MHVideoSettings();
        //Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        //settings.fullScreen = true;
        settings.fullScreen = false;
        settings.displayWidth = 800;//(int)(screenSize.width * 0.75);
        settings.displayHeight = 600;//(int)(screenSize.height * 0.75);
        settings.showSplashScreen = false;
        settings.windowCaption = "Tile Map Test";

        MHPCPlatform.run(new JFrame(), new TileMapTestScreen(), settings);
    }

}
