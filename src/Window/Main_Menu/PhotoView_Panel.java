package Window.Main_Menu;

import javax.swing.*;
import javax.swing.border.EtchedBorder;

import Window.Gallery.Gallery_Main;

import java.awt.*;
import java.awt.event.*;

public class PhotoView_Panel extends JPanel {
    private JPanel photoGalleryPanel, naviPanel;
    private Gallery_Main photoList;

    PhotoView_Panel () {
        this.setPreferredSize(new Dimension(1000, 400));
        this.setMinimumSize(new Dimension(900, 400));
        this.setBackground(new Color(45, 45, 45));
        this.setLayout(new BorderLayout());
        createPhotoGallery();
        createToolsPanel();
    }

    // Create the navigation buttons
    private void createToolsPanel() {
        naviPanel = new JPanel();
        naviPanel.setBackground(Color.blue);
        naviPanel.setPreferredSize(new Dimension(150, 150));

        this.add(naviPanel, BorderLayout.NORTH);
    }

    // Photo LayoutPanel
    private void createPhotoGallery() {
        photoGalleryPanel = new JPanel();
        photoGalleryPanel.setBackground(Color.red);//new Color(55, 55, 55));
        photoGalleryPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED, new Color(80, 80, 80), getBackground()));
        photoGalleryPanel.setLayout(new BorderLayout());
        createPhotoList();
        this.add(photoGalleryPanel, BorderLayout.CENTER);
    }

    // Create the photo gallery
    private void createPhotoList() {
        photoList = new Gallery_Main();
        photoGalleryPanel.add(photoList, BorderLayout.CENTER);
    }
}
