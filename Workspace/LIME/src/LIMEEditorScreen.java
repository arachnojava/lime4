import java.util.ArrayList;
import com.mhframework.MHGame;
import com.mhframework.MHGlobalConstants;
import com.mhframework.MHScreen;
import com.mhframework.MHScreenManager;
import com.mhframework.core.math.MHVector;
import com.mhframework.core.math.geom.MHRectangle;
import com.mhframework.gameplay.MHGameWorldData;
import com.mhframework.gameplay.actor.MHActor;
import com.mhframework.gameplay.actor.MHActorList;
import com.mhframework.gameplay.actor.MHTileMapActor;
import com.mhframework.gameplay.tilemap.MHITileMapContent;
import com.mhframework.gameplay.tilemap.MHLayersEnum;
import com.mhframework.gameplay.tilemap.MHMapCellAddress;
import com.mhframework.gameplay.tilemap.MHTile;
import com.mhframework.gameplay.tilemap.MHTileGrid;
import com.mhframework.gameplay.tilemap.MHTileSet;
import com.mhframework.gameplay.tilemap.io.MHMapFileInfo;
import com.mhframework.gameplay.tilemap.io.MHWorldIO;
import com.mhframework.gameplay.tilemap.view.MHIsometricMapView;
import com.mhframework.gameplay.tilemap.view.MHTileMapView;
import com.mhframework.platform.MHPlatform;
import com.mhframework.platform.event.MHInputEventHandler;
import com.mhframework.platform.event.MHKeyEvent;
import com.mhframework.platform.event.MHMouseTouchEvent;
import com.mhframework.platform.graphics.MHBitmapImage;
import com.mhframework.platform.graphics.MHColor;
import com.mhframework.platform.graphics.MHFont;
import com.mhframework.platform.graphics.MHGraphicsCanvas;
import com.mhframework.resources.MHResourceManager;
import com.mhframework.ui.MHButton;
import com.mhframework.ui.MHDialogBox;
import com.mhframework.ui.MHDropDownMenu;
import com.mhframework.ui.MHImageScreen;
import com.mhframework.ui.MHInputDialogBox;
import com.mhframework.ui.MHLabel;
import com.mhframework.ui.MHMouseCursor.Type;
import com.mhframework.ui.MHOpenFileDialog;
import com.mhframework.ui.MHSlideOutPanel;
import com.mhframework.ui.event.MHButtonListener;


public class LIMEEditorScreen extends MHScreen implements MHButtonListener
{
	// Instance variables
	private static final MHScreen instance = new LIMEEditorScreen();
	private boolean isInitialized = false;
	private boolean isPainting = false;
	
	// Actor list
	MHActorList actorList = new MHActorList();
	
	// Fonts
	private MHFont dataFont;
	
	// GUI
	private MHTileMapView editorPane;
	private MHTileMapView rectangularMap, isoDiamondMap, isoStaggeredMap;
	
	private MHDropDownMenu mnuFile;
	private MHButton btnFileNew;
	private MHButton btnFileOpen;
	private MHButton btnFileSave;
	private MHButton btnFileSaveAs;
	private MHButton btnFileMapInfo;
	private MHButton btnFileExportImage;
	private MHButton btnFileExit;

//	private MHDropDownMenu mnuEdit;
	private MHButton btnEditFloors;
	private MHButton btnEditFloorDetails;
	private MHButton btnEditWalls;
	private MHButton btnEditWallDetails;
    private MHButton btnEditActors;

	private MHDropDownMenu mnuView;
	private MHButton btnViewFloors;
	private MHButton btnViewFloorDetails;
	private MHButton btnViewWalls;
	private MHButton btnViewWallDetails;
	private MHButton btnViewActors;
//	private MHButton btnViewOverlay;
//	private MHButton btnViewObjectBrowser;
	private MHButton btnViewGrid;
	private MHButton btnViewMap;
    private MHButton btnViewRectangular;
	private MHButton btnViewIsoDiamond;
    private MHButton btnViewIsoStaggered;
	
	//private MHButton btnViewTileIDs;

	private MHDropDownMenu mnuData;
	private MHButton btnDataFill;
	private MHButton btnDataClear;

	// Palette mode selectors.
	private MHButton btnTilesPalette;
	private MHButton btnActorsPalette;
	
	// Toolbox controls.
	private MHLabel lblTools;
	private MHButton btnEraser;
    private MHButton btnPicker;
    private MHButton btnGridToggle;
    private MHButton btnIDToggle;
    
    private MHBitmapImage iconEraser;
    private MHBitmapImage iconPicker;
    private boolean eraserToolActive = false;
    private boolean pickToolActive = false; 
	
    // Scroll arrows.
    private MHButton btnScrollUp;
    private MHButton btnScrollDown;
    private MHButton btnScrollLeft;
    private MHButton btnScrollRight;
    
    // Custom dialogs
    MHInputDialogBox dlgSaveAs;
    LIMENewMapScreen dlgNewMap;
    LIMEMapInfoScreen dlgMapInfo;
    MHOpenFileDialog dlgOpen;
    
    private MHLayersEnum currentLayer = MHLayersEnum.FLOOR;
	
	private ArrayList<MHDropDownMenu> menuBar = new ArrayList<MHDropDownMenu>();

	// Map editor
	private MHRectangle rectEditor;
	
