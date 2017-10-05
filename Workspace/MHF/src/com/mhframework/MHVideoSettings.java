package com.mhframework;

/********************************************************************
 * Class used for requesting specific video mode parameters.
 * Think of it as a bean, albeit an improper one.
 * 
 * @author Michael Henson
 *
 */
public class MHVideoSettings
{
    // 720p = 1280×720
    // 1080p = 1920×1080
    public int displayWidth=1280, displayHeight=720, bitDepth=32;
    public boolean showSplashScreen = true;

    public String windowCaption = "MHFramework Application";

    public boolean fullScreen = false;
    
    
}
