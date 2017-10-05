/*****************************************************************************
IsoHex16_3.cpp
Ernest S. Pazera
11AUG2000
Start a WIN32 Application Workspace, add in this file
Requires the following libs:
ddraw.lib, dxguid.lib
Requires the following files:
DDFuncs.h.cpp, GDICanvas.h/cpp, IsoMouseMap.h/cpp,
IsoScroller.h/cpp, IsoTilePlotter.h/cpp, IsoTileWalker.h/cpp
TileSet.h/cpp. IsoHexCore.h, IsoHexDefs.h
*****************************************************************************/

//////////////////////////////////////////////////////////////////////////////
//INCLUDES
//////////////////////////////////////////////////////////////////////////////
#define WIN32_LEAN_AND_MEAN  

#include <windows.h>  
#include "DDFuncs.h"
#include "TileSet.h"
#include "IsoHexCore.h"
 

//////////////////////////////////////////////////////////////////////////////
//DEFINES
//////////////////////////////////////////////////////////////////////////////
//name for our window class
#define WINDOWCLASS "ISOHEX16"
//title of the application
#define WINDOWTITLE "IsoHex 16-3"

const int MAPWIDTH=200;
const int MAPHEIGHT=400;

//////////////////////////////////////////////////////////////////////////////
//PROTOTYPES
//////////////////////////////////////////////////////////////////////////////
bool Prog_Init();//game data initalizer
void Prog_Loop();//main game loop
void Prog_Done();//game clean up

//////////////////////////////////////////////////////////////////////////////
//GLOBALS
//////////////////////////////////////////////////////////////////////////////
HINSTANCE hInstMain=NULL;//main application handle
HWND hWndMain=NULL;//handle to our main window

//directdraw
LPDIRECTDRAW7 lpdd=NULL;
LPDIRECTDRAWSURFACE7 lpddsMain=NULL;
LPDIRECTDRAWSURFACE7 lpddsBack=NULL;
LPDIRECTDRAWCLIPPER lpddClip=NULL;

//tilesets
CTileSet tsBack;//background
CTileSet tsShadow;//tree shadow
CTileSet tsTree;//tree foreground
CTileSet tsCursor;//cursor

//isohexcore components
CTilePlotter TilePlotter;//plotter
CTileWalker TileWalker;//walker
CScroller Scroller;//scroller
CMouseMap MouseMap;//mousemap

POINT ptCursor;//keep track of the cursor
POINT ptScroll;//keep track of how quickly we scroll

int iMap[MAPWIDTH][MAPHEIGHT];//map array(0=no tree, 1=tree)

//////////////////////////////////////////////////////////////////////////////
//WINDOWPROC
//////////////////////////////////////////////////////////////////////////////
LRESULT CALLBACK TheWindowProc(HWND hwnd,UINT uMsg,WPARAM wParam,LPARAM lParam)
{
	//which message did we get?
	switch(uMsg)
	{
	case WM_KEYDOWN:
		{
			switch(wParam)
			{
			case VK_ESCAPE:
				{
					DestroyWindow(hWndMain);
					return(0);
				}break;
			}
		}break;
	case WM_LBUTTONDOWN:
		{
			//change the status of the map
			iMap[ptCursor.x][ptCursor.y]=1-iMap[ptCursor.x][ptCursor.y];
			return(0);
		}break;
	case WM_MOUSEMOVE:
		{
			//grab mouse x and y
			int x=LOWORD(lParam);
			int y=HIWORD(lParam);

			//reset scrolling speeds to zero
			ptScroll.x=0;
			ptScroll.y=0;

			//left scroll?
			if(x<8) ptScroll.x=x-8;

			//upward scroll?
			if(y<8) ptScroll.y=y-8;

			//right scroll?
			if(x>=632) ptScroll.x=x-632;

			//downward scroll?
			if(y>=472) ptScroll.y=y-472;
		}break;
	case WM_DESTROY://the window is being destroyed
		{

			//tell the application we are quitting
			PostQuitMessage(0);

			//handled message, so return 0
			return(0);

		}break;
	case WM_PAINT://the window needs repainting
		{
			//a variable needed for painting information
			PAINTSTRUCT ps;
			
			//start painting
			HDC hdc=BeginPaint(hwnd,&ps);

			/////////////////////////////
			//painting code would go here
			/////////////////////////////

			//end painting
			EndPaint(hwnd,&ps);
						
			//handled message, so return 0
			return(0);
		}break;
	}

	//pass along any other message to default message handler
	return(DefWindowProc(hwnd,uMsg,wParam,lParam));
}


