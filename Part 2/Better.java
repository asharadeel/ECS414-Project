import java.awt.event.ActionListener;
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
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("GAMBLR");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
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
        JPanel horsePanel = new JPanel();
        horsePanel.setLayout(new BoxLayout(horsePanel, BoxLayout.Y_AXIS));
        horsePanel.setBorder(BorderFactory.createTitledBorder(horse.getSymbol() + " | " + horse.getName()));

        // Main panel for all bet types
        JPanel betsPanel = new JPanel();
        betsPanel.setLayout(new GridLayout(3, 1, 5, 5));

        // Create components for each bet type
        // Distance
        JPanel distancePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        distancePanel.add(new JLabel("Distance:"));
        JRadioButton distanceNone = new JRadioButton("None", true); // Default selected
        JRadioButton distanceValue = new JRadioButton("Value");
        JRadioButton distanceRegion = new JRadioButton("Region");
        ButtonGroup distanceGroup = new ButtonGroup();
        distanceGroup.add(distanceNone);
        distanceGroup.add(distanceValue);
        distanceGroup.add(distanceRegion);
        distancePanel.add(distanceNone);
        distancePanel.add(distanceValue);
        distancePanel.add(distanceRegion);

        // Distance input panel
        JPanel distanceInputPanel = new JPanel();
        distanceInputPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JTextField distanceSingleField = new JTextField(5);
        JTextField distanceMinField = new JTextField(5);
        JTextField distanceMaxField = new JTextField(5);
        distancePanel.add(distanceInputPanel);

        // Speed
        JPanel speedPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        speedPanel.add(new JLabel("Average Speed:"));
        JRadioButton speedNone = new JRadioButton("None", true); // Default selected
        JRadioButton speedValue = new JRadioButton("Value");
        JRadioButton speedRegion = new JRadioButton("Region");
        ButtonGroup speedGroup = new ButtonGroup();
        speedGroup.add(speedNone);
        speedGroup.add(speedValue);
        speedGroup.add(speedRegion);
        speedPanel.add(speedNone);
        speedPanel.add(speedValue);
        speedPanel.add(speedRegion);

        // Speed input panel
        JPanel speedInputPanel = new JPanel();
        speedInputPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JTextField speedSingleField = new JTextField(5);
        JTextField speedMinField = new JTextField(5);
        JTextField speedMaxField = new JTextField(5);
        speedPanel.add(speedInputPanel);

        // Time
        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        timePanel.add(new JLabel("Time:"));
        JRadioButton timeNone = new JRadioButton("None", true); // Default selected
        JRadioButton timeValue = new JRadioButton("Value");
        JRadioButton timeRegion = new JRadioButton("Region");
        ButtonGroup timeGroup = new ButtonGroup();
        timeGroup.add(timeNone);
        timeGroup.add(timeValue);
        timeGroup.add(timeRegion);
        timePanel.add(timeNone);
        timePanel.add(timeValue);
        timePanel.add(timeRegion);

        // Time input panel
        JPanel timeInputPanel = new JPanel();
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
        JButton saveButton = new JButton("Save Bet for " + horse.getName());
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
                    int min = validateField(timeMinField, "Time Min");
                    int max = validateField(timeMaxField, "Time Max");
                    if (min >= max) throw new IllegalArgumentException("Time Min must be less than Max");
                    current.BetTime(min, max);
                    hasBets = true;
                }

                if (!hasBets) {
                    throw new IllegalArgumentException("Please select at least one bet type");
                }

                int confirm = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to save this bet?",
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


}
