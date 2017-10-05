package com.mhframework.platform.pc;

import java.awt.Canvas;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import com.mhframework.MHVideoSettings;
import com.mhframework.core.math.MHVector;
import com.mhframework.platform.MHGameApplication;
import com.mhframework.platform.graphics.MHGraphicsCanvas;
import com.mhframework.platform.pc.graphics.MHPCGraphics;


public class MHPCApplication implements MHGameApplication
{
    private MHVector displayOrigin, displaySize;
    private JFrame window;
	@SuppressWarnings("unused")
	private MHPCInputEventHandler eventHandler;
    private GraphicsEnvironment env;
    private GraphicsDevice device;
    private GraphicsConfiguration gc;
    private Canvas canvas;
    private DisplayMode displayMode;
    
    private static DisplayMode[] BEST_DISPLAY_MODES = new DisplayMode[] {
        // new DisplayMode(1280, 800, 32, 0),
        // Widescreen mode
        new DisplayMode(1280, 800, 32, 0),
        new DisplayMode(1280, 720, 32, 0),
        new DisplayMode(1024, 768, 32, 0),
        new DisplayMode(800, 600, 32, 0),
        new DisplayMode(800, 600, 24, 0),
        new DisplayMode(800, 600, 16, 0),
        new DisplayMode(640, 480, 32, 0),
        new DisplayMode(640, 480, 24, 0),
        new DisplayMode(640, 480, 16, 0) };


    private MHPCApplication(JFrame window, MHVideoSettings displaySettings)
    {
        this.window = window;
        
        displaySize = new MHVector(displaySettings.displayWidth, displaySettings.displayHeight);
        
        configureWindow(window, displaySettings);
        
        // Register event handlers.
        eventHandler = new MHPCInputEventHandler(window);
    }
    
    
    
    public static MHGameApplication create(JFrame window, MHVideoSettings displaySettings)
    {
        return new MHPCApplication(window, displaySettings);
    }



    public void shutdown()
    {
        window.dispose();
        //System.exit(0);
    }



    public void present(MHGraphicsCanvas backBuffer)
    {
        //Graphics g = getGraphics();
        Graphics g = window.getGraphics();
        //Graphics g = window.getBufferStrategy().getDrawGraphics();
        //Graphics g = window.getContentPane().getGraphics();
        Image img = ((MHPCGraphics)backBuffer).getImage();
        g.drawImage(img, (int)displayOrigin.getX(), (int)displayOrigin.getY(), null);
    }

    private void configureWindow(JFrame window, final MHVideoSettings settings)
    {
        displayOrigin = new MHVector(0, 0);
        //MHDisplayModeChooser.configureGameWindow(window, settings);
        if (settings.fullScreen)
        {
            
            env = GraphicsEnvironment.getLocalGraphicsEnvironment();
            device = env.getDefaultScreenDevice();
            gc = device.getDefaultConfiguration();

//            if (!settings.fullScreen) // If user requested windowed mode...
//                configureWindowedMode(settings);

            // First, find out if full screen is supported and the user requested it.
            if (device.isFullScreenSupported() && settings.fullScreen)
            {
                System.out.println("MHDisplayModeChooser:  Configuring game screen for full-screen mode.");

                window = new JFrame(gc);
                canvas = new Canvas(gc);
                //canvas.setSize(settings.displayWidth, settings.displayHeight);
                window.getContentPane().add(canvas);

                // Disable the visible GUI parts of the window
                window.setUndecorated(true);
                window.pack();



                // Ignore paint requests from the operating system
                window.setIgnoreRepaint(true);
                
                // Associate the application frame with the screen device
                device.setFullScreenWindow(window);

                // If we are able to change the screen display . . .
                if (device.isDisplayChangeSupported())
                {
                    // . . . then get the best display mode for the
                    // GraphicsDevice . . .
//                    if (settings == null)
//                    {
//                        displayMode = getBestDisplayMode(device);
//                        settings = new MHVideoSettings();
//                    }
//                    else
                        displayMode = getRequestedDisplayMode(device, settings.displayWidth, settings.displayHeight, settings.bitDepth);

                    if (displayMode != null)
                    {
                        // . . . and set the display mode.
                        device.setDisplayMode(displayMode);

                        System.out.println("MHDisplayModeChooser:  Setting display mode ("
                                        + displayMode.getWidth() + "x"
                                        + displayMode.getHeight() + "x"
                                        + displayMode.getBitDepth() + ")");
                    } // if (displayMode != null)
                    else
                    {
                        System.err.println("ERROR:  No valid display modes specified.  Trying windowed mode.");
                    }
                }
                else
                {
                    System.err.println("ERROR:  Display change not supported.  Trying windowed mode.");
                } // if (device.isDisplayChangeSupported())
            } // if fullScreenSupported...

        }
        else
        {
            final Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            window.setLocation((d.width - settings.displayWidth) / 2, (d.height - settings.displayHeight) / 2);
            window.setTitle(settings.windowCaption);
            window.setVisible(true);
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            final Insets insets = window.getInsets();
            displayOrigin = new MHVector(insets.left, insets.top);

            resizeToInternalSize(window, settings.displayWidth, settings.displayHeight);
        }


        // Create a BufferStrategy with two buffers
        window.createBufferStrategy(2);
        waitForReadyStrategy(window);
        
        // Hide mouse cursor.
        BufferedImage cursorImg = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);

