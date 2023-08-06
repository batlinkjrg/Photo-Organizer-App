package Window.Main_Menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Album_Panel extends JPanel {
    private JLabel albumLabel;
    private JPanel albumPane;

    Album_Panel () {
        createPanel();
    }

    private void createPanel() {
        this.setPreferredSize(new Dimension(200, 200));
        this.setBackground(Color.darkGray);
        this.setLayout(new BorderLayout(0, 10));
        this.setVisible(true);
        this.setBorder(BorderFactory.createBevelBorder(1, new Color(150, 150, 150), new Color(100, 100, 100)));

        createAlbumLabel();
        this.setMinimumSize(new Dimension(100, 200));
        this.add(albumLabel, BorderLayout.NORTH);
    }

    private void createAlbumPane() {
        albumPane = new JPanel();
        albumPane.setPreferredSize(new Dimension(200, 200));
        albumPane.setBackground(Color.darkGray);
        albumPane.setLayout(new BorderLayout());
        albumPane.setVisible(true);
        albumPane.setBorder(BorderFactory.createBevelBorder(1, new Color(150, 150, 150), new Color(100, 100, 100)));


        createAlbumLabel();
    }

    private void createAlbumLabel() {
        this.albumLabel = new JLabel("Albums", SwingConstants.CENTER);
        albumLabel.setPreferredSize(new Dimension(30, 30));
        albumLabel.setFont(new Font("New Times Roman", Font.ROMAN_BASELINE, 20));
        albumLabel.setOpaque(true);
        albumLabel.setBackground(new Color(70, 70, 80));
        albumLabel.setForeground(Color.WHITE);
        albumLabel.setVisible(true);
    }

    public void paintComponent(Graphics g) {
        g.setColor(Color.darkGray);
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}
