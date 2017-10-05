package com.mhframework.ui;

import com.mhframework.core.math.MHVector;
import com.mhframework.platform.MHPlatform;
import com.mhframework.platform.event.MHMouseTouchEvent;
import com.mhframework.platform.event.MHMouseTouchListener;
import com.mhframework.platform.graphics.MHBitmapImage;
import com.mhframework.platform.graphics.MHColor;
import com.mhframework.platform.graphics.MHGraphicsCanvas;

public class MHMouseCursor implements MHMouseTouchListener
{
    public enum Type
    {
        CROSSHAIR, CIRCLE, HIDDEN;
    }
 
    //private Type type = Type.CROSSHAIR;
    protected String imageID;
    protected int x, y;
    private MHMouseCursorRenderer renderer;
    //private MHVector hotspot = new MHVector(0,0);
    
    public MHMouseCursor(Type type)
    {
        MHPlatform.addMouseTouchListener(this);
        //this.type = type;

        changeCursor(type);
    }

    
    public void changeCursor(MHBitmapImage image, int hotspotX, int hotspotY)
    {
        renderer = new CustomImage(image, hotspotX, hotspotY);
    }

    
    public void changeCursor(Type type)
    {
        switch (type)
        {
            case CROSSHAIR:
                renderer = new Crosshair();
                break;
            case CIRCLE:
                break;
            case HIDDEN:
                break;
            default:
                break;
        }
    }
    
    
    public void render(MHGraphicsCanvas g)
    {
        renderer.render(g, x, y);
    }


    @Override
    public void onMouseDown(MHMouseTouchEvent e)
    {
        x = e.getX();
        y = e.getY();
    }


    @Override
    public void onMouseUp(MHMouseTouchEvent e)
    {
        x = e.getX();
        y = e.getY();
    }


    @Override
    public void onMouseMoved(MHMouseTouchEvent e)
    {
        x = e.getX();
        y = e.getY();
    }
}


class CustomImage implements MHMouseCursorRenderer
{
	private MHBitmapImage image;
	private MHVector hotspot;

	
	public CustomImage(MHBitmapImage image, int hotspotX, int hotspotY)
	{
		this.image = image;
		hotspot = new MHVector(hotspotX, hotspotY);
	}
	

    @Override
    public void render(MHGraphicsCanvas g, int x, int y)
    {
    	int cx = (int)(x - hotspot.x);
    	int cy = (int)(y - hotspot.y);
    	g.drawImage(image, cx, cy);
    }
}


class Crosshair implements MHMouseCursorRenderer
{
    private static final int SIZE = 8;
    @Override
    public void render(MHGraphicsCanvas g, int x, int y)
    {
    	g.setColor(MHColor.BLACK);
        g.drawLine(x+1, y-SIZE+1, x+1, y+SIZE+1);
        g.drawLine(x-SIZE+1, y+1, x+SIZE+1, y+1);
    	
    	g.setColor(MHColor.WHITE);
        g.drawLine(x, y-SIZE, x, y+SIZE);
        g.drawLine(x-SIZE, y, x+SIZE, y);
    }


}