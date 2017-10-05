import java.io.File;
import java.util.ArrayList;
import com.mhframework.MHGlobalConstants;
import com.mhframework.MHScreen;
import com.mhframework.MHScreenManager;
import com.mhframework.core.math.MHVector;
import com.mhframework.core.math.geom.MHRectangle;
import com.mhframework.gameplay.MHGameWorldData;
import com.mhframework.gameplay.tilemap.MHLayersEnum;
import com.mhframework.gameplay.tilemap.MHTileGrid;
import com.mhframework.gameplay.tilemap.io.MHWorldIO;
import com.mhframework.platform.MHPlatform;
import com.mhframework.platform.event.MHKeyEvent;
import com.mhframework.platform.event.MHMouseTouchEvent;
import com.mhframework.platform.graphics.MHColor;
import com.mhframework.platform.graphics.MHFont;
import com.mhframework.platform.graphics.MHGraphicsCanvas;
import com.mhframework.ui.MHButton;
import com.mhframework.ui.MHCycleControl;
import com.mhframework.ui.MHInputDialogBox;
import com.mhframework.ui.MHLabel;
import com.mhframework.ui.MHNumericCycleControl;
import com.mhframework.ui.event.MHButtonListener;


public class LIMENewMapScreen extends MHScreen implements MHButtonListener
{
    public static final MHColor SHADOW_COLOR = MHPlatform.createColor(0, 0, 0, 128);

    private final String title = "New Map";
    private MHFont titleFont, textFont, dataFont;
    private static final int DIALOG_WIDTH = 640;
    private static final int DIALOG_HEIGHT = 480;
    private MHRectangle dialogBounds = new MHRectangle(), 
            titleBounds = new MHRectangle();

    private MHButton btnOK, btnCancel;
    
    private boolean initialized = false;
    
    // File name
    private MHLabel lblFileName;
    private MHButton btnMapName;
    private MHInputDialogBox dlgFileName;
    
    // Map Dimensions
    private MHLabel lblMapRows, lblMapColumns;
    private MHNumericCycleControl cycMapRows, cycMapColumns;
    
    // Tile Size
    private MHLabel lblTileWidth, lblTileHeight;
    private MHNumericCycleControl cycTileWidth, cycTileHeight;
    
    // Tile Set
    private MHLabel lblTileSet;
    private MHCycleControl cycTileSet;
    
    private MHScreen parent;
    private boolean cancelled = true;

    private boolean finished = false;
    
    public LIMENewMapScreen(MHScreen parentScreen)
    {
        parent = parentScreen;
        
        lblFileName = MHLabel.create("File Name:");
        btnMapName = MHButton.create("Untitled");
        btnMapName.addButtonListener(this);
        add(lblFileName);
        add(btnMapName);
        
        lblMapRows = MHLabel.create("Rows:");
        cycMapRows = MHNumericCycleControl.create();
        add(lblMapRows);
        add(cycMapRows);
        
        lblMapColumns = MHLabel.create("Columns:");
        cycMapColumns = MHNumericCycleControl.create();
        add(lblMapColumns);
        add(cycMapColumns);

        lblTileSet = MHLabel.create("Tile Set:");
        cycTileSet = MHCycleControl.create();
        // Set the list of tile sets in the assets/images/tilesets directory.
        cycTileSet.setValues(getTileSetDirectories());
        add(lblTileSet);
        add(cycTileSet);
        
//        lblTileWidth = MHLabel.create("Tile Width:");
//        cycTileWidth = MHNumericCycleControl.create();
//        add(lblTileWidth);
//        add(cycTileWidth);
//        
//        lblTileHeight = MHLabel.create("Tile Height:");
//        cycTileHeight = MHNumericCycleControl.create();
//        add(lblTileHeight);
//        add(cycTileHeight);
        
        btnOK = MHButton.create("OK");
        btnOK.addButtonListener(this);
        btnCancel = MHButton.create("Cancel");
        btnCancel.addButtonListener(this);
        add(btnOK);
        add(btnCancel);
    }

    
    private String[] getTileSetDirectories()
    {
        File file = new File(MHGlobalConstants.DIR_TILE_SETS);
        String[] names = file.list();
        ArrayList<String> tilesets = new ArrayList<String>();

        for(String name : names)
        {
            if (new File(MHGlobalConstants.DIR_TILE_SETS + "\\" + name).isDirectory())
            {
                tilesets.add(name);
            }
        }
        
        String[] results = new String[tilesets.size()];
        
        for (int i = 0; i < tilesets.size(); i++)
            results[i] = tilesets.get(i);
        
        return results;
    }
    

