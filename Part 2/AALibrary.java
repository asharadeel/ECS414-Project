import java.awt.*;
import java.util.Arrays;
import java.util.Scanner;

public class AALibrary {



    /**
     * Add a value to a classic array
     * @param arr - The array to manipulate
     * @param value - The value to add
     * @return - return new size array
     * @param <T> - made for any type for future use too
     */
    public static <T> T[] appendValue (T[] arr, T value){
        if (arr == null) {
            throw new IllegalArgumentException("Input array cannot be null");
        }

        T[] newArray = Arrays.copyOf(arr, arr.length + 1);
        newArray[arr.length] = value;
        return newArray;
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

    public static Color getColorFromString(String colorName) {
        if (colorName == null) return Color.WHITE; // Default fallback

        switch (colorName.toUpperCase()) {
            case "RED":     return Color.RED;
            case "GREEN":   return Color.GREEN;
            case "BLUE":    return Color.BLUE;
            case "WHITE":   return Color.WHITE;
            case "BLACK":   return Color.BLACK;
            case "CYAN":    return Color.CYAN;
            case "MAGENTA": return Color.MAGENTA;
            case "PINK":    return Color.PINK;
            default:        return Color.WHITE; // Fallback
        }
    }

    public static void printHorses(Horse[] h){
        int count = 1;
        System.out.println("HORSES ARRAY ---------");
        if (h == null) {
            System.out.println("Array is null!");
            return;
        }

        for(Horse x : h){
            if(x != null) {
                System.out.print("\n" +
                        count + "= " +
                        " Name: " + x.getName() +
                        " Symbol: " + x.getSymbol() +
                        " Confidence: " + x.getConfidence());
            }
            else{
                System.out.print("\n" + count + " = null index");
            }
            count++;
        }
    }

    public static void printHorses(HorseData[] h){
        System.out.println("DETAILED HORSES ARRAY -----------------------");
        int count = 1;

        if (h == null) {
            System.out.println("Array is null!");
            return;
        }

        for(HorseData x : h) {
            if (x != null) {
                    System.out.print("\n" +
                            count + "= " +
                            " Name: " + x.getName() +
                            " Symbol: " + x.getSymbol() +
                            " Confidence: " + x.getConfidence() +
                            " | Distance Travelled: " + x.getDistanceTravelled() +
                            " | AvgSpeed: " + x.getAverageSpeed() +
                            " | Time Taken: " + x.getTimeTaken() +
                            " | Checked?: " + x.isHorseChecked() +
                            " | Lane: " + x.getLane());
            }
            else {
                System.out.println("\n"+count + " = null index");
            }
            count++;
        }
        System.out.println("----------------------------------------------");
    }
}
