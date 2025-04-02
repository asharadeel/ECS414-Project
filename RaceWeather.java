import java.util.concurrent.TimeUnit;

public class RaceWeather extends Race{


    private int raceLength;
    private Horse lane1Horse;
    private Horse lane2Horse;
    private Horse lane3Horse;

    //weather conditions
    private boolean rain;
    private boolean wind;
    private boolean snow;

    /**
     * Constructor for objects of class Race
     * Initially there are no horses in the lanes
     *
     * @param distance the length of the racetrack (in metres/yards...)
     */
    public RaceWeather(int distance) {
        super(distance);
    }

    @Override
    public void startRace()
    {}
}
