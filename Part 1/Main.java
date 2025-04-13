import java.util.*;

/**
 * Main Method to execute the Horse Race in terminal
 *
 * @author ashar
 * @version 1.6
 */
public class Main {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);

        boolean sameHorses = false;
        Horse[] repeatHorses = new Horse[0]; //for repeated races

        //Ask the user if they want to start the race, with validation for answer
        String askStart = Ask("Would you like to start a new race? [Y/N]",
                new String[] {"Y","N"},
                "Please choose a valid output");

        //Loop controls race
        while(askStart.equalsIgnoreCase("y")) {
            Horse[] RaceHorses = repeatHorses; //current horses

            //Create race via method call
            Race r1 = CreateRace();

            //check for laneChange request
            boolean laneChange = false;

            //check if previous horses are to be used
            if(!sameHorses) {
                //ask for n of horses
                int horses = NOfHorses();

                //Fill in information for the horse
                Horse[] currentHorses = new Horse[horses];
                do {
                    for (int i = 0; i <= horses - 1; i++) {
                        System.out.println("Horse " + (i + 1) + ": ");
                        currentHorses[i] = CreateHorse();
                    }
                } while (IsSymbolDuplicate(currentHorses));

                //lane change must occur since no initial lanes applied
                laneChange = true;

                //update horses to match modified ones
                RaceHorses = currentHorses;
            }
            //if same horses are used execute this block
            else{
                Horse[] currentHorses = RaceHorses;

                //remind user of the horses
                System.out.println("You have chosen the same horses for this race.");
                for(int i = 0; i < currentHorses.length; i++) {
                    System.out.println("Horse " + (i + 1) + ": " +
                            currentHorses[i].getName() + ", " +
                            currentHorses[i].getSymbol() + " " +
                            currentHorses[i].getConfidence() + "."); //updated
                }

                //update lanes request
                String askLanes = Ask("Would you like to change lanes of any horses? (Y/N)",
                        new String[] {"Y","N"},
                        "Please choose a valid output");

                if(askLanes.equalsIgnoreCase("Y")) {
                    laneChange = true;
                }
                else{
                    laneChange = false;
                }


                //update the race horses to match modified ones
                RaceHorses = currentHorses;
            }

            //if lane change request passed
            if(laneChange) {
                RaceHorses = ChooseLanes(RaceHorses);
            }

            //Add the horses to the race
            for(int i = 0; i < RaceHorses.length; i++) {
                r1.addHorse(RaceHorses[i],i+1);
            }

            //Start thr Race
            r1.startRace();

            //At end of race, ask for next or terminate program.
            askStart = Ask("Race Over, go again? [Y/N]",
                    new String[] {"Y","N"},
                    "Please choose a valid output");
            //ask repeat
            if(askStart.equalsIgnoreCase("y")) {
               String askRepeat = Ask("Would you like to use same horses? (Y/N)",
                        new String[] {"Y","N"},
                        "Please choose a valid output");

                if(askRepeat.equalsIgnoreCase("y")) {
                    sameHorses = true;
                    repeatHorses = RaceHorses;
                }
                else{
                    sameHorses = false;
                }
            }
            //goodbye messages
            else{
                System.out.println("Thank you!");
            }
        }
    }

    /**
     * Simple input method using a scanner and printing message
     * @param x
     * @return
     */
    public static String input(String x){
        System.out.println(x);
        return new Scanner(System.in).nextLine();
    }


    /**
     * Ask for an input, requiring specific answer
     * @param question - the question to ask the user input for
     * @param answers - potential answer/s that can be result
     * @param response - repsonse for incorrect input
     * @return - valid input passing tests
     */
    public static String Ask(String question, String[] answers, String response){
        Scanner s = new Scanner(System.in);
        while(true){
            System.out.println(question);
            String x = s.nextLine().trim();

            for(String ans : answers){
                if(ans.equalsIgnoreCase(x)){
                    return x;
                }
            }
            System.out.println(response);
        }
    }


    /**
     * Check if the symbol of the horse is unique
     * @param symbols - Pass the horses array to be compared against eachother
     * @return - true if symbol is not unqiue, else false
     */
    public static boolean IsSymbolDuplicate(Horse[] symbols) {
        for (int i = 0; i < symbols.length; i++) {
            Horse h1 = symbols[i];
            for (int j = i + 1; j < symbols.length; j++) {
                Horse h2 = symbols[j];
                if (h1.getSymbol() == h2.getSymbol()) {
                    System.out.println("Error: Symbols are not unique");
                    return true;
                }
            }
        }
        System.out.println("Horse Information Captured.");
        return false;
    }

    /**
     * Ask the Number of Horses for a race
     * @return - number of horses for the race
     */
    public static int NOfHorses(){
        do {
            String horsesInp = input("How many horses? (2-10)");
            try {
                int horses = Integer.parseInt(horsesInp);
                if(horses >= 2 && horses <= 10) {
                    return horses;
                }
                else{
                    System.out.println("Please enter a number between 2 and 10");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number between 2 and 10");
            }
        }while(true);
    }

    /**
     * Create Race method to take input like distance, validate and return race
     * @return
     */
    public static Race CreateRace(){
        String rlenInp = input("Please enter the length of the race (50 - 150): ");
        do{
            try{
                int rlen = Integer.parseInt(rlenInp);
                if(rlen >= 50 && rlen <= 150 ){
                    return new Race(rlen);
                }
                else{
                    System.out.println("Error: Length must be between 50 and 150.");
                }
            }
            catch(NumberFormatException e){
                System.out.println("Error: Please enter a valid integer. ");
            }
            rlenInp = input("Please enter the length of the race (50 - 150): ");
        }while(true);
    }

    /**
     * Create a horse - validate the inputs
     * @return
     */
    public static Horse CreateHorse(){
        //DECLARATIONS
        String name = input("Horse Name (String): ");
        char symbol = '#'; //default
        double conf = 0.0;

        //symbol capture
        boolean validSymbol = false;
        do{
            String symbolInput = input("Horse Symbol (Character): ");
            if(symbolInput.length() == 1){
                symbol = symbolInput.charAt(0);
                validSymbol = true;
            }
            else {
                System.out.println("Please enter a ONE character");
            }
        }while(validSymbol == false);

        //confidence capture
        boolean validConf = false;
        do {
            String confInp = input("Horse Confidence (0-1) : ");
            try {
                conf = Double.parseDouble(confInp);
                if (conf > 0 && conf < 1) {
                    validConf = true;  // Valid input, exit loop
                } else {
                    System.out.println("Error: Confidence must be between 0 and 1 (exclusive).");
                }
            } catch (Exception e) {
                System.out.println("Error: Please enter a valid double.");
            }
        } while(validConf == false);

        return new Horse(symbol, name, conf);
    }

    /**
     * Choose the lanes of the horses
     * @param RaceHorses - The horses created for game
     * @return - Return readjusted array with horses in right lane
     */
    public static Horse[] ChooseLanes(Horse[] RaceHorses) {
        int[] lanes = new int[RaceHorses.length];

        for (int i = 0; i < RaceHorses.length; i++) {
            boolean validInp = false;
            do {
                String laneInp = input("Choose lane (1-" + (RaceHorses.length) +
                        ") for " + RaceHorses[i].getName() + ": ");
                try {
                    int chosenLane = Integer.parseInt(laneInp) - 1;

                    // Check if lane is within bounds
                    if (chosenLane < 0 || chosenLane >= RaceHorses.length) {
                        System.out.println("Error: Lane must be between 1 and " +
                                (RaceHorses.length));
                        continue;
                    }

                    // Check if lane is already taken
                    boolean laneTaken = false;
                    for (int j = 0; j < i; j++) {
                        if (lanes[j] == chosenLane) {
                            laneTaken = true;
                            break;
                        }
                    }

                    if (laneTaken) {
                        System.out.println("Error: Lane " + chosenLane +
                                " is already occupied");
                    } else {
                        lanes[i] = chosenLane;
                        validInp = true;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Error: Please enter a valid number");
                }
            } while (!validInp);
        }

        // Debug print (optional)
        System.out.println("Lane assignments:");
        for (int i = 0; i < lanes.length; i++) {
            System.out.println("Horse " + RaceHorses[i].getName() +
                    " -> Lane " + lanes[i]);
        }

        // Reorganize horses by lane order
        Horse[] newRaceHorses = new Horse[RaceHorses.length];
        for (int i = 0; i < lanes.length; i++) {
            newRaceHorses[lanes[i]] = RaceHorses[i];
        }

        return newRaceHorses;
    }


}