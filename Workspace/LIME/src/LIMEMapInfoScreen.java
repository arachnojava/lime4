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


public class LIMEMapInfoScreen extends MHScreen implements MHButtonListener
{
    public static final MHColor SHADOW_COLOR = MHPlatform.createColor(0, 0, 0, 128);

    private final String title = "Map Info";
    private MHFont titleFont, textFont;
    private static final int DIALOG_WIDTH = 640;
    private static final int DIALOG_HEIGHT = 480;
    private MHRectangle dialogBounds = new MHRectangle(), 
            titleBounds = new MHRectangle();

    private MHButton btnOK;
    
    private boolean initialized = false;
    
    // File name
    private MHLabel lblFileName;
    private MHLabel btnFileName;
    
    // Map Dimensions
    private MHLabel lblMapRows, lblMapColumns;
    private MHLabel cycMapRows, cycMapColumns;
    
    // Tile Size
    private MHLabel lblTileWidth, lblTileHeight;
    private MHLabel cycTileWidth, cycTileHeight;
    
    // Tile Set
    private MHLabel lblTileSet;
    private MHLabel cycTileSet;
    
    // Layer files
    private MHLabel lblFloorFile, txtFloorFile;
    private MHLabel lblFloorDetailsFile, txtFloorDetailsFile;
    private MHLabel lblWallsFile, txtWallsFile;
    private MHLabel lblWallDetailsFile, txtWallDetailsFile;
    
    private MHScreen parent;
    
    public LIMEMapInfoScreen(MHScreen parentScreen, MHGameWorldData data)
    {
        parent = parentScreen;
        
        lblFileName = MHLabel.create("File Name:");
        btnFileName = MHLabel.create("Untitled");
        add(lblFileName);
        add(btnFileName);
        
        lblMapRows = MHLabel.create("Rows:");
        cycMapRows = MHLabel.create("");
        add(lblMapRows);
        add(cycMapRows);
        
        lblMapColumns = MHLabel.create("Columns:");
        cycMapColumns = MHLabel.create("");
        add(lblMapColumns);
        add(cycMapColumns);

        lblTileSet = MHLabel.create("Tile Set:");
        cycTileSet = MHLabel.create("");
        add(lblTileSet);
        add(cycTileSet);
        
        lblTileWidth = MHLabel.create("Tile Width:");
        cycTileWidth = MHLabel.create("");
        add(lblTileWidth);
        add(cycTileWidth);
        
        lblTileHeight = MHLabel.create("Tile Height:");
        cycTileHeight = MHLabel.create("");
        add(lblTileHeight);
        add(cycTileHeight);
        
        txtFloorFile = MHLabel.create("");
        txtFloorDetailsFile = MHLabel.create("");
        txtWallsFile = MHLabel.create("");
        txtWallDetailsFile = MHLabel.create("");
        add(txtFloorFile);
        add(txtFloorDetailsFile);
        add(txtWallsFile);
        add(txtWallDetailsFile);
        
        btnOK = MHButton.create("OK");
        btnOK.addButtonListener(this);
        add(btnOK);
        
        setWorldData(data);
    }


