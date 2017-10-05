package com.mhframework.resources;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.mhframework.gameplay.tilemap.MHTileSet;
import com.mhframework.platform.MHPlatform;
import com.mhframework.platform.graphics.MHBitmapImage;

/******************************************************************************
 * Resource manager responsible for loading external resources from files,
 * storing and caching them, and providing a global point of access to them.
 * 
 * @author Michael Henson
 */
public class MHResourceManager
{
    public static final String MHF_LOGO = "assets//images//MHFLogo-GlowWhite.png";
    
    private static MHResourceManager instance;
    private Map<String, MHResourceHeader> images;
    private Map<String, MHResourceHeader> tileSets;
    
    
    /**************************************************************************
     * Private constructor to support the Singleton pattern.
     */
    private MHResourceManager()
    {
        images = Collections.synchronizedMap(new HashMap<String, MHResourceHeader>());
        tileSets = Collections.synchronizedMap(new HashMap<String, MHResourceHeader>());
    }
    
    
    /**************************************************************************
     * Return the singleton instance of the resource manager.
     * 
     * @return The singleton instance of the resource manager.
     */
    public static MHResourceManager getInstance()
    {
        if (instance == null)
            instance = new MHResourceManager();
        
        return instance;
    }
    
    
    /**************************************************************************
     * Retrieves an image resource from the resource manager.  If the image
     * does not exist in the collection, an attempt is made to load it and add
     * it by calling <code>MHPlatform.createImage</code>.
     * 
     * @param id The string identifier of the requested image.
     * 
     * @return The requested image.
     */
    public MHBitmapImage getImage(String id)
    {
        if (!images.containsKey(id))
        {
            MHBitmapImage img = MHPlatform.createImage(id);
            putImage(id, img);
        }
        
        return (MHBitmapImage)images.get(id).getResource();
    }
    
    
    /**************************************************************************
     * Adds an image to the resource manager.
     * 
     * @param id A unique identifier to assign to the image resource.
     * @param image An image to be added to the resource manager's collection.
     */
    public void putImage(String id, MHBitmapImage image)
    {
        MHResourceHeader header = new MHResourceHeader(id, image);
        images.put(id, header);
    }

    
    public MHTileSet getTileSet(String id)
    {
        if (!tileSets.containsKey(id))
        {
            MHTileSet ts = new MHTileSet(id);
            putTileSet(id, ts);
        }
        
        return (MHTileSet)tileSets.get(id).getResource();
    }
    

    private void putTileSet(String id, MHTileSet ts) 
    {
        MHResourceHeader header = new MHResourceHeader(id, ts);
        tileSets.put(id, header);
	}


	/**************************************************************************
     * Deletes resources that have not been accessed recently.  The parameter
     * specifies a time in milliseconds such that any resource that has not
     * been accessed within the given time is removed from the collection.
     * 
     * @param threshold Time in milliseconds to use as a filter.
     */
    public void clearCache(long threshold)
    {
        MHResourceHeader[] array = images.values().toArray(new MHResourceHeader[0]);
        for (int i = 0; i < array.length; i++)
        {
            MHResourceHeader h = array[i];
            long lifetime = System.currentTimeMillis() - h.getLastAccessTime();
            if (lifetime > threshold)
            {
                images.remove(h);
            }
        }
    }

    
    /**************************************************************************
     * Shame on you for trying to clone a Singleton, cheater!
     */
    public Object clone() throws CloneNotSupportedException
    {
        throw new CloneNotSupportedException(); 
    }
}
