import java.util.concurrent.TimeUnit;
import java.lang.Math;
import java.util.*;

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
     * Adds a horse to the race in a given lane
     *
     * @param theHorse the horse to be added to the race
     * @param laneNumber the lane that the horse will be added to
     */
    public void addHorse(Horse theHorse, int laneNumber) {
        raceHorses = appendValue(raceHorses,theHorse);
    }

    /**
     * Add a value to a classic array
     * @param arr - The array to manipulate
     * @param value - The value to add
     * @return - return new size array
     * @param <T> - made for any type for future use too
     */
    public <T> T[] appendValue (T[] arr, T value){
        if (arr == null) {
            throw new IllegalArgumentException("Input array cannot be null");
        }

        T[] newArray = Arrays.copyOf(arr, arr.length + 1);
        newArray[arr.length] = value;
        return newArray;
    }

    /**
     * The main of this race
     */
    public void startRace()
    {
        //declare a local variable to tell us when the race is finished and a horse for the winner place
        Horse winner = null;
        boolean finished = false;

       Horse[] currentRace=  raceHorses; //make a copy of all the horses

        //reset all horses to 0;
        for(Horse x : currentRace){
            x.goBackToStart();
        }

        //The race
        while (!finished)
        {
            //move each horse IF still standing
            for(Horse x: currentRace){
                if(!x.hasFallen()) {
                    moveHorse(x);
                }
            }

            // Check race completion conditions
            boolean allFell = true;
            for (Horse x : raceHorses) {
                if (raceWonBy(x)) {
                    finished = true;
                    winner = x;
                    break;
                }
                if (!x.hasFallen()) {
                    allFell = false;
                }
            }

            // If all horses fell, end the race
            if (allFell) {
                finished = true;
            }

            //print the race positions
            printRace();

            //wait for 100 milliseconds
            try{
                TimeUnit.MILLISECONDS.sleep(100);
            }catch(Exception e){}

        }
        if(finished){
            if(winner != null){
                System.out.println("The winner is " + winner.getName() + ", " + winner.getSymbol() + "!");
            }
            else{
                System.out.println("They all fell :/");
            }
            //updates and modifications based on race finish
            UpdateConfidence(currentRace, winner);
            FinishedRace(currentRace);

        }
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
            if (Math.random() < (theHorse.getConfidence()))
            {
                theHorse.moveForward();
            }

            //the probability that the horse will fall is very small (max is 0.1)
            //but will also will depends exponentially on confidence
            //so if you double the confidence, the probability that it will fall is *2
            
            if (Math.random() < (0.1*theHorse.getConfidence()*theHorse.getConfidence()))
            {
                theHorse.fall();
                System.out.println(theHorse.getName() + " has fallen!");
            }
        }
    }


    /***
     * Print the race on the terminal
     */
    private void printRace()
    {
        System.out.print('\u000C');  //clear the terminal window

        multiplePrint('=',raceLength+3); //top edge of track
        System.out.println();


        //print each horse
        for(int i =0; i<raceHorses.length; i++){
            printLane(raceHorses[i]);
            System.out.println(); //gap between horses
        }

        multiplePrint('=',raceLength+3); //bottom edge of track
        System.out.println();
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
        for (Horse x : RaceHorses) {
            x.resetHorse();
        }
    }
}
