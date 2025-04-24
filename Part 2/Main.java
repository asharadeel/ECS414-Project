import java.awt.*;
import java.util.*;
import javax.swing.*;

/**
 * Main Method to execute the Horse Race in terminal
 *
 * @author ashar
 * @version 1.6
 */
public class Main {
    static Horse[] raceHorses; // Stores horses from GetRaceInfoUI


    public static void main(String[] args) {


        Scanner s = new Scanner(System.in);

        boolean sameHorses = false;
        Horse[] repeatHorses = new Horse[0]; //for repeated races


        MainMenu.showUI();

    }

    // Called when GetRaceInfoUI confirms horses
    public static void startRace(Horse[] horses) {
        raceHorses = horses;
        System.out.println("Race started with horses:");
        Race race = new Race(100);

        int pos = 0;
        for (Horse horse : raceHorses) {
            if (horse != null) {
                System.out.println(horse.getName());
                race.addHorse(horse,pos+1);
            }
            pos++;
        }

        race.startRace();
    }



    




}