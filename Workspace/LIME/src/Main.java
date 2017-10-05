import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

import com.mhframework.MHVideoSettings;
import com.mhframework.platform.pc.MHPCPlatform;


public class Main 
{
    private static final String VERSION = "4.0.01A";
    
	public static void main(String[] args)
	{
		MHVideoSettings settings = new MHVideoSettings();
		//Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		//settings.fullScreen = true;
        settings.fullScreen = false;
		settings.displayWidth = 800;//(int)(screenSize.width * 0.75);
		settings.displayHeight = 600;//(int)(screenSize.height * 0.75);
		settings.showSplashScreen = true;
		settings.windowCaption = "LIME " + VERSION;;

		MHPCPlatform.run(new JFrame(), LIMEEditorScreen.getInstance(), settings);
	}
}