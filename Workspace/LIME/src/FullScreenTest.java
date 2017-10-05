import java.awt.BorderLayout;
import java.awt.GraphicsEnvironment;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

 public final class FullScreenTest {
 
    public static final void main(final String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
 
        final JFrame fullscreenFrame = new JFrame();
        fullscreenFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        fullscreenFrame.setUndecorated(true);
        fullscreenFrame.setResizable(false);
        fullscreenFrame.add(new JLabel("Press ALT+F4 to exit fullscreen.", SwingConstants.CENTER), BorderLayout.CENTER);
        fullscreenFrame.validate();
 
        GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(fullscreenFrame);
    }
 
 }