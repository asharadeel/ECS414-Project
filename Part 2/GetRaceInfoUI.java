import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.Arrays;

/**
 * Using UI, capture information for horses in the race, and customise race
 *
 * @author ashar
 * @version 1.1
 */

public class GetRaceInfoUI {

    // DECLARATIONS
    static Horse[] createdHorses = new Horse[0];
    static Horse[] finalHorses = new Horse[10];

    //DROPDOWN INFORMATION
    static Character[] horseIcons = {'♔','♕','♖','♗','♘','♚','♛','♜','♝','♞'};
    static Integer[] lanes = {1,2,3,4,5,6,7,8,9,10};
    static String[] colours = {"WHITE","BLACK","RED","GREEN","BLUE","CYAN","MAGENTA","PINK"};
    static DefaultComboBoxModel<Character> iconModel;
    static DefaultComboBoxModel<Integer> laneModel;
    static DefaultComboBoxModel<String> colourModel;
    static DefaultComboBoxModel<String> fontColourModel;

    //MAIN UI FRAME
    static JFrame mainFrame;

    /**
     * Show the main UI for setting up a new race
     *  - take inputs for the horse symbols, name and confidence
     *  - take inputs for the race customisation - font colour and background details.
     */
    public static void showUI(){
        createdHorses = new Horse[0];
        finalHorses = new Horse[10];
        //CREATE MAIN FRAME AND SET BACKGROUND
        mainFrame = new JFrame("Horse Creator") {
            private Image backgroundImage;

            {
                try {
                    backgroundImage = new ImageIcon("images/racingGrid.jpg").getImage();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Set the content pane to be a custom JPanel with the background
                setContentPane(new JPanel() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        if (backgroundImage != null) {
                            int imgWidth = backgroundImage.getWidth(this);
                            int imgHeight = backgroundImage.getHeight(this);

                            int tilesX = (int) Math.ceil((double) getWidth() / imgWidth);
                            int tilesY = (int) Math.ceil((double) getHeight() / imgHeight);

                            for (int y = 0; y < tilesY; y++) {
                                for (int x = 0; x < tilesX; x++) {
                                    g.drawImage(backgroundImage,
                                            x * imgWidth,
                                            y * imgHeight,
                                            this);
                                }
                            }

                            g.setColor(new Color(0, 0, 0, 200));
                            g.fillRect(0, 0, getWidth(), getHeight());
                        }
                    }
                });
            }
        };
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(900, 400);
        mainFrame.setLayout(new BorderLayout());


        //INITIALIZE DROPDOWN AND ITS VALUES
        initializeModels();

        // Create title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        ImageIcon logoIcon = new ImageIcon("Images/racemanager.png");
        Image image = logoIcon.getImage();
        int maxWidth = 500;
        Image scaledImage = image.getScaledInstance(maxWidth, -1, Image.SCALE_SMOOTH);
        ImageIcon scaledLogoIcon = new ImageIcon(scaledImage);
        JLabel titleLabel = new JLabel(scaledLogoIcon);


        titleLabel.setForeground(Color.white);
        titlePanel.add(titleLabel);


