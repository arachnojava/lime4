package com.mhframework.platform.android.graphics;

import java.util.Hashtable;

import android.content.res.AssetManager;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.Log;

import com.mhframework.MHGame;
import com.mhframework.core.math.MHVector;
import com.mhframework.core.math.geom.MHRectangle;
import com.mhframework.platform.android.MHAndroidApplication;
import com.mhframework.platform.graphics.MHFont;
import com.mhframework.platform.graphics.MHGraphicsCanvas;

public class MHAndroidFont extends MHFont 
{
	public static final String FONT_DIR = "fonts/";
	private Typeface font;
	//private FontMetrics fontMetrics;
    private final static Hashtable<String, Typeface> cache = new Hashtable<String, Typeface>();

	
	public MHAndroidFont(String fontName)
	{
		super(fontName, MHFont.Type.TTF);
		font = getFont(fontName);
		this.setHeight(DEFAULT_SIZE);
	}
	

	
	@Override
	public void drawString(MHGraphicsCanvas g, String text, double x, double y) 
	{
		g.setFont(this);
		g.drawString(text, (int)x, (int)y);
	}

	
	@Override
	public MHVector centerOn(MHRectangle r, MHGraphicsCanvas g, String text) 
	{
		
		return null;
	}

	
	@Override
	public int getHeight() 
	{
		return (int)(((MHAndroidCanvas)MHGame.getBackBuffer()).getPaint().getTextSize());
	}

	
	@Override
	public int stringWidth(String text) 
	{
		Rect bounds = new Rect();
		((MHAndroidCanvas)MHGame.getBackBuffer()).getPaint().getTextBounds(text, 0, text.length(), bounds);
		return bounds.width();
	}

	
	@Override
	public void setHeight(int fontSize) 
	{
		((MHAndroidCanvas)MHGame.getBackBuffer()).getPaint().setTextSize(fontSize);
	}

	
	@Override
	public MHFont clone() 
	{
		return new MHAndroidFont(this.getFontName());
	}
	
	
    

	// This is a workaround to a known Android OS bug documented at
	// http://stackoverflow.com/questions/12766930/native-typeface-cannot-be-made-only-for-some-people
    private static Typeface getFont(String name) 
    {
        synchronized (cache)
        {
        	AssetManager assets = MHAndroidApplication.getView().getContext().getAssets();
        	String path = MHAndroidFont.FONT_DIR + name;

            if (!cache.containsKey(path)) 
            {
                try 
                {
                    Typeface t = Typeface.createFromAsset(assets, path);
                    cache.put(path, t);
                } 
                catch (Exception e) 
                {
                    Log.e("MHAndroidFont.getFont()", 
                    	  "Could not get typeface '" + path + "' because " + e.getMessage());
                    return null;
                }
            }
            return cache.get(path);
        }
    }



	public Typeface getTypeface() 
	{
		return font;
	}

}