        // Create a new blank cursor.
        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
            cursorImg, new Point(0, 0), "blank cursor");

        // Set the blank cursor to the JFrame.
        window.getContentPane().setCursor(blankCursor);
    }

    private void resizeToInternalSize(final JFrame window, final int internalWidth, final int internalHeight)
    {
        final Insets insets = window.getInsets();
        final int newWidth = internalWidth + insets.left + insets.right;
        final int newHeight = internalHeight + insets.top + insets.bottom;

        final Runnable resize = new Runnable()
        {
            public void run()
            {
                window.setSize(newWidth, newHeight);
            }
        };

        if(!SwingUtilities.isEventDispatchThread())
        {
            try
            {
                SwingUtilities.invokeAndWait(resize);
            }
            catch(final Exception e) {}
        }
        else
            resize.run();

        window.setResizable(false);
        window.validate();
    }

    
    public MHVector getDisplayOrigin()
    {
        return displayOrigin;
    }

    
     public MHVector getDisplaySize()
    {
        return displaySize;
    }

     /*****************************************************************
     *
     * @param device
     * @param height
     * @param width
     * @param bitDepth
     * @return
     */
    private static DisplayMode getRequestedDisplayMode(final GraphicsDevice device, final int height, final int width, final int bitDepth)
    {
        final DisplayMode[] modes = device.getDisplayModes();

        final DisplayMode requestedMode = new DisplayMode(height, width, bitDepth, DisplayMode.REFRESH_RATE_UNKNOWN);

        for (final DisplayMode deviceDisplayMode : modes)
        {
            if (deviceDisplayMode.getWidth() == requestedMode.getWidth()
                            && deviceDisplayMode.getHeight() == requestedMode.getHeight()
                            && deviceDisplayMode.getBitDepth() == requestedMode.getBitDepth())
            {
                return requestedMode;
            }
        }

        System.out.println("MHDisplayModeChooser:  Requested display mode not available.  Choosing best mode available.");
        return getBestDisplayMode(device);
    }


    /*****************************************************************
    *
    * @param device
    * @return
    */
   private static DisplayMode getBestDisplayMode(final GraphicsDevice device)
   {
       for (final DisplayMode listedDisplayMode : BEST_DISPLAY_MODES)
       {
           final DisplayMode[] modes = device.getDisplayModes();
           for (final DisplayMode deviceDisplayMode : modes)
           {
               if (deviceDisplayMode.getWidth() == listedDisplayMode.getWidth()
                               && deviceDisplayMode.getHeight() == listedDisplayMode.getHeight()
                               && deviceDisplayMode.getBitDepth() == listedDisplayMode.getBitDepth())
               {
                   return listedDisplayMode;
               }
           }
       }
       return null;
   }

   private static void waitForReadyStrategy(JFrame window)
   {
       int iterations = 0;

       while(true)
       {
           try
           {
               Thread.sleep(20);
           }
           catch(final InterruptedException e) {}

           try
           {
               window.getBufferStrategy().getDrawGraphics();
               break;
           }
           catch(final IllegalStateException e)
           {
               System.out.println("BufferStrategy not ready yet");
           }

           iterations++;
           if(iterations == 100)
           {
               //  (Unlikely event) No use after 2 seconds (100*20ms = 2secs) give up trying
               System.out.println("Exiting Program, unable to use BufferStrategy");
               System.exit(0);
           }
       }
   }


}
