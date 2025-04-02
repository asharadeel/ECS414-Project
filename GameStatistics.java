public class GameStatistics {
    int raceLength;

    private Horse horse1;
    private Horse horse2;
    private Horse horse3;

    private Horse winner;

    public int time = 0;
    private double h1speed = 0;
    private double h2speed = 0;
    private double h3speed = 0;

    //declaration
    GameStatistics(int distance, Horse h1, Horse h2, Horse h3) {
        this.raceLength = distance;
        this.horse1 = h1;
        this.horse2 = h2;
        this.horse3 = h3;
    }

    GameStatistics(){
        this.raceLength = 0;
        this.horse1 = null;
        this.horse2 = null;
        this.horse3 = null;
    }

    //accessors and mutators
    public Horse getWinner() {
        return winner;
    }

    /*
    Active detection during game to track statistcs.
     */
    public void activeDetect(){
        detectFall();
        detectWinner();
        trackTime();

        System.out.println("detect()");
    }

    private void detectWinner(){
        if(horse1.getDistanceTravelled() == raceLength){
            winner = horse1;
        }
        else if(horse2.getDistanceTravelled() == raceLength){
            winner = horse2;
        }
        else if(horse3.getDistanceTravelled() == raceLength){
            winner = horse3;
        }
        else {
            winner = null;
        }

        System.out.print("detectWinner(): ");
        if(winner!=null){
            System.out.println(winner.getName() + "\n");
        }

    }

    private void detectFall(){
        //calculate speed
        if(horse1.hasFallen()){
            h1speed = calculateAvgSpeed(horse1, time);
        }
        if(horse2.hasFallen()){
            h2speed = calculateAvgSpeed(horse2, time);
        }
        if(horse3.hasFallen()){
            h3speed = calculateAvgSpeed(horse3, time);
        }
        System.out.println("detectFall()" + h1speed + h2speed + h3speed);

    }

    public boolean isGameOver(){
        if((horse1.hasFallen() && horse2.hasFallen() && horse3.hasFallen()) || (winner!= null)){
            h1speed = calculateAvgSpeed(horse1, time);
            h2speed = calculateAvgSpeed(horse2, time);
            h3speed = calculateAvgSpeed(horse3, time);

            System.out.println("GAME IS OVER METHOD ACCESSED SPEEDS UPDATED!" + h1speed + h2speed + h3speed);

            return true;
        }
        else{
            return false;
        }
    }


    public void gameFinished(){
        System.out.print("\nThe Horses Average Speed");
        System.out.print("\n " + horse1.getSymbol() + ": " + h1speed +" | " + horse1.getDistanceTravelled());
        System.out.print("\n " + horse2.getSymbol() + ": " + h2speed + " | " + horse2.getDistanceTravelled());
        System.out.print("\n " + horse3.getSymbol() + ": " + h3speed + " | " + horse3.getDistanceTravelled());
        System.out.print("\n " + time);
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
