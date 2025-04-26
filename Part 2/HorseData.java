public class HorseData extends Horse{
    private int lane;
    private double averageSpeed;
    private double timeTaken;
    private boolean horseChecked;


    /**
     * Create a horse.
     * @param horse
     */
    public HorseData(Horse horse) {
        super(horse.getSymbol(), horse.getName(), horse.getConfidence());
    }


    //ACCESSORS

    public int getLane(){
        return lane;
    }
    /**
     * Checks if the horse has been verified/inspected
     * @return true if the horse has been checked, false otherwise
     */
    public boolean isHorseChecked() {
        return this.horseChecked;
    }

    /**
     * Returns the average speed of the horse
     * @return the average speed in units per time
     */
    public double getAverageSpeed() {
        return this.averageSpeed;
    }

    /**
     * Returns the time taken by the horse to complete the race
     * @return the time taken in seconds (or appropriate time unit)
     */
    public double getTimeTaken() {
        return this.timeTaken;
    }

    //MUTATORS

    /**
     * Sets the verification status of the horse
     * @param horseChecked true if the horse has been verified, false otherwise
     */
    public void setHorseChecked(boolean horseChecked) {
        this.horseChecked = horseChecked;
    }
    /**
     * Sets the average speed of the horse
     * @param averageSpeed the average speed to set (in units per time)
     */
    public void setAverageSpeed(double averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    /**
     * Sets the time taken by the horse to complete the race
     * @param timeTaken the time taken to set (in seconds or appropriate time unit)
     */
    public void setTimeTaken(double timeTaken) {
        this.timeTaken = timeTaken;
    }


    public void setLane(int lane) {
        this.lane = lane;
    }

    public void setDistanceTravelled(int distanceTravelled) {
        super.distance = distanceTravelled;
    }

}
