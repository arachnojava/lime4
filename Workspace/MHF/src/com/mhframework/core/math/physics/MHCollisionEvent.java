package com.mhframework.core.math.physics;

import com.mhframework.gameplay.actor.MHActor;

public class MHCollisionEvent
{
    private MHActor actorA, actorB;
    
    public MHCollisionEvent(MHActor a, MHActor b)
    {
        actorA = a;
        actorB = b;
    }

    public MHActor getActorA()
    {
        return actorA;
    }

    public MHActor getActorB()
    {
        return actorB;
    }
}
