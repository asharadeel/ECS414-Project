
/**
 * Write a description of class Horse here.
 *
 * @author ashar
 * @version 2.0.0
 */

public class Horse
{
    //DECLARATIONS
    private String name;
    private char symbol;
    private int distance;
    private boolean fall;
    private double confidence;

    /**
     * Constructor Method
     * @param horseSymbol
     * @param horseName
     * @param horseConfidence
     */
    public Horse(char horseSymbol, String horseName, double horseConfidence)
    {
        this.symbol = horseSymbol;
        this.name = horseName;
        this.confidence = horseConfidence;
    }

    /**
     * Make the horse fall
     */
    public void fall()
    {
        this.fall = true;
    }

    /**
     * Get confidence
     * @return
     */
    public double getConfidence()
    {
        return this.confidence;
    }

    /**
     * Get the distance travelled by the horse
     * @return
     */
    public int getDistanceTravelled()
    {
        return this.distance;
    }

    /**
     * Get the name
     * @return
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Get the symbol of the horse
     * @return
     */
    public char getSymbol()
    {
        return this.symbol;
    }

    /**
     * Make the horse go back to the start (reset)
     */
    public void goBackToStart()
    {
        distance = 0;
    }

    /**
     * Check if the horse has fallen
     * @return
     */
    public boolean hasFallen()
    {
        return fall;
    }

    /**
     * Make the horse move forward
     */
    public void moveForward()
    {
        distance += 1;
    }

    /**
     * Set the confidence of the horse to this value
     * @param newConfidence
     */
    public void setConfidence(double newConfidence)
    {
        this.confidence = newConfidence;
    }

    /**
     * Set the symbol of the horse to this value
     * @param newSymbol
     */
    public void setSymbol(char newSymbol)
    {
        this.symbol = newSymbol;
    }

    public void resetHorse(){
        this.distance = 0;
        this.fall = false;
    }
}