        // CREATE HORSE PANEL ----------------------------------------
        JPanel createHorse = new JPanel();
        TitledBorder CHborder = BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Create Horse:",
                TitledBorder.LEFT,
                TitledBorder.TOP
        );
        CHborder.setTitleColor(Color.WHITE);
        CHborder.setTitleFont(new Font("Segoe UI", Font.ITALIC, 14));
        createHorse.setBorder(CHborder);
        createHorse.repaint();

        createHorse.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        createHorse.setOpaque(false);


        //DROPDOWNS FOR SETTING HORSE
        JComboBox<Integer> laneDropdown = new JComboBox<>(laneModel);
        JLabel laneLabel = new JLabel("Lane:");
        laneLabel.setForeground(Color.WHITE);
        createHorse.add(laneLabel);
        createHorse.add(laneDropdown);

        JComboBox<Character> iconDropdown = new JComboBox<>(iconModel);
        JLabel SymbolLabel = new JLabel("Symbol:");
        SymbolLabel.setForeground(Color.WHITE);
        createHorse.add(SymbolLabel);
        createHorse.add(iconDropdown);

        JTextField nameField = new JTextField(15);
        JLabel NameLabel = new JLabel("Name:");
        NameLabel.setForeground(Color.WHITE);
        createHorse.add(NameLabel);
        createHorse.add(nameField);

        JSlider confidenceSlider = new JSlider(1, 99, 50);
        confidenceSlider.setMajorTickSpacing(25);
        confidenceSlider.setPaintTicks(true);
        confidenceSlider.setPaintLabels(true);
        JLabel ConfidenceLabel = new JLabel("Confidence:");
        ConfidenceLabel.setForeground(Color.WHITE);
        createHorse.add(ConfidenceLabel);
        createHorse.add(confidenceSlider);

        JTextArea outputArea = new JTextArea(4, 60);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        //Customise Race Panel --------------------------------
        JPanel customiseRace = new JPanel();

        TitledBorder CRBorder = BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Customise Race:",
                TitledBorder.LEFT,
                TitledBorder.TOP
        );

        CRBorder.setTitleColor(Color.WHITE);
        CRBorder.setTitleFont(new Font("Segoe UI", Font.ITALIC, 14));
        customiseRace.setBorder(CRBorder);
        customiseRace.repaint();

        customiseRace.setOpaque(false);
        customiseRace.setLayout(new FlowLayout(FlowLayout.LEFT,10,5));

        //DROPDOWNS FOR SETTING RACE
        JComboBox<String> colourDropdown = new JComboBox<>(colourModel);
        JLabel colourLabel = new JLabel("Background Colour: ");
        colourLabel.setForeground(Color.white);
        colourDropdown.setSelectedIndex(0);
        customiseRace.add(colourLabel);
        customiseRace.add(colourDropdown);

        JComboBox<String> fontColourDropdown = new JComboBox<>(fontColourModel);
        fontColourDropdown.setSelectedIndex(1);
        JLabel fontColourLabel = new JLabel("Text Colour: ");
        fontColourLabel.setForeground(Color.white);
        customiseRace.add(fontColourLabel);
        customiseRace.add(fontColourDropdown);

        JSlider raceLengthField =  new JSlider(50,150,100);
        raceLengthField.setMajorTickSpacing(25);
        raceLengthField.setPaintTicks(true);
        raceLengthField.setPaintLabels(true);
        JLabel raceLengthLabel = new JLabel("Race Length: ");
        raceLengthLabel.setForeground(Color.WHITE);
        customiseRace.add(raceLengthLabel);
        customiseRace.add(raceLengthField);


        //BUTTONS PANEL ------------------------------------

        JPanel buttonPanel = new JPanel();

        //declare buttons
        JButton submitButton = new JButton("Submit");
        submitButton.setPreferredSize(new Dimension(150, 40));
        submitButton.setBackground(Color.BLACK);
        submitButton.setForeground(Color.WHITE);
        submitButton.setFont(new Font("Helvetica", Font.BOLD, 12));

        JButton clearAllButton = new JButton("Clear All");
        clearAllButton.setPreferredSize(new Dimension(150, 40));
        clearAllButton.setBackground(Color.BLACK);
        clearAllButton.setForeground(Color.WHITE);
        clearAllButton.setFont(new Font("Helvetica", Font.BOLD, 12));


        JButton doneButton = new JButton("Done");
        doneButton.setPreferredSize(new Dimension(150, 40));
        doneButton.setBackground(Color.BLACK);
        doneButton.setForeground(Color.WHITE);
        doneButton.setFont(new Font("Helvetica", Font.BOLD, 12));


        //BUTTONS ACTIONS
        submitButton.addActionListener(e -> {
            try {
                Integer lane = (Integer) laneDropdown.getSelectedItem();
                Character icon = (Character) iconDropdown.getSelectedItem();
                String name = nameField.getText().trim();
                float confidence = confidenceSlider.getValue() / 100.0f;

                // Validate name length
                if (name.length() < 3 || name.length() > 20) {
                    JOptionPane.showMessageDialog(mainFrame,
                            "Name must be between 3-20 characters long",
                            "Invalid Name Length",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Horse current = new Horse(icon, name, confidence);
                createdHorses = AALibrary.appendValue(createdHorses, current);
                finalHorses[lane-1] = current;

                outputArea.append(String.format("Lane %d: %s (%s) - Confidence: %.2f\n",
                        lane, name, icon, confidence));

                laneModel.removeElement(lane);
                iconModel.removeElement(icon);

                if (laneModel.getSize() > 0) laneDropdown.setSelectedIndex(0);
                if (iconModel.getSize() > 0) iconDropdown.setSelectedIndex(0);
                nameField.setText("");
                confidenceSlider.setValue(50);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(mainFrame, "Error creating horse: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        clearAllButton.addActionListener(e -> {
            outputArea.setText("");
            Arrays.fill(finalHorses, null);
            createdHorses = new Horse[0];
            initializeModels();
            laneDropdown.setModel(laneModel);
            iconDropdown.setModel(iconModel);
            nameField.setText("");
            confidenceSlider.setValue(50);
        });

        doneButton.addActionListener(e -> {
            int horseCount = countHorses();
            if (horseCount < 2) {
                JOptionPane.showOptionDialog(mainFrame,
                        "Please add at least 2 horses",
                        "Not Enough Horses",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        null,
                        new Object[]{"Close"},
                        "Close");
            } else {
                // Create a panel to display horse information
                JPanel confirmationPanel = new JPanel(new BorderLayout(10, 10));
                confirmationPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                // Add confirmation question
                JLabel questionLabel = new JLabel("Confirm horses and proceed?");
                questionLabel.setFont(questionLabel.getFont().deriveFont(Font.BOLD));
                confirmationPanel.add(questionLabel, BorderLayout.NORTH);

                // Create text area for horse info
                JTextArea horsesTextArea = new JTextArea();
                horsesTextArea.setEditable(false);
                horsesTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

                // Build the horse info string
                StringBuilder horsesInfo = new StringBuilder("Final Horses:\n\n");
                for (int i = 0; i < finalHorses.length; i++) {
                    Horse h = finalHorses[i];
                    if (h != null) {
                        horsesInfo.append(String.format("Lane %d: %s (%s) - Confidence: %.2f%n",
                                i+1, h.getName(), h.getSymbol(), h.getConfidence()));
                    }
                }
                horsesTextArea.setText(horsesInfo.toString());

                // Add scroll pane with horse info
                confirmationPanel.add(new JScrollPane(horsesTextArea), BorderLayout.CENTER);

                // Show the dialog with the custom panel
                int choice = JOptionPane.showOptionDialog(mainFrame,
                        confirmationPanel,  // Our custom panel
                        "Confirmation",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new Object[]{"Yes", "No"},
                        "No");

                if (choice == JOptionPane.YES_OPTION) {
                    mainFrame.dispose();
                    printFinalHorses();
                    RaceData raceData = new RaceData();
                    raceData.horses = finalHorses;
                    raceData.fontColour = fontColourDropdown.getSelectedItem().toString();
                    raceData.backgroundColour = colourDropdown.getSelectedItem().toString();
                    raceData.RaceLength = raceLengthField.getValue();

                    Main.StartBets(raceData);

                }
            }
        });

        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        buttonPanel.add(submitButton);
        buttonPanel.add(clearAllButton);
        buttonPanel.add(doneButton);


        //MENU BAR --------------------------------------
        JMenuBar menuBar = new JMenuBar();

        JMenu BackMenu = new JMenu("Back");
        JMenuItem RTMMButton = new JMenuItem("Return to Menu");
        RTMMButton.addActionListener(e -> {
            outputArea.setText("");
            Arrays.fill(finalHorses, null);
            createdHorses = new Horse[0];
            initializeModels();
            laneDropdown.setModel(laneModel);
            iconDropdown.setModel(iconModel);
            nameField.setText("");
            confidenceSlider.setValue(50);
            mainFrame.dispose();
            Main.RTMM();
        });


        BackMenu.add(RTMMButton);
        menuBar.add(BackMenu);
        mainFrame.setJMenuBar(menuBar);

        //CONTENTS PANEL ----------------------------
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel , BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.add(createHorse);
        centerPanel.add(customiseRace);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        contentPanel.add(centerPanel, BorderLayout.CENTER);
        contentPanel.add(scrollPane, BorderLayout.SOUTH);

        mainFrame.add(titlePanel, BorderLayout.NORTH);
        mainFrame.add(contentPanel, BorderLayout.CENTER);
        mainFrame.add(buttonPanel, BorderLayout.SOUTH);
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    /**
     * Coun the number of horses in the race (since some lanes may be null, it is important to clarify for null)
     * @return n of horses
     */
    private static int countHorses() {
        int count = 0;
        for (Horse horse : finalHorses) {
            if (horse != null) count++;
        }
        return count;
    }

    /**
     * Print the final horses that are to be passed to race for debugging.
     */
    private static void printFinalHorses() {
        System.out.println("\nHorses by :");
        for (int i = 0; i < finalHorses.length; i++) {
            Horse h = finalHorses[i];
            if (h != null) {
                System.out.printf("Lane %d: %s (%s) - Confidence: %.2f\n",
                        i+1, h.getName(), h.getSymbol(), h.getConfidence());
            }
        }
    }


    /**
     * initialize all models for dropdown - add all values
     */
    private static void initializeModels() {
        iconModel = new DefaultComboBoxModel<>();
        laneModel = new DefaultComboBoxModel<>();
        colourModel = new DefaultComboBoxModel<>();
        fontColourModel = new DefaultComboBoxModel<>();
        for (Character c : horseIcons) iconModel.addElement(c);
        for (Integer lane : lanes) laneModel.addElement(lane);
        for(String x: colours)  colourModel.addElement(x);
        for(String x: colours)   fontColourModel.addElement(x);

    }
}