
/**
 * Write a description of class Horse here.
 *
 * @author ashar
 * @version 2.0.0
 */

public class Horse
{
    private String name;
    private char symbol;
    private int distance;
    private boolean fall;
    private double confidence;

    public Horse(char horseSymbol, String horseName, double horseConfidence)
    {
        this.symbol = horseSymbol;
        this.name = horseName;
        this.confidence = horseConfidence;
    }

    //Other methods of class Horse
    public void fall()
    {
        this.fall = true;
    }

    public double getConfidence()
    {
        return this.confidence;
    }

    public int getDistanceTravelled()
    {
        return this.distance;
    }

    public String getName()
    {
        return this.name;
    }

    public char getSymbol()
    {
        return this.symbol;
    }

    public void goBackToStart()
    {
        distance = 0;
    }

    public boolean hasFallen()
    {
        return fall;
    }

    public void moveForward()
    {
        distance += 1;
    }

    public void setConfidence(double newConfidence)
    {
        this.confidence = newConfidence;
    }

    public void setSymbol(char newSymbol)
    {
        this.symbol = newSymbol;
    }


}
