package com.mhframework.gameplay.tilemap.view;

import com.mhframework.core.math.MHVector;
import com.mhframework.gameplay.tilemap.MHMapCellAddress;
import com.mhframework.gameplay.tilemap.MHTileMapDirection;

public interface MHTileMapInterface
{
    /****************************************************************
     * Calculates the next map position to which an actor would walk
     * if it were in position (<i>row</i>, <i>column</i>) and
     * traveled in the direction specified by <i>direction</i>.
     *
     * @param row
     *            The actor's current row position.
     * @param column
     *            The actor's current column position.
     * @param direction
     *            The direction in which to walk.
     *
     * @return A point indicating the actor's new column and row
     *         position after walking from its original position.
     */
    public MHMapCellAddress tileWalk(int row, int column, MHTileMapDirection direction);

    public MHVector plotTile(int row, int column);
    
    public void calculateAnchorSpace();

    public void calculateWorldSpace();
}
