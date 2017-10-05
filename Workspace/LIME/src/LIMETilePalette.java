import java.util.ArrayList;

import com.mhframework.MHGame;
import com.mhframework.core.math.MHVector;
import com.mhframework.core.math.geom.MHRectangle;
import com.mhframework.gameplay.tilemap.MHITileMapContent;
import com.mhframework.gameplay.tilemap.MHTile;
import com.mhframework.gameplay.tilemap.MHTileSet;
import com.mhframework.platform.MHPlatform;
import com.mhframework.platform.event.MHMouseTouchEvent;
import com.mhframework.platform.graphics.MHBitmapImage;
import com.mhframework.platform.graphics.MHColor;
import com.mhframework.platform.graphics.MHGraphicsCanvas;


public class LIMETilePalette //implements MHMouseTouchListener
{
	protected MHTileSet tileset;
	protected ArrayList<LIMESelectableObject> tiles = new ArrayList<LIMESelectableObject>();
    
	protected LIMESelectableObject selectedTile;
	protected MHBitmapImage image;
	protected MHRectangle bounds, worldSpace;
	protected int viewY = 0;
	protected boolean cursorFlash = true;
	private long lastRenderTime;

	private String debug = "Debug Message";

	
	/****************************************************************
	 * 
	 * @param bounds
	 */
	public LIMETilePalette(MHRectangle bounds)
	{
		this.bounds = bounds;
		lastRenderTime = 0L;
		//MHPlatform.addMouseTouchListener(this);
	}

	/****************************************************************
	 * 
	 * @param g
	 */
	public void render(MHGraphicsCanvas g)
	{
	    // Generate the palette image and draw it.
	    long timeNow = MHGame.getGameTimerValue(); 
	    if (timeNow - lastRenderTime > 500)
	    {
	        image = getImage();
	        lastRenderTime = timeNow;
	    }
	    
	    g.drawImage(image, bounds.x, bounds.y);
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
		for (int gy = 0; gy < bounds.height;  gy += tileset.getBaseTileHeight())
		{
			for (int gx = 0; gx < bounds.width; gx += tileset.getBaseTileWidth())
			{
				g.drawRect(gx, gy, tileset.getBaseTileWidth(), tileset.getBaseTileHeight());
			}
		}
		
		//  Draw the tiles visible in the viewport.
		int x = 0;
		int y = viewY;
		for (int t = 0; t < tileset.countTiles(); t++)
		{
			g.drawImage(tileset.getTile(t).image, x, y);

			if (selectedTile != null && t == selectedTile.id /*&& cursorFlash*/)
			{
				g.setColor(MHColor.YELLOW);
				g.drawRect(x, y, tileset.getBaseTileWidth(), tileset.getBaseTileHeight());
			}
			cursorFlash = !cursorFlash;

			x += tileset.getBaseTileWidth();
			
			if (x + tileset.getBaseTileWidth() > bounds.width)
			{
				x = 0;
				y += tileset.getBaseTileHeight();
			}
		}
		
//		g.setColor(MHColor.WHITE);
//		debug = "viewY = " + viewY;
//		g.drawString(debug, 400, 30);
		
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
		return tileset.getTile(selectedTile.id);
	}
	
	
	/****************************************************************
	 * 
	 */
	private void createTileGrid()
	{
		int numColumns = bounds.width / tileset.getBaseTileWidth();
		int numRows = Math.max(1, tileset.countTiles() / numColumns);
		
		worldSpace = new MHRectangle();
		worldSpace.width = bounds.width;
		worldSpace.height = numRows * tileset.getBaseTileHeight();
		
		//grid = new MHTileGrid(numRows, numColumns);
		
		int c = 0, r = 0;
		for (int i = 0; i < MHTileSet.MAX_TILE_ID; i++)
		{
			MHTile tile = tileset.getTile(i);
			if (tile != null && tile.id != MHTileSet.NULL_TILE_ID)
			{
				//grid.putTile(tile, r, c++);
				LIMESelectableObject selectableTile = new LIMESelectableObject();
				selectableTile.id = i;
				selectableTile.bounds = new MHRectangle();
				selectableTile.bounds.x = c * tileset.getBaseTileWidth();
				selectableTile.bounds.y = r * tileset.getBaseTileHeight();
				selectableTile.bounds.width = tileset.getBaseTileWidth();
				selectableTile.bounds.height = tileset.getBaseTileHeight();
				tiles.add(selectableTile);
				
				if (++c >= numColumns)
				{
					c = 0;
					r++;
				}
			}
		}
		
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

    public void setSelectedTile(MHITileMapContent tile)
    {
        selectedTile.id = tile.getTileID();
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
        return true;
    }
}
