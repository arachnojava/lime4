package com.mhframework.core.math.physics;

import com.mhframework.gameplay.actor.MHActor;

public class MHPhysicsBody
{
    //private Body body;
    private MHActor subject;
    private boolean hasGravity = true;
    
    public MHPhysicsBody(MHActor actor)
    {
        subject = actor;
    }
    
    
    public MHActor getActor()
    {
        return subject;
    }
    
    
    public boolean hasGravity()
    {
        return hasGravity;
    }
    
    
    public void setGravity(boolean gravityOn)
    {
        hasGravity = gravityOn;
    }
    

//    public Body getPhys2DBody()
//    {
//        return body;
//    }
    
    
}
