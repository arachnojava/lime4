package com.mhframework.platform.android;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.mhframework.MHVideoSettings;
import com.mhframework.core.math.MHVector;
import com.mhframework.platform.MHGameApplication;
import com.mhframework.platform.android.graphics.MHAndroidCanvas;
import com.mhframework.platform.graphics.MHGraphicsCanvas;
import com.mhframework.resources.MHResourceManager;

public class MHAndroidApplication implements MHGameApplication
{
    private static final MHVector DISPLAY_ORIGIN = new MHVector(0, 0);
    private MHVector displaySize;
	@SuppressWarnings("unused")
	private MHAndroidInputEventHandler eventHandler;
	private static Activity activity;
    private static MHView view;
    
    private  MHAndroidApplication(Activity activity,
            MHVideoSettings displaySettings)
    {
    	MHAndroidApplication.activity = activity;
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
         
        displaySize = new MHVector(displaySettings.displayWidth, displaySettings.displayHeight);
        
        view = new MHView(activity);
        activity.setContentView(view);
        // Register event handlers.
        eventHandler = new MHAndroidInputEventHandler(view);
    }

    
    public static Activity getActivity()
    {
    	return activity;
    }
    
    
    public static MHView getView()
    {
        return view;
    }
    

    public MHVector getDisplayOrigin()
    {
        return DISPLAY_ORIGIN;
    }


    public MHVector getDisplaySize()
    {
        return displaySize;
    }


    public void shutdown()
    {
        // TODO Auto-generated method stub

    }


    public void present(MHGraphicsCanvas backBuffer)
    {
        view.present(backBuffer);
    }


    public static MHGameApplication create(Activity activity, MHVideoSettings displaySettings)
    {
        return new MHAndroidApplication(activity, displaySettings);
    }

    
    /**************************************************************************
     * 
     *
     */
    public class MHView extends SurfaceView
    {
        private SurfaceHolder holder;

        public MHView(Context context)
        {
            super(context);
            holder = getHolder();
        }
        

        public void present(MHGraphicsCanvas backBuffer)
        {
            if (!holder.getSurface().isValid()) return;
            
            Bitmap bufferImage = ((MHAndroidCanvas)backBuffer).getBitmap();
            Canvas canvas = holder.lockCanvas();
            canvas.drawBitmap(bufferImage, 0, 0, null);
            holder.unlockCanvasAndPost(canvas);
        }
        
        
        @Override 
        public void onDraw(Canvas canvas) 
        {

        }

    }
}