	// Tile palette
	private MHRectangle rectPalette;
	private LIMETilePalette tilePalette, currentPalette;
	private LIMEActorPalette actorPalette;
	private MHButton btnPaletteUp, btnPaletteDown;

	// Slide-out panels
	private MHSlideOutPanel viewControlPanel;
    private MHSlideOutPanel editControlPanel;
	
	// Colors
	public static MHColor bgColor;
	
	// Input
	private static double SCROLL_SPEED = 0.5;
	private double vScroll=0, hScroll=0;
    private MHButton btnViewPanelTab;
    private MHButton btnEditPanelTab;

	
	private LIMEEditorScreen()
	{
	}

	
	public static MHScreen getInstance()
	{
		return instance;
	}

	
	private void init()
	{
		int menuWidth = 150;
		
		dataFont = MHFont.getDefaultFont().clone();
		dataFont.setHeight(20);
		
		viewControlPanel = new MHSlideOutPanel(150, 150);
		btnViewPanelTab = viewControlPanel.createTab("Layer Visibility", MHFont.getDefaultFont(), MHSlideOutPanel.TAB_LEFT);
        btnViewPanelTab.addButtonListener(this);
        add(btnViewPanelTab);
		viewControlPanel.setHiddenLocation(MHScreenManager.getDisplayWidth(), 50);
		viewControlPanel.setVisibleLocation(MHScreenManager.getDisplayWidth()-viewControlPanel.getWidth(), 50);
		btnViewFloors = MHButton.create("Floor");
		btnViewFloors.addButtonListener(this);
		btnViewFloors.setBackgroundColor(MHColor.GREEN);
        btnViewFloorDetails = MHButton.create("Floor Details");
        btnViewFloorDetails.addButtonListener(this);
        btnViewFloorDetails.setBackgroundColor(MHColor.GREEN);
        btnViewWalls = MHButton.create("Walls");
        btnViewWalls.addButtonListener(this);
        btnViewWalls.setBackgroundColor(MHColor.GREEN);
        btnViewWallDetails = MHButton.create("Wall Details");
        btnViewWallDetails.addButtonListener(this);
        btnViewWallDetails.setBackgroundColor(MHColor.GREEN);
        btnViewActors = MHButton.create("Actors");
        btnViewActors.addButtonListener(this);
        btnViewActors.setBackgroundColor(MHColor.GREEN);
        add(btnViewActors);
        add(btnViewWallDetails);
        add(btnViewWalls);
        add(btnViewFloorDetails);
        add(btnViewFloors);
        viewControlPanel.addComponent(btnViewActors);
        viewControlPanel.addComponent(btnViewWallDetails);
        viewControlPanel.addComponent(btnViewWalls);
        viewControlPanel.addComponent(btnViewFloorDetails);
        viewControlPanel.addComponent(btnViewFloors);
        
		
        editControlPanel = new MHSlideOutPanel(150, 150);
        btnEditPanelTab = editControlPanel.createTab("Edit Layer", MHFont.getDefaultFont(), MHSlideOutPanel.TAB_LEFT);
        editControlPanel.setHiddenLocation(MHScreenManager.getDisplayWidth(), (int) (viewControlPanel.getY()+viewControlPanel.getHeight()+64));
        editControlPanel.setVisibleLocation(MHScreenManager.getDisplayWidth()-viewControlPanel.getWidth(), (int) (viewControlPanel.getY()+viewControlPanel.getHeight()+64));
        btnEditPanelTab.addButtonListener(this);
        add(btnEditPanelTab);
        btnEditFloors = MHButton.create(MHButton.Type.TEXT, "Edit Floor");
        btnEditFloors.addButtonListener(this);
        add(btnEditFloors);
        btnEditFloorDetails = MHButton.create(MHButton.Type.TEXT, "Edit Floor Details");
        btnEditFloorDetails.addButtonListener(this);
        add(btnEditFloorDetails);
        btnEditWalls = MHButton.create(MHButton.Type.TEXT, "Edit Walls");
        btnEditWalls.addButtonListener(this);
        add(btnEditWalls);
        btnEditWallDetails = MHButton.create(MHButton.Type.TEXT, "Edit Wall Details");
        btnEditWallDetails.addButtonListener(this);
        add(btnEditWallDetails);
        btnEditActors = MHButton.create(MHButton.Type.TEXT, "Edit Actors");
        btnEditActors.addButtonListener(this);
        add(btnEditActors);

        editControlPanel.addComponent(btnEditActors);
        editControlPanel.addComponent(btnEditWallDetails);
        editControlPanel.addComponent(btnEditWalls);
        editControlPanel.addComponent(btnEditFloorDetails);
        editControlPanel.addComponent(btnEditFloors);
        
		// File menu
		mnuFile = new MHDropDownMenu("File", 0, 0, menuWidth, 20, this);
		btnFileNew = MHButton.create(MHButton.Type.TEXT, "New");
		btnFileNew.addButtonListener(this);
		mnuFile.addMenuItem(btnFileNew);
		
		btnFileOpen = MHButton.create(MHButton.Type.TEXT, "Open...");
		btnFileOpen.addButtonListener(this);
		mnuFile.addMenuItem(btnFileOpen);
		
		btnFileSave = MHButton.create(MHButton.Type.TEXT, "Save");
		btnFileSave.addButtonListener(this);
		mnuFile.addMenuItem(btnFileSave);
		
		btnFileSaveAs = MHButton.create(MHButton.Type.TEXT, "Save As...");
		btnFileSaveAs.addButtonListener(this);
		mnuFile.addMenuItem(btnFileSaveAs);
		
		btnFileMapInfo = MHButton.create(MHButton.Type.TEXT, "Map Info");
		btnFileMapInfo.addButtonListener(this);
		mnuFile.addMenuItem(btnFileMapInfo);

		btnFileExportImage = MHButton.create(MHButton.Type.TEXT, "Export Image");
		btnFileExportImage.addButtonListener(this);
		mnuFile.addMenuItem(btnFileExportImage);
		
		btnFileExit = MHButton.create(MHButton.Type.TEXT, "Exit LIME");
		btnFileExit.addButtonListener(this);
		mnuFile.addMenuItem(btnFileExit);

		// Edit menu
		//mnuEdit = new MHDropDownMenu("Edit", mnuFile.getX()+mnuFile.getWidth()+1, 0, menuWidth, 20, this);

		//mnuEdit.addMenuItem(btnEditWallDetails);
		
//		btnEditActors = MHButton.create(MHButton.Type.TEXT, "Edit Actors");
//		btnEditActors.addButtonListener(this);
		//mnuEdit.addMenuItem(btnEditActors);
			
		// View menu
		mnuView = new MHDropDownMenu("View", mnuFile.getX()+mnuFile.getWidth()+1, 0, menuWidth, 20, this);
		btnViewGrid = MHButton.create(MHButton.Type.TEXT, "Toggle Grid");
		btnViewGrid.addButtonListener(this);
		mnuView.addMenuItem(btnViewGrid);

		btnViewMap = MHButton.create(MHButton.Type.TEXT, "View Full Map");
		btnViewMap.addButtonListener(this);
		mnuView.addMenuItem(btnViewMap);
		
        btnViewRectangular = MHButton.create(MHButton.Type.TEXT, "Rectangular");
        btnViewRectangular.addButtonListener(this);
        mnuView.addMenuItem(btnViewRectangular);

		btnViewIsoDiamond = MHButton.create(MHButton.Type.TEXT, "Isometric Diamond");
		btnViewIsoDiamond.addButtonListener(this);
        mnuView.addMenuItem(btnViewIsoDiamond);

        btnViewIsoStaggered = MHButton.create(MHButton.Type.TEXT, "Isometric Staggered");
        btnViewIsoStaggered.addButtonListener(this);
        mnuView.addMenuItem(btnViewIsoStaggered);
        
		// Data menu
		mnuData = new MHDropDownMenu("Data", mnuView.getX()+mnuView.getWidth()+1, 0, menuWidth, 20, this);
		btnDataFill = MHButton.create(MHButton.Type.TEXT, "Fill Layer");
		btnDataFill.addButtonListener(this);
		mnuData.addMenuItem(btnDataFill);

		btnDataClear = MHButton.create(MHButton.Type.TEXT, "Clear Layer");
		btnDataClear.addButtonListener(this);
		mnuData.addMenuItem(btnDataClear);

		menuBar.add(mnuFile);
		//menuBar.add(mnuEdit);
		menuBar.add(mnuView);
		menuBar.add(mnuData);

		for (MHDropDownMenu m : menuBar)
		{
			add(m);
		}
		
		bgColor = MHPlatform.createColor(0, 100, 0);
		
        int scrollButtonSize = 16;

        rectEditor = new MHRectangle(10+scrollButtonSize, 30+scrollButtonSize, MHScreenManager.getDisplayWidth()-20-scrollButtonSize*2, (int)(MHScreenManager.getDisplayHeight()*0.75)-scrollButtonSize*2);

		btnScrollUp = MHButton.create(MHButton.Type.ICON, "assets\\images\\IconUpArrow.png");
		btnScrollUp.setPosition(rectEditor.x, rectEditor.y-scrollButtonSize);
		btnScrollUp.setSize(rectEditor.width, scrollButtonSize);
		
        btnScrollDown = MHButton.create(MHButton.Type.ICON, "assets\\images\\IconDownArrow.png");
        btnScrollDown.setPosition(rectEditor.x, rectEditor.y+rectEditor.height);
        btnScrollDown.setSize(rectEditor.width, scrollButtonSize);
        
        btnScrollRight = MHButton.create(MHButton.Type.ICON, "assets\\images\\IconRightArrow.png");
        btnScrollRight.setPosition(rectEditor.x+rectEditor.width, rectEditor.y);
        btnScrollRight.setSize(scrollButtonSize, rectEditor.height);
        
        btnScrollLeft = MHButton.create(MHButton.Type.ICON, "assets\\images\\IconLeftArrow.png");
        btnScrollLeft.setPosition(rectEditor.x-scrollButtonSize, rectEditor.y);
        btnScrollLeft.setSize(scrollButtonSize, rectEditor.height);
		
        add(btnScrollUp);
        add(btnScrollDown);
        add(btnScrollLeft);
        add(btnScrollRight);
        
		// FIXME:  Remove this test code once it's working.
		//MHWorldIO.generateTestFiles("TestMap");
		
//        open(MHGlobalConstants.DIR_DATA+"/test.lime");
		MHGameWorldData world = MHWorldIO.loadGameWorld(MHGlobalConstants.DIR_DATA+"/TestMap.lime");
		rectangularMap = MHTileMapView.create(MHTileMapView.Type.RECTANGULAR, world);
		rectangularMap.setScreenSpace(rectEditor);
		rectangularMap.setMouseScroll(false);
		rectangularMap.setCursorOn(true);
        rectangularMap.setMapData(world);

		isoDiamondMap = MHTileMapView.create(MHTileMapView.Type.DIAMOND, world);
        isoDiamondMap.setScreenSpace(rectEditor);
        isoDiamondMap.setMouseScroll(false);
        isoDiamondMap.setCursorOn(true);
        isoDiamondMap.setMapData(world);

        isoStaggeredMap = MHTileMapView.create(MHTileMapView.Type.STAGGERED, world);
        isoStaggeredMap.setScreenSpace(rectEditor);
        isoStaggeredMap.setMouseScroll(false);
        isoStaggeredMap.setCursorOn(true);
        isoStaggeredMap.setMapData(world);

		editorPane = rectangularMap;
		
        selectEditLayer(MHLayersEnum.FLOOR, btnEditFloors);
        
        // Toolbox
        final int BUTTON_SIZE = 32;
        lblTools = MHLabel.create("Tools");
        lblTools.setPosition(rectEditor.x-scrollButtonSize, rectEditor.y+rectEditor.height+scrollButtonSize+10);
        lblTools.setSize(66, 22);
        lblTools.setAlignment(MHLabel.ALIGN_CENTER);
        this.add(lblTools);
        
        iconEraser = MHResourceManager.getInstance().getImage("assets\\images\\IconEraser.png");
        btnEraser = MHButton.create(MHButton.Type.ICON, "assets\\images\\IconEraser.png");
        btnEraser.setSize(BUTTON_SIZE, BUTTON_SIZE);
        btnEraser.setPosition(lblTools.getX(), lblTools.getY() + lblTools.getHeight() + 2);
        btnEraser.addButtonListener(this);
        this.add(btnEraser);
        
        iconPicker = MHResourceManager.getInstance().getImage("assets\\images\\IconPicker.png");
        btnPicker = MHButton.create(MHButton.Type.ICON, "assets\\images\\IconPicker.png");
        btnPicker.setSize(BUTTON_SIZE, BUTTON_SIZE);
        btnPicker.setPosition(btnEraser.getX() + BUTTON_SIZE + 2, btnEraser.getY());
        btnPicker.addButtonListener(this);
        this.add(btnPicker);

        btnGridToggle = MHButton.create(MHButton.Type.ICON, "assets\\images\\IconToggleGrid.png");
        btnGridToggle.setSize(BUTTON_SIZE, BUTTON_SIZE);
        btnGridToggle.setPosition(btnEraser.getX(), btnEraser.getY() + BUTTON_SIZE + 2);
        btnGridToggle.addButtonListener(this);
        this.add(btnGridToggle);

        btnIDToggle = MHButton.create(MHButton.Type.ICON, "assets\\images\\IconToggleIDs.png");
        btnIDToggle.setSize(BUTTON_SIZE, BUTTON_SIZE);
        btnIDToggle.setPosition(btnPicker.getX(), btnGridToggle.getY());
        btnIDToggle.addButtonListener(this);
        this.add(btnIDToggle);

        // Make room for the toolbox to the left of the palette.
        final int PAL_SPACING = 1;
		rectPalette = new MHRectangle(lblTools.getX() + lblTools.getWidth() + 50, lblTools.getY(), rectEditor.width - BUTTON_SIZE - PAL_SPACING*2 - lblTools.getWidth() - 30, MHScreenManager.getDisplayHeight()-(rectEditor.y+rectEditor.height+50));
		tilePalette = new LIMETilePalette(rectPalette);
		actorPalette = new LIMEActorPalette(rectPalette);
		currentPalette = tilePalette;
		MHTileSet ts = new MHTileSet(world.getTileSetID());
		tilePalette.setTileSet(ts);
		actorPalette.setTileSet(ts);
		btnPaletteUp = MHButton.create(MHButton.Type.ICON, "assets\\images\\IconUpArrow.png");
		btnPaletteUp.setSize(BUTTON_SIZE, BUTTON_SIZE);
		btnPaletteUp.setPosition(rectPalette.x+rectPalette.width+PAL_SPACING, rectPalette.y);
		btnPaletteUp.addButtonListener(this);
        add(btnPaletteUp);

        btnPaletteDown = MHButton.create(MHButton.Type.ICON, "assets\\images\\IconDownArrow.png");
        btnPaletteDown.setSize(BUTTON_SIZE, BUTTON_SIZE);
        btnPaletteDown.setPosition(btnPaletteUp.getX(),btnPaletteUp.getY()+btnPaletteUp.getHeight()+PAL_SPACING);
        btnPaletteDown.addButtonListener(this);
        add(btnPaletteDown);
		
	    // Palette mode selectors.
        btnTilesPalette = MHButton.create(MHButton.Type.ICON, "assets\\images\\IconTiles.png");
        btnTilesPalette.setSize(BUTTON_SIZE, BUTTON_SIZE);
        btnTilesPalette.setPosition(rectPalette.x - btnTilesPalette.getWidth() - PAL_SPACING, rectPalette.y);
        btnTilesPalette.addButtonListener(this);
        this.add(btnTilesPalette);

        btnActorsPalette = MHButton.create(MHButton.Type.ICON, "assets\\images\\IconActors.png");
        btnActorsPalette.setSize(BUTTON_SIZE, BUTTON_SIZE);
        btnActorsPalette.setPosition(btnTilesPalette.getX(), btnTilesPalette.getY() + btnTilesPalette.getHeight() + PAL_SPACING);
        btnActorsPalette.addButtonListener(this);
        this.add(btnActorsPalette);


		super.setStatusBarText("Status bar!");

		isInitialized = true;
	}

	
	private void switchMapView(MHTileMapView newView)
	{
	    newView.setCursorOn(editorPane.isCursorOn());
	    newView.setGridOn(editorPane.isGridOn());
	    newView.setIdOn(editorPane.isIdOn());
	    newView.setMapData(editorPane.getMapData());
	    newView.setScreenSpace(editorPane.getScreenSpace());
	    
	    editorPane = newView;
	}
	
