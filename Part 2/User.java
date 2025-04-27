import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * User class for saving data and history of user's bets
 */
public class User {
    private int betsWon = 0;
    private int betsLost = 0;
    private int pointsEarnt = 0;

    private static String filename = "user.csv";

    public void addPoints(int x) {
        pointsEarnt += x;
        System.out.println(pointsEarnt);
    }

    // Default constructor
    public User() {}

    // Constructor with initialization
    public User(int betsWon, int betsLost, int pointsEarnt) {
        this.betsWon = betsWon;
        this.betsLost = betsLost;
        this.pointsEarnt = pointsEarnt;
    }

    // Getters
    public int getBetsMade() { return betsWon + betsLost; }
    public int getBetsWon() { return betsWon; }
    public int getBetsLost() { return betsLost; }
    public int getPointsEarnt() { return pointsEarnt; }

    /**
     * Fetches user data from file and returns as User object
     */
    public static User Fetch() throws IOException {
        if (!Files.exists(Paths.get(filename))) {
            return new User(0, 0, 0);
        }

        try (BufferedReader fileRead = new BufferedReader(new FileReader(filename))) {
            String line = fileRead.readLine();
            if (line != null) {
                String[] split = line.split(",");
                if (split.length >= 3) {
                    return new User(
                            Integer.parseInt(split[0]),  // betsWon
                            Integer.parseInt(split[1]),  // betsLost
                            Integer.parseInt(split[2])   // pointsEarnt
                    );
                }
            }
        }
        return new User(0, 0, 0);
    }

    /**
     * Saves user data by merging current instance with saved data
     */
    public void Save() throws IOException {
        User savedUser = Fetch();

        User mergedUser = new User(
                savedUser.getBetsWon() + this.betsWon,
                savedUser.getBetsLost() + this.betsLost,
                savedUser.getPointsEarnt() + this.pointsEarnt
        );

        try (PrintWriter fileWrite = new PrintWriter(new FileWriter(filename, false))) {
            fileWrite.println(
                    mergedUser.getBetsWon() + "," +
                            mergedUser.getBetsLost() + "," +
                            mergedUser.getPointsEarnt()
            );
        }
    }

    public void addBetWon() {
        this.betsWon++;
    }

    public void addBetsLost() {
        this.betsLost++;
    }

    public static void ShowUser() {
        // Create and configure the frame
        JFrame frame = new JFrame();
        frame.setTitle("User Data");
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        // Main panel with GridLayout
        JPanel dataPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        dataPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Initialize data variables
        int pointsEarnt = 0;
        int betsWon = 0;
        int betsLost = 0;
        int betsMade = 0;

        // Read from user.csv
        try {
            if (Files.exists(Paths.get("user.csv"))) {
                BufferedReader reader = new BufferedReader(new FileReader("user.csv"));
                String line = reader.readLine();
                if (line != null) {
                    String[] values = line.split(",");
                    if (values.length >= 3) {
                        betsWon = Integer.parseInt(values[0]);
                        betsLost = Integer.parseInt(values[1]);
                        pointsEarnt = Integer.parseInt(values[2]);
                        betsMade = betsWon + betsLost;
                    }
                }
                reader.close();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error reading user data: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Create labels with styling
        Font labelFont = new Font("Arial", Font.BOLD, 16);
        Font valueFont = new Font("Arial", Font.PLAIN, 16);
        Color labelColor = new Color(70, 70, 70);

        // Points
        JLabel pointsLabel = new JLabel("Points Earned:");
        pointsLabel.setFont(labelFont);
        pointsLabel.setForeground(labelColor);
        JLabel pointsValue = new JLabel(String.valueOf(pointsEarnt));
        pointsValue.setFont(valueFont);

        // Bets Made
        JLabel betsMadeLabel = new JLabel("Bets Made:");
        betsMadeLabel.setFont(labelFont);
        betsMadeLabel.setForeground(labelColor);
        JLabel betsMadeValue = new JLabel(String.valueOf(betsMade));
        betsMadeValue.setFont(valueFont);

        // Bets Won
        JLabel betsWonLabel = new JLabel("Bets Won:");
        betsWonLabel.setFont(labelFont);
        betsWonLabel.setForeground(labelColor);
        JLabel betsWonValue = new JLabel(String.valueOf(betsWon));
        betsWonValue.setFont(valueFont);

        // Bets Lost
        JLabel betsLostLabel = new JLabel("Bets Lost:");
        betsLostLabel.setFont(labelFont);
        betsLostLabel.setForeground(labelColor);
        JLabel betsLostValue = new JLabel(String.valueOf(betsLost));
        betsLostValue.setFont(valueFont);

        // Add to panel
        dataPanel.add(pointsLabel);
        dataPanel.add(pointsValue);
        dataPanel.add(betsMadeLabel);
        dataPanel.add(betsMadeValue);
        dataPanel.add(betsWonLabel);
        dataPanel.add(betsWonValue);
        dataPanel.add(betsLostLabel);
        dataPanel.add(betsLostValue);

        // Close button
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> frame.dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);

        // Add components to frame
        frame.add(dataPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Show frame
        frame.setVisible(true);
    }
}
