package com.mhframework.platform;

import com.mhframework.core.math.MHVector;
import com.mhframework.platform.graphics.MHGraphicsCanvas;


public interface MHGameApplication
{
    public MHVector getDisplayOrigin();
    public MHVector getDisplaySize();
    public abstract void shutdown();
    public abstract void present(MHGraphicsCanvas backBuffer);
}
