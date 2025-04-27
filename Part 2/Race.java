import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

/**
 * A multi-horse (2-10) race using interface
 *
 * @author McRaceface, ashar
 * @version 3.2
 */


public class Race
{
    //DECLARATIONS
    private int raceLength;
    private Horse[] raceHorses = new Horse[0];
    private String weatherCondition;

    //INTERFACE
    JFrame RaceMenu;
    JPanel RacePanel;
    private String fontColour;
    private String backgroundColour;

    //OTHER RELATED OBJECTS
    private GameStatistics stats;
    private Timer RaceTimer;


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

    /**
     * Set the colours for styling text foreground and background
     * @param fc
     * @param bc
     */
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

    /**
     * The main race controller
     */
    public void startRace() {
        //CREATE INTERFACE --------------
        RaceMenu = new JFrame("Race!");

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        RaceMenu.setSize((int) screenSize.getWidth(),(int) screenSize.getHeight());
        RaceMenu.setExtendedState(JFrame.MAXIMIZED_BOTH);
        RaceMenu.setUndecorated(true);

        RacePanel = new JPanel();
        RacePanel.setLayout(new BoxLayout(RacePanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(RacePanel);
        RaceMenu.add(scrollPane);
        RaceMenu.setVisible(true);
        RaceMenu.setLocationRelativeTo(null);
        RacePanel.setOpaque(true);
        RacePanel.setBackground(AALibrary.getColorFromString(backgroundColour));

        //MENU BAR ---------------
        JMenuBar menuBar = new JMenuBar();
        JMenu CancelRace = new JMenu("Cancel");
        JMenuItem cancelButton = new JMenuItem("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                closeUI();
                Main.reset();
            }
        });

        JMenu ViewRace = new JMenu("View");
        JMenuItem fsView = new JMenuItem("FullScreen");
        JMenuItem wwView = new JMenuItem("Window");
        fsView.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                RaceMenu.setSize((int) screenSize.getWidth(),(int) screenSize.getHeight());
                RaceMenu.setExtendedState(JFrame.MAXIMIZED_BOTH);
                RaceMenu.setUndecorated(true);
            }
        });
        wwView.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int Twidth = raceLength*11;
                RaceMenu.setSize(Twidth,500);
                RaceMenu.setLocationRelativeTo(null);
            }
        });

        ViewRace.add(fsView);
        ViewRace.add(wwView);
        menuBar.add(ViewRace);
        CancelRace.add(cancelButton);
        menuBar.add(CancelRace);
        RaceMenu.setJMenuBar(menuBar);


        // -------- RACE STARTS HERE
        weatherCondition = FindWeatherFactor();
        stats = new GameStatistics(raceLength);
        stats.setWeatherCondition(weatherCondition);

        // Reset all horses
        for(Horse x : raceHorses) {
            if(x != null) {
                x.goBackToStart();
            }
        }

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

                    AALibrary.printHorses(raceHorses);//PRINT FOR DEBUGGING
                    ((Timer)e.getSource()).stop();

                    Object[] options = {"View Stats"};

                    String message = (winner != null) //message based on outcome
                            ? "The winner is " + winner.getName() + "!"
                            : "All horses fell!";


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

                    // POST RACE METHODS ---

                    RaceMenu.dispose();
                    RaceFinishedUI();

                    UpdateConfidence(raceHorses, winner);
                    FinishedRace(raceHorses);
                    Main.UpdateBets(stats);
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
     * Added factors to take weather into account. if weather is more intense,
     * it will move less, and more chance to fall, else reverse.
     * Intensity goes in following order: Mud -> Rain -> Snow
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

            double successThreshold = theHorse.getConfidence();

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
            //factor will increase chance of fall.
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
     * Print the race repeatedly on UI
     */
    private void printRace() {
        RacePanel.removeAll();
        RacePanel.setLayout(new BoxLayout(RacePanel, BoxLayout.Y_AXIS));

        Color textColor = AALibrary.getColorFromString(fontColour);
        Font boldFont = new Font("Monospaced", Font.BOLD, 14);

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

        // Weather message
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
        weatherLabel.setFont(boldFont);
        weatherLabel.setForeground(textColor);
        RacePanel.add(weatherLabel);

        //repaint and edit each repeat.
        RacePanel.revalidate();
        RacePanel.repaint();
    }

    /**
     * Return the lane, for spaces before and after each text.
     * Replacing previous multipleLane and printLane method.
     * @param theHorse
     * @return
     */
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

    /**
     * Take a random number and choose weather based on return
     * 25% Chance of normal weather
     * 40% Chance of Muddy weather
     * 30% Chance of Rain
     * 5% Chance of Snow
     * @return
     */
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
            winnersConf = Math.min(0.99, winnersConf);  //cap
            Winner.setConfidence(winnersConf);
        }

        for (Horse x : RaceHorses) {
            if(x != Winner && x != null) {
                double factor = Math.round(Math.random() * 0.1 * 100.0) / 100.0;
                double losersConf = x.getConfidence() - factor;
                losersConf = Math.max(0.01, losersConf);
                x.setConfidence(losersConf);
            }
        }
    }


    /**
     * Method to execute at the end of any race
     * @param RaceHorses
     */
    private void FinishedRace(Horse[] RaceHorses){
        for (Horse x : RaceHorses) {
            if(x != null) {
                x.resetHorse();
            }
        }
    }


    /**
     * Close all UI when cancel race is pressed or when race is terminated.
     */
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

    /**
     * Post race method, to open up game statistics.
     */
    public void RaceFinishedUI(){
      //  Main.ReviewBets(stats);

        JFrame statsFrame = new JFrame("Game Statistics");
        statsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        statsFrame.setSize(900, 400);
        statsFrame.setLayout(new BorderLayout());

        // ==================== TITLE PANEL ====================
        JPanel titlePanel = new JPanel(){
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
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setPreferredSize(new Dimension(0, 100));


        JLabel titleLabel = new JLabel("Race Statistics", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setOpaque(false);
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        if(stats.getWinner() != null){
            JLabel winnerLabel = new JLabel("Winner: " + stats.getWinner().getName(), SwingConstants.CENTER);
            winnerLabel.setFont(new Font("Arial", Font.BOLD, 20));
            winnerLabel.setForeground(Color.WHITE);
            winnerLabel.setOpaque(false);
            titlePanel.add(winnerLabel, BorderLayout.SOUTH);
        }
        else{
            JLabel winnerLabel = new JLabel("There was no winner this game", SwingConstants.CENTER);
            winnerLabel.setFont(new Font("Arial", Font.BOLD, 12));
            winnerLabel.setForeground(Color.WHITE);
            winnerLabel.setOpaque(false);
            titlePanel.add(winnerLabel, BorderLayout.SOUTH);
        }

        // ==================== TABLE PANEL ====================
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

        JButton raceAgainBtn = new JButton("Race Again with Same Bets");
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

        JButton newGameWithBets = new JButton("Race Game with New Bets");
        newGameWithBets.setFont(new Font("Arial", Font.BOLD, 16));
        newGameWithBets.addActionListener(e -> {
            statsFrame.dispose();
            Main.StartBets();
        });

        JButton reviewBets = new JButton("Review Bets");
        reviewBets.setFont(new Font("Arial", Font.BOLD, 16));
        reviewBets.addActionListener(e -> {
            Main.ReviewBets(stats);
        });

        buttonPanel.add(raceAgainBtn);
        buttonPanel.add(newGameWithBets);
        buttonPanel.add(reviewBets);
        buttonPanel.add(mainMenuBtn);


        JMenuBar menuBar = new JMenuBar();
        JMenu viewMenu = new JMenu("Statistics");
        JMenuItem viewStats = new JMenuItem("Open Statistics Analysis");
        viewStats.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ViewStatistics view = new ViewStatistics(stats);
                view.showUI();
            }
        });

        viewMenu.add(viewStats);
        menuBar.add(viewMenu);
        statsFrame.setJMenuBar(menuBar);


        // ==================== ASSEMBLE FRAME ====================
        statsFrame.add(titlePanel, BorderLayout.NORTH);
        statsFrame.add(tableScroll, BorderLayout.CENTER);
        statsFrame.add(buttonPanel, BorderLayout.SOUTH);

        // Center and show
        statsFrame.setLocationRelativeTo(RaceMenu);
        statsFrame.setVisible(true);
    }
}
