package BasicWindow;

import javax.swing.*;
import java.awt.*;


public class BasicWindow_main extends JFrame {
        private DirectoryPicker inputDirPicker, outputDirPicker;
        private OptionActions optActions;
        private OperationsPanel operPanel;

        public BasicWindow_main () {
                initializeFrame();
        }

        private void initializeFrame() {
                this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                this.setSize(620, 400);
                this.setResizable(false);
                this.setVisible(true);
                this.setLayout(new BorderLayout());
                this.setBackground(Color.black);

                addComponents();
                this.revalidate();
        }        

        // create component structure and add them to the window
        private void addComponents() {
                JPanel northPanel = new JPanel();
                northPanel.setBackground(Color.darkGray);
                northPanel.add(createTitleLabel());
                this.add(northPanel, BorderLayout.NORTH);

                JPanel centerPanel = new JPanel();
                centerPanel.setBackground(Color.darkGray);
                centerPanel.add(createDirPickers());
                this.add(centerPanel, BorderLayout.CENTER);
                
                JPanel southPanel = new JPanel();
                southPanel.setBackground(Color.darkGray);
                southPanel.setPreferredSize(new Dimension(600, 130));
                southPanel.setLayout(new BorderLayout(0, 0));

                this.optActions = new OptionActions();
                southPanel.add(this.optActions, BorderLayout.NORTH);
                this.operPanel = new OperationsPanel(this.inputDirPicker, this.outputDirPicker, this.optActions);
                southPanel.add(this.operPanel, BorderLayout.SOUTH);

                this.add(southPanel, BorderLayout.SOUTH);
        }

        private JPanel createDirPickers() {
                JPanel dirPickers = new JPanel(new BorderLayout(0, 2));
                dirPickers.setBackground(Color.darkGray);

                this.inputDirPicker = new DirectoryPicker("input");
                dirPickers.add(inputDirPicker, BorderLayout.NORTH);

                this.outputDirPicker = new DirectoryPicker("output");
                dirPickers.add(outputDirPicker, BorderLayout.SOUTH);

                return dirPickers;
        }

        private JPanel createTitleLabel() {
                String font = "New Times Roman";
                JLabel titleLabel = new JLabel("Welcome to my photo sorter ");
                titleLabel.setHorizontalAlignment(2);
                titleLabel.setFont(new Font(font, Font.BOLD, 30));
                JLabel instruct1 = new JLabel("- Just enter your input directory, than enter your output directory");
                instruct1.setFont(new Font(font, Font.ROMAN_BASELINE, 20));
                JLabel instruct2 = new JLabel("- Select optional actions");
                instruct2.setFont(new Font(font, Font.ROMAN_BASELINE, 20));

                JPanel titlePanel = new JPanel();
                titlePanel.setVisible(true);
                titlePanel.setPreferredSize(new Dimension(600, 100));
                titlePanel.setLayout(new BorderLayout());
                titlePanel.setBackground(new Color(200, 200, 200));

                titlePanel.add(titleLabel, BorderLayout.NORTH);
                titlePanel.add(instruct1, BorderLayout.CENTER);
                titlePanel.add(instruct2, BorderLayout.SOUTH);
                
                return titlePanel;
        }
}
