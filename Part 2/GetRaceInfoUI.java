import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class GetRaceInfoUI {
    static Horse[] createdHorses = new Horse[0];
    static Horse[] finalHorses = new Horse[10];
    static Character[] horseIcons = {'♔','♕','♖','♗','♘','♚','♛','♜','♝','♞'};
    static Integer[] lanes = {1,2,3,4,5,6,7,8,9,10};
    static DefaultComboBoxModel<Character> iconModel;
    static DefaultComboBoxModel<Integer> laneModel;
    static JFrame mainFrame;

    public static void showUI(){
        mainFrame = new JFrame("Horse Creator");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(900, 250);
        mainFrame.setLayout(new BorderLayout());

        initializeModels();

        JPanel createHorse = new JPanel();
        createHorse.setBorder(BorderFactory.createTitledBorder("CreateHorse"));
        createHorse.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));

        JComboBox<Integer> laneDropdown = new JComboBox<>(laneModel);
        createHorse.add(new JLabel("Lane:"));
        createHorse.add(laneDropdown);

        JComboBox<Character> iconDropdown = new JComboBox<>(iconModel);
        createHorse.add(new JLabel("Icon:"));
        createHorse.add(iconDropdown);

        JTextField nameField = new JTextField(15);
        createHorse.add(new JLabel("Name (3-20 chars):"));
        createHorse.add(nameField);

        JSlider confidenceSlider = new JSlider(1, 99, 50);
        confidenceSlider.setMajorTickSpacing(25);
        confidenceSlider.setPaintTicks(true);
        confidenceSlider.setPaintLabels(true);
        createHorse.add(new JLabel("Confidence:"));
        createHorse.add(confidenceSlider);

        JTextArea outputArea = new JTextArea(4, 60);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        JPanel buttonPanel = new JPanel();
        JButton submitButton = new JButton("Submit");
        JButton clearAllButton = new JButton("Clear All");
        JButton doneButton = new JButton("Done");

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
                int choice = JOptionPane.showOptionDialog(mainFrame,
                        "Confirm horses and proceed?",
                        "Confirmation",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new Object[]{"Yes", "No"},
                        "No");

                if (choice == JOptionPane.YES_OPTION) {
                    mainFrame.dispose();
                    printFinalHorses();
                    Main.startRace(finalHorses);
                }
            }
        });

        buttonPanel.add(submitButton);
        buttonPanel.add(clearAllButton);
        buttonPanel.add(doneButton);

        mainFrame.add(createHorse, BorderLayout.NORTH);
        mainFrame.add(buttonPanel, BorderLayout.CENTER);
        mainFrame.add(scrollPane, BorderLayout.SOUTH);

        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    private static int countHorses() {
        int count = 0;
        for (Horse horse : finalHorses) {
            if (horse != null) count++;
        }
        return count;
    }

    private static void printFinalHorses() {
        System.out.println("\nFinal Horses by Lane:");
        for (int i = 0; i < finalHorses.length; i++) {
            Horse h = finalHorses[i];
            if (h != null) {
                System.out.printf("Lane %d: %s (%s) - Confidence: %.2f\n",
                        i+1, h.getName(), h.getSymbol(), h.getConfidence());
            }
        }
    }



    private static void initializeModels() {
        iconModel = new DefaultComboBoxModel<>();
        laneModel = new DefaultComboBoxModel<>();
        for (Character c : horseIcons) iconModel.addElement(c);
        for (Integer lane : lanes) laneModel.addElement(lane);
    }
}