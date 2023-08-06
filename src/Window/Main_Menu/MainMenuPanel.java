package Window.Main_Menu;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import java.awt.*;
import java.awt.event.*;

public class MainMenuPanel extends JPanel {
    private Album_Panel albumPanel;
    private PhotoView_Panel photoPanel;
    private HiddenMenu_Panel hiddenPanel;
    private JPanel splitPanel;
    private JSplitPane split;



    public MainMenuPanel () {
        createMainScreen();
    }

    private void createMainScreen() {
        this.setBackground(Color.black);
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(1280, 720));
        createHiddenMenu();
        createAlbumMenu();
        createPhotoView();
        createSplit();
        
    }

    // Create the ability to resize the JPanels
    private void createSplit() {
        split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, photoPanel, albumPanel);
        split.setResizeWeight(0.75);
        split.setOpaque(false);
        split.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, new Color(50, 50, 50), new Color(25, 25, 25)));
        split.setDividerSize(3);
        split.setUI(new BasicSplitPaneUI() {
            @Override
            public BasicSplitPaneDivider createDefaultDivider() {
                return new BasicSplitPaneDivider(this) {
                    
                    @Override
                    public void paint(Graphics g) {

                    }
                };
            }
        }); 
        this.add(split, BorderLayout.CENTER);
    }

    // Create the album panel
    private void createAlbumMenu() {
        albumPanel = new Album_Panel();
    }

    // Create the Photos view panel
    private void createPhotoView() {
        photoPanel = new PhotoView_Panel();
    }

    // Create hidden menu
    private void createHiddenMenu() {
        hiddenPanel = new HiddenMenu_Panel();
        this.add(hiddenPanel, BorderLayout.WEST);
    }
}
