package com.mhframework.gameplay.actor;

import java.util.ArrayList;
import java.util.Collections;
import com.mhframework.core.math.MHVector;
import com.mhframework.gameplay.tilemap.io.MHMapFileInfo;

/******************************************************************************
 * 
 * @author Michael Henson
 *
 */
public class MHActorList //implements MHTileMapLayer
{
    private ArrayList<MHActor> list;
    
    public MHActorList()
    {
        list = new ArrayList<MHActor>();
    }
    
    public void add(MHActor actor)
    {
        list.add(actor);
    }
    
    public void remove(MHActor actor)
    {
        list.remove(actor);
    }

    public void update(long elapsedTime)
    {
        for (MHActor a : list)
            a.update(elapsedTime);
    }

    
    public int size()
    {
        return list.size();
    }
    
    
    public MHActor get(int index)
    {
        if (index < 0)
            return list.get(0);
     
        if (index >= list.size())
            return list.get(list.size()-1);
        
        return list.get(index);
    }
    
    
    
    
//    private void sortOnePass()
//    {
//        int size = list.size()-1;
//        for (int a = 0; a < size; a++)
//        {
//            MHActor thisOne = list.get(a);
//            MHActor nextOne = list.get(a+1);
//            
//            if (thisOne.compareBasePoint(nextOne) < 0)
//            {
//                list.set(a, nextOne);
//                list.set(a+1, thisOne);
//            }
//        }
//    }


    /****************************************************************
     * Return the actor whose bounds contain the given point, if any.
     * If no actor contains the point, this method returns null.  If
     * more than one actor's bounds contain the point, it returns
     * whichever actor is in front.
     * 
     * @param point The (x, y) coordinate to be checked for the 
     *        presence of an actor.
     *        
     * @return The foremost actor at the given location, or null if
     *         there is no actor at that location.
     */
    public MHActor getActorAt(MHVector point)
    {
        sort();
        
        for (int a = list.size() - 1; a >= 0; a--)
        {
            if (list.get(a).getBounds().contains(point))
                return list.get(a);
        }
        
        return null;
    }
    

    @SuppressWarnings("unchecked")
    public void sort()
    {
        Collections.sort(list);
    }
    
    
    public String toString()
    {
        String data = "";
        
        for (MHActor actor : list)
        {
            data += actor.getImageID() + MHMapFileInfo.MAP_FILE_DELIMITER;
            data += actor.getX() + MHMapFileInfo.MAP_FILE_DELIMITER;
            data += actor.getY() + MHMapFileInfo.MAP_FILE_DELIMITER;
        }
        
        return data;
    }

    
    public void clear()
    {
        list.clear();
    }

    public MHActorList merge(MHActorList other)
    {
        MHActorList merged = new MHActorList();
        int myIndex=0, otherIndex=0;
        sort();
        other.sort();
        
        while (myIndex < list.size() && otherIndex < other.size())
        {
            if (list.get(myIndex).compareTo(other.get(otherIndex)) < 0)
            {
                merged.add(list.get(myIndex));
                myIndex++;
            }
            else
            {
                merged.add(other.get(otherIndex));
                otherIndex++;
            }
        }
        
        if (myIndex < list.size())
        {
            for (int i = myIndex; i < list.size(); i++)
                merged.add(list.get(i));
        }
        else
        {
            for (int i = otherIndex; i < other.size(); i++)
                merged.add(other.get(i));
        }
        
        return merged;
    }
}
