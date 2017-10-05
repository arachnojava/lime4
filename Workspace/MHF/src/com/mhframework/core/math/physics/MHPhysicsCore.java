package com.mhframework.core.math.physics;

import java.util.ArrayList;

import com.mhframework.core.math.MHImmutableVector;
import com.mhframework.core.math.MHVector;
import com.mhframework.gameplay.actor.MHActor;

public class MHPhysicsCore
{
    //private static final Vector2f gravityVector = new Vector2f(0, 1);
    private static final MHVector GRAVITY_VECTOR = new MHImmutableVector(0,0.1);

    private static MHPhysicsCore instance;
    //private World world;
    private ArrayList<MHPhysicsBody> bodies;
    private ArrayList<MHCollisionListener> listeners;
    
    // Global physics parameters
    private MHVector gravityVector = GRAVITY_VECTOR;
    private double maxFallingSpeed = Double.MAX_VALUE;

    
    private MHPhysicsCore()
    {
        bodies = new ArrayList<MHPhysicsBody>();
        //world = new World(gravityVector, 1);
        //world.addListener(this);
        
        listeners = new ArrayList<MHCollisionListener>();
    }
    
    
    public static MHPhysicsCore getInstance()
    {
        if (instance == null)
            instance = new MHPhysicsCore();
        
        return instance;
    }
    
    
    public void addBody(MHPhysicsBody body)
    {
        bodies.add(body);
    }
    
    
    public void removeBody(MHPhysicsBody body)
    {
        bodies.remove(body);
    }
    
    
    public void update(long elapsedTime)
    {
        applyGravity(elapsedTime);
        checkCollisions();
    }

    
    private void applyGravity(long elapsedTime)
    {
    	for (int i = 0; i < bodies.size(); i++)
        {
    		MHPhysicsBody body = bodies.get(i);
            if (body.hasGravity())
            {
                MHActor actor = body.getActor();
                MHVector v = actor.getVelocity().add(gravityVector);
                if (v.getY() > maxFallingSpeed)
                    v.setY(maxFallingSpeed);
                
                 actor.setVelocity(v);
            }
        }
    }
    
    public MHVector getGravityVector()
    {
        return GRAVITY_VECTOR;
    }


    private void checkCollisions()
    {
        for (int i = 0; i < bodies.size()-1; i++)
        {
            if (bodies.get(i).getActor().isCollidable())
            {
            	// Collision-check this body against bodies i+1 to bodies.size.
            	for (int j = i+1; j < bodies.size(); j++)
            	{
            		if (j < bodies.size() &&
            			bodies.get(j).getActor().isCollidable() &&
            			bodies.get(i).getActor().isColliding(bodies.get(j).getActor()))
            		{
            			// IF collision detected, create event and notify listeners.
            			MHCollisionEvent event = new MHCollisionEvent(bodies.get(i).getActor(), bodies.get(j).getActor());
                    	collisionOccured(event);
            		}
            	}
            }
        }        
    }
    
    
    /**
     * @return the maxFallingSpeed
     */
    public double getMaxFallingSpeed()
    {
        return maxFallingSpeed;
    }


    /**
     * @param maxFallingSpeed the maxFallingSpeed to set
     */
    public void setMaxFallingSpeed(double maxFallingSpeed)
    {
        this.maxFallingSpeed = maxFallingSpeed;
    }


    public void addCollisionListener(MHCollisionListener listener)
    {
        listeners.add(listener);
    }
    

    public void collisionOccured(MHCollisionEvent event)
    {
        for (MHCollisionListener listener : listeners)
            listener.collisionOccured(event);
    }
}
