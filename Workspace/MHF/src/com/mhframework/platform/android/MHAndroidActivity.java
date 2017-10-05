package com.mhframework.platform.android;

import android.app.Activity;
import com.mhframework.MHGame;

/********************************************************************
 * Specialization of <code>android.app.Activity</code> that supports
 * the engine's multithreaded game loop.
 */
public class MHAndroidActivity extends Activity
{
    //handle resume/focus events
    @Override 
    public void onResume() 
    {
        super.onResume();
        MHGame.resume();
    }


    //handle pause/minimize events
    @Override 
    public void onPause() 
    {
        super.onPause();
        MHGame.pause();
    }
}
