public class Archive {

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
            String horsesInp = AALibrary.input("How many horses? (2-10)");
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
        String rlenInp = AALibrary.input("Please enter the length of the race (50 - 150): ");
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
            rlenInp = AALibrary.input("Please enter the length of the race (50 - 150): ");
        }while(true);
    }

    /**
     * Create a horse - validate the inputs
     * @return
     */
    public static Horse CreateHorse(){
        //DECLARATIONS
        String name = AALibrary.input("Horse Name (String): ");
        char symbol = '#'; //default
        double conf = 0.0;

        //symbol capture
        boolean validSymbol = false;
        do{
            String symbolInput = AALibrary.input("Horse Symbol (Character): ");
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
            String confInp = AALibrary.input("Horse Confidence (0-1) : ");
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
                String laneInp = AALibrary.input("Choose lane (1-" + (RaceHorses.length) +
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
