package com.mhframework.core.math.geom;

import com.mhframework.core.math.MHVector;

/******************************************************************************
 * Basic rectangle implementation.
 * 
 * @author Michael Henson
 */
public class MHRectangle
{
    public int x;
    public int y;
    public int width;
    public int height;


   
    
    public MHRectangle()
    {
    }

    
    
    public MHRectangle(int x, int y, int width, int height)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }


    /**************************************************************************
     * Sets all properties of this rectangle by copying the properties of the 
     * input parameter.
     * 
     * @param rect The rectangle containing the properties to be copied.
     */
    public void setRect(MHRectangle rect)
    {
        setRect(rect.x, rect.y, rect.width, rect.height);
    }


    /**************************************************************************
     * Sets all properties of this rectangle.
     * 
     * @param x      The x value to set.
     * @param y      The y value to set.
     * @param width  The width to set.
     * @param height The height to set.
     */
    public void setRect(int x, int y, int width, int height)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }


    /**************************************************************************
     * Predicate method for determining whether a vector lies within this
     * rectangle.
     * 
     * @param v The vector to test for inclusion.
     * 
     * @return A boolean value indicating whether this vector lies within the
     *         bounds of this rectangle;
     */
    public boolean contains(MHVector v)
    {
        return contains(v.getX(), v.getY());
    }

    
    /**************************************************************************
     * Predicate method for determining whether a given point lies within this
     * rectangle.
     * 
     * @param px The horizontal component of the point to test for inclusion.
     * @param py The vertical component of the point to test for inclusion.
     * 
     * @return A boolean value indicating whether the point lies within the
     *         bounds of this rectangle;
     */
    public boolean contains(double px, double py)
    {
        int left = x;
        int top = y;
        int right = left + width;
        int bottom = top + height;
        
        return (left <= px && px <= right && top <= py && py <= bottom);
    }

    
     /**************************************************************************
      * Tests to see if the rectangle passed in intersects with this one.
      * 
      * Algorithm ported from Python version retrieved from
      * http://davch.blogspot.com/2011/05/intersection-algorithm.html.
      *
      * @param r The other rectangle to check for intersection.
      * 
      * @return True if the rectangles intersect; false otherwise.
      */
      public boolean intersects(MHRectangle r)
      {
          if (r == null)
              return false;
     
          int rx = r.x;
          int ry = r.y;
          int rw = rx + r.width;
          int rh = ry + r.height;

          if (rw <= 0 || rh <= 0)
              return false;
          
          int tx = x;
          int ty = y;
          int tw = tx + width;
          int th = ty + height;

          if (tw <= 0 || th <= 0)
              return false;
          
          // The first sub-test checks if rw/rh/tw/th overflowed, meaning
          // wrapped around to value < rx/ry/tw/ry.
          return ((rw < rx || rw > tx) &&
                  (rh < ry || rh > ty) &&
                  (tw < tx || tw > rx) &&
                  (th < ty || th > ry));
      }
      
      
      
      public int top()
      {
          return y;
      }
      
      
      public int bottom()
      {
          return y + height;
      }
      
      
      public int left()
      {
          return x;
      }
      
      
      public int right()
      {
          return x + width;
      }
      
      
      public MHRectangle clone()
      {
          return new MHRectangle(x, y, width, height);
      }



	@Override
	public String toString() 
	{
		return "Location: (" + x + ", " + y + ")  Size: " + width + " x " + height;
	}
      
      
      
}
