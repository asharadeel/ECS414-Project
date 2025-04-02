//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Horse h1 = new Horse('♘', "PETH", 0.8);
        Horse h2 = new Horse('♞' , "AARHSH", 0.6);
        Horse h3 = new Horse('⾺' , "BOOBY", 0.4);

        Race r1 = new Race(100);
        r1.addHorse(h1, 1);
        r1.addHorse(h2, 2);
        r1.addHorse(h3, 3);
        r1.startRace();
    }
}