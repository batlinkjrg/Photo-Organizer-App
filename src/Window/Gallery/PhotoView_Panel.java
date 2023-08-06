package Window.Gallery;

import javax.swing.*;
import javax.swing.border.EtchedBorder;

import java.awt.*;
import java.awt.event.*;

public class PhotoView_Panel extends JScrollPane {
    private JList photoList;

    PhotoView_Panel () {
        this.setBackground(Color.PINK);
    }

    private void createPhotoList() {
        photoList = new JList<>(new DefaultListModel<>());
    
    }

    private void sortPhotoList() {

    }

}
