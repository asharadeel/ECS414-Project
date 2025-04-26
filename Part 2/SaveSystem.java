import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.text.*;
import java.util.*;

/**
 * Save system for history of races
 * @author ashar
 * @version 1.0
 */

public class SaveSystem {
    //file name declaration
    private static String fileName = "History.txt";

    /**
     * The Save method, for saving data to file
     * - create new file if file does not exist
     * - if file exists, then append new values
     * @param stats - fetch the stats of the completed race
     * @throws IOException - handle any IO exceptions
     */
    public static void Save(GameStatistics stats) throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss - dd:MM:yyyy");
        String timestamp = dateFormat.format(new Date());

        File historyFile = new File(fileName);
        boolean fileExists = historyFile.exists();

        try (PrintWriter fileWrite = new PrintWriter(new FileWriter(historyFile, true))) { // true for append mode

            if (fileExists) {
                fileWrite.println("\n\n");
            }

            fileWrite.println("###############################################################");
            fileWrite.println("GAME - " + timestamp);
            fileWrite.println("---------------------------------");

            if (stats.getWinner() != null) {
                Horse winner = stats.getWinner();
                fileWrite.printf("Winner: %s | %s%n", winner.getSymbol(), winner.getName());
            } else {
                fileWrite.println("No winner - all horses fell!");
            }

            fileWrite.println("\nHORSE STATISTICS:");
            fileWrite.println("---------------------------------");
            for (HorseData horse : stats.getHorses()) {
                if (horse != null) {
                    fileWrite.printf(
                            "%s | %-15s | Lane: %d | Distance: %dm | Speed: %.2fkm/h | Time: %.2fs%n",
                            horse.getSymbol(),
                            horse.getName(),
                            horse.getLane(),
                            horse.getDistanceTravelled(),
                            horse.getAverageSpeed(),
                            horse.getTimeTaken()
                    );
                }
            }

            fileWrite.println("---------------------------------");
            fileWrite.println("END OF RACE");
            fileWrite.println("###############################################################");

            System.out.println("NEW SAVE: " + timestamp);
        }
    }

    /**
     * Fetch the user data for the History menu.
     * retrieve data from history.txt and show in UI that can be accessed from
     * main menu
     * @throws IOException - handle exceptions
     */
    public static void Fetch() throws IOException {
        // Create main frame
        JFrame historyFrame = new JFrame("Game History");
        historyFrame.setSize(900, 400);
        historyFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        historyFrame.setLayout(new BorderLayout());

        // ===== TITLE PANEL WITH BACKGROUND IMAGE =====
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

                    g.setColor(new Color(0, 0, 0, 150));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setPreferredSize(new Dimension(0, 100));

        // Add title label
        JLabel titleLabel = new JLabel("Race History", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        // ===== CONTENT PANEL (WHITE BACKGROUND) =====
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Read file and add content
        try (BufferedReader reader = new BufferedReader(new FileReader("History.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                JLabel label = new JLabel(line);
                label.setFont(new Font("Monospaced", Font.PLAIN, 12));
                label.setAlignmentX(Component.LEFT_ALIGNMENT);
                contentPanel.add(label);
            }
        }

        // ===== BUTTON PANEL =====
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 20));
        buttonPanel.setBackground(Color.WHITE);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> historyFrame.dispose());
        buttonPanel.add(closeButton);

        // ===== ASSEMBLE COMPONENTS =====
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        historyFrame.add(titlePanel, BorderLayout.NORTH);
        historyFrame.add(scrollPane, BorderLayout.CENTER);
        historyFrame.add(buttonPanel, BorderLayout.SOUTH);

        historyFrame.setLocationRelativeTo(null);
        historyFrame.setVisible(true);
    }
}