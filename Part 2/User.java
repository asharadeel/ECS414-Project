/**
 * User class for saving data and history of users bets
 */
public class User {
    String username;
    String password;

    int betsMade = 0;
    int betsWon = 0;
    int betsLost = 0;
    int pointsEarnt = 0;

    public void addPoints(int x){
        pointsEarnt += x;
        System.out.println(pointsEarnt);
    }
}

