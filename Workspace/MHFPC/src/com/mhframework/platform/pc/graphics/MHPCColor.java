package com.mhframework.platform.pc.graphics;

import java.awt.Color;
import com.mhframework.platform.graphics.MHColor;

public class MHPCColor extends MHColor
{

    private Color color;


    protected MHPCColor(int r, int g, int b, int a)
    {
        super(r, g, b, a);
        color = new Color(r, g, b, a);
    }

    
    public static MHPCColor create(int r, int g, int b, int a)
    {
        return new MHPCColor(r, g, b, a);
    }
    
    
    public Color getColor()
    {
        return color;
    }


    @Override
    public int getRed()
    {
        return color.getRed();
    }


    @Override
    public void setRed(int red)
    {
        super.setRed(red);
        color = new Color(red, getGreen(), getBlue(), getAlpha());
    }


    @Override
    public int getGreen()
    {
        return color.getGreen();
    }


    @Override
    public void setGreen(int green)
    {
        color = new Color(getRed(), green, getBlue(), getAlpha());
    }


    @Override
    public int getBlue()
    {
        return color.getBlue();
    }


    @Override
    public void setBlue(int blue)
    {
        color = new Color(getRed(), getGreen(), blue, getAlpha());
    }


    @Override
    public int getAlpha()
    {
        return color.getAlpha();
    }


    @Override
    public void setAlpha(int alpha)
    {
        color = new Color(getRed(), getGreen(), getBlue(), alpha);
    }
}
