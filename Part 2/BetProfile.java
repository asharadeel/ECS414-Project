public class BetProfile {
    private HorseData horse;
    private User user;

    private int expectedTime = 0;
    private int expectedAvg = 0;
    private int expectedDistance = 0;

    private int[] expectedTimeR = {0,0};
    private int[] expectedAvgR = {0,0};
    private int[] expectedDistanceR = {0,0};

    public void view(){
        System.out.println("EXCEED: " +  expectedTime);
        System.out.println("EXCEED: " +  expectedAvg);
        System.out.println("EXCEED: " +  expectedDistance);

        System.out.println("REGION: " +  expectedTimeR[0] + "," +  expectedTimeR[1]);
        System.out.println("REGION: " +  expectedAvgR[0] + "," +  expectedAvgR[1]);
        System.out.println("REGION: " +  expectedDistanceR[0] + "," +  expectedDistanceR[1]);



    }

    BetProfile(User current, HorseData h){
        this.user = current;
        this.horse = h;
    }

    public void BetDistance(int d){
        expectedDistance = d;
    }

    public void BetDistance(int d, int e){
        expectedDistanceR[0] = d;
        expectedDistanceR[1] = e;
    }

    public void BetTime(int t){
        expectedTime = t;
    }

    public void BetTime(int t, int a){
        expectedTimeR[0] = t;
        expectedTimeR[1] = a;
    }

    public void BetAvgSpeed(int d){
        expectedAvg = d;
    }

    public void BetAvgSpeed(int d, int e){
        expectedAvgR[0] = d;
        expectedAvgR[1] = e;
    }



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
                user.addPoints(100);
                return true;
            }
            else return false;
        }
    }

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
                user.addPoints(100);
                return true;
            }
            else return false;
        }
    }


    public boolean CheckTimeBets(){
        if(expectedTimeR[0] == 0 && expectedTimeR[1] == 0) {
            if(horse.getTimeTaken() >= expectedTime){
                user.addPoints(75);
                return true;
            }
            else return false;
        }
        else{
            if(horse.getTimeTaken() >= expectedTimeR[0] && horse.getTimeTaken() <= expectedTimeR[1]){
                user.addPoints(100);
                return true;
            }
            else return false;
        }
    }




}
