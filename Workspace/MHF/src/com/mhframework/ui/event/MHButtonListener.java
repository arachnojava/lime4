package com.mhframework.ui.event;

import com.mhframework.platform.event.MHMouseTouchEvent;
import com.mhframework.ui.MHButton;

public interface MHButtonListener
{
    public void onButtonPressed(MHButton button, MHMouseTouchEvent e);
}
