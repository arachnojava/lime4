package com.test;

import android.os.Bundle;

import com.mhframework.MHScreen;
import com.mhframework.MHVideoSettings;
import com.mhframework.platform.android.MHAndroidActivity;
import com.mhframework.platform.android.MHAndroidPlatform;


public class PlatformTestAndroid extends MHAndroidActivity 
{

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);


		// The only requirements for a programmer to use MHFramework
		// are these:

		// 1.  One screen (minimum).  If you do not provide one, the
		//     Screen manager will create a placeholder. Of course,
		//     your app will have no UI in this case.
		MHScreen startingScreen = new TestScreen();
	    TestScreen.IMAGES_DIR = "images/"; // On PC, this is "assets/images/".

		// 2.  Video settings to determine screen resolution, etc.
		MHVideoSettings displaySettings = new MHVideoSettings();
		displaySettings.displayWidth = 800;
		displaySettings.displayHeight = 480;

		// 3.  Pass the above two things into the engine along with
		//     the window the app will run in.
		MHAndroidPlatform.run(this, startingScreen, displaySettings);

	}

}
