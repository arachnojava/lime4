package com.mhframework.platform.graphics;

import com.mhframework.platform.MHPlatform;


public abstract class MHColor
{
    private int red, green, blue, alpha;
    
    protected MHColor(int r, int g, int b, int a)
    {
        this.red = r;
        this.green = g;
        this.blue = b;
        this.alpha = a;
    }
    
    
    public int getRed()
    {
        return red;
    }


    public void setRed(int red)
    {
        this.red = red;
    }


    public int getGreen()
    {
        return green;
    }


    public void setGreen(int green)
    {
        this.green = green;
    }


    public int getBlue()
    {
        return blue;
    }


    public void setBlue(int blue)
    {
        this.blue = blue;
    }


    public int getAlpha()
    {
        return alpha;
    }


    public void setAlpha(int alpha)
    {
        this.alpha = alpha;
    }


    // Predefined colors.
    public static final MHColor RED        = MHPlatform.createColor(255,   0,   0);
    public static final MHColor GREEN      = MHPlatform.createColor(  0, 255,   0);
    public static final MHColor BLUE       = MHPlatform.createColor(  0,   0, 255);
    public static final MHColor CYAN       = MHPlatform.createColor(  0, 255, 255);
    public static final MHColor MAGENTA    = MHPlatform.createColor(255,   0, 255);
    public static final MHColor YELLOW     = MHPlatform.createColor(255, 255,   0);
    public static final MHColor BLACK      = MHPlatform.createColor(  0,   0,   0);
    public static final MHColor WHITE      = MHPlatform.createColor(255, 255, 255);
    public static final MHColor LIGHT_GRAY = MHPlatform.createColor(192, 192, 192);
    public static final MHColor GRAY       = MHPlatform.createColor(128, 128, 128);
    public static final MHColor DARK_GRAY  = MHPlatform.createColor( 64,  64,  64);
}

