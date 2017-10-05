package com.mhframework.gameplay.tilemap.io;

import com.mhframework.gameplay.actor.MHActor;


/********************************************************************
 * This interface is to be implemented by the class in your game that
 * is responsible for taking a tile ID and returning a special
 * non-static actor.

 * @author Michael Henson
 */
public interface MHActorFactory
{
	/****************************************************************
	 * Accepts tile identifiers, instantiates a special
	 * object indicated by them, and returns a reference to it.  If
	 * there is no special object for a given identifier, this
	 * method should return <tt>null</tt>.
	 * 
	 * @return  An actor to be spawned onto the map.
	 */
	public abstract MHActor spawnActor(int id, int spawnRow, int spawnColumn);
}
