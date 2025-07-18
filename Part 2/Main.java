/**
 * Main Method to execute the Horse Race in terminal
 *
 * @author ashar
 * @version 2.1
 */
public class Main {
    static Horse[] raceHorses;
    static Better Bets;
    static RaceData previousData;

    public static void main(String[] args) {
        GlobalStyler.applyGlobalStyle();


        MainMenu.showUI();
    }


    /**
     * Start a Race, using the Race class
     * @param dataR - Normally used for when "race again" is used, to use previous data.
     */
    public static void startRace(RaceData dataR) {
        previousData= dataR;
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
    //Race using previous variables
    public static void startRace() {
        RaceData dataR = previousData;
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

    /**
     * Open Return to Main Menu Window.
     */
    public static void RTMM(){
        MainMenu.showUI();
    }


    /**
     * This is for starting a new race, which will use bets.
     * @param dataR
     */
    public static void StartBets(RaceData dataR){
        previousData= dataR;
        Bets = new Better(dataR);
    }
    //For repeated race with new bets
    public static void StartBets(){
        RaceData dataR = previousData;
        Bets = new Better(dataR);
    }

    /**
     * Review the bets after game is finished.
     * @param stats - race data
     */
    public static void ReviewBets(GameStatistics stats){
        Bets.ReviewBets(stats);
    }

    /**
     * Update the bets to match data of the stats of  the current race.
     * @param stats - race data
     */
    public static void UpdateBets(GameStatistics stats){
        Bets.UpdateBets(stats);
    }

    /**
     * Reset all after a race is finished, or if race ended abruptly.
     */
    public static void reset(){
        raceHorses = null;
        Bets = null;
    }
}