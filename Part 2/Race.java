
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

/**
 * A multi-horse (2-10) race
 *
 * @author McRaceface, ashar
 * @version 2.5
 */


public class Race
{
    private int raceLength;
    private Horse[] raceHorses = new Horse[0];
    private String weatherCondition;

    JFrame RaceMenu;
    JPanel RacePanel;
    private GameStatistics stats;
    private Timer RaceTimer;
    private String fontColour;
    private String backgroundColour;


    /**
     * Constructor for objects of class Race
     * Initially there are no horses in the lanes
     *
     * @param distance the length of the racetrack (in metres/yards...)
     */
    public Race(int distance)
    {
        // initialise instance variables
        raceLength = distance;
    }

    public void setColours(String fc, String bc){
        fontColour = fc;
        backgroundColour = bc;
    }

    /**
     * Adds a horse to the race in a given lane
     *
     * @param theHorse the horse to be added to the race
     * @param laneNumber the lane that the horse will be added to
     */
    public void addHorse(Horse theHorse, int laneNumber) {
        raceHorses = AALibrary.appendValue(raceHorses,theHorse);
    }

    public void startRace() {
        RaceMenu = new JFrame("Race!");

        int frameWidth = raceLength*9;
        RaceMenu.setSize(frameWidth, 300);

        System.out.println(backgroundColour);
        RacePanel = new JPanel();
        RacePanel.setLayout(new BoxLayout(RacePanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(RacePanel);
        RaceMenu.add(scrollPane);
        RaceMenu.setVisible(true);
        RaceMenu.setLocationRelativeTo(null);
        RacePanel.setOpaque(true);
        RacePanel.setBackground(AALibrary.getColorFromString(backgroundColour));



        JMenuBar menuBar = new JMenuBar();
        JMenu CancelRace = new JMenu("Cancel");
        JMenuItem cancelButton = new JMenuItem("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                closeUI();
            }
        });

        CancelRace.add(cancelButton);
        menuBar.add(CancelRace);
        RaceMenu.setJMenuBar(menuBar);



        // - -- - -- - GAME STARTS HERE
        weatherCondition = FindWeatherFactor();
        stats = new GameStatistics(raceLength);
        stats.setWeatherCondition(weatherCondition);

        // Reset all horses
        for(Horse x : raceHorses) {
            if(x != null) {
                x.goBackToStart();
            }
        }

        // Create a Swing Timer for animation
        RaceTimer = new Timer(100, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean finished = false;
                int index = 0;
                Horse winner = null;
                boolean allFell = true;


                // Move each horse
                for(Horse x : raceHorses) {
                    if(x == null){
                        continue;
                    }
                    if(!x.hasFallen()) {
                        moveHorse(x);
                        allFell = false;
                    }
                    if(x.hasFallen()){
                        stats.HorseFinished(x, index);
                    }
                    if (raceWonBy(x)) {
                        stats.HorseFinished(x, index);
                        stats.setWinner(x);
                        finished = true;
                        winner = x;
                        break;
                    }
                    index++;
                    stats.trackTime();
                }


                // Update display
                printRace();
                RacePanel.revalidate();
                RacePanel.repaint();

                // Check if race finished
                if (finished || allFell) {
                    try {
                        stats.finished();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    AALibrary.printHorses(raceHorses);
                    ((Timer)e.getSource()).stop();

                    // Always show one button: "View Stats"
                    Object[] options = {"View Stats"};

                    String message = (winner != null)
                            ? "The winner is " + winner.getName() + "!"
                            : "All horses fell!";

                    // Show dialog
                    JOptionPane.showOptionDialog(
                            Race.this.RaceMenu,
                            message,
                            "Race Finished",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.INFORMATION_MESSAGE,
                            null,
                            options,
                            options[0]
                    );

                    // Run stats method after dialog is dismissed
                    RaceMenu.dispose();
                    RaceFinishedUI();

                    UpdateConfidence(raceHorses, winner);
                    FinishedRace(raceHorses);
                }
            }
        });

