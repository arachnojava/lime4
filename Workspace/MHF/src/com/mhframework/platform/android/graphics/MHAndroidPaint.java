package com.mhframework.platform.android.graphics;

import android.graphics.Paint;
import com.mhframework.platform.graphics.MHColor;

public class MHAndroidPaint extends MHColor
{

    private Paint color;


    protected MHAndroidPaint(int r, int g, int b, int a)
    {
        super(r, g, b, a);
        color = new Paint();
        color.setARGB(a, r, g, b);
        color.setAntiAlias(true);
    }

    
    public static MHAndroidPaint create(int r, int g, int b, int a)
    {
        return new MHAndroidPaint(r, g, b, a);
    }
    
    
    public Paint getPaint()
    {
        return color;
    }


    @Override
    public void setRed(int red)
    {
        super.setRed(red);
        color.setARGB(getAlpha(), red, getGreen(), getBlue());
    }


    @Override
    public void setGreen(int green)
    {
        super.setGreen(green);
        color.setARGB(getAlpha(), getRed(), green, getBlue());
    }


    @Override
    public void setBlue(int blue)
    {
        super.setBlue(blue);
        color.setARGB(getAlpha(), getRed(), getGreen(), blue);
    }


    @Override
    public int getAlpha()
    {
        return color.getAlpha();
    }


    @Override
    public void setAlpha(int alpha)
    {
        super.setAlpha(alpha);
        color.setARGB(alpha, getRed(), getGreen(), getBlue());
    }
}
