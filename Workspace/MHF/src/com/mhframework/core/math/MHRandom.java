package com.mhframework.core.math;

import java.util.Random;

/********************************************************************
 * Static convenience methods for working with random numbers.
 * 
 * @author Michael Henson
 *
 */
public class MHRandom
{
    private static MHRandom instance;
    private Random rand;
    
    /****************************************************************
     * Constructor.
     */
    private MHRandom()
    {
        rand = new Random();
    }

    
    /****************************************************************
     * Gets the singleton instance.
     * 
     * @return The single instance of this class.
     */
    private static MHRandom getInstance()
    {
        if (instance == null)
            instance = new MHRandom();
        
        return instance;
    }
    
    
    /****************************************************************
     * Generates a random integer between <code>min</code> and 
     * <code>max</code>.
     * 
     * @param min The smallest value that may be generated.
     * @param max The largest value that may be generated.
     * @return A random integer between the two input parameters.
     */
    public static int random(int min, int max)
    {
        int range = Math.abs(max - min);
        
        return min + getInstance().rand.nextInt(range+1);
    }

    
    /****************************************************************
     * Generates a random double between <code>min</code> and 
     * <code>max</code>.
     * 
     * @param min The smallest value that may be generated.
     * @param max The largest value that may be generated.
     * @return A random double between the two input parameters.
     */
    public static double random(double min, double max)
    {
        double range = Math.abs(max - min);
        
        return min + getInstance().rand.nextDouble() * range;
    }
    
    
    /****************************************************************
     * Simulates the rolling of a four-sided die.
     * 
     * @return An integer in the range of 1 to 4.
     */
    public static int rollD4()
    {
        return random(1, 4);
    }
    
    
    /****************************************************************
     * Simulates the rolling of a six-sided die.
     * 
     * @return An integer in the range of 1 to 6.
     */
    public static int rollD6()
    {
        return random(1, 6);
    }
    

    /****************************************************************
     * Simulates the rolling of an eightr-sided die.
     * 
     * @return An integer in the range of 1 to 8.
     */
    public static int rollD8()
    {
        return random(1, 8);
    }

    
    /****************************************************************
     * Simulates the rolling of a ten-sided die.
     * 
     * @return An integer in the range of 0 to 9.
     */
    public static int rollD10()
    {
        return random(0, 9);
    }

    
    /****************************************************************
     * Simulates the rolling of a twelve-sided die.
     * 
     * @return An integer in the range of 1 to 12.
     */
    public static int rollD12()
    {
        return random(1, 12);
    }
    

    /****************************************************************
     * Simulates the rolling of a twenty-sided die.
     * 
     * @return An integer in the range of 1 to 20.
     */
    public static int rollD20()
    {
        return random(1, 20);
    }
    
    
    /****************************************************************
     * Simulates the flipping of a coin.
     * 
     * @return A random boolean value.
     */
    public static boolean flipCoin()
    {
        return rollD4() % 2 == 0;
    }
    
    
    public static void main(String args[])
    {
        long total = 0;
        long min = 9999;
        long max = -9999;
        int count = 5000;
        int rand = 0;
        for (int i = 0; i < count; i++)
        {
            rand = random(-100, 100);
            total += rand;
            min = Math.min(min, rand);
            max = Math.max(max, rand);
        }
        
        long average = total / count;
        System.out.println("Min value = " + min);
        System.out.println("Max value = " + max);
        System.out.println("Average   = " + average);
    }
}
