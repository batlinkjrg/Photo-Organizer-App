/*
 * DirectoryPicker.java
 * This class is used to add a directory bar
 * Included file browser as well
 */


package BasicWindow;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import Main.app_Main;

public class DirectoryPicker extends JPanel {

        private String path;
        private JTextField pathLabel;
        private JButton browse;
        private String name;

        public DirectoryPicker(String name) {
                this.name = name;
                initialize();
                createComponents();
        }

        private void initialize() {
                Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
                this.setVisible(true);
                this.setBorder(padding);
                this.setPreferredSize(new Dimension(600, 50));
                this.setBackground(Color.darkGray);
                this.setLayout(new BorderLayout());
        }

        private void createComponents() {
                createLabel();
                createButton();
        }
        
        private void createLabel() {
                Border padding = BorderFactory.createEmptyBorder(2, 2, 2, 2);
                this.pathLabel = new JTextField("Click Browse to pick " + this.name + " path ->");
                this.pathLabel.setEditable(false);
                this.pathLabel.setVisible(true);
                this.pathLabel.setBackground(new Color(75, 75, 75));
                this.pathLabel.setForeground(Color.WHITE);
                this.pathLabel.setPreferredSize(new Dimension(450, 20));
                this.pathLabel.setBorder(padding);

                JPanel pathPanel = new JPanel();
                pathPanel.setBackground(new Color(75, 75, 75));
                pathPanel.setPreferredSize(new Dimension(500, 30));
                pathPanel.add(pathLabel);
                this.add(pathPanel, BorderLayout.WEST);
        }

        private void createButton() {
                this.browse = new JButton("Browse");
                this.browse.setBackground(new Color(80, 80, 80));
                this.browse.setForeground(Color.WHITE);
                this.browse.addActionListener(new BrowseButton());

                this.add(this.browse, BorderLayout.EAST);
        }

        public String getPath() {
                return path;
        }

        private void setLabelPath(String path) {
                String labelPath = path;

                if(labelPath.length() >= 70) {
                        int newStart = labelPath.length() - 70;
                        labelPath = "..." + labelPath.substring(newStart);
                }

                this.path = path;
                this.pathLabel.setText(labelPath);
                System.out.println("Current Selection -> " + path);
        }

        // Call this class when a file browser is open
        class BrowseButton implements ActionListener {

                public void actionPerformed(ActionEvent e) {
                        JFileChooser c = new JFileChooser();
                        c.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                        int rVal = c.showOpenDialog(app_Main.window());

                        if (rVal == JFileChooser.APPROVE_OPTION) {
                                setLabelPath(c.getSelectedFile().getAbsolutePath().replace('\\', '/'));
                        }               
                }
        }
}