        RaceTimer.start();
    }





    /**
     * Determines if a horse has won the race
     *
     * @param theHorse The horse we are testing
     * @return true if the horse has won, false otherwise.
     */
    private boolean raceWonBy(Horse theHorse)
    {
        if (theHorse.getDistanceTravelled() == raceLength)
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    /**
     * Randomly make a horse move forward or fall depending
     * on its confidence rating
     * A fallen horse cannot move
     *
     * @param theHorse the horse to be moved
     */
    private void moveHorse(Horse theHorse)
    {
        //if the horse has fallen it cannot move,
        //so only run if it has not fallen
        if  (!theHorse.hasFallen())
        {
            //the probability that the horse will move forward depends on the confidence;
            double successThreshold = theHorse.getConfidence(); // Base threshold

            if (weatherCondition.equals("Mud")) {
                successThreshold *= 0.95;  // 5% harder
            }
            else if (weatherCondition.equals("Rain")) {
                successThreshold *= 0.9;   // 10% harder
            }
            else if (weatherCondition.equals("Snow")) {
                successThreshold *= 0.8;   // 20% harder
            }

            if (Math.random() < successThreshold) {
                theHorse.moveForward();
            }

            //the probability that the horse will fall is very small (max is 0.1)
            //but will also will depends exponentially on confidence
            //so if you double the confidence, the probability that it will fall is *2
            double factor = 0.02;
            if(weatherCondition.equals("Mud")){
                factor = 0.04;
            }
            else if(weatherCondition.equals("Rain")){
                factor = 0.07;
            }
            else if(weatherCondition.equals("Snow")){
                factor = 0.1;
            }

            if (Math.random() < (factor*theHorse.getConfidence()*theHorse.getConfidence()))
            {
                theHorse.fall();
                System.out.println(theHorse.getName() + " has fallen!");
            }
        }
    }


    /***
     * Print the race on the terminal
     */
    private void printRace() {
        RacePanel.removeAll();
        RacePanel.setLayout(new BoxLayout(RacePanel, BoxLayout.Y_AXIS));

        Color textColor = AALibrary.getColorFromString(fontColour);
        Font boldFont = new Font("Monospaced", Font.BOLD, 12);  // Only change: Font.BOLD

        // Top border
        JLabel topBorder = new JLabel(new String(new char[raceLength+3]).replace('\0', '='));
        topBorder.setFont(boldFont);
        topBorder.setForeground(textColor);
        RacePanel.add(topBorder);

        // Horse lanes
        for (int i = 0; i < raceHorses.length; i++) {
            if (raceHorses[i] != null) {
                JLabel laneLabel = new JLabel(getLaneString(raceHorses[i]));
                laneLabel.setFont(boldFont);
                laneLabel.setForeground(textColor);
                RacePanel.add(laneLabel);
            } else {
                JLabel emptyLabel = new JLabel(" ");
                emptyLabel.setFont(boldFont);
                emptyLabel.setForeground(textColor);
                RacePanel.add(emptyLabel);
            }
        }

        // Bottom border
        JLabel bottomBorder = new JLabel(new String(new char[raceLength+3]).replace('\0', '='));
        bottomBorder.setFont(boldFont);
        bottomBorder.setForeground(textColor);
        RacePanel.add(bottomBorder);

        // Weather message (now bold)
        JLabel weatherLabel;
        if (weatherCondition.equals("Mud")) {
            weatherLabel = new JLabel("The weather is kind of muddy today...");
        } else if (weatherCondition.equals("Rain")) {
            weatherLabel = new JLabel("It is raining today");
        } else if (weatherCondition.equals("Snow")) {
            weatherLabel = new JLabel("There is snow on the track!");
        } else {
            weatherLabel = new JLabel("The weather is normal today!");
        }
        weatherLabel.setFont(boldFont);  // Bold weather text
        weatherLabel.setForeground(textColor);
        RacePanel.add(weatherLabel);

        RacePanel.revalidate();
        RacePanel.repaint();
    }

    private String getLaneString(Horse theHorse) {
        int spacesBefore = theHorse.getDistanceTravelled();
        int spacesAfter = raceLength - theHorse.getDistanceTravelled();

        StringBuilder lane = new StringBuilder();
        lane.append('|');
        lane.append(new String(new char[spacesBefore]).replace('\0', ' '));
        lane.append(theHorse.hasFallen() ? '☠' : theHorse.getSymbol());
        lane.append(new String(new char[spacesAfter]).replace('\0', ' '));
        lane.append('|');
        lane.append(" ").append(theHorse.getName())
                .append(" (Confidence: ").append(String.format("%.2f", theHorse.getConfidence())).append(")");

        return lane.toString();
    }


    public static String FindWeatherFactor(){
        double val =Math.round(Math.random()*100)/100.0;
        if(val >= 0.0 && val < 0.25){
            return "None";
        }
        else if( val >= 0.25 && val < 0.65){
            return "Mud";
        }
        else if(val >= 0.65 && val <= 0.95){
            return "Rain";
        }
        else{
            return "Snow";
        }
    }

    /**
     * Update the confidence of winning and loosing horses.
     * @param RaceHorses tbe horses of the race
     * @param Winner the winner of the race, if any
     */
    private void UpdateConfidence(Horse[] RaceHorses, Horse Winner){

        if(Winner != (null)) { //not always a winner
            double factor = Math.round(Math.random() * 0.1 * 100.0) / 100.0;
            double winnersConf = Winner.getConfidence() + factor; //slight increase
            Winner.setConfidence(winnersConf);
        }

        for (Horse x : RaceHorses) {
            if(x != Winner && x != null) {
                double factor = Math.round(Math.random() * 0.1 * 100.0) / 100.0;
                double losersConf = x.getConfidence() - factor;
                x.setConfidence(losersConf);
            }
        }
    }


    private boolean isRaceFinished(Horse[] x){
        for(Horse x1 : x) {
            if(x1 == null){
                continue;
            }
            if(!x1.hasFallen()){
                return false;
            }
        }
        return true;
    }

    /**
     * Method to execute at the end of any race
     * @param RaceHorses
     */

    private void FinishedRace(Horse[] RaceHorses){
        //Reset horses
        for (Horse x : RaceHorses) {
            if(x != null) {
                x.resetHorse();
            }
        }
    }

    public void closeUI(){
        if (RaceTimer != null && RaceTimer.isRunning()) {
            RaceTimer.stop();
        }
        if (RacePanel != null) {
            RacePanel.removeAll();
            RacePanel.revalidate();
            RacePanel.repaint();
        }
        if (RaceMenu != null) {
            RaceMenu.dispose();
            RaceMenu = null;
        }

        Main.RTMM();
    }

    public void RaceFinishedUI(){
        JFrame statsFrame = new JFrame("Game Statistics");
        statsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        statsFrame.setSize(900, 400);
        statsFrame.setLayout(new BorderLayout());

        // ==================== TITLE PANEL ====================
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setPreferredSize(new Dimension(0, 100)); // Height of title panel

        JLabel titleLabel = new JLabel("Race Statistics", SwingConstants.CENTER); // Center aligned
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setOpaque(false);
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        if(stats.getWinner() != null){
            JLabel winnerLabel = new JLabel("Winner: " + stats.getWinner().getName(), SwingConstants.CENTER); // Center aligned
            winnerLabel.setFont(new Font("Arial", Font.BOLD, 20));
            winnerLabel.setForeground(Color.BLACK);
            winnerLabel.setOpaque(false);
            titlePanel.add(winnerLabel, BorderLayout.SOUTH); // Add to panel, not to another label
        }
        else{
            JLabel winnerLabel = new JLabel("There was no winner this game", SwingConstants.CENTER); // Center aligned
            winnerLabel.setFont(new Font("Arial", Font.BOLD, 12));
            winnerLabel.setForeground(Color.BLACK);
            winnerLabel.setOpaque(false);
            titlePanel.add(winnerLabel, BorderLayout.SOUTH); // Add to panel, not to another label
        }

        // ==================== TABLE PANEL ====================
        // Column headers
        String[] columns = {"Lane", "Symbol", "Name", "Confidence", "Avg Speed", "Time Taken"};

        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        // Add sorted horse data
        Arrays.stream(stats.getHorses())
                .filter(Objects::nonNull)
                .sorted(Comparator.comparingInt(HorseData::getLane))
                .forEach(horse -> model.addRow(new Object[]{
                        horse.getLane(),
                        horse.getSymbol(),
                        horse.getName(),
                        String.format("%.2f", horse.getConfidence()),
                        String.format("%.2f", horse.getAverageSpeed()),
                        String.format("%.2f", horse.getTimeTaken())
                }));

        JTable table = new JTable(model);
        table.setAutoCreateRowSorter(true);
        table.setRowHeight(30);
        table.getColumnModel().getColumn(0).setMaxWidth(60);

        JScrollPane tableScroll = new JScrollPane(table);

        // ==================== BUTTON PANEL ====================
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton raceAgainBtn = new JButton("Race Again");
        raceAgainBtn.setFont(new Font("Arial", Font.BOLD, 16));
        raceAgainBtn.addActionListener(e -> {
            statsFrame.dispose();
            startRace();
        });

        JButton mainMenuBtn = new JButton("Return to Main Menu");
        mainMenuBtn.setFont(new Font("Arial", Font.BOLD, 16));
        mainMenuBtn.addActionListener(e -> {
            statsFrame.dispose();
            closeUI();

        });

        buttonPanel.add(raceAgainBtn);
        buttonPanel.add(mainMenuBtn);

        // ==================== ASSEMBLE FRAME ====================
        statsFrame.add(titlePanel, BorderLayout.NORTH);
        statsFrame.add(tableScroll, BorderLayout.CENTER);
        statsFrame.add(buttonPanel, BorderLayout.SOUTH);

        // Center and show
        statsFrame.setLocationRelativeTo(RaceMenu);
        statsFrame.setVisible(true);
    }

    public static void RaceAgain(){

    }
}
