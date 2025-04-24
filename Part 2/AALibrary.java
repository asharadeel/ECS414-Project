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
}
