package com.mhframework;

import com.mhframework.platform.graphics.MHGraphicsCanvas;

/******************************************************************************
 * Interface to be implemented by all visual elements.
 * 
 * @author Michael Henson
 * @since 1.0
 */
public interface MHRenderable
{
    /**************************************************************************
     * Render onto the sent graphics canvas.
     * 
     * @param g The graphics canvas on which to render.
     */
    public void render(MHGraphicsCanvas g);

    
    /**************************************************************************
     * Render onto the sent graphics canvas relative to the specified screen
     * anchor point.
     * 
     * @param g The graphics canvas on which to render.
     * @param x The x coordinate in view space.
     * @param y The y coordinate in view space.
     */
    public void render(MHGraphicsCanvas g, int x, int y);

}
