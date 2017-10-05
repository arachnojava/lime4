package com.mhframework.gameplay.actor;

import java.util.ArrayList;
import com.mhframework.platform.MHPlatform;
import com.mhframework.platform.graphics.MHBitmapImage;

/******************************************************************************
 * Maintains a sequence of animation frames which comprise an animation.  This
 * class contains the data that is used by MHAnimator.
 * 
 * @author Michael Henson
 *
 */
public class MHAnimationSequence
{
    private ArrayList<MHAnimationFrame> frames;
    private long globalDuration = 1000/8L;
    
    /**************************************************************************
     * Constructor.
     */
    public MHAnimationSequence()
    {
        frames = new ArrayList<MHAnimationFrame>();
    }
    
    
    /**************************************************************************
     * Adds an animation frame to this animation sequence and defaults the
     * duration to the last global duration specified.  (Default is 1/8 of a 
     * second.)
     * 
     * @param imageID The image ID, possibly a filename, for this frame's 
     *                image file.
     * @return The image ID used for this frame's image, just for convenience
     *         and confirmation.
     */
    public String addFrame(String imageID)
    {
        return addFrame(imageID, globalDuration);
    }
    
    
    /**************************************************************************
     * Adds an animation frame to this animation sequence and sets its 
     * duration to the input value.
     * 
     * @param imageID        The image ID, possibly a filename, for this 
     *                       frame's image file.
     * @param durationMillis The duration in milliseconds for which this frame
     *                       is to be displayed before advancing to the next
     *                       frame.
     * @return The image ID used for this frame's image, just for convenience
     *         and confirmation.
     */
    public String addFrame(String imageID, long durationMillis)
    {
        frames.add(new MHAnimationFrame(imageID, durationMillis));
        return imageID;
    }

    
    /**************************************************************************
     * Sets the input value as the duration for all frames in this sequence.
     * 
     * @param duration The duration in milliseconds to apply to all frames in
     *                 this sequence.
     */
    public void setDurationMillis(long duration)
    {
        globalDuration = duration;
        
        for (MHAnimationFrame frame : frames)
            frame.durationMillis = globalDuration;
    }

    
    /**************************************************************************
     * Sets the input value as the duration for the given frame number.
     * 
     * @param frameNumber The number of the frame for which to set the 
     *                    duration.
     * @param duration    The duration in seconds to apply to the given frame.
     */
    public void setDurationSeconds(int frameNumber, double duration)
    {
        frames.get(frameNumber).durationMillis = (long)(duration*1000);
    }

    
    /**************************************************************************
     * Sets the input value as the duration for all frames in this sequence.
     * 
     * @param duration The duration in seconds to apply to all frames in
     *                 this sequence.
     */
    public void setDurationSeconds(double duration)
    {
        globalDuration = (long)(duration*1000);
        
        for (MHAnimationFrame frame : frames)
            frame.durationMillis = globalDuration;
    }

    
    /**************************************************************************
     * Sets the input value as the duration for the given frame number.
     * 
     * @param frameNumber The number of the frame for which to set the 
     *                    duration.
     * @param duration    The duration in milliseconds to apply to the given 
     *                    frame.
     */
    public void setDurationMillis(int frameNumber, long duration)
    {
        frames.get(frameNumber).durationMillis = duration;
    }

    
    /**************************************************************************
     * Return the image ID for the given frame number.
     * 
     * @param frameNumber The number of the frame for which to get the ID.
     * @return The image ID string.
     */
    public String getImageID(int frameNumber)
    {
        frameNumber = Math.max(frameNumber, 0);
        frameNumber = Math.min(frameNumber, getNumFrames()-1);
        
        return frames.get(frameNumber).imageID;
    }


    /**************************************************************************
     * Get the number of frames in this sequence.
     * 
     * @return The number of frames in this sequence.
     */
    public int getNumFrames()
    {
        return frames.size();
    }
    

    /**************************************************************************
     * Get the duration in milliseconds for the given frame number.
     * 
     * @param frameNumber The number of the frame whose duration is returned.
     * 
     * @return The duration in milliseconds for the given frame number.
     */
    public long getDurationMillis(int frameNumber)
    {
        frameNumber = Math.max(frameNumber, 0);
        frameNumber = Math.min(frameNumber, getNumFrames()-1);
        
        return frames.get(frameNumber).durationMillis;
    }
    
        
    /**************************************************************************
     * Get the duration in nanoseconds for the given frame number.
     * 
     * @param frameNumber The number of the frame whose duration is returned.
     * 
     * @return The duration in nanoseconds for the given frame number.
     */
    public long getDurationNanos(int frameNumber)
    {
        return getDurationMillis(frameNumber)*1000000;
    }
    
        
    private static class MHAnimationFrame
    {
        public String imageID;
        public long durationMillis;

        public MHAnimationFrame(String imageID, long durationMillis)
        {
            this.imageID = imageID;
            this.durationMillis = durationMillis;
            
            // Preload for safety and performance.
            MHBitmapImage img = MHPlatform.createImage(imageID);
        }
    }
}
