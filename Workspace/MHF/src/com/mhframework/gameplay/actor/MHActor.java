package com.mhframework.gameplay.actor;

import com.mhframework.MHRenderable;
import com.mhframework.core.math.MHVector;
import com.mhframework.core.math.geom.MHRectangle;
import com.mhframework.core.math.physics.MHPhysicsBody;
import com.mhframework.platform.MHPlatform;
import com.mhframework.platform.graphics.MHBitmapImage;
import com.mhframework.platform.graphics.MHGraphicsCanvas;
import com.mhframework.resources.MHResourceManager;

/******************************************************************************
 * Base class for deriving game actors.
 * 
 * @author Michael Henson
 *
 */
public class MHActor implements MHRenderable, Comparable<MHActor>
{
    private static MHBitmapImage NULL_IMAGE;
    protected MHVector position;
    private MHVector velocity = MHVector.ZERO;
    private double maxSpeed;
    private String imageFile;
    private MHBitmapImage image;
    private boolean flipHorizontal = false;
    private boolean flipVertical = false;
    private boolean destroyed = false;
    private MHRectangle bounds;
    private MHPhysicsBody physics;
    
    
    public MHActor()
    {
        position = new MHVector();
    }
    
    
    public MHActor(String imageFile)
    {
        this();
        setImageID(imageFile);
    }

    
    public void setPosition(double x, double y)
    {
        position.setVector(x, y);
    }
    
    
    public int getX()
    {
        return (int)position.getX();
    }
    

    public int getY()
    {
        return (int)position.getY();
    }
    

    public void update(long elapsedTime)
    {
       MHVector v = velocity.multiply(elapsedTime);
       position.translate(v.getX(), v.getY());
    }


    public void render(MHGraphicsCanvas g)
    {
        render(g, 0, 0);
    }


    public void render(MHGraphicsCanvas g, int x, int y)
    {
        if (image == null)
            image = MHResourceManager.getInstance().getImage(imageFile);
        
        // Convert from world to screen coordinates.
        x = getX() - x;
        y = getY() - y;

        int w = image.getWidth();
        int h = image.getHeight();
        
        if (isFlippedHorizontal())
        {
            x += w;
            w = -w;
        }
        if (isFlippedVertical())
        {
            y += h;
            h = -h;
        }

        g.drawImage(image, x, y, w, h);
    }

    
    protected static final MHBitmapImage getNullImage()
    {
        if (NULL_IMAGE == null)
            NULL_IMAGE = MHPlatform.createImage(10, 10);
        
        return NULL_IMAGE;
    }


    public MHVector getPosition()
    {
        return position;
    }
    
    
    public void setVelocity(double x, double y)
    {
        if (velocity == null || velocity == MHVector.ZERO)
            velocity = new MHVector();
        
        velocity.setVector(x, y);
    }
    
    
    public MHVector getVelocity()
    {
        return velocity;
    }
    
    
    public void destroy()
    {
        destroyed = true;
    }


    public boolean isDestroyed()
    {
        return destroyed;
    }


    public boolean isFlippedHorizontal()
    {
        return flipHorizontal;
    }


    public void flipHorizontal(boolean flip)
    {
        this.flipHorizontal = flip;
    }


    public boolean isFlippedVertical()
    {
        return flipVertical;
    }


    public void flipVertical(boolean flip)
    {
        this.flipVertical = flip;
    }


    /**
     * @return the maxSpeed
     */
    public double getMaxSpeed()
    {
        return maxSpeed;
    }


    /**
     * @param maxSpeed the maxSpeed to set
     */
    public void setMaxSpeed(double maxSpeed)
    {
        this.maxSpeed = maxSpeed;
    }


    public int getHeight()
    {
        return image.getHeight();
    }


    public int getWidth()
    {
        return image.getWidth();
    }


    public void setImageID(String imageID)
    {
        imageFile = imageID;
        image = MHResourceManager.getInstance().getImage(imageFile);
    }


    public void setVelocity(MHVector v)
    {
        velocity = v;
    }


    public boolean isCollidable()
    {
        // TODO Return true if this actor has a collision geometry.
        return true;
    }
    
    
    public void setGravity(boolean hasGravity)
    {
        if (hasGravity)
        {
            physics = new MHPhysicsBody(this);
            physics.setGravity(hasGravity);
        }
    }
    
    
    public MHPhysicsBody getPhysics()
    {
        return physics;
    }


	public MHRectangle getBounds() 
	{
		if (bounds == null)
			bounds = new MHRectangle();

		bounds.setRect(getX(), getY(), getWidth(), getHeight());
		
		return bounds;
	}


	public boolean isColliding(MHActor actor) 
	{
		if (isCollidable())
		{
			// TODO: Check this actor's collision geometry with the parameter's.
			return true;
		}
			
		return false;
	}


    public void setImage(MHBitmapImage image)
    {
        this.image = image;
        this.imageFile = image.getImageID();
    }


    public void setPosition(MHVector pos)
    {
        position = pos;
    }


    @Override
    public int compareTo(MHActor another)
    {
        return getY() - another.getY();
    }


    public String getImageID()
    {
        return this.imageFile;
    }


    public MHBitmapImage getImage()
    {
        if (image == null)
            image = MHResourceManager.getInstance().getImage(imageFile);
        
        return image;
    }

    
//    public MHCollisionGeometry getCollisionGeometry()
//    {
//        return this.getBounds();
//    }
}
