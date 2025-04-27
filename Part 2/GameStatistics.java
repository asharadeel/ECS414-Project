import java.io.IOException;


/**
 * Review the game stats for any race that has happened. Essential for statistics and bets of a race.
 * @author ashar
 * @version 1.7
 */

public class GameStatistics {
    //Declarations
    private int raceLength;
    private HorseData[] RaceHorseData = new HorseData[0];
    private Horse winner;
    private int time = 0;
    private boolean raceFinished;
    private String weatherCondition;


    /**
     * Create the game statistics using same initialisation as the race
     * @param distance - the length of the race
     */
    GameStatistics(int distance) {
        this.raceLength = distance;
       raceFinished = false;
    }

    // =================== ACCESSORS ===================

    /**
     * Return the length of a race
     * @return
     */
    public int getRaceLength() {
        return raceLength;
    }

    /**
     * Retrieve the horses of a race
     * @return
     */
    public HorseData[] getHorses(){
        return RaceHorseData;
    }

    /**
     * Retrieve the time taken in a race
     * @return
     */
    public int getTime() {
        return time;
    }

    /**
     * Retrieve the winner of the race (if none then null)
     * @return
     */
    public Horse getWinner() {
        return winner;
    }

    /**
     * Retrieve the weather condition
     * @return
     */
    public String getWeatherCondition() {
        return weatherCondition;
    }

    /**
     * Set the winner of the race
     * @param h
     */
    public void setWinner(Horse h) {
        winner = h;
        HorseData w = new HorseData(h);
    }

    /**
     * Set the weather condition of the current race
     * @param weatherCondition
     */
    public void setWeatherCondition(String weatherCondition) {
        this.weatherCondition = weatherCondition;
    }

    /**
     * Set the game finished to be true if race is completed
     */
    public void setFinished(){
        raceFinished = true;
    }

    /**
     * Check if race is finished. If it is, then update the save system with new data of the horses.
     * @throws IOException - handle any IO errors
     */
    public void finished() throws IOException {
        setFinished();
        if(raceFinished){
            AALibrary.printHorses(RaceHorseData);
        }
        SaveSystem.Save(this);
    }


    /**
     * Check if a horse has finished the race, then create the horse data class for it and add it to array.
     * Update values for the horse, like find average speed, time taken and distance travelled.
     * Print for debugging.
     * @param h - the horse
     * @param index - the lane the horse is in.
     */
    public void HorseFinished(Horse h, int index) {
        //check if the horse exists first.
        boolean horseExists = false;
        for (HorseData b : RaceHorseData) {
            if (b != null && b.getSymbol() == h.getSymbol()) {
                horseExists = true;
                break;
            }
        }

        // Only create new HorseData if it doesn't exist
        if (!horseExists) {
            HorseData x = new HorseData(h);
            x.setDistanceTravelled(h.getDistanceTravelled());
            double avg = calculateAvgSpeed(x, time);
            x.setHorseChecked(true);
            x.setTimeTaken(time);
            x.setAverageSpeed(avg);
            x.setLane(index+1);
            System.out.println("avg " + avg);
            System.out.println("time " + time);
            System.out.println("index " + index);
            RaceHorseData = AALibrary.appendValue(RaceHorseData, x);
            System.out.println("HorseData made for " + x.getName());
        }
    }


    /**
     * Track the time of the race
     */
    public void trackTime(){
        time++;
    }

    /**
     * Calculate the vaerage speed of the horse during the race
     * @param h - the horse
     * @param t - the time at which the horse fell
     * @return - horse.distance / time
     */
    public static double calculateAvgSpeed(Horse h, int t){
        double x = (double) h.getDistanceTravelled();
        return (Math.round((x/t*10.0)*10.0)/2.0);
    }
}
