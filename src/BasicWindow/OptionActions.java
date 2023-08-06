package BasicWindow;

import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JPanel;

public class OptionActions extends JPanel {
        private Checkbox sortNoDate, moveFile, screenshotFilter;

        public OptionActions() {
            initializePanel(); 
        }

        private void initializePanel() {
                this.setPreferredSize(new Dimension(600, 60));
                this.setLayout(new GridLayout(1, 3));
                this.setBackground(Color.darkGray);
                createCheckBoxes();
                addCheckBoxes();
        }

        private void createCheckBoxes() {
                Font font = new Font("New Times Roman", Font.ITALIC, 13);

                this.sortNoDate = new Checkbox("Sort photos without valid dates");
                this.sortNoDate.setForeground(Color.white);
                this.sortNoDate.setFont(font);

                this.moveFile = new Checkbox("Move files to new location");
                this.moveFile.setForeground(Color.white);
                this.moveFile.setFont(font);

                this.screenshotFilter = new Checkbox("Filter screenshots from photos");
                this.screenshotFilter.setForeground(Color.white);
                this.screenshotFilter.setFont(font);
        }

        private void addCheckBoxes() {
                this.add(this.sortNoDate);
                this.add(this.moveFile);
                this.add(this.screenshotFilter);
        }

        public boolean getSortNoDate() {
                return this.sortNoDate.getState();
        }

        public boolean getMoveFile() {
                return this.moveFile.getState();
        }

        public boolean getScreenShotFilter() {
                return this.screenshotFilter.getState();
        }
}
