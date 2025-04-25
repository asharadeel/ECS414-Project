
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;

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

    public void setColours(String f, String b){
        fontColour = f;
        backgroundColour = b;
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

        int frameWidth = raceLength*10;
        RaceMenu.setSize(frameWidth, 300);

        RaceMenu.setBackground(AALibrary.getColorFromString(backgroundColour));

        RacePanel = new JPanel();
        RacePanel.setLayout(new BoxLayout(RacePanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(RacePanel);
        RaceMenu.add(scrollPane);
        RaceMenu.setVisible(true);
        RaceMenu.setLocationRelativeTo(null);
        RacePanel.setOpaque(true);
        RacePanel.setBackground(Color.WHITE);


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
        stats = new GameStatistics(raceLength, raceHorses);

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
                Horse winner = null;

                // Move each horse
                for(Horse x : raceHorses) {
                    if(x != null && !x.hasFallen()) {
                        moveHorse(x);
                        stats.detect();
                    }
                }

                // Check race completion
                boolean allFell = true;
                for (Horse x : raceHorses) {
                    if(x == null) continue;
                    if (raceWonBy(x)) {
                        finished = true;
                        winner = x;
                        break;
                    }
                    if (!x.hasFallen()) allFell = false;
                }

                // Update display
                printRace();
                RacePanel.revalidate();
                RacePanel.repaint();

                // Check if race finished
                if (finished || allFell) {
                    stats.setFinished();
                    stats.finished();

                    AALibrary.printHorses(raceHorses);
                    ((Timer)e.getSource()).stop();
                    if (winner != null) {
                        System.out.println("Winner = " + winner.getName());
                        JOptionPane.showMessageDialog(Race.this.RaceMenu, "The winner is " + winner.getName() + "!");
                    } else {
                        JOptionPane.showMessageDialog(Race.this.RaceMenu, "All horses fell!");
                    }


                    RaceFinishedUI(stats);
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
        RacePanel.removeAll(); // Clear previous content
        RacePanel.setLayout(new BoxLayout(RacePanel, BoxLayout.Y_AXIS));

        // Add top border
        RacePanel.add(new JLabel(new String(new char[raceLength+3]).replace('\0', '=')));

        // Print each horse's lane
        for(int i = 0; i < raceHorses.length; i++) {
            if(raceHorses[i] != null) {
                JLabel laneLabel = new JLabel(getLaneString(raceHorses[i]));
                laneLabel.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
                RacePanel.add(laneLabel);
            } else {
                RacePanel.add(new JLabel(" "));
            }
        }

        // Add bottom border
        RacePanel.add(new JLabel(new String(new char[raceLength+3]).replace('\0', '=')));

        RacePanel.add(new JLabel(""));

        if(weatherCondition.equals("Mud")) {
            RacePanel.add(new JLabel(new String("The weather is kind of muddy today...")));
        }
        else if(weatherCondition.equals("Rain")) {
            RacePanel.add(new JLabel(new String("It is raining today")));
        }
        else if(weatherCondition.equals("Snow")) {
            RacePanel.add(new JLabel(new String("There is snow on the track!")));

        }

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

    /**
     * print a horse's lane during the race
     * for example
     * |           X                      |
     * to show how far the horse has run
     */
    private void printLane(Horse theHorse)
    {
        //calculate how many spaces are needed before
        //and after the horse
        int spacesBefore = theHorse.getDistanceTravelled();
        int spacesAfter = raceLength - theHorse.getDistanceTravelled();

        //print a | for the beginning of the lane
        System.out.print('|');

        //print the spaces before the horse
        multiplePrint(' ',spacesBefore);

        //if the horse has fallen then print dead
        //else print the horse's symbol
        if(theHorse.hasFallen())
        {
            System.out.print('☠');
        }
        else
        {
            System.out.print(theHorse.getSymbol());
        }

        //print the spaces after the horse
        multiplePrint(' ',spacesAfter);

        //print the | for the end of the track
        System.out.print('|');

        System.out.println(theHorse.getName() + " (Confidence: " + theHorse.getConfidence() + ")");

    }

    /***
     * print a character a given number of times.
     * e.g. printmany('x',5) will print: xxxxx
     *
     * @param aChar the character to Print
     */
    private void multiplePrint(char aChar, int times)
    {
        int i = 0;
        while (i < times)
        {
            System.out.print(aChar);
            i = i + 1;
        }
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



    /**
     * Method to execute at the end of any race
     * @param RaceHorses
     */

    private void FinishedRace(Horse[] RaceHorses){
        //Reset horses
        stats.finished();
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

    public static void RaceFinishedUI(GameStatistics stats){
        JFrame RaceStatus = new JFrame("Race Status");



    }
    public static void RaceAgain(){

    }
}