	@Override
	public void load() 
	{
	    if (!isInitialized) 
			init();
	    else if (dlgOpen != null && dlgOpen.isFinished())
	    {
	        String filename = dlgOpen.getSelectedFileName();

	        if (filename != null)
	            open(filename);

	        dlgOpen = null;
	    }
	    else if (dlgSaveAs != null && dlgSaveAs.getInput().trim().length() > 0)
        {
	        save(dlgSaveAs.getInput());
            
            dlgSaveAs = null;
        }
	    else if (dlgNewMap != null && dlgNewMap.isFinished())
	    {
	        // TODO:  Make sure new map files are saved in the right place.
	        if (!dlgNewMap.isCancelled())
	        {
	            save(dlgNewMap.getWorldData().getMapName());
	            
                editorPane.setMapData(dlgNewMap.getWorldData());
	            commandFillLayer(MHLayersEnum.FLOOR,        tilePalette.getTileSet().getTile(MHTileSet.NULL_TILE_ID));
                commandFillLayer(MHLayersEnum.FLOOR_DECALS, tilePalette.getTileSet().getTile(MHTileSet.NULL_TILE_ID));
                commandFillLayer(MHLayersEnum.WALLS,        tilePalette.getTileSet().getTile(MHTileSet.NULL_TILE_ID));
                commandFillLayer(MHLayersEnum.WALL_DECALS,  tilePalette.getTileSet().getTile(MHTileSet.NULL_TILE_ID));
                this.actorList = new MHActorList();
                
                MHTileSet ts = new MHTileSet(dlgNewMap.getWorldData().getTileSetID());
                tilePalette.setTileSet(ts);
                actorPalette.setTileSet(ts);
	        }
	        
	        dlgNewMap = null;
	    }
		else
			for (int i = 0; i < menuBar.size(); i++)
			{
				menuBar.get(i).hide();
				menuBar.remove(i);
			}
	}

	
	private void open(String filename)
	{
        filename = validateMapFileExtension(validateFilePath(filename));
        MHGameWorldData world = MHWorldIO.loadGameWorld(filename);
        editorPane.setMapData(world);
        MHTileSet ts = new MHTileSet(world.getTileSetID());
        tilePalette.setTileSet(ts);
        actorPalette.setTileSet(ts);
	}
	
	
	private void save(String mapName)
	{
        editorPane.getMapData().setMapName(mapName);
        editorPane.getMapData().setLayerFileName(MHLayersEnum.FLOOR, mapName+"Floor.ltm");
        editorPane.getMapData().setLayerFileName(MHLayersEnum.FLOOR_DECALS, mapName+"FloorDetails.ltm");
        editorPane.getMapData().setLayerFileName(MHLayersEnum.WALLS, mapName+"Walls.ltm");
        editorPane.getMapData().setLayerFileName(MHLayersEnum.WALL_DECALS, mapName+"WallDetails.ltm");
        editorPane.getMapData().setLayerFileName(MHLayersEnum.ACTORS, mapName+"Actors.ltm");
        MHWorldIO.saveGameWorld(editorPane.getMapData().getMapFileName(), editorPane.getMapData());
        //MHWorldIO.saveActorFile(editorPane.getMapData().getLayerFileName(MHLayersEnum.ACTORS), actorList);
	}
	
	
	private String validateFilePath(String filename)
	{
        if (!filename.startsWith(MHGlobalConstants.DIR_DATA+"/"))
            filename = MHGlobalConstants.DIR_DATA + "/" + filename;
        
        return filename;
	}

	   
    private String validateMapFileExtension(String filename)
    {
        if (!filename.endsWith(MHMapFileInfo.WORLD_FILE_EXTENSION))
            filename += MHMapFileInfo.WORLD_FILE_EXTENSION;
        
        return filename;
    }

    
	@Override
	public void update(long elapsedTime) 
	{
		if (isPainting)// && currentPalette == tilePalette)
		{
			int r = editorPane.getCursorAddress().row;
			int c = editorPane.getCursorAddress().column;
		    if (eraserToolActive)
		        editorPane.getMapData().getLayer(currentLayer).getDataGrid()[r][c] = currentPalette.getTileSet().getTile(MHTileSet.NULL_TILE_ID);
		    else
		        editorPane.getMapData().getLayer(currentLayer).getDataGrid()[r][c] = currentPalette.getSelectedTile();
		}

		viewControlPanel.update(elapsedTime);
		editControlPanel.update(elapsedTime);
		
		int min = (int) Math.min(viewControlPanel.getX(), editControlPanel.getX());
        MHRectangle es = editorPane.getScreenSpace();
        es.width = min - btnScrollRight.getWidth()*4 - es.x ;
        //editorPane.setScreenSpace(es);
		editorPane.update(elapsedTime);
		editorPane.scrollMap((int)(hScroll * elapsedTime), (int)(vScroll * elapsedTime));
		btnScrollRight.setPosition(editorPane.getScreenSpace().x+editorPane.getScreenSpace().width, editorPane.getScreenSpace().y);
		btnScrollUp.setWidth(editorPane.getScreenSpace().width);
        btnScrollDown.setWidth(editorPane.getScreenSpace().width);

		if (editorPane.canScrollUp())
		    btnScrollUp.setVisible(true);
		else
		    btnScrollUp.setVisible(false);

		if (editorPane.canScrollDown())
            btnScrollDown.setVisible(true);
        else
            btnScrollDown.setVisible(false);
		
        if (editorPane.canScrollLeft())
            btnScrollLeft.setVisible(true);
        else
            btnScrollLeft.setVisible(false);
        
        if (editorPane.canScrollRight())
            btnScrollRight.setVisible(true);
        else
            btnScrollRight.setVisible(false);
		
		super.update(elapsedTime);
	}

	
	@Override
	public void render(MHGraphicsCanvas g) 
	{
		g.fill(bgColor);
		
		// Draw menu bar background.
		g.setColor(MHColor.LIGHT_GRAY);
		g.fillRect(0, 0, MHScreenManager.getDisplayWidth(), mnuFile.getHeight());
		
		editorPane.render(g);

		// Data display on editor
		drawText(g, editorPane.getMapData().getMapFileName(), editorPane.getScreenSpace().x + 10, editorPane.getScreenSpace().y + dataFont.getHeight() + 10);
		int right = editorPane.getScreenSpace().x + editorPane.getScreenSpace().width;
		String editLayer = "Editing " + currentLayer.toString();
		drawText(g, editLayer, right-dataFont.stringWidth(editLayer)-10, editorPane.getScreenSpace().y  + dataFont.getHeight() + 10);

		// DEBUG and TEST
		//MHBitmapImage img = MHResourceManager.getInstance().getImage(MHResourceManager.MHF_LOGO);
		//img = img.rotate(MHGame.getGameTimerValue() * 0.1);
		//g.drawImage(img, 100, 100);
		
		// Status bar
		MHVector mousePos = MHInputEventHandler.getInstance().getMousePosition();
		MHVector local = editorPane.screenToLocal(mousePos);
		MHVector world = editorPane.screenToWorld(mousePos);
		setStatusBarText("Grid:  " + editorPane.getCursorAddress() + "  |  Screen:  " + mousePos + "  |  Local:  " + local + "  |  World:  " + world);

		// DEBUG output
		//g.setColor(MHColor.WHITE);
		//g.drawString("Cursor Point: "+editorPane.getCursorPoint(), 400, 50);

		// Draw divider between toolbox and palette.
		g.setColor(MHColor.DARK_GRAY);
		int x = lblTools.getX() + lblTools.getWidth() + 8;
		int y = lblTools.getY();
		g.drawLine(x, y, x, y+rectPalette.height);
		
		currentPalette.render(g);
		
        viewControlPanel.render(g);
        editControlPanel.render(g);

		super.render(g);
	}
	
	
	private void drawText(MHGraphicsCanvas g, String text, int x, int y)
	{
	    g.setFont(dataFont);
	    g.setColor(MHColor.WHITE);
	    g.drawString(text, x-1, y-1);
        g.drawString(text, x+1, y-1);
        g.drawString(text, x-1, y+1);
        g.drawString(text, x+1, y+1);
        g.setColor(MHColor.BLACK);
        g.drawString(text, x, y);
	}


