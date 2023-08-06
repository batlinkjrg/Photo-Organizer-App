package Window.Gallery;

import javax.swing.*;
import javax.swing.border.EtchedBorder;

import java.awt.*;
import java.awt.event.*;

public class Gallery_Main extends JPanel {
    private PhotoView_Panel photoView;
    public Gallery_Main() {
        this.setBackground(Color.green);
        this.setLayout(new BorderLayout());
        creatPhotoView();
    }

    private void creatPhotoView() {
        this.photoView = new PhotoView_Panel();
        this.add(photoView, BorderLayout.CENTER);
    }
}
