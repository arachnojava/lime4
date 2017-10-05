package com.mhframework.platform.android.graphics;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;

import com.mhframework.core.math.geom.MHRectangle;
import com.mhframework.platform.graphics.MHBitmapImage;
import com.mhframework.platform.graphics.MHColor;
import com.mhframework.platform.graphics.MHFont;
import com.mhframework.platform.graphics.MHGraphicsCanvas;

public class MHAndroidCanvas extends MHGraphicsCanvas
{
    private Paint paint;
    private Canvas canvas;
    private Bitmap buffer;
    private Typeface font;
    
    private MHAndroidCanvas(int width, int height)
    {
        this(Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888));
    }


    MHAndroidCanvas(Bitmap bitmap)
    {
        buffer = bitmap;
        canvas = new Canvas(buffer);
        paint = new Paint(Color.BLACK);
        setSize(buffer.getWidth(), buffer.getHeight());
//    	x0 = (int)(MHPlatform.getDisplayOrigin().x);
//    	y0 = (int)(MHPlatform.getDisplayOrigin().y);
    }

    
    public Paint getPaint()
    {
    	return paint;
    }

    
    @Override
    public void setColor(MHColor color)
    {
        paint = ((MHAndroidPaint)color).getPaint();
    }


    @Override
    public void drawString(String text, int x, int y)
    {
        canvas.drawText(text, x, y, paint);
    }


    @Override
    public void fill(MHColor bgColor)
    {
        setColor(bgColor);
        canvas.drawColor(paint.getColor());
    }


    @Override
    public void drawImage(MHBitmapImage image, int x, int y)
    {
        Bitmap bitmap = ((MHAndroidBitmap)image).getBitmap();
        canvas.drawBitmap(bitmap, x, y, paint);
    }


    public static MHGraphicsCanvas create(int width, int height)
    {
        return new MHAndroidCanvas(width, height);
    }

    
    
    public Bitmap getBitmap()
    {
        return buffer;
    }


    @Override
    public void fillRect(MHRectangle rect)
    {
        fillRect(rect.x, rect.y, rect.width, rect.height);
    }


    @Override
    public void fillRect(int x, int y, int width, int height)
    {
        paint.setStyle(Style.FILL);
        canvas.drawRect(x, y, x+width, y+height, paint);
    }


    @Override
    public void drawRect(MHRectangle rect)
    {
        drawRect(rect.x, rect.y, rect.width, rect.height);
    }


    @Override
    public void drawRect(int x, int y, int width, int height)
    {
        paint.setStyle(Style.STROKE);
        canvas.drawRect(x, y, x+width, y+height, paint);
    }


    @Override
    public void drawImage(MHBitmapImage image, int x, int y, int w, int h)
    {
        Bitmap bitmap = ((MHAndroidBitmap)image).getBitmap();
        
        if (w <= 0 || h <= 0)
        	canvas.drawBitmap(bitmap, x, y, paint);
        else
        {
        	Bitmap flipped = Bitmap.createScaledBitmap(bitmap, w, h, false);
        	canvas.drawBitmap(flipped, x, y, paint);
        }
    }


    @Override
    public void setFont(MHFont font)
    {
    	this.font = ((MHAndroidFont)font).getTypeface();
    	paint.setTypeface(this.font);
    }


    @Override
    public void drawLine(int x1, int y1, int x2, int y2)
    {
    	canvas.drawLine(x1, y1, x2, y2, paint);
    }
}