	private void selectEditLayer(MHLayersEnum layer, MHButton button)
	{
	    currentLayer = layer;
        editorPane.setLayerVisible(currentLayer, true);

        MHColor bg = MHColor.DARK_GRAY;
        btnEditFloors.setBackgroundColor(bg);
        btnEditFloors.setForegroundColor(MHColor.LIGHT_GRAY);
        btnEditFloorDetails.setBackgroundColor(bg);
        btnEditFloorDetails.setForegroundColor(MHColor.LIGHT_GRAY);
        btnEditWalls.setBackgroundColor(bg);
        btnEditWalls.setForegroundColor(MHColor.LIGHT_GRAY);
        btnEditWallDetails.setBackgroundColor(bg);
        btnEditWallDetails.setForegroundColor(MHColor.LIGHT_GRAY);
        btnEditActors.setBackgroundColor(bg);
        btnEditActors.setForegroundColor(MHColor.LIGHT_GRAY);
        
        button.setForegroundColor(bgColor);
        button.setBackgroundColor(MHColor.YELLOW);
	}
	
	
	private void toggleLayerVisibility(MHLayersEnum layer, MHButton button)
	{
        boolean b = !editorPane.isLayerVisible(layer);
        editorPane.setLayerVisible(layer, b);
        if (b)
        {
            button.setForegroundColor(MHColor.BLACK);
            button.setBackgroundColor(MHColor.GREEN);
        }
        else
        {
            button.setForegroundColor(MHColor.WHITE);
            button.setBackgroundColor(MHColor.RED);
        }
	}
	
		
	@Override
	public void onButtonPressed(MHButton button, MHMouseTouchEvent e) 
	{
		if (button == this.btnFileExit)
			MHGame.setProgramOver(true);
		else if (button == btnEditPanelTab)
		    editControlPanel.setVisible(!editControlPanel.isVisible());
        else if (button == btnViewPanelTab)
            viewControlPanel.setVisible(!viewControlPanel.isVisible());
        else if (button == this.btnEditFloors)
        {
            selectEditLayer(MHLayersEnum.FLOOR, button);
        }
        else if (button == this.btnEditFloorDetails)
        {
            selectEditLayer(MHLayersEnum.FLOOR_DECALS, button);
        }
        else if (button == this.btnEditWalls)
        {
            selectEditLayer(MHLayersEnum.WALLS, button);
        }
        else if (button == this.btnEditWallDetails)
        {
            selectEditLayer(MHLayersEnum.WALL_DECALS, button);
        }
        else if (button == this.btnEditActors)
        {
            selectEditLayer(MHLayersEnum.ACTORS, button);
        }
        else if (button == this.btnViewActors)
        {
            toggleLayerVisibility(MHLayersEnum.ACTORS, button);
        }
        else if (button == this.btnViewFloors)
        {
            toggleLayerVisibility(MHLayersEnum.FLOOR, button);
        }
        else if (button == this.btnViewFloorDetails)
        {
            toggleLayerVisibility(MHLayersEnum.FLOOR_DECALS, button);
        }
        else if (button == this.btnViewWalls)
        {
            toggleLayerVisibility(MHLayersEnum.WALLS, button);
        }
        else if (button == this.btnViewWallDetails)
        {
            toggleLayerVisibility(MHLayersEnum.WALL_DECALS, button);
        }
        else if (button == this.btnEraser)
        {
            eraserToolActive = !eraserToolActive;
            if (eraserToolActive)
                btnEraser.setBackgroundColor(MHColor.GREEN);
            else
                btnEraser.setBackgroundColor(MHColor.LIGHT_GRAY);
        }
        else if (button == this.btnPicker)
        {
            pickToolActive = !pickToolActive;
            if (pickToolActive)
                btnPicker.setBackgroundColor(MHColor.GREEN);
            else
                btnPicker.setBackgroundColor(MHColor.LIGHT_GRAY);
        }
        else if (button == btnTilesPalette)
        {
            currentPalette = tilePalette;
        }
        else if (button == btnActorsPalette)
        {
            currentPalette = actorPalette;
        }
		else if (button == this.btnFileOpen)
		{
		    dlgOpen = MHOpenFileDialog.create(this, MHGlobalConstants.DIR_DATA, MHMapFileInfo.WORLD_FILE_EXTENSION);
		    MHScreenManager.getInstance().changeScreen(dlgOpen);
		}
		else if (button == this.btnFileSave)
		{
		    save(editorPane.getMapData().getMapName());
		    
            MHDialogBox d = new MHDialogBox(this, "Map data saved.", "Save");
            MHScreenManager.getInstance().changeScreen(d);
		}
		else if (button == this.btnFileSaveAs)
		{
	        dlgSaveAs = new MHInputDialogBox(this, "Enter a new name for this map file.", "Save As...");
			MHScreenManager.getInstance().changeScreen(dlgSaveAs);
		}
		else if (button == this.btnFileMapInfo)
		{
		    dlgMapInfo = new LIMEMapInfoScreen(this, editorPane.getMapData());
			MHScreenManager.getInstance().changeScreen(dlgMapInfo);
		}
		else if (button == this.btnFileNew)
		{
		    dlgNewMap = new LIMENewMapScreen(this);
			MHScreenManager.getInstance().changeScreen(dlgNewMap);
		}
		else if (button == this.btnFileExportImage)
		{
			editorPane.renderCompleteImage().savePNG("export.png");
		}
		else if (button == btnViewGrid || button == btnGridToggle)
		{
			editorPane.setGridOn(!editorPane.isGridOn());
			if (editorPane.isGridOn())
			    btnGridToggle.setBackgroundColor(MHColor.GREEN);
			else
                btnGridToggle.setBackgroundColor(MHColor.LIGHT_GRAY);
		}
		else if (button == this.btnIDToggle)
		{
            editorPane.setIdOn(!editorPane.isIdOn());
            if (editorPane.isIdOn())
                btnIDToggle.setBackgroundColor(MHColor.GREEN);
            else
                btnIDToggle.setBackgroundColor(MHColor.LIGHT_GRAY);
		}
		else if (button == this.btnViewMap)
		{
			MHBitmapImage mapImg = editorPane.renderCompleteImage();
			MHImageScreen mapViewScreen = MHImageScreen.getInstance(this, mapImg);
			MHScreenManager.getInstance().changeScreen(mapViewScreen);
		}
        else if (button == this.btnViewRectangular)
        {
            switchMapView(rectangularMap);
        }
        else if (button == this.btnViewIsoDiamond)
        {
            switchMapView(isoDiamondMap);
        }
        else if (button == this.btnViewIsoStaggered)
        {
            switchMapView(isoStaggeredMap);
        }
		else if (button == this.btnDataFill)
			commandFillLayer(currentLayer, tilePalette.getSelectedTile());
		else if (button == this.btnDataClear)
			commandFillLayer(currentLayer, tilePalette.getTileSet().getTile(MHTileSet.NULL_TILE_ID));
		else if (button == btnPaletteUp)
		    currentPalette.scrollUp();
        else if (button == btnPaletteDown)
            currentPalette.scrollDown();
	}
	
	
	private void commandFillLayer(MHLayersEnum layer, MHTile tile)
	{
		MHTileGrid grid = editorPane.getMapData().getLayer(layer);
		int h = grid.getNumRows();
		int w = grid.getNumColumns();
		for (int row = 0; row < h; row++)
			for (int column = 0; column < w; column++)
				grid.putTile(tile, row, column);
	}


