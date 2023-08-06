package BasicWindow;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Main.PhotoSorter_API;

public class OperationsPanel extends JPanel {

        private DirectoryPicker inDirPicker, outDirPicker;
        private OptionActions optAct;

        private boolean sortNoDate, moveFile, screenshotFilter;
        private String inPath, outPath, outName = "Sorted Media";

        private JButton startButton;
        private JTextField progress, outNameField;


        private PhotoSorter_API sorterThread;

        private int foundFiles, processedFiles, placedFiles;

        public OperationsPanel(DirectoryPicker indirPicker, DirectoryPicker outdirPicker, OptionActions optAct) {
                this.inDirPicker = indirPicker;
                this.outDirPicker = outdirPicker;
                this.optAct = optAct;

                initializePanel();
                createButtonPanel();
        }

        private void initializePanel() {
                this.setVisible(true);
                this.setLayout(new BorderLayout());
                this.setPreferredSize(new Dimension(600, 35));
                this.setBackground(new Color(70, 70, 70));
        }

        private boolean checkAPIOptions() {
                boolean start = false;
                
                start = isValidPath(this.inPath);
                start = isValidPath(this.outPath);
                
                start = isValidBool(this.sortNoDate);
                start = isValidBool(this.moveFile);     
                start = isValidBool(this.screenshotFilter);

                return start;
        }

        private static final boolean isValidBool(boolean bool) {
                if (bool == true || bool == false) {
                        return true;
                } else {
                        return false;
                }
        }

        private static final boolean isValidPath(String path) {
                try {
                        Paths.get(path);
                } catch (InvalidPathException | NullPointerException ex) {
                        return false;
                }
                return true;
        }

        private void initializePhotoProcessor() {
                gatherAPIOptions();
                boolean start = checkAPIOptions();

                if (start) {
                        this.sorterThread = new PhotoSorter_API(this.inPath, this.outPath, this.outName, this.sortNoDate, this.moveFile, this.screenshotFilter);
                        sorterThread.start();
                } else {
                        try {
                                throw new Exception("Sorter failed to start, invalid arguments");
                        } catch (Exception e) {
                                System.err.println("Sorter failed to start, invalid arguments");
                        }
                }
        }

        private void gatherAPIOptions() {
                this.inPath = this.inDirPicker.getPath();
                this.outPath = this.outDirPicker.getPath();

                this.sortNoDate = this.optAct.getSortNoDate();
                this.moveFile = this.optAct.getMoveFile();
                this.screenshotFilter = this.optAct.getScreenShotFilter();
        }

        private void createButtonPanel() {
                JPanel buttonPanel = new JPanel(new BorderLayout());

                createStartButton();
                this.progress = createProgress();
                this.outNameField = createOutNameFeild();

                buttonPanel.setBackground(Color.darkGray);
                buttonPanel.setBorder(BorderFactory.createLineBorder(Color.black, 2, true));
                buttonPanel.add(this.outNameField);
                buttonPanel.add(this.startButton, BorderLayout.EAST);
                buttonPanel.add(this.progress, BorderLayout.WEST);
                this.add(buttonPanel);
        }

        // TODO: Fix JTextFeild not updating
        private void startProgress() {
                boolean sorting = true;
                int stage = 1;
                do {
                        switch (stage) {
                                case 1:
                                        showFilesFound();
                                        if(this.processedFiles >= 1) {
                                                stage++;
                                        }
                                        break;
                                case 2:
                                        showFilesProcessed();
                                        if(this.placedFiles >= 1) {
                                                stage++;
                                        }
                                        break;
                                case 3:
                                        showFilesPlaced();
                                        if(this.placedFiles >= 1) {
                                                stage++;
                                        }
                                        break;
                                default:
                                        showFinProcessing();
                                        this.startButton.setEnabled(true);
                                        sorting = false;
                                        break;
                        }
                        OperationsPanel.this.revalidate();
                }while(sorting);
        }

        private void showFinProcessing() {
                this.progress.setForeground(Color.white);
                this.progress.setText("All Done! - Thank You! ;)");
        }

        private void showFilesFound() {
                this.foundFiles = sorterThread.getFoundFiles();
                this.progress.setForeground(Color.cyan);
                this.progress.setText("Compatible Files: " + this.foundFiles);
                this.processedFiles = sorterThread.getFileProcessedCount();
        }

        private void showFilesProcessed() {
                this.processedFiles = this.sorterThread.getFileProcessedCount();
                this.progress.setForeground(Color.yellow);
                this.progress.setText("Processed: " + this.processedFiles + "/" + this.foundFiles);
                this.placedFiles = this.sorterThread.getPlacedFiles();
        }

        private void showFilesPlaced() {
                this.progress.setForeground(Color.green);
                this.progress.setText("Placing: " + this.placedFiles + "/" + this.processedFiles);
        }

        private JTextField createProgress() {
                JTextField progressFeild = new JTextField("Don't Forget Folder Name ->");
                progressFeild.setEditable(false);
                progressFeild.setVisible(true);
                progressFeild.setHorizontalAlignment((int) CENTER_ALIGNMENT);
                progressFeild.setBackground(Color.gray);
                progressFeild.setForeground(Color.white);
                progressFeild.setPreferredSize(new Dimension(170, 50));
                progressFeild.setBorder(BorderFactory.createEtchedBorder(Color.white, Color.darkGray));

                return progressFeild;
        }

        private void createStartButton() {
                this.startButton = new JButton("Start");
                this.startButton.setBackground(Color.gray);
                this.startButton.setForeground(Color.white);
                this.startButton.setPreferredSize(new Dimension(100, 50));
                this.startButton.setBorder(BorderFactory.createEtchedBorder(Color.white, Color.darkGray));
                this.startButton.addActionListener(new StartAction());
        }

        private JTextField createOutNameFeild() {
                JTextField outTextField = new JTextField();
                outTextField.setText("Sorted Media");
                outTextField.setEditable(true);
                outTextField.setVisible(true);
                outTextField.setHorizontalAlignment((int) CENTER_ALIGNMENT);
                outTextField.setBackground(Color.gray);
                outTextField.setForeground(Color.white);
                outTextField.setBorder(BorderFactory.createEtchedBorder(Color.white, Color.darkGray));
                return outTextField;
        }

        class StartAction implements ActionListener {

                public void actionPerformed(ActionEvent e) {
                        OperationsPanel.this.progress.setText("Processing");
                        OperationsPanel.this.startButton.setEnabled(false);
                        OperationsPanel.this.outName = OperationsPanel.this.outNameField.getText();

                        initializePhotoProcessor();

                        try {
                                OperationsPanel.this.sorterThread.join();
                        } catch (Exception e1) {
                                System.err.println("Sorter thread failed to merge with main thread");
                                OperationsPanel.this.progress.setText("Fatel Error - Try Again");
                                e1.printStackTrace();
                        }

                        OperationsPanel.this.startButton.setEnabled(true);
                        OperationsPanel.this.progress.setText("Success!!!");
                }
        }
}