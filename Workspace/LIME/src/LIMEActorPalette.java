import com.mhframework.core.math.MHVector;
import com.mhframework.core.math.geom.MHRectangle;
import com.mhframework.gameplay.actor.MHTileMapActor;
import com.mhframework.gameplay.tilemap.MHTile;
import com.mhframework.gameplay.tilemap.MHTileSet;
import com.mhframework.platform.MHPlatform;
import com.mhframework.platform.event.MHMouseTouchEvent;
import com.mhframework.platform.graphics.MHBitmapImage;
import com.mhframework.platform.graphics.MHColor;
import com.mhframework.platform.graphics.MHGraphicsCanvas;


public class LIMEActorPalette extends LIMETilePalette
{
    
    final int TILE_SIZE = 32;
    
	/****************************************************************
	 * 
	 * @param bounds
	 */
	public LIMEActorPalette(MHRectangle bounds)
	{
	    super(bounds);
	}

	
	/****************************************************************
	 * 
	 * @return
	 */
	protected MHBitmapImage getImage()
	{
		if (image == null)
			image = MHPlatform.createImage(bounds.width, bounds.height);
		
		MHGraphicsCanvas g = image.getGraphicsCanvas();
		
		// Draw border or background.
		g.setColor(MHColor.DARK_GRAY);
		g.fillRect(0, 0, bounds.width, bounds.height);
		g.setColor(MHColor.BLACK);
		for (int gy = 0; gy < bounds.height;  gy += TILE_SIZE)
		{
			for (int gx = 0; gx < bounds.width; gx += TILE_SIZE)
			{
				g.drawRect(gx, gy, TILE_SIZE, TILE_SIZE);
			}
		}
		
		//  Draw the tiles visible in the viewport.
		int x = 0;
		int y = viewY;
		for (int t = 0; t < tileset.countActors(); t++)
		{
		    // TODO:  Switch to uniform scaling for rendering actor tiles.
			g.drawImage(tileset.getActorTile(t).image, x, y, TILE_SIZE, TILE_SIZE);

			if (selectedTile != null && t == selectedTile.id && cursorFlash)
			{
				g.setColor(MHColor.YELLOW);
				g.drawRect(x, y, TILE_SIZE, TILE_SIZE);
			}
			cursorFlash = !cursorFlash;

			x += TILE_SIZE;
			
			if (x + TILE_SIZE > bounds.width)
			{
				x = 0;
				y += TILE_SIZE;
			}
		}
		
		return image;
	}
	
	
	/****************************************************************
	 * 
	 * @param tileset
	 */
	public void setTileSet(MHTileSet tileset)
	{
		this.tileset = tileset;
		createTileGrid();
		//selectionRect.setRect(0, 0, tileset.getBaseTileWidth(), tileset.getBaseTileHeight());
	}

	
	public MHTileSet getTileSet()
	{
		return tileset;
	}
	
	/****************************************************************
	 * 
	 * @return
	 */
	public MHTile getSelectedTile()
	{
		return tileset.getActorTile(selectedTile.id);
	}
	
	
	public MHTileMapActor getSelectedActor()
	{
	    MHTileMapActor actor = new MHTileMapActor();
	    
	    actor.setImage(getSelectedTile().image);
	    actor.setTileID(getSelectedTile().id);
	    
	    return actor;
	}
	
	
	/****************************************************************
	 * 
	 */
	private void createTileGrid()
	{
		int numColumns = bounds.width / TILE_SIZE;
		int numRows = Math.max(1, tileset.countActors() / numColumns);
		
		worldSpace = new MHRectangle();
		worldSpace.width = bounds.width;
		worldSpace.height = numRows * TILE_SIZE;
		
		int c = 0, r = 0;
		for (int i = 0; i < MHTileSet.MAX_TILE_ID; i++)
		{
			MHTile tile = tileset.getActorTile(i);
			if (tile != null && tile.id != MHTileSet.NULL_TILE_ID)
			{
				LIMESelectableObject selectableTile = new LIMESelectableObject();
				selectableTile.id = i;
				selectableTile.bounds = new MHRectangle();
				selectableTile.bounds.x = c * TILE_SIZE;
				selectableTile.bounds.y = r * TILE_SIZE;
				selectableTile.bounds.width = TILE_SIZE;
				selectableTile.bounds.height = TILE_SIZE;
				tiles.add(selectableTile);
				
				if (++c >= numColumns)
				{
					c = 0;
					r++;
				}
			}
		}
		
		if (tiles.size() > 0)
		    selectedTile = tiles.get(0);
	}
	


	/****************************************************************
	 * 
	 */
	public void onMouseDown(MHMouseTouchEvent e) 
	{
		if (!bounds.contains(e.getPoint())) return;
		
		MHVector localClick = new MHVector();
		localClick.x = e.getX() - bounds.x;
		localClick.y = e.getY() - bounds.y;
		
		// Convert to world coordinates.
		localClick.y -= viewY;
		
		for (LIMESelectableObject obj : tiles)
		{
			if (obj.bounds.contains(localClick))
			{
				selectedTile = obj;
				
				//debug = "Tile " + selectedTile.id + " selected";
				return;
			}
		}
	}

	
	/****************************************************************
	 * 
	 * @param e
	 */
	public void onMouseMoved(MHMouseTouchEvent e) 
	{
		MHVector worldClick = new MHVector();
		worldClick.x = e.getX() - bounds.x;
		worldClick.y = e.getY() - bounds.y;
		
		//debug = "Local Mouse Coordinates:  " + worldClick.toString();
	}

    public void setSelectedTile(MHTile tile)
    {
        selectedTile.id = tile.id;
    }
    
    
    public void scrollUp()
    {
        if (viewY < 0)
            viewY += tileset.getBaseTileHeight();
        
        getImage();
    }
    
    
    public void scrollDown()
    {
        if (viewY - tileset.getBaseTileHeight() > -image.getHeight())
            viewY -= tileset.getBaseTileHeight();
        
        getImage();
    }
    
    public boolean isTileMode()
    {
        return false;
    }
}
