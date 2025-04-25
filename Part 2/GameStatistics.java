public class GameStatistics {
    private int raceLength;
    private HorseData[] RaceHorseData = new HorseData[0];
    private Horse winner;
    private int time = 0;
    private boolean raceFinished;

    //declaration
    GameStatistics(int distance, Horse[] horses) {
        this.raceLength = distance;
        for(Horse x: horses){
            if(x != null) {
                HorseData current = new HorseData(x);
                RaceHorseData = AALibrary.appendValue(RaceHorseData, current);
            }
            else{
                RaceHorseData = AALibrary.appendValue(RaceHorseData, null);
            }
        }
        raceFinished = false;

        System.out.println("- GAME STATS STARTED FOR RACE  -----------------------");
        System.out.println("Race Length: " + raceLength);
        for(HorseData x : RaceHorseData){
            if(x != null) {
                System.out.println(x.getSymbol() + " | Name: " + x.getName() + " | Conf: " + x.getConfidence());
            }
        }
        System.out.println("------------------------------------------------------");
    }

    public HorseData[] getHorses(){
        return RaceHorseData;
    }

    public void finished(){
        if(raceFinished){
            AALibrary.printHorses(RaceHorseData);
        }
    }
    //accessors and mutators
    public void setWinner(Horse h) {
        winner = h;
    }

    public void setFinished(){
        raceFinished = true;
    }

    /*
    Active detection during game to track statistcs.
     */
    public void detect(){
        detectFall();
        trackTime();

        System.out.println("detect()");
    }



    private void detectFall(){
        for(int i = 0; i < 10; i++){
            try {
                if (RaceHorseData[i] != null) {
                    if (RaceHorseData[i].hasFallen() && !RaceHorseData[i].isHorseChecked()) {
                        HorseData current = RaceHorseData[i];
                        double avg = calculateAvgSpeed(current, time);
                        current.setTimeTaken(time);
                        current.setAverageSpeed(avg);
                        current.setHorseChecked(true);
                    }
                }
            }
            catch(Exception e){
                System.out.println(e);
            }
        }
    }

    //maths
    public void trackTime(){
        time++;
    }


    public static double FindWeatherFactor(){
        int speedpercentage = 100;
        for(int i = 0; i< 20; i++){
            //System.out.println(Math.random());
            long val =Math.round(Math.random()*100)%5;
            if(val < 1){
                speedpercentage--;
                continue;
            }
            else if(val<2){
                speedpercentage-= 3;
                continue;
            }
            else if(val <3){
                speedpercentage-= 6;
            }
        }
        return speedpercentage/100.0;
    }




    public static double calculateAvgSpeed(Horse h, int t){
        double x = (double) h.getDistanceTravelled();
        return (Math.round((x/t*10.0)*10.0)/2.0);
    }





}