	@Override
	public void onMouseDown(MHMouseTouchEvent e) 
	{
        if (btnScrollUp.contains(e.getX(), e.getY()))
            vScroll = -SCROLL_SPEED;
        else if (btnScrollDown.contains(e.getX(), e.getY()))
            vScroll = SCROLL_SPEED;
        else if (btnScrollLeft.contains(e.getX(), e.getY()))
            hScroll = -SCROLL_SPEED;
        else if (btnScrollRight.contains(e.getX(), e.getY()))
            hScroll = SCROLL_SPEED;
        
        
		super.onMouseDown(e);
		
		if (e.isHandled())
			return;

		if (editorPane.isScreenCoordinate(e.getPoint()))
		{
		    if (pickToolActive)
		    {
		        MHMapCellAddress tilePos = editorPane.mapMouse(e.getPoint());
		        MHITileMapContent tile = editorPane.getMapData().getLayer(currentLayer.getID()).getTile(tilePos.row, tilePos.column);
		        currentPalette.setSelectedTile(tile);
		        pickToolActive = false;
		        btnPicker.setBackgroundColor(MHColor.LIGHT_GRAY);
		    }
		    else
		        isPainting = true;
		}
	}


	@Override
	public void unload() 
	{
	}


	@Override
	public void onMouseUp(MHMouseTouchEvent e) 
	{
        vScroll = hScroll = 0;

        super.onMouseUp(e);
		isPainting = false;
		
		if (e.isHandled())
			return;
		
		
		if (rectPalette.contains(e.getPoint()))
		{
			currentPalette.onMouseDown(e);
		
			MHTile tile = currentPalette.getSelectedTile(); 
			if (tile != null)
			{
			    int cx = tile.image.getWidth()/2;
			    int cy = tile.image.getHeight()/2;

			    getMouseCursor().changeCursor(tile.image, cx, cy);
			}
		}
//		else if (this.editorPane.isScreenCoordinate(e.getPoint()) && currentPalette == actorPalette)
//		{
//		    // place or erase actor.
//		    if (eraserToolActive)
//		    {
//		        MHActor a = actorList.getActorAt(editorPane.screenToWorld(e.getPoint()));
//		        if (a != null)
//		            actorList.remove(a);
//		    }
//		    else
//		    {
//	            MHTile tile = currentPalette.getSelectedTile(); 
//	            if (tile != null)
//	            {
//	                int ax = e.getX() - tile.image.getWidth()/2;
//	                int ay = e.getY() - tile.image.getHeight();
//                    // TODO: Use the actor grid too.
//	                MHActor a = new MHTileMapActor();
//	                a.setImage(tile.image);
//	                a.setPosition(editorPane.screenToWorld(ax, ay));
//		            actorList.add(a);
//	            }
//		    }
//		}
	}


	@Override
	public void onMouseMoved(MHMouseTouchEvent e) 
	{
		if (e.getEventSite() != this)
			return;

		if (eraserToolActive)
		{
            getMouseCursor().changeCursor(iconEraser, 8, 28);
		}
		else if (pickToolActive)
		{
            getMouseCursor().changeCursor(iconPicker, 8, 28);
		}
		else if (editorPane.isScreenCoordinate(e.getPoint()))
		{
			MHTile tile = currentPalette.getSelectedTile(); 
			if (tile != null)
			{
                int cx = tile.image.getWidth()/2;
                int cy;

                if (editorPane == rectangularMap)
                    cy = tile.image.getHeight()/2;
			    else
                    cy = tile.image.getHeight() - editorPane.getTileHeight()/2;
                
                getMouseCursor().changeCursor(tile.image, cx, cy);
			}
		}
		else
		{
			getMouseCursor().changeCursor(Type.CROSSHAIR);
		}
		
		editorPane.onMouseMoved(e);
		currentPalette.onMouseMoved(e);
		super.onMouseMoved(e);
	}
	
	
	@Override
	public void onKeyDown(MHKeyEvent e) 
	{
		int key = e.getKeyCode();
		
		// Scroll horizontally
		if (key == MHPlatform.getKeyCodes().keyA())
			hScroll = -SCROLL_SPEED;
		else if (key == MHPlatform.getKeyCodes().keyD())
			hScroll = SCROLL_SPEED;

		// Scroll vertically
		if (key == MHPlatform.getKeyCodes().keyW())
			vScroll = -SCROLL_SPEED;
		else if (key == MHPlatform.getKeyCodes().keyS())
			vScroll = SCROLL_SPEED;
	}


	@Override
	public void onKeyUp(MHKeyEvent e) 
	{
		int key = e.getKeyCode();

		// Scroll horizontally
		if (key == MHPlatform.getKeyCodes().keyA() ||
			key == MHPlatform.getKeyCodes().keyD())
			hScroll = 0;

		// Scroll vertically
		if (key == MHPlatform.getKeyCodes().keyW() || 
			key == MHPlatform.getKeyCodes().keyS())
			vScroll = 0;
		
        if (key == MHPlatform.getKeyCodes().keyUpArrow())
            currentPalette.scrollUp();
            
        if (key == MHPlatform.getKeyCodes().keyDownArrow())
            currentPalette.scrollDown();
        

	}
}
