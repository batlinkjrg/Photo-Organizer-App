package Window.Main_Menu;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class HiddenMenu_Panel extends JPanel {
    private JButton hiddenMenu, hiddenImport;
    private JPanel buttonPanel;
    private Image importCache, menuCache;
    private ImageIcon importIcon, menuIcon;

    HiddenMenu_Panel () {
        this.setPreferredSize(new Dimension(50, 100));
        this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, new Color(50, 50, 50), new Color(25, 25, 25)));
        this.setBackground(Color.DARK_GRAY);
        this.setLayout(new GridLayout(7, 1));
        createButtonPanel();
        createMenuButton();
        createImportButton();

    }

    // Create the menu button
    private void createMenuButton() {
        // Set attributes
        hiddenMenu = new JButton();
        hiddenMenu.setBackground(new Color(70, 70, 70));
        hiddenMenu.setBorder(BorderFactory.createLineBorder(Color.black));
        hiddenMenu.setFocusPainted(false);
        hiddenMenu.setPreferredSize(new Dimension(50, 50));

        // Create Icon
        try {
            menuCache = ImageIO.read(new File("resources\\icons\\menu-icon.png")); 
            menuCache = menuCache.getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            menuIcon = new ImageIcon(menuCache);
            hiddenMenu.setIcon(menuIcon);
         } catch (IOException exc) {
             exc.printStackTrace();
         }

        buttonPanel.add(hiddenMenu, BorderLayout.NORTH);
    }

    // Create import button
    private void createImportButton() {
        // Set attributes
        hiddenImport = new JButton();
        hiddenImport.setBackground(new Color(70, 70, 70));
        hiddenImport.setBorder(BorderFactory.createLineBorder(Color.black));
        hiddenImport.setFocusPainted(false);
        hiddenImport.setPreferredSize(new Dimension(50, 50));
        
        // Create Icon
        try {
            importCache = ImageIO.read(new File("resources\\icons\\import-icon.png")); 
            importCache = importCache.getScaledInstance(25, 25, Image.SCALE_DEFAULT);
            importIcon = new ImageIcon(importCache);
            hiddenImport.setIcon(importIcon);
         } catch (IOException exc) {
             exc.printStackTrace();
         }

        buttonPanel.add(hiddenImport, BorderLayout.CENTER);
    }

    // Create panel for the buttons
    private void createButtonPanel() {
        buttonPanel = new JPanel();
        buttonPanel.setPreferredSize(new Dimension(50, 100));
        buttonPanel.setMaximumSize(new Dimension(50, 105));
        buttonPanel.setBackground(Color.darkGray);
        buttonPanel.setLayout(new BorderLayout());
        this.add(buttonPanel, BorderLayout.NORTH);
    }
}
