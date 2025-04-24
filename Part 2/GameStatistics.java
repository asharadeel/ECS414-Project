public class GameStatistics {
    private int raceLength;
    private HorseData[] RaceHorseData = new HorseData[0];
    private Horse winner;
    private int time = 0;
    private boolean raceStatus;

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

        System.out.println("Race Length: " + raceLength);
        for(HorseData x : RaceHorseData){
            if(x != null) {
                System.out.println(x.getName());
            }
        }
    }

    //accessors and mutators
    public void setWinner(Horse h) {
        winner = h;
    }

    /*
    Active detection during game to track statistcs.
     */
    public void activeDetect(){
        detectFall();
        trackTime();

        System.out.println("detect()");
    }



    private void detectFall(){
        for(int i = 0; i < raceLength; i++){
            if(RaceHorseData[i] != null) {
                if(RaceHorseData[i].hasFallen() && !RaceHorseData[i].isHorseChecked()){
                    HorseData current = RaceHorseData[i];
                    double avg = calculateAvgSpeed(current,time);
                    current.setTimeTaken(time);
                    current.setAverageSpeed(avg);
                    current.setHorseChecked(true);
                }
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
