package com.mhframework.core.math;

public class MHImmutableVector extends MHVector
{

    public MHImmutableVector()
    {
        super(0,0);
    }


    public MHImmutableVector(double x, double y)
    {
        super(x, y);
    }


    public MHImmutableVector(MHVector v)
    {
        super(v);
    }

    
    public void setVector(double x, double y)
    {
        // Do nothing.  This vector must be immutable.
    }


    @Override
    public void setX(double x)
    {
        // Do nothing.  This vector must be immutable.
    }


    @Override
    public void setY(double y)
    {
        // Do nothing.  This vector must be immutable.
    }

}
