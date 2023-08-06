package Window;

import Window.Main_Menu.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Window extends JFrame {
    private Image windowIconCache;
    private ImageIcon windowIcon;
    
    public Window() {
        initializeFrame();
        this.pack();
    }

    // Create Window
    private void initializeFrame() {
        UIManager.put("activeCaption", new javax.swing.plaf.ColorUIResource(Color.gray));
        JFrame.setDefaultLookAndFeelDecorated(true); 

        this.setSize(1280, 720);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(100, 100);
        this.setVisible(true);
        this.setLayout(new BorderLayout());
        this.setTitle("Jonathan's Photo Organizer");
        setIcon();

        MainMenuPanel mainMenu = new MainMenuPanel();
        this.getContentPane().add(mainMenu, BorderLayout.CENTER);
    }

    // Set Window Icon
    private void setIcon() {
        // Create Icon
        try {
            windowIconCache = ImageIO.read(new File("resources\\icons\\window-icon.png")); 
            windowIcon = new ImageIcon(windowIconCache);
            this.setIconImage((windowIcon).getImage());
         } catch (IOException exc) {
             exc.printStackTrace();
         }
    }
}