    @Override
    public void load()
    {
        if (this.dlgFileName != null)
        {
            btnMapName.setText(dlgFileName.getInput());
            dlgFileName = null;
        }
        
        
        if (!initialized)
        {
            titleFont = MHFont.getDefaultFont();
            titleFont.setHeight(32);

            textFont = MHFont.getDefaultFont();
            textFont.setHeight(16);
            
            //dataFont = MHPlatform.createFont("ORBITBN.TTF");
            dataFont = textFont;
            dataFont.setHeight(16);

            final int hgap = 20;
            final int vgap = 40;
            int dlgX = MHScreenManager.getDisplayWidth()/2 - DIALOG_WIDTH/2;
            int dlgY = MHScreenManager.getDisplayHeight()/2 - DIALOG_HEIGHT/2;
            dialogBounds = new MHRectangle(dlgX, dlgY, DIALOG_WIDTH, DIALOG_HEIGHT);
            titleBounds = new MHRectangle(dlgX, dlgY, DIALOG_WIDTH, titleFont.getHeight()+vgap);

            lblFileName.setPosition(dialogBounds.x + hgap, dialogBounds.y + titleBounds.height + vgap);
            initLabel(lblFileName);
            btnMapName.setPosition(lblFileName.getX() + lblFileName.getWidth() + hgap, lblFileName.getY());
            btnMapName.setSize(256, 32);
            btnMapName.setFont(dataFont);


            lblMapRows.setPosition(lblFileName.getX(), lblFileName.getY()+lblFileName.getHeight()+vgap);
            initLabel(lblMapRows);
            cycMapRows.setPosition(lblMapRows.getX() + lblMapRows.getWidth() + hgap, lblMapRows.getY());
            cycMapRows.setMinValue(10);
            cycMapRows.setMaxValue(1000);
            cycMapRows.setIncrement(5);
            initCycle(cycMapRows);

            lblMapColumns.setPosition(dialogBounds.x + dialogBounds.width/2, lblMapRows.getY());
            initLabel(lblMapColumns);
            cycMapColumns.setPosition(lblMapColumns.getX() + lblMapColumns.getWidth() + hgap, lblMapColumns.getY());
            cycMapColumns.setMinValue(10);
            cycMapColumns.setMaxValue(1000);
            cycMapColumns.setIncrement(5);
            initCycle(cycMapColumns);

            lblTileSet.setPosition(this.lblFileName.getX(), cycMapRows.getY()+cycMapRows.getHeight()+vgap);
            initLabel(lblTileSet);
            initCycle(cycTileSet);
            cycTileSet.setPosition(lblTileSet.getX() + lblTileSet.getWidth() + hgap, lblTileSet.getY());
            cycTileSet.setBounds(btnMapName.getBounds().clone());
            cycTileSet.getBounds().y = lblTileSet.getY();
            
//            lblTileWidth.setPosition(lblMapRows.getX(), lblTileSet.getY() + lblTileSet.getHeight() + vgap);
//            initLabel(lblTileWidth);
//            cycTileWidth.setPosition(cycMapColumns.getX(), lblTileWidth.getY());
//            cycTileWidth.setMinValue(10);
//            cycTileWidth.setMaxValue(128);
//            cycTileWidth.setIncrement(2);
//            initCycle(cycTileWidth);
//
//            lblTileHeight.setPosition(lblMapColumns.getX(), lblTileWidth.getY());
//            initLabel(lblTileHeight);
//            cycTileHeight.setPosition(cycMapRows.getX(), cycTileWidth.getY());
//            cycTileHeight.setMinValue(10);
//            cycTileHeight.setMaxValue(128);
//            cycTileHeight.setIncrement(2);
//            initCycle(cycTileHeight);

            int okButtonWidth = lblFileName.getWidth(); 
            int okButtonHeight = lblFileName.getHeight();
            int okButtonY = dialogBounds.y + dialogBounds.height - hgap - okButtonHeight;
            btnCancel.setBounds(dialogBounds.x + dialogBounds.width - okButtonWidth - vgap, okButtonY, okButtonWidth, okButtonHeight);
            btnCancel.setFont(textFont);
            btnOK.setBounds(btnCancel.getX() - hgap - okButtonWidth, okButtonY, okButtonWidth, okButtonHeight);
            btnOK.setFont(textFont);
        }
    }

    
    private void initCycle(MHCycleControl control)
    {
        control.setSize(128, 32);
        control.setFont(dataFont);
        control.setNormalColors(MHColor.LIGHT_GRAY, MHColor.BLACK, MHColor.WHITE);
    }
    
    
    private void initLabel(MHLabel label)
    {
        label.setSize(128, 32);
        label.setNormalColors(MHColor.WHITE, LIMEEditorScreen.bgColor, LIMEEditorScreen.bgColor);
        label.setFont(textFont);
        label.setAlignment(MHLabel.ALIGN_RIGHT);
    }
    
    
    private void showFileNameInput()
    {
        dlgFileName = new MHInputDialogBox(this, "Enter a name for the map file.", "File Name");
        dlgFileName.setFont(textFont);
        dlgFileName.setTitleFont(textFont);
        MHScreenManager.getInstance().changeScreen(dlgFileName);
    }

    @Override
    public void render(MHGraphicsCanvas g)
    {
        parent.render(g);
        drawShadow(g);
        
        g.setColor(LIMEEditorScreen.bgColor);
        g.fillRect(dialogBounds);
        g.setColor(MHColor.WHITE);
        g.drawRect(titleBounds);
        g.drawRect(dialogBounds);
        
        MHVector titleCenter = titleFont.centerOn(titleBounds, g, title);
        g.setFont(titleFont);
        g.setColor(SHADOW_COLOR);
        g.drawString(title, (int)titleCenter.x+4, (int)titleCenter.y+4);
        g.setColor(MHColor.WHITE);
        g.drawString(title, (int)titleCenter.x, (int)titleCenter.y);
        
        super.render(g);
    }

    
    private void drawShadow(MHGraphicsCanvas g)
    {
        final int SHADOW_DISTANCE = 20;
        g.setColor(SHADOW_COLOR);
        g.fillRect(dialogBounds.x+SHADOW_DISTANCE, dialogBounds.y+SHADOW_DISTANCE, dialogBounds.width, dialogBounds.height);
    }

    
    @Override
    public void onKeyUp(MHKeyEvent e)
    {
        if (e.getKeyCode() == MHPlatform.getKeyCodes().keyEscape())
        {
            cancelled = true;
            finished = true;
            MHScreenManager.getInstance().changeScreen(parent);
        }
        else
            super.onKeyUp(e);
    }


    @Override
    public void onButtonPressed(MHButton button, MHMouseTouchEvent e)
    {
        if (button == this.btnCancel)
        {
            cancelled = true;
            finished = true;
            MHScreenManager.getInstance().changeScreen(parent);
        }
        else if (button == this.btnOK)
        {
            cancelled = false;
            finished = true;
            MHScreenManager.getInstance().changeScreen(parent);
        }
        else if (button == this.btnMapName)
        {
            showFileNameInput();
        }

    }


    public boolean isCancelled()
    {
        return cancelled;
    }


    public MHGameWorldData getWorldData()
    {
        String mapName = btnMapName.getText();
        String tileset = (String)cycTileSet.getSelectedValue();
//        int tileHeight = ((Integer)cycTileHeight.getSelectedValue()).intValue();
//        int tileWidth = ((Integer)cycTileWidth.getSelectedValue()).intValue();
        int columns = ((Integer)cycMapColumns.getSelectedValue()).intValue();
        int rows = ((Integer)cycMapRows.getSelectedValue()).intValue();
        
        MHTileGrid floor        = new MHTileGrid(rows, columns);
        MHTileGrid floorDetails = new MHTileGrid(rows, columns);
        MHTileGrid walls        = new MHTileGrid(rows, columns);
        MHTileGrid wallDetails  = new MHTileGrid(rows, columns);
        MHTileGrid actors       = new MHTileGrid(rows, columns);
        
        MHGameWorldData data = new MHGameWorldData();
        data.setMapName(mapName);
        data.setTileSetID(tileset);
        data.setTileGrid(MHLayersEnum.FLOOR,        floor,        mapName+"Floor.ltm");
        data.setTileGrid(MHLayersEnum.FLOOR_DECALS, floorDetails, mapName+"FloorDetails.ltm");
        data.setTileGrid(MHLayersEnum.WALLS,        walls,        mapName+"Walls.ltm");
        data.setTileGrid(MHLayersEnum.WALL_DECALS,  wallDetails,  mapName+"WallDetails.ltm");
        data.setTileGrid(MHLayersEnum.ACTORS,       actors,       mapName+"Actors.ltm");
        
        MHWorldIO.saveGameWorld(data.getMapFileName(), data);
        
        return data;
    }


    public boolean isFinished()
    {
        return finished;
    }
    
    
    

}
