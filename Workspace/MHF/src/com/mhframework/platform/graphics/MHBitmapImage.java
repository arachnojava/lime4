package com.mhframework.platform.graphics;

import com.mhframework.core.math.MHVector;

public abstract class MHBitmapImage
{
    protected static long autoID = 0;
    protected MHBitmapImage(){}
    private MHVector size = new MHVector();
    protected String id;
    
    public int getWidth()
    {
        return (int)size.getX();
    }
    
    
    public int getHeight()
    {
        return (int)size.getY();
    }

    
    public String getImageID()
    {
        return id;
    }
    
    protected void setSize(int width, int height)
    {
        size.setVector(width, height);
    }
    
    
    public abstract MHGraphicsCanvas getGraphicsCanvas();
    
    public abstract void savePNG(String filename);

    public abstract MHBitmapImage rotate(double degrees);

    public abstract void redimension(int width, int height);
}
