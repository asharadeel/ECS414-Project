/**
 * The Betting Profile for each horse - save all bets made on the horse, and values
 */
public class BetProfile {
    private User user;
    int points = 0;

    //DECLARE THE HORSE TO BE SAVED
    private HorseData horse;
    private boolean valid = false;
    //BET VALUES
    private int expectedTime = 0;
    private int expectedAvg = 0;
    private int expectedDistance = 0;

    private int[] expectedTimeR = {0,0};
    private int[] expectedAvgR = {0,0};
    private int[] expectedDistanceR = {0,0};



    /**
     * Constructor for betting profile
     * @param current - the user
     * @param h - the horse to bet upon
     */
    BetProfile(User current, HorseData h){
        this.user = current;
        this.horse = h;
    }

    /**
     * Bet on the minimum distance ran by horse
     * @param d
     */
    public void BetDistance(int d){
        expectedDistance = d;
    }

    /**
     * Bet the user will get value within this region.
     * @param d -min
     * @param e -max
     */
    public void BetDistance(int d, int e){
        expectedDistanceR[0] = d;
        expectedDistanceR[1] = e;
    }

    /**
     * Place a bet on the minimum time the horse will take
     * @param t
     */
    public void BetTime(int t){
        expectedTime = t;
    }

    /**
     * Place a bet on the time horse will (region)
     * @param t
     * @param a
     */
    public void BetTime(int t, int a){
        expectedTimeR[0] = t;
        expectedTimeR[1] = a;
    }

    /**
     * Place a bet on the horses minimum average speed
     * @param d
     */
    public void BetAvgSpeed(int d){
        expectedAvg = d;
    }

    /**
     * Place a bet on the horses average speed to land within this region.
     * @param d
     * @param e
     */
    public void BetAvgSpeed(int d, int e){
        expectedAvgR[0] = d;
        expectedAvgR[1] = e;
    }

    /**
     * Check if the average speed check was successful
     * - if horses speed was MORE than the expected, then win, else fail.
     * - if the horses speed lands within the region (min and max) we set, then win, else fail.
     * @return - true if bet won, else false
     */
    public boolean CheckAverageBets(){
        if(expectedAvgR[0] == 0 && expectedAvgR[1] == 0) {
            if (horse.getAverageSpeed() >= expectedAvg){
                user.addPoints(75);
                return true;
            }
            else return false;
        }
        else{
            if(horse.getAverageSpeed() >= expectedAvgR[0] && horse.getAverageSpeed() <= expectedAvgR[1]){
                user.addPoints(75/(expectedAvgR[1] - expectedAvgR[0]));
                return true;
            }
            else return false;
        }
    }

    /**
     * if the horses distance exceeds what we bet, then win, else fail
     * if the horses distance travelled is within defiend region ,then win, else fail.
     * @return
     */
    public boolean CheckDistanceBets(){
        if(expectedDistanceR[0] == 0 && expectedDistanceR[1] == 0) {
            if (horse.getDistanceTravelled() >= expectedDistance){
                user.addPoints(75);
                return true;
            }
            else return false;
        }
        else{
            if(horse.getDistanceTravelled() >= expectedDistanceR[0] && horse.getDistanceTravelled() <= expectedDistanceR[1]){
                user.addPoints(75/(expectedDistanceR[1] - expectedDistanceR[0]));
                return true;
            }
            else return false;
        }
    }

    /**
     * If the time taken is less than what user bet, then win, else fail
     * If the time falls within define region, then win else fail.
     * @return
     */
    public boolean CheckTimeBets(){
        if(expectedTimeR[0] == 0 && expectedTimeR[1] == 0) {
            if(horse.getTimeTaken() <= expectedTime){
                user.addPoints(75);
                return true;
            }
            else return false;
        }
        else{
            if(horse.getTimeTaken() >= expectedTimeR[0] && horse.getTimeTaken() <= expectedTimeR[1]){
                user.addPoints(75/(expectedTimeR[1] - expectedTimeR[0]));
                return true;
            }
            else return false;
        }
    }

    /**
     * For debugging - view all values of bet profile
     */
    public void view(){
        System.out.println("BET PROFILE FOR " + horse.getSymbol() + " | " + horse.getName());
        System.out.println("EXCEED: " +  expectedTime);
        System.out.println("EXCEED: " +  expectedAvg);
        System.out.println("EXCEED: " +  expectedDistance);

        System.out.println("REGION: " +  expectedTimeR[0] + "," +  expectedTimeR[1]);
        System.out.println("REGION: " +  expectedAvgR[0] + "," +  expectedAvgR[1]);
        System.out.println("REGION: " +  expectedDistanceR[0] + "," +  expectedDistanceR[1]);
        System.out.println("----------------------------------------------------------------");
    }



    public HorseData getHorseData(){
        return horse;
    }

    public void setHorse(HorseData h){
        this.horse = h;
    }

    public void valid(){
        this.valid = true;
    }


    public int[] GetTimeBets() {
        if (expectedTimeR[0] != 0 || expectedTimeR[1] != 0) {
            return expectedTimeR; // Return the range if it exists
        } else if (expectedTime != 0) {
            return new int[]{expectedTime}; // Return single value
        } else {
            return new int[]{-1}; // No bet placed
        }
    }

    public int[] GetDistanceBets() {
        if (expectedDistanceR[0] != 0 || expectedDistanceR[1] != 0) {
            return expectedDistanceR;
        } else if (expectedDistance != 0) {
            return new int[]{expectedDistance};
        } else {
            return new int[]{-1};
        }
    }

    public int[] GetAverageSpeed() {
        if (expectedAvgR[0] != 0 || expectedAvgR[1] != 0) {
            return expectedAvgR;
        } else if (expectedAvg != 0) {
            return new int[]{expectedAvg};
        } else {
            return new int[]{-1};
        }
    }

}
