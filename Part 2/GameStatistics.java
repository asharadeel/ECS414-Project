import java.io.IOException;

public class GameStatistics {
    private int raceLength;
    private HorseData[] RaceHorseData = new HorseData[0];
    private Horse winner;
    private int time = 0;
    private boolean raceFinished;
    private String weatherCondition;


    //declaration
    GameStatistics(int distance) {
        this.raceLength = distance;
       raceFinished = false;
    }

    public HorseData[] getHorses(){
        return RaceHorseData;
    }

    public void finished() throws IOException {
        setFinished();
        if(raceFinished){
            AALibrary.printHorses(RaceHorseData);
        }
        SaveSystem.Save(this);
    }
    //accessors and mutators
    public void setWinner(Horse h) {
        winner = h;
        HorseData w = new HorseData(h);
    }
    public String getWeatherCondition() {
        return weatherCondition;
    }
    public void setWeatherCondition(String weatherCondition) {
        this.weatherCondition = weatherCondition;
    }

    public Horse getWinner() {
        return winner;
    }

    public void setFinished(){
        raceFinished = true;
    }


    public void HorseFinished(Horse h, int index) {
        // First check if this horse already exists in RaceHorseData
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


    //maths
    public void trackTime(){
        time++;
    }

    public static double calculateAvgSpeed(Horse h, int t){
        double x = (double) h.getDistanceTravelled();
        return (Math.round((x/t*10.0)*10.0)/2.0);
    }
}
