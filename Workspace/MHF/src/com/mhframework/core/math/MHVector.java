package com.mhframework.core.math;



/********************************************************************
 * Represents a two-dimensional vector.
 *
 */
public class MHVector
{
    private static final double THRESHHOLD = 0.0000001;
    public static final MHVector ZERO = ZeroVector.getInstance();
    public double x, y;
    
    /****************************************************************
     * Default constructor.  Initializes a zero-length vector.
     */
    public MHVector()
    {
        this(0,0);
    }
    
    
    /****************************************************************
     * Overloaded constructor.  Initializes a vector with the given
     * <i>x</i> and <i>y</i> coordinates.
     *  
     * @param x The initial x component of the vector.
     * @param y The initial y component of the vector.
     */
    public MHVector(double x, double y)
    {
        this.x = x;
        this.y = y;
    }
    
    
    /****************************************************************
     * Moves this vector by the horizontal and vertical distances
     * passed in.
     * 
     * @param dx
     * @param dy
     * @return
     */
    public MHVector translate(double dx, double dy)
    {
        setVector(x + dx, y + dy);
        
        return this;
    }
    
    /****************************************************************
     * Copy constructor.  Initializes a vector with the same 
     * component values as the parameter.
     * 
     * @param v An existing vector whose components are to be copied
     *          into a new vector.
     */
    public MHVector(MHVector v)
    {
        setVector(v.x, v.y);
    }
    
    
    /****************************************************************
     * Sets the x and y components of this vector.
     * 
     * @param x The x component of the vector.
     * @param y The y component of the vector.
     */
    public void setVector(double x, double y)
    {
        this.x = x;
        this.y = y;
    }
    
    
    /****************************************************************
     * Sets the x and y components of this vector by copying them
     * from the vector passed in as a parameter.
     * 
     * @param v The vector whose components are copied into this
     *          vector.
     */
    public void setVector(MHVector v)
    {
        setVector(v.x, v.y);
    }

    
    /****************************************************************
     * Returns the x component of this vector.
     * 
     * @return The x component of this vector.
     */
    public double getX()
    {
        return x;
    }

    
    /****************************************************************
     * Sets the x component of this vector.
     * 
     * @param x The value to set in the x component.
     */
    public void setX(double x)
    {
        this.x = x;
    }


    /****************************************************************
     * Returns the y component of this vector.
     * 
     * @return The y component of this vector.
     */
    public double getY()
    {
        return y;
    }

    
    /****************************************************************
     * Sets the y component of this vector.
     * 
     * @param y The value to set in the y component.
     */
    public void setY(double y)
    {
        this.y = y;
    }
    
    
    /****************************************************************
     * Calculates the magnitude of this vector.
     *  
     * @return This vector's magnitude.
     */
    public double magnitude()
    {
        double xSqr = x * x;
        double ySqr = y * y;
        return Math.sqrt(xSqr + ySqr);
    }

    
    /****************************************************************
     * Creates a new vector that is the normalized form of this
     * vector.
     * 
     * @return A normalized copy of this vector.
     */
    public MHVector normalize()
    {
        MHVector r = new MHVector();
        double mag = magnitude();
        mag = Math.max(mag, THRESHHOLD);
        r.setX(x/mag);
        r.setY(y/mag);
        return r;
    }


    /****************************************************************
     * Creates a new vector by adding the input vector to this one.
     * 
     * @param v The vector to be added to this one.
     * @return A new vector containing the result of the addition.
     */
    public MHVector add(MHVector v)
    {
        return add(x + v.x, y + v.y);
    }

    
    public MHVector add(double x, double y)
    {
        return new MHVector(x, y);
    }

    
    /****************************************************************
     * Creates a new vector by subtracting the input vector from this
     * one.
     * 
     * @param v The vector to be subtracted from this one.
     * @return A new vector containing the result of the subtraction.
     */
    public MHVector subtract(MHVector v)
    {
        return subtract(x - v.x, y - v.y);
    }

    
    public MHVector subtract(double x, double y)
    {
        return new MHVector(x, y);
    }

    
    /****************************************************************
     * Creates a new vector by multiplying this vector by the input 
     * scalar.
     * 
     * @param scalar The scalar by which to multiply this vector.
     * @return A new vector containing a scaled version of this one.
     */
    public MHVector multiply(double scalar)
    {
        return new MHVector(x*scalar, y*scalar);
    }


    /****************************************************************
     * Creates a new vector by multiplying this vector by -1.
     * 
     * @return A negated copy of this vector.
     */
    public MHVector negate()
    {
        return multiply(-1);
    }

    
    /****************************************************************
     * Calculates the Euclidean distance between this vector and the 
     * one sent in as a parameter.
     * 
     * @param v The other vector to whom we're measuring the distance.
     * @return The distance between the two vectors.
     */
    public double distance(MHVector v)
    {
        return subtract(v).magnitude();
    }
    


    /****************************************************************
     * Utility method for converting radians to degrees.
     */
    public static double rad2Deg(double radians)
    {
        return radians * (180/Math.PI);
    }


    /****************************************************************
     * Utility method for converting degrees to radians.
     */
    public static double deg2Rad(double degrees)
    {
        return degrees * (Math.PI/180);
    }




    
    /****************************************************************
     * Creates a new vector by rotating this vector by the angle
     * specified by <code>radians</code>.
     * 
     * @param radians The angle in radians by which to rotate this
     *                vector.
     *                
     * @return A new vector containing the result of the rotation.
     */
    public MHVector rotate(double radians)
    {
        MHVector result = new MHVector();
        final double cosine = Math.cos(radians);
        final double sine =   Math.sin(radians);
        result.setVector(cosine*x - sine*y, sine*x + cosine*y);
        
        return result;
    }
    
    
    public MHVector clone()
    {
        return new MHVector(x, y);
    }
    
    
    public String toString()
    {
        int dx = (int) x;
        int dy = (int) y;
        //int dm = (int) magnitude();
        //int dd = (int) rotation();
        return "(" + dx + ", " + dy + ")";// <" + dm + ", " + dd + ">";
    }
    
    
    public double rotation()
    {
        return (Math.atan2(y, x)/Math.PI);// * 180;
    }


    private static final class ZeroVector extends MHVector
    {
        private static final MHVector instance = new ZeroVector();
        
        private ZeroVector()
        {
            super(0, 0);
        }

        
        public static MHVector getInstance()
        {
            return instance;
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
}
