package com.mhframework.gameplay.tilemap.view;

import com.mhframework.gameplay.tilemap.*;
import com.mhframework.gameplay.tilemap.view.MHRectangularMapView;


public class MHTileWalker
{
    private MHRectangularMapView.Type mapType = MHRectangularMapView.Type.RECTANGULAR;
    private ITileWalker walker = new RectangularWalker();
    
    private MHTileWalker()
    {
        
    }

    public static MHTileWalker create(MHRectangularMapView.Type mapType)
    {
        MHTileWalker w = new MHTileWalker();
        w.mapType = mapType;
        
        if (mapType == MHRectangularMapView.Type.RECTANGULAR)
            w.walker = w.new RectangularWalker();
        else if (mapType == MHRectangularMapView.Type.DIAMOND)
            w.walker = w.new DiamondWalker();
        else if (mapType == MHRectangularMapView.Type.STAGGERED)
            w.walker = w.new StaggeredWalker();
        
        return w;
    }
    
    
    public MHRectangularMapView.Type getMapType()
    {
        return mapType;
    }
    
    
    public MHMapCellAddress tileWalk(int startRow, int startCol, MHTileMapDirection direction)
    {
        return walker.tileWalk(startRow, startCol, direction);
    }

    
    public MHMapCellAddress tileWalk(MHMapCellAddress start, MHTileMapDirection direction)
    {
        return walker.tileWalk(start.row, start.column, direction);
    }
    
    

    
    private interface ITileWalker
    {
        public MHMapCellAddress tileWalk(int startRow, int startCol, MHTileMapDirection direction);
    }
    
    
    private class RectangularWalker implements ITileWalker
    {
        public MHMapCellAddress tileWalk(int startRow, int startCol, MHTileMapDirection direction)
        {
            int r = startRow, c = startCol;
            
            switch (direction)
            {
            case NORTH:      r--;      break;
            case NORTHEAST:  r--; c++; break;
            case EAST:            c++; break;
            case SOUTHEAST:  r++; c--; break;
            case SOUTH:      r++;      break;
            case SOUTHWEST:  r++; c--; break;
            case WEST:            c--; break;
            case NORTHWEST:  r--; c--; break;
            case CENTER:
                break;
            default:
                break;
            }
            
            return new MHMapCellAddress(r, c);
        }
    }
    
    private class StaggeredWalker implements ITileWalker
    {
        public MHMapCellAddress tileWalk(int startRow, int startCol, MHTileMapDirection direction)
        {
            int r = startRow, c = startCol;
            
            switch (direction)
            {
            case NORTH:      r -= 2;                        break;
            case NORTHEAST:  r--;    c += (startRow&1);     break;
            case EAST:               c++;                   break;
            case SOUTHEAST:  r++;    c += (startRow&1);     break;
            case SOUTH:      r += 2;                        break;
            case SOUTHWEST:  r++;    c += ((startRow&1)-1); break;
            case WEST:               c--;                   break;
            case NORTHWEST:  r--;    c += ((startRow&1)-1); break;
            case CENTER:
                break;
            default:
                break;
            }
            
            return new MHMapCellAddress(r, c);
        }
    }

    
    private class DiamondWalker implements ITileWalker
    {
        public MHMapCellAddress tileWalk(int startRow, int startCol, MHTileMapDirection direction)
        {
            int r = startRow, c = startCol;
            
            switch (direction)
            {
            case NORTH:      r--; c--; break;
            case NORTHEAST:  r--;      break;
            case EAST:       r--; c++; break;
            case SOUTHEAST:       c++; break;
            case SOUTH:      r++; c++; break;
            case SOUTHWEST:  r++;      break;
            case WEST:       r++; c--; break;
            case NORTHWEST:       c--; break;
            case CENTER:
                break;
            default:
                break;
            }
            
            return new MHMapCellAddress(r, c);
        }
    }

}