//////////////////////////////////////////////////////////////////////////////
//WINMAIN
//////////////////////////////////////////////////////////////////////////////
int WINAPI WinMain(HINSTANCE hInstance,HINSTANCE hPrevInstance,LPSTR lpCmdLine,int nShowCmd)
{
	//assign instance to global variable
	hInstMain=hInstance;

	//create window class
	WNDCLASSEX wcx;

	//set the size of the structure
	wcx.cbSize=sizeof(WNDCLASSEX);

	//class style
	wcx.style=CS_OWNDC | CS_HREDRAW | CS_VREDRAW | CS_DBLCLKS;

	//window procedure
	wcx.lpfnWndProc=TheWindowProc;

	//class extra
	wcx.cbClsExtra=0;

	//window extra
	wcx.cbWndExtra=0;

	//application handle
	wcx.hInstance=hInstMain;

	//icon
	wcx.hIcon=LoadIcon(NULL,IDI_APPLICATION);

	//cursor
	wcx.hCursor=LoadCursor(NULL,IDC_ARROW);

	//background color
	wcx.hbrBackground=(HBRUSH)GetStockObject(BLACK_BRUSH);

	//menu
	wcx.lpszMenuName=NULL;

	//class name
	wcx.lpszClassName=WINDOWCLASS;

	//small icon
	wcx.hIconSm=NULL;

	//register the window class, return 0 if not successful
	if(!RegisterClassEx(&wcx)) return(0);

	//create main window
	hWndMain=CreateWindowEx(0,WINDOWCLASS,WINDOWTITLE, WS_POPUP | WS_VISIBLE,0,0,320,240,NULL,NULL,hInstMain,NULL);

	//error check
	if(!hWndMain) return(0);

	//if program initialization failed, then return with 0
	if(!Prog_Init()) return(0);

	//message structure
	MSG msg;

	//message pump
	for(;;)	
	{
		//look for a message
		if(PeekMessage(&msg,NULL,0,0,PM_REMOVE))
		{
			//there is a message

			//check that we arent quitting
			if(msg.message==WM_QUIT) break;
			
			//translate message
			TranslateMessage(&msg);

			//dispatch message
			DispatchMessage(&msg);
		}

		//run main game loop
		Prog_Loop();
	}
	
	//clean up program data
	Prog_Done();

	//return the wparam from the WM_QUIT message
	return(msg.wParam);
}

//////////////////////////////////////////////////////////////////////////////
//INITIALIZATION
//////////////////////////////////////////////////////////////////////////////
bool Prog_Init()
{
	//create IDirectDraw object
	lpdd=LPDD_Create(hWndMain,DDSCL_EXCLUSIVE | DDSCL_FULLSCREEN | DDSCL_ALLOWREBOOT);

	//set display mode
	lpdd->SetDisplayMode(640,480,16,0,0);

	//create primary surface
	lpddsMain=LPDDS_CreatePrimary(lpdd,1);

	//get back buffer
	lpddsBack=LPDDS_GetSecondary(lpddsMain);

	//create clipper
	lpdd->CreateClipper(0,&lpddClip,NULL);

	//associate window with the clipper
	lpddClip->SetHWnd(0,hWndMain);

	//attach clipper to back buffer
	lpddsBack->SetClipper(lpddClip);

	//load in the mousemap
	MouseMap.Load("MouseMap.bmp");

	//set up the tile plotter
	TilePlotter.SetMapType(ISOMAP_DIAMOND);//diamond mode
	TilePlotter.SetTileSize(MouseMap.GetWidth(),MouseMap.GetHeight());//grab width and height from mousemap

	//set up tile walker to diamond mode
	TileWalker.SetMapType(ISOMAP_DIAMOND);

	//set up screeen space
	RECT rcTemp;
	SetRect(&rcTemp,0,0,640,480);
	Scroller.SetScreenSpace(&rcTemp);

	//load in tiles and cursor
	tsBack.Load(lpdd,"backgroundts.bmp");
	tsShadow.Load(lpdd,"treeshadowts.bmp");
	tsTree.Load(lpdd,"treets.bmp");
	tsCursor.Load(lpdd,"cursor.bmp");

	//grab tile extent from tileset
	CopyRect(&rcTemp,&tsBack.GetTileList()[0].rcDstExt);

	//calculate the worldspace
	Scroller.CalcWorldSpace(&TilePlotter,&rcTemp,MAPWIDTH,MAPHEIGHT);

	//calculate the mousemap reference point
	MouseMap.CalcReferencePoint(&TilePlotter,&rcTemp);

	//calculate anchor space
	Scroller.AdjustWorldSpace(MouseMap.GetWidth()/2,MouseMap.GetHeight()/2,-MouseMap.GetWidth()/2,-MouseMap.GetHeight()/2);
	Scroller.CalcAnchorSpace();

	//set wrap modes for scroller
	Scroller.SetHWrapMode(WRAPMODE_CLIP);
	Scroller.SetVWrapMode(WRAPMODE_CLIP);

	//set scroller anchor to (0,0)
	Scroller.GetAnchor()->x=0;
	Scroller.GetAnchor()->y=0;

	//attach scrolelr and tilewalker to mousemap
	MouseMap.SetScroller(&Scroller);
	MouseMap.SetTileWalker(&TileWalker);

	//set up the map to a random tilefield
	for(int x=0;x<MAPWIDTH;x++)
	{
		for(int y=0;y<MAPHEIGHT;y++)
		{
			iMap[x][y]=rand()%2;
		}
	}

	return(true);//return success
}

