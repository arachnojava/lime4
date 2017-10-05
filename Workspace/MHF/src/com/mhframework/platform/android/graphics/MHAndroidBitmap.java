package com.mhframework.platform.android.graphics;


import java.io.IOException;
import java.io.InputStream;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.mhframework.platform.android.MHAndroidApplication;
import com.mhframework.platform.graphics.MHBitmapImage;
import com.mhframework.platform.graphics.MHGraphicsCanvas;


public class MHAndroidBitmap extends MHBitmapImage
{
    private Bitmap bitmap;
    private MHAndroidCanvas canvas;
    
    private MHAndroidBitmap(String filename) 
    {
    	try
    	{
    		AssetManager assetManager = MHAndroidApplication.getActivity().getAssets();
    		InputStream inputStream = assetManager.open(filename);
    		bitmap = BitmapFactory.decodeStream(inputStream).copy(Bitmap.Config.ARGB_8888, true);
    		inputStream.close();
            canvas = new MHAndroidCanvas(bitmap);
            this.setSize(bitmap.getWidth(), bitmap.getHeight());
            id = filename;
    	}
    	catch (IOException ioe)
    	{
    	    System.err.println("ERROR: " + ioe.toString());
    	}
	}
    
    
    private MHAndroidBitmap(int width, int height) 
    {
		bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        canvas = new MHAndroidCanvas(bitmap);
        this.setSize(bitmap.getWidth(), bitmap.getHeight());
        id = "" + autoID++;
	}


	public static MHAndroidBitmap create(String filename)
    {
    	return new MHAndroidBitmap(filename);
    }


	public static MHBitmapImage create(int width, int height) 
	{
		return new MHAndroidBitmap(width, height);
	}
	
	
	public Bitmap getBitmap()
    {
        return bitmap;
    }
	
	
    @Override
    public MHGraphicsCanvas getGraphicsCanvas()
    {
        return canvas;
    }


	@Override
	public void savePNG(String filename) 
	{
		// TODO Figure out how to save an image file in Android.		
	}


    @Override
    public MHBitmapImage rotate(double d)
    {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public void redimension(int width, int height)
    {
        // TODO Auto-generated method stub
        
    }
}
