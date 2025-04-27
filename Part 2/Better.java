import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.*;
import java.awt.*;
import java.util.List;
import javax.swing.*;

/**
 * The main betting module for placing bets and retrieivng results
 *
 * @author ashar
 * @version 1.0.5
 */

public class Better {
    User user = new User();

    RaceData RaceProfile;
    Horse[] StartHorses;
    HorseData[] FinalHorses;

    static DefaultComboBoxModel<String> winnerModel;

    String predictedWinner;
    GameStatistics stats;
    List<BetProfile> Bets = new ArrayList<>();
    JPanel savedBetsPanel; // Panel to display saved bets

    Better(RaceData h){
        this.StartHorses = h.horses;
        this.RaceProfile = h;
        PlaceBets();
    }


    // START --- PLACE BETS
    public void PlaceBets() {
        initializeModels();

        JFrame frame = new JFrame("GAMBLR - Place Your Bets");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 400);
        frame.setLayout(new BorderLayout());

        // 1. Top Panel - Title
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)){
            private Image backgroundImage;

            {
                try {
                    backgroundImage = new ImageIcon("images/racingGrid.jpg").getImage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

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
                            g.drawImage(backgroundImage, x * imgWidth, y * imgHeight, this);
                        }
                    }

                    g.setColor(new Color(0, 0, 0, 200));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };

        JLabel titleLabel = new JLabel("GAMBLR");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(new Color(255, 255, 255));
        topPanel.add(titleLabel);
        frame.add(topPanel, BorderLayout.NORTH);

        // Main center container
        JPanel mainCenterPanel = new JPanel();
        mainCenterPanel.setLayout(new BoxLayout(mainCenterPanel, BoxLayout.Y_AXIS));

        // Scroll pane for the center content
        JScrollPane scrollPane = new JScrollPane(mainCenterPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Panel for horse betting options
        JPanel horsesPanel = new JPanel();
        horsesPanel.setLayout(new BoxLayout(horsesPanel, BoxLayout.Y_AXIS));
        horsesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel for saved bets (will appear below horses)
        savedBetsPanel = new JPanel();
        savedBetsPanel.setLayout(new BoxLayout(savedBetsPanel, BoxLayout.Y_AXIS));
        savedBetsPanel.setBorder(BorderFactory.createTitledBorder("Your Saved Bets"));

        mainCenterPanel.add(horsesPanel);
        mainCenterPanel.add(savedBetsPanel);

        JPanel selectWinner = new JPanel();
        JComboBox<String> winnerDropdown = new JComboBox<>(winnerModel);
        selectWinner.setBorder(BorderFactory.createTitledBorder("Choose Predicted Winner"));
        JLabel winnerLabel = new JLabel("Winner:");
        winnerDropdown.setSelectedIndex(0);
        selectWinner.add(winnerLabel);
        selectWinner.add(winnerDropdown);
        mainCenterPanel.add(selectWinner);

        // Create a panel for each horse
        for (Horse x : StartHorses) {
            if(x != null){
            JPanel horsePanel = createHorseBettingPanel(x, horsesPanel);
            horsesPanel.add(horsePanel);
            horsesPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add spacing
                }
        }

        // 3. Bottom Panel - Buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));

        JButton closeButton = new JButton("Start Race!");
        closeButton.addActionListener(e -> {
            predictedWinner = winnerDropdown.getSelectedItem().toString();
            System.out.println("PREDICTED WINNER = " + predictedWinner);
            frame.dispose();
            Main.startRace(RaceProfile);
        });


        bottomPanel.add(closeButton);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel createHorseBettingPanel(Horse horse, JPanel parentPanel) {
        JPanel horsePanel = new JPanel(){
            private Image backgroundImage;

            {
                try {
                    backgroundImage = new ImageIcon("images/horseBack.jpg").getImage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

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
                            g.drawImage(backgroundImage, x * imgWidth, y * imgHeight, this);
                        }
                    }

                    g.setColor(new Color(255, 255, 255, 150));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };

        horsePanel.setLayout(new BoxLayout(horsePanel, BoxLayout.Y_AXIS));
        horsePanel.setBorder(BorderFactory.createTitledBorder(horse.getSymbol() + " | " + horse.getName()));

        // Main panel for all bet types
        JPanel betsPanel = new JPanel();
        betsPanel.setLayout(new GridLayout(3, 1, 5, 5));
        betsPanel.setOpaque(false);
        // Create components for each bet type
        // Distance
        JPanel distancePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        distancePanel.add(new JLabel("Distance:"));
        distancePanel.setOpaque(false);
        JRadioButton distanceNone = new JRadioButton("None", true); // Default selected
        distanceNone.setOpaque(false);
        JRadioButton distanceValue = new JRadioButton("Value");
        distanceValue.setOpaque(false);
        JRadioButton distanceRegion = new JRadioButton("Region");
        distanceRegion.setOpaque(false);
        ButtonGroup distanceGroup = new ButtonGroup();
        distanceGroup.add(distanceNone);
        distanceGroup.add(distanceValue);
        distanceGroup.add(distanceRegion);
        distancePanel.add(distanceNone);
        distancePanel.add(distanceValue);
        distancePanel.add(distanceRegion);

        // Distance input panel
        JPanel distanceInputPanel = new JPanel();
        distanceInputPanel.setOpaque(false);
        distanceInputPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JTextField distanceSingleField = new JTextField(5);
        JTextField distanceMinField = new JTextField(5);
        JTextField distanceMaxField = new JTextField(5);
        distancePanel.add(distanceInputPanel);

        // Speed
        JPanel speedPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        speedPanel.setOpaque(false);
        speedPanel.add(new JLabel("Average Speed:"));
        JRadioButton speedNone = new JRadioButton("None", true);
        speedNone.setOpaque(false);
        JRadioButton speedValue = new JRadioButton("Value");
        speedValue.setOpaque(false);
        JRadioButton speedRegion = new JRadioButton("Region");
        speedRegion.setOpaque(false);
        ButtonGroup speedGroup = new ButtonGroup();
        speedGroup.add(speedNone);
        speedGroup.add(speedValue);
        speedGroup.add(speedRegion);
        speedPanel.add(speedNone);
        speedPanel.add(speedValue);
        speedPanel.add(speedRegion);

        // Speed input panel
        JPanel speedInputPanel = new JPanel();
        speedInputPanel.setOpaque(false);
        speedInputPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JTextField speedSingleField = new JTextField(5);
        JTextField speedMinField = new JTextField(5);
        JTextField speedMaxField = new JTextField(5);
        speedPanel.add(speedInputPanel);

        // Time
        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        timePanel.setOpaque(false);
        timePanel.add(new JLabel("Time:"));
        JRadioButton timeNone = new JRadioButton("None", true);
        timeNone.setOpaque(false);
        JRadioButton timeValue = new JRadioButton("Value");
        timeValue.setOpaque(false);
        JRadioButton timeRegion = new JRadioButton("Region");
        timeRegion.setOpaque(false);
        ButtonGroup timeGroup = new ButtonGroup();
        timeGroup.add(timeNone);
        timeGroup.add(timeValue);
        timeGroup.add(timeRegion);
        timePanel.add(timeNone);
        timePanel.add(timeValue);
        timePanel.add(timeRegion);

        // Time input panel
        JPanel timeInputPanel = new JPanel();
        timeInputPanel.setOpaque(false);
        timeInputPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JTextField timeSingleField = new JTextField(5);
        JTextField timeMinField = new JTextField(5);
        JTextField timeMaxField = new JTextField(5);
        timePanel.add(timeInputPanel);

        // Add radio button listeners
        ActionListener clearInputs = e -> {
            distanceInputPanel.removeAll();
            distanceInputPanel.revalidate();
            distanceInputPanel.repaint();
        };

        distanceNone.addActionListener(clearInputs);
        distanceValue.addActionListener(e -> {
            distanceInputPanel.removeAll();
            distanceInputPanel.add(new JLabel("Value:"));
            distanceInputPanel.add(distanceSingleField);
            distanceInputPanel.revalidate();
            distanceInputPanel.repaint();
        });
        distanceRegion.addActionListener(e -> {
            distanceInputPanel.removeAll();
            distanceInputPanel.add(new JLabel("Min:"));
            distanceInputPanel.add(distanceMinField);
            distanceInputPanel.add(new JLabel("Max:"));
            distanceInputPanel.add(distanceMaxField);
            distanceInputPanel.revalidate();
            distanceInputPanel.repaint();
        });

        speedNone.addActionListener(e -> {
            speedInputPanel.removeAll();
            speedInputPanel.revalidate();
            speedInputPanel.repaint();
        });
        speedValue.addActionListener(e -> {
            speedInputPanel.removeAll();
            speedInputPanel.add(new JLabel("Value:"));
            speedInputPanel.add(speedSingleField);
            speedInputPanel.revalidate();
            speedInputPanel.repaint();
        });
        speedRegion.addActionListener(e -> {
            speedInputPanel.removeAll();
            speedInputPanel.add(new JLabel("Min:"));
            speedInputPanel.add(speedMinField);
            speedInputPanel.add(new JLabel("Max:"));
            speedInputPanel.add(speedMaxField);
            speedInputPanel.revalidate();
            speedInputPanel.repaint();
        });

        timeNone.addActionListener(e -> {
            timeInputPanel.removeAll();
            timeInputPanel.revalidate();
            timeInputPanel.repaint();
        });
        timeValue.addActionListener(e -> {
            timeInputPanel.removeAll();
            timeInputPanel.add(new JLabel("Value:"));
            timeInputPanel.add(timeSingleField);
            timeInputPanel.revalidate();
            timeInputPanel.repaint();
        });
        timeRegion.addActionListener(e -> {
            timeInputPanel.removeAll();
            timeInputPanel.add(new JLabel("Min:"));
            timeInputPanel.add(timeMinField);
            timeInputPanel.add(new JLabel("Max:"));
            timeInputPanel.add(timeMaxField);
            timeInputPanel.revalidate();
            timeInputPanel.repaint();
        });

        // Add all bet panels
        betsPanel.add(distancePanel);
        betsPanel.add(speedPanel);
        betsPanel.add(timePanel);

        // Save button with validation
        JButton saveButton = new JButton("Lock Bet for " + horse.getName());
        saveButton.addActionListener(e -> {
            try {
                BetProfile current = new BetProfile(user, new HorseData(horse));
                boolean hasBets = false;

                // Distance bet
                if (distanceValue.isSelected()) {
                    int value = validateField(distanceSingleField, "Distance");
                    current.BetDistance(value);
                    hasBets = true;
                } else if (distanceRegion.isSelected()) {
                    int min = validateField(distanceMinField, "Distance Min");
                    int max = validateField(distanceMaxField, "Distance Max");
                    if (min >= max) throw new IllegalArgumentException("Distance Min must be less than Max");
                    current.BetDistance(min, max);
                    hasBets = true;
                }

                // Speed bet
                if (speedValue.isSelected()) {
                    int value = validateField(speedSingleField, "Speed");
                    current.BetAvgSpeed(value);
                    hasBets = true;
                } else if (speedRegion.isSelected()) {
                    int min = validateField(speedMinField, "Speed Min");
                    int max = validateField(speedMaxField, "Speed Max");
                    if (min >= max) throw new IllegalArgumentException("Speed Min must be less than Max");
                    current.BetAvgSpeed(min, max);
                    hasBets = true;
                }

                // Time bet
                if (timeValue.isSelected()) {
                    int value = validateField(timeSingleField, "Time");
                    current.BetTime(value);
                    hasBets = true;
                } else if (timeRegion.isSelected()) {
                    int min = validateTimeField(timeMinField, "Time Min");
                    int max = validateTimeField(timeMaxField, "Time Max");
                    if (min >= max) throw new IllegalArgumentException("Time Min must be less than Max");
                    current.BetTime(min, max);
                    hasBets = true;
                }

                if (!hasBets) {
                    throw new IllegalArgumentException("Please select at least one bet type");
                }

                int confirm = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to lock this bet?",
                        "Confirm Save",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    current.view();
                    Bets.add(current);
                    parentPanel.remove(horsePanel);
                    parentPanel.revalidate();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter valid numbers (0-150)");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        });

        horsePanel.add(betsPanel);
        horsePanel.add(saveButton);

        return horsePanel;
    }

    private int validateField(JTextField field, String fieldName) throws NumberFormatException {
        if (field.getText().trim().isEmpty()) {
            throw new NumberFormatException(fieldName + " cannot be empty");
        }
        int value = Integer.parseInt(field.getText());
        if (value < 0 || value > 150) {
            throw new NumberFormatException(fieldName + " must be between 0-150");
        }
        return value;
    }

    private int validateTimeField(JTextField field, String fieldName) throws NumberFormatException {
        if (field.getText().trim().isEmpty()) {
            throw new NumberFormatException(fieldName + " cannot be empty");
        }
        int value = Integer.parseInt(field.getText());
        if (value < 0 || value > 5000) {
            throw new NumberFormatException(fieldName + " must be between 0-5000");
        }
        return value;
    }


    private void initializeModels() {
        winnerModel = new DefaultComboBoxModel<String>();
        winnerModel.addElement("N/A");
        winnerModel.addElement("None");
        for(Horse x : StartHorses)  {
            if(x != null) {
                winnerModel.addElement(x.getName());
            }
        }
    }


    /**
     * Create UI to review bets
     * @param stats - review stats and check against stats the horse
     */
    public void ReviewBets(GameStatistics stats) {
        // Create the JFrame
        JFrame betReviewFrame = new JFrame("Bet Review");
        betReviewFrame.setSize(900, 400);
        betReviewFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        betReviewFrame.setLayout(new BorderLayout());

        // ===== 1. TITLE PANEL =====
        JPanel titlePanel = new JPanel() {
            private Image backgroundImage;

            {
                try {
                    backgroundImage = new ImageIcon("images/racingGrid.jpg").getImage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

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
                            g.drawImage(backgroundImage, x * imgWidth, y * imgHeight, this);
                        }
                    }

                    g.setColor(new Color(0, 0, 0, 200));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };

        JLabel titleLabel = new JLabel("BET REVIEW");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.white);
        titlePanel.add(titleLabel);
        betReviewFrame.add(titlePanel, BorderLayout.NORTH);

        // ===== 2. CONTENT PANEL =====
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(contentPanel);

        // Build the results text
        StringBuilder resultsText = new StringBuilder();

        // Check winner prediction
        if (stats.getWinner() != null) {
            if (stats.getWinner().getName().equals(predictedWinner)) {
                resultsText.append(" SUCCESSFUL GUESS OF THE WINNER!\n\n");
            } else {
                resultsText.append(" FAILURE GUESS OF THE WINNER!\n\n");
            }
        } else if (predictedWinner.equals("N/A")) {
            resultsText.append(" SUCCESSFUL GUESS OF THE WINNER!\n\n");
        } else {
            resultsText.append(" FAILURE GUESS OF THE WINNER!\n\n");
        }

        // Update HorseData for each bet
        Iterator<BetProfile> updater = Bets.iterator();
        while (updater.hasNext()) {
            BetProfile bet = updater.next();
            for (HorseData x : FinalHorses) {
                if (bet.getHorseData().getSymbol() == x.getSymbol()) {
                    bet.setHorse(x);
                    break;
                }
            }
        }

        // Review each bet
        Iterator<BetProfile> iterator = Bets.iterator();
        while (iterator.hasNext()) {
            BetProfile bet = iterator.next();
            resultsText.append("----------------------------------\n");
            resultsText.append("Bet on Horse: ").append(bet.getHorseData().getName()).append("\n");

            int[] avg = bet.GetAverageSpeed();
            if (avg[0] != -1) {
                boolean won = bet.CheckAverageBets();

                resultsText.append("- Average Speed Bet: ").append(won ? " WON" : " LOST").append(" | ");
                resultsText.append("Bet: ").append(avg.length == 1 ? "≥" + avg[0] : "Range [" + avg[0] + "km/h -" + avg[1] + "km/h]").append("\n");
                resultsText.append("Actual: " + bet.getHorseData().getAverageSpeed() + "km/h.\n");
            }

            int[] time = bet.GetTimeBets();
            if (time[0] != -1) {
                boolean won = bet.CheckTimeBets();

                resultsText.append("- Time Bet: ").append(won ? " WON" : " LOST").append(" | ");
                resultsText.append("Bet: ").append(time.length == 1 ? "≤" + time[0] : "Range [" + time[0] + "s -" + time[1] + "s]").append("\n");
                resultsText.append("Actual: " + bet.getHorseData().getTimeTaken() + "s.\n");
            }

            int[] distance = bet.GetDistanceBets();
            if (distance[0] != -1) {
                boolean won = bet.CheckDistanceBets();

                resultsText.append("- Distance Bet: ").append(won ? " WON" : " LOST").append(" | ");
                resultsText.append("Bet: ").append(distance.length == 1 ? "≥" + distance[0] : "Range [" + distance[0] + "m -" + distance[1] + "m]").append("\n");
                resultsText.append("Actual: " + bet.getHorseData().getDistanceTravelled() + "m.\n");

            }

            resultsText.append("----------------------------------\n");
        }

        // Add results to a JTextArea
        JTextArea textArea = new JTextArea(resultsText.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        contentPanel.add(textArea);

        betReviewFrame.add(scrollPane, BorderLayout.CENTER);

        // ===== 3. BOTTOM PANEL (Close Button) =====
        JPanel bottomPanel = new JPanel();
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                betReviewFrame.dispose();
            }
        });
        bottomPanel.add(closeButton);
        betReviewFrame.add(bottomPanel, BorderLayout.SOUTH);

        // Display the frame
        betReviewFrame.setLocationRelativeTo(null);
        betReviewFrame.setVisible(true);

    }

    /**
     * Reviwe for terminal for bet status
     * @param stats
     */
    public void UpdateBets(GameStatistics stats) {
        // Update HorseData for each bet
        UpdateUser();
        FinalHorses = stats.getHorses();
        Iterator<BetProfile> updater = Bets.iterator();
        while (updater.hasNext()) {
            BetProfile bet = updater.next();
            for (HorseData x : FinalHorses) {
                if(x != null) {
                    if (bet.getHorseData().getSymbol() == x.getSymbol()) {
                        bet.setHorse(x);
                        break; // No need to check further once matched
                    }
                }
            }
        }

        // Review each bet
        Iterator<BetProfile> iterator = Bets.iterator();
        while (iterator.hasNext()) {
            BetProfile bet = iterator.next();

            System.out.println("\nBet on Horse: " + bet.getHorseData().getName());

            // Check and print each bet type (only if placed)
            int[] avg = bet.GetAverageSpeed();
            if (avg[0] != -1) {
                boolean won = bet.CheckAverageBets();
                System.out.print("- Average Speed Bet: " + (won ? "WON" : "LOST") + " | ");
                System.out.println("Bet: " + (avg.length == 1 ? "≥" + avg[0] : "Range [" + avg[0] + "km/h -" + avg[1] + "km/h]"));
                System.out.println("Actual: " + bet.getHorseData().getAverageSpeed() + "km/h.");

            }

            int[] time = bet.GetTimeBets();
            if (time[0] != -1) {
                boolean won = bet.CheckTimeBets();
                System.out.print("- Time Bet: " + (won ? "WON" : "LOST") + " | ");
                System.out.println("Bet: " + (time.length == 1 ? "≤=" + time[0] : "Range [" + time[0] + "ms-" + time[1] + "ms]"));
                System.out.println("Actual: " + bet.getHorseData().getTimeTaken() + "ms.");

            }

            int[] distance = bet.GetDistanceBets();
            if (distance[0] != -1) {
                boolean won = bet.CheckDistanceBets();
                System.out.print("- Distance Bet: " + (won ? "WON" : "LOST") + " | ");
                System.out.println("Bet: " + (distance.length == 1 ? "≥" + distance[0] : "Range [" + distance[0] + "m-" + distance[1] + "m]"));
                System.out.println("Actual: " + bet.getHorseData().getDistanceTravelled() + "m.");
            }

            System.out.println("----------------------------------");
        }
        //save user data
        try {
            user.Save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Update the user to reward points and update bets won/lost values
     */
    private void UpdateUser(){
        System.out.println(" ##### USER UPDATED! #### ");
        try {
            // Check winner prediction
            if (stats != null && stats.getWinner() != null) {
                if (stats.getWinner().getName().equals(predictedWinner)) {
                    user.addPoints(100);
                }
            } else if (predictedWinner != null && predictedWinner.equals("N/A")) {
                user.addPoints(100);
            }

            // Review each bet if Bets exists
            if (Bets != null) {
                Iterator<BetProfile> iterator = Bets.iterator();
                while (iterator.hasNext()) {
                    BetProfile bet = iterator.next();
                    if (bet != null) {
                        // Check each bet type
                        if (bet.CheckAverageBets()) {
                            user.addBetWon();
                        } else {
                            user.addBetsLost();
                        }

                        if (bet.CheckTimeBets()) {
                            user.addBetWon();
                        } else {
                            user.addBetsLost();
                        }

                        if (bet.CheckDistanceBets()) {
                            user.addBetWon();
                        } else {
                            user.addBetsLost();
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error updating user: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