//////////////////////////////////////////////////////////////////////////////
//CLEANUP
//////////////////////////////////////////////////////////////////////////////
void Prog_Done()
{
	//release main/back surfaces
	LPDDS_Release(&lpddsMain);

	//release clipper
	LPDDCLIP_Release(&lpddClip);

	//release directdraw
	LPDD_Release(&lpdd);
}

//////////////////////////////////////////////////////////////////////////////
//MAIN GAME LOOP
//////////////////////////////////////////////////////////////////////////////
void Prog_Loop()
{
	//clear out backbuffer
	DDBLTFX ddbltfx;
	DDBLTFX_ColorFill(&ddbltfx,0);
	lpddsBack->Blt(NULL,NULL,NULL,DDBLT_WAIT | DDBLT_COLORFILL,&ddbltfx);
	
	//move the anchor based on scrolling speed
	Scroller.MoveAnchor(ptScroll.x,ptScroll.y);

	//grab the mouse position
	POINT ptMouse;
	GetCursorPos(&ptMouse);
	//map the mouse
	ptCursor=MouseMap.MapMouse(ptMouse);

	//clip the cursor to valid map coordinates
	if(ptCursor.x<0) ptCursor.x=0;
	if(ptCursor.y<0) ptCursor.y=0;
	if(ptCursor.x>(MAPWIDTH-1)) ptCursor.x=MAPWIDTH-1;
	if(ptCursor.y>(MAPHEIGHT-1)) ptCursor.y=MAPHEIGHT-1;

	//the corner map locations of the display
	POINT ptCornerUpperLeft;
	POINT ptCornerUpperRight;
	POINT ptCornerLowerLeft;
	POINT ptCornerLowerRight;

	//working variables for calculating corners
	POINT ptScreen;
	POINT ptWorld;
	POINT ptCoarse;
	POINT ptMap;

	//calculate upper left corner
	//screen point
	ptScreen.x=Scroller.GetScreenSpace()->left;
	ptScreen.y=Scroller.GetScreenSpace()->top;
	//change into world coordinate
	ptWorld=Scroller.ScreenToWorld(ptScreen);
	//adjust by mousemap reference point	
	ptWorld.x-=MouseMap.GetReferencePoint()->x;
	ptWorld.y-=MouseMap.GetReferencePoint()->y;
	//calculate coarse coordinates
	ptCoarse.x=ptWorld.x/MouseMap.GetWidth();
	ptCoarse.y=ptWorld.y/MouseMap.GetHeight();
	//adjust for negative remainders
	if(ptWorld.x%MouseMap.GetWidth()<0) ptCoarse.x--;
	if(ptWorld.y%MouseMap.GetHeight()<0) ptCoarse.y--;
	//set map point to 0,0
	ptMap.x=0;
	ptMap.y=0;
	//do eastward tilewalk
	ptMap=TileWalker.TileWalk(ptMap,ISO_EAST);
	ptMap.x*=ptCoarse.x;
	ptMap.y*=ptCoarse.x;
	//assign ptmap to corner point
	ptCornerUpperLeft.x=ptMap.x;
	ptCornerUpperLeft.y=ptMap.y;
	//reset ptmap to 0,0
	ptMap.x=0;
	ptMap.y=0;
	//do southward tilewalk
	ptMap=TileWalker.TileWalk(ptMap,ISO_SOUTH);
	ptMap.x*=ptCoarse.y;
	ptMap.y*=ptCoarse.y;
	//add ptmap to corner point
	ptCornerUpperLeft.x+=ptMap.x;
	ptCornerUpperLeft.y+=ptMap.y;

	//calculate upper right corner
	//screen point
	ptScreen.x=Scroller.GetScreenSpace()->right;
	ptScreen.y=Scroller.GetScreenSpace()->top;
	//change into world coordinate
	ptWorld=Scroller.ScreenToWorld(ptScreen);
	//adjust by mousemap reference point	
	ptWorld.x-=MouseMap.GetReferencePoint()->x;
	ptWorld.y-=MouseMap.GetReferencePoint()->y;
	//calculate coarse coordinates
	ptCoarse.x=ptWorld.x/MouseMap.GetWidth();
	ptCoarse.y=ptWorld.y/MouseMap.GetHeight();
	//adjust for negative remainders
	if(ptWorld.x%MouseMap.GetWidth()<0) ptCoarse.x--;
	if(ptWorld.y%MouseMap.GetHeight()<0) ptCoarse.y--;
	//set map point to 0,0
	ptMap.x=0;
	ptMap.y=0;
	//do eastward tilewalk
	ptMap=TileWalker.TileWalk(ptMap,ISO_EAST);
	ptMap.x*=ptCoarse.x;
	ptMap.y*=ptCoarse.x;
	//assign ptmap to corner point
	ptCornerUpperRight.x=ptMap.x;
	ptCornerUpperRight.y=ptMap.y;
	//reset ptmap to 0,0
	ptMap.x=0;
	ptMap.y=0;
	//do southward tilewalk
	ptMap=TileWalker.TileWalk(ptMap,ISO_SOUTH);
	ptMap.x*=ptCoarse.y;
	ptMap.y*=ptCoarse.y;
	//add ptmap to corner point
	ptCornerUpperRight.x+=ptMap.x;
	ptCornerUpperRight.y+=ptMap.y;

	//calculate lower left corner
	//screen point
	ptScreen.x=Scroller.GetScreenSpace()->left;
	ptScreen.y=Scroller.GetScreenSpace()->bottom;
	//change into world coordinate
	ptWorld=Scroller.ScreenToWorld(ptScreen);
	//adjust by mousemap reference point	
	ptWorld.x-=MouseMap.GetReferencePoint()->x;
	ptWorld.y-=MouseMap.GetReferencePoint()->y;
	//calculate coarse coordinates
	ptCoarse.x=ptWorld.x/MouseMap.GetWidth();
	ptCoarse.y=ptWorld.y/MouseMap.GetHeight();
	//adjust for negative remainders
	if(ptWorld.x%MouseMap.GetWidth()<0) ptCoarse.x--;
	if(ptWorld.y%MouseMap.GetHeight()<0) ptCoarse.y--;
	//set map point to 0,0
	ptMap.x=0;
	ptMap.y=0;
	//do eastward tilewalk
	ptMap=TileWalker.TileWalk(ptMap,ISO_EAST);
	ptMap.x*=ptCoarse.x;
	ptMap.y*=ptCoarse.x;
	//assign ptmap to corner point
	ptCornerLowerLeft.x=ptMap.x;
	ptCornerLowerLeft.y=ptMap.y;
	//reset ptmap to 0,0
	ptMap.x=0;
	ptMap.y=0;
	//do southward tilewalk
	ptMap=TileWalker.TileWalk(ptMap,ISO_SOUTH);
	ptMap.x*=ptCoarse.y;
	ptMap.y*=ptCoarse.y;
	//add ptmap to corner point
	ptCornerLowerLeft.x+=ptMap.x;
	ptCornerLowerLeft.y+=ptMap.y;

	//calculate lower right corner
	//screen point
	ptScreen.x=Scroller.GetScreenSpace()->right;
	ptScreen.y=Scroller.GetScreenSpace()->bottom;
	//change into world coordinate
	ptWorld=Scroller.ScreenToWorld(ptScreen);
	//adjust by mousemap reference point	
	ptWorld.x-=MouseMap.GetReferencePoint()->x;
	ptWorld.y-=MouseMap.GetReferencePoint()->y;
	//calculate coarse coordinates
	ptCoarse.x=ptWorld.x/MouseMap.GetWidth();
	ptCoarse.y=ptWorld.y/MouseMap.GetHeight();
	//adjust for negative remainders
	if(ptWorld.x%MouseMap.GetWidth()<0) ptCoarse.x--;
	if(ptWorld.y%MouseMap.GetHeight()<0) ptCoarse.y--;
	//set map point to 0,0
	ptMap.x=0;
	ptMap.y=0;
	//do eastward tilewalk
	ptMap=TileWalker.TileWalk(ptMap,ISO_EAST);
	ptMap.x*=ptCoarse.x;
	ptMap.y*=ptCoarse.x;
	//assign ptmap to corner point
	ptCornerLowerRight.x=ptMap.x;
	ptCornerLowerRight.y=ptMap.y;
	//reset ptmap to 0,0
	ptMap.x=0;
	ptMap.y=0;
	//do southward tilewalk
	ptMap=TileWalker.TileWalk(ptMap,ISO_SOUTH);
	ptMap.x*=ptCoarse.y;
	ptMap.y*=ptCoarse.y;
	//add ptmap to corner point
	ptCornerLowerRight.x+=ptMap.x;
	ptCornerLowerRight.y+=ptMap.y;

	//tilewalk from corners
	ptCornerUpperLeft=TileWalker.TileWalk(ptCornerUpperLeft,ISO_NORTHWEST);
	ptCornerUpperRight=TileWalker.TileWalk(ptCornerUpperRight,ISO_NORTHEAST);
	ptCornerLowerLeft=TileWalker.TileWalk(ptCornerLowerLeft,ISO_SOUTHWEST);
	ptCornerLowerRight=TileWalker.TileWalk(ptCornerLowerRight,ISO_SOUTHEAST);

	//main rendering loop
	//vars for rendering loop
	POINT ptCurrent;
	POINT ptRowStart;
	POINT ptRowEnd;
	DWORD dwRowCount=0;

	//set up rows
	ptRowStart=ptCornerUpperLeft;
	ptRowEnd=ptCornerUpperRight;

	//start rendering loops
	for(;;)//"infinite" loop
	{
		//set current point to rowstart
		ptCurrent=ptRowStart;

		//render a row of tiles
		for(;;)//'infinite' loop
		{

			//check for valid point. if valid, render
			if(ptCurrent.x>=0 && ptCurrent.y>=0 && ptCurrent.x<MAPWIDTH && ptCurrent.y<MAPHEIGHT)
			{
				//valid, so render
				ptScreen=TilePlotter.PlotTile(ptCurrent);//plot tile
				ptScreen=Scroller.WorldToScreen(ptScreen);//world->screen
				tsBack.PutTile(lpddsBack,ptScreen.x,ptScreen.y,0);//put background tile
				if(iMap[ptCurrent.x][ptCurrent.y])//check for tree
				{
					tsShadow.PutTile(lpddsBack,ptScreen.x,ptScreen.y,0);//put shadow
					tsTree.PutTile(lpddsBack,ptScreen.x,ptScreen.y,0);//put tree
				}
			}
			
			//check if at end of row. if we are, break out of inner loop
			if(ptCurrent.x==ptRowEnd.x && ptCurrent.y==ptRowEnd.y) break;

			//walk east to next tile
			ptCurrent=TileWalker.TileWalk(ptCurrent,ISO_EAST);
		}

		//check to see if we are at the last row. if we are, break out of loop
		if(ptRowStart.x==ptCornerLowerLeft.x && ptRowStart.y==ptCornerLowerLeft.y) break;

		//move the row start and end points, based on the row number
		if(dwRowCount&1)
		{
			//odd
			//start moves SW, end moves SE
			ptRowStart=TileWalker.TileWalk(ptRowStart,ISO_SOUTHWEST);
			ptRowEnd=TileWalker.TileWalk(ptRowEnd,ISO_SOUTHEAST);
		}
		else
		{
			//even
			//start moves SE, end moves SW
			ptRowStart=TileWalker.TileWalk(ptRowStart,ISO_SOUTHEAST);
			ptRowEnd=TileWalker.TileWalk(ptRowEnd,ISO_SOUTHWEST);
		}

		//increase the row number
		dwRowCount++;
	}

	//flip to show the back buffer
	lpddsMain->Flip(0,DDFLIP_WAIT);
}

