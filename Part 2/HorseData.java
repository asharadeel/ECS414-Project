public class HorseData extends Horse{
    private double averageSpeed;
    private double timeTaken;
    private boolean winner;


    /**
     * Create a horse.
     * @param horse
     */
    public HorseData(Horse horse) {
        super(horse.getSymbol(), horse.getName(), horse.getConfidence());
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

    /**
     * Checks if the horse is a winner
     * @return true if the horse won the race, false otherwise
     */
    public boolean isWinner() {
        return this.winner;
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

    /**
     * Sets the winner status of the horse
     * @param winner true if the horse won, false otherwise
     */
    public void setWinner(boolean winner) {
        this.winner = winner;
    }
}
