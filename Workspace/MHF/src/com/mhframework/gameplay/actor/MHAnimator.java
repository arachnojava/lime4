package com.mhframework.gameplay.actor;



public class MHAnimator
{
    private int currentFrame = 0;
    private long timeSinceLastUpdate = 0L;
    private MHAnimationSequence frames;
    
    public MHAnimator(MHAnimationSequence animation)
    {
        setAnimationSequence(animation);
    }
    
    
    public void setAnimationSequence(MHAnimationSequence animation)
    {
        frames = animation;
        
        if (frames.getNumFrames() >= currentFrame)
            currentFrame = 0;
        
        //timeSinceLastUpdate = 0L;
    }
    
    
    public void update(long elapsedTime, MHAnimationListener listener)
    {
        timeSinceLastUpdate += elapsedTime;
        
        //DEBUG
        //System.out.println("Time since last update: " + timeSinceLastUpdate);
        
        if (timeSinceLastUpdate > frames.getDurationMillis(currentFrame))
        {
            timeSinceLastUpdate = 0L;
            currentFrame++;
            
            if (currentFrame >= frames.getNumFrames())
            {
                currentFrame = 0;

                if (listener != null)
                    listener.animationSequenceEnded(frames);
            }

            if (listener != null)
                listener.animationFrameChanged(getImageID());
        }
    }
    
    
    public String getImageID()
    {
        return frames.getImageID(currentFrame);
    }
    
    
    public long getFrameDuration()
    {
        return frames.getDurationMillis(currentFrame);
    }
    
    
    public int getCurrentFrameIndex()
    {
        return currentFrame;
    }

    
    public static interface MHAnimationListener
    {
        public void animationFrameChanged(String imageID);
        public void animationSequenceEnded(MHAnimationSequence animation);
    }
}