    @Override
    public void load()
    {
        if (!initialized)
        {
            titleFont = MHFont.getDefaultFont();
            titleFont.setHeight(32);

            textFont = MHFont.getDefaultFont();
            textFont.setHeight(16);
            
            final int hgap = 20;
            final int vgap = 24;
            int dlgX = MHScreenManager.getDisplayWidth()/2 - DIALOG_WIDTH/2;
            int dlgY = MHScreenManager.getDisplayHeight()/2 - DIALOG_HEIGHT/2;
            dialogBounds = new MHRectangle(dlgX, dlgY, DIALOG_WIDTH, DIALOG_HEIGHT);
            titleBounds = new MHRectangle(dlgX, dlgY, DIALOG_WIDTH, titleFont.getHeight()+vgap);

            lblFileName.setPosition(dialogBounds.x + hgap, dialogBounds.y + titleBounds.height + vgap);
            initLabel(lblFileName);
            btnFileName.setPosition(lblFileName.getX() + lblFileName.getWidth() + hgap, lblFileName.getY());
            btnFileName.setSize(256, 32);


            lblMapRows.setPosition(lblFileName.getX(), lblFileName.getY()+lblFileName.getHeight()+vgap);
            initLabel(lblMapRows);
            cycMapRows.setPosition(lblMapRows.getX() + lblMapRows.getWidth() + hgap, lblMapRows.getY());

            lblMapColumns.setPosition(dialogBounds.x + dialogBounds.width/2, lblMapRows.getY());
            initLabel(lblMapColumns);
            cycMapColumns.setPosition(lblMapColumns.getX() + lblMapColumns.getWidth() + hgap, lblMapColumns.getY());

            lblTileSet.setPosition(this.lblFileName.getX(), cycMapRows.getY()+cycMapRows.getHeight()+vgap);
            initLabel(lblTileSet);

            cycTileSet.setPosition(lblTileSet.getX() + lblTileSet.getWidth() + hgap, lblTileSet.getY());
            cycTileSet.setBounds(btnFileName.getBounds().clone());
            cycTileSet.getBounds().y = lblTileSet.getY();
            
            lblTileWidth.setPosition(lblMapRows.getX(), lblTileSet.getY() + lblTileSet.getHeight() + vgap);
            initLabel(lblTileWidth);
            cycTileWidth.setPosition(cycMapColumns.getX(), lblTileWidth.getY());


            lblTileHeight.setPosition(lblMapColumns.getX(), lblTileWidth.getY());
            initLabel(lblTileHeight);
            cycTileHeight.setPosition(cycMapRows.getX(), cycTileWidth.getY());

            lblFloorFile = MHLabel.create("Floor File:");
            initLabel(lblFloorFile);
            lblFloorFile.setBounds(lblTileWidth.getBounds().clone());
            lblFloorFile.setPosition(lblFloorFile.getX(), lblTileWidth.getY()+lblTileWidth.getHeight()+vgap);
            txtFloorFile.setBounds(lblFloorFile.getBounds().clone());
            txtFloorFile.setPosition(cycTileHeight.getX(), lblFloorFile.getY());
            add(lblFloorFile);

            lblFloorDetailsFile = MHLabel.create("Floor Details File:");
            initLabel(lblFloorDetailsFile);
            lblFloorDetailsFile.setBounds(lblTileHeight.getBounds().clone());
            lblFloorDetailsFile.setPosition(lblFloorDetailsFile.getX(), lblTileHeight.getY()+lblTileHeight.getHeight()+vgap);
            txtFloorDetailsFile.setBounds(lblFloorFile.getBounds().clone());
            txtFloorDetailsFile.setPosition(cycTileWidth.getX(), lblFloorFile.getY());
            add(lblFloorDetailsFile);
            
            lblWallsFile = MHLabel.create("Walls File:");
            initLabel(lblWallsFile);
            lblWallsFile.setBounds(lblFloorFile.getX(), lblFloorFile.getY()+lblFloorFile.getHeight()+vgap, lblFloorFile.getWidth(), lblFloorFile.getHeight());
            txtWallsFile.setBounds(txtFloorFile.getX(), lblWallsFile.getY(), lblWallsFile.getWidth(), lblWallsFile.getHeight());
            add(lblWallsFile);

            lblWallDetailsFile = MHLabel.create("Wall Details File:");
            initLabel(lblWallDetailsFile);
            lblWallDetailsFile.setBounds(lblFloorDetailsFile.getX(), lblWallsFile.getY(), lblWallsFile.getWidth(), lblWallsFile.getHeight());
            txtWallDetailsFile.setBounds(txtFloorDetailsFile.getX(), lblWallDetailsFile.getY(), lblWallsFile.getWidth(), lblWallsFile.getHeight());
            add(lblWallDetailsFile);
            
            int okButtonWidth = lblFileName.getWidth(); 
            int okButtonHeight = lblFileName.getHeight();
            int okButtonY = dialogBounds.y + dialogBounds.height - hgap - okButtonHeight;
            btnOK.setBounds(dialogBounds.x + dialogBounds.width - okButtonWidth - vgap, okButtonY, okButtonWidth, okButtonHeight);
            btnOK.setFont(textFont);
        }
    }

    
    private void initCycle(MHCycleControl control)
    {
        control.setSize(128, 32);
        control.setFont(textFont);
        control.setNormalColors(MHColor.LIGHT_GRAY, MHColor.BLACK, MHColor.WHITE);
    }
    
    
    private void initLabel(MHLabel label)
    {
        label.setSize(128, 32);
        label.setNormalColors(MHColor.WHITE, LIMEEditorScreen.bgColor, LIMEEditorScreen.bgColor);
        label.setFont(textFont);
        label.setAlignment(MHLabel.ALIGN_RIGHT);
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
            MHScreenManager.getInstance().changeScreen(parent);
        }
        else
            super.onKeyUp(e);
    }


    @Override
    public void onButtonPressed(MHButton button, MHMouseTouchEvent e)
    {
        if (button == this.btnOK)
        {
            MHScreenManager.getInstance().changeScreen(parent);
        }
    }



    private void setWorldData(MHGameWorldData data)
    {
        btnFileName.setText(data.getMapFileName());
        cycTileSet.setText(data.getTileSetID());
        cycTileHeight.setText(data.getTileHeight()+"");
        cycTileWidth.setText(data.getTileWidth()+"");
        cycMapColumns.setText(data.getWorldWidth()+"");
        cycMapRows.setText(data.getWorldHeight()+"");
        txtFloorFile.setText(data.getLayerFileName(MHLayersEnum.FLOOR.getID()));
        txtFloorDetailsFile.setText(data.getLayerFileName(MHLayersEnum.FLOOR_DECALS.getID()));
        txtWallsFile.setText(data.getLayerFileName(MHLayersEnum.WALLS.getID()));
        txtWallDetailsFile.setText(data.getLayerFileName(MHLayersEnum.WALL_DECALS.getID()));
        
//        data.setTileGrid(MHLayersEnum.FLOOR,        floor,        filename+"Floor.ltm");
//        data.setTileGrid(MHLayersEnum.FLOOR_DECALS, floorDetails, filename+"FloorDetails.ltm");
//        data.setTileGrid(MHLayersEnum.WALLS,        walls,        filename+"Walls.ltm");
//        data.setTileGrid(MHLayersEnum.WALL_DECALS,  wallDetails,  filename+"WallDetails.ltm");
    }
}
