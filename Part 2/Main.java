
import java.util.*;


/**
 * Main Method to execute the Horse Race in terminal
 *
 * @author ashar
 * @version 1.6
 */
public class Main {
    static Horse[] raceHorses; // Stores horses from GetRaceInfoUI


    public static void main(String[] args) {
        MainMenu.showUI();

    }

    // Called when GetRaceInfoUI confirms horses
    public static void startRace(RaceData dataR) {
        raceHorses = dataR.horses;
        System.out.println("Race started with horses:");
        Race race = new Race(dataR.RaceLength);
        race.setColours(dataR.fontColour, dataR.backgroundColour);
        int pos = 0;
        for (Horse horse : raceHorses) {
            if (horse != null) {
                System.out.println(horse.getName());
            }
            race.addHorse(horse,pos+1);
            pos++;
        }

        race.startRace();
    }

    public static void RTMM(){
        MainMenu.showUI();
    }








}