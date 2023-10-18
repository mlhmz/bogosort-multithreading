package xyz.mlhmz.bogosort.cli;

import java.util.Scanner;

public class BogosortCli {
    private static final BogosortCli instance = new BogosortCli();
    private Scanner scanner = new Scanner(System.in);

    public static BogosortCli getInstance() {
        return instance;
    }

    public UserInput prompt() {
        System.out.print("How big should the size of objects to sort be?: ");
        int arraySize = promptInt();
        System.out.print("How many threads should the bogosort run in? (1 or lower for no Multithreading): ");
        int threads = promptInt();
        return new UserInput(arraySize, threads);
    }

    private int promptInt() {
        return scanner.nextInt();
    }
}
