package xyz.mlhmz.bogosort;

import xyz.mlhmz.bogosort.cli.BogosortCli;
import xyz.mlhmz.bogosort.cli.UserInput;

import static xyz.mlhmz.bogosort.IntArrayUtils.*;

public class Main {
    public static void main(String[] args) {
        BogosortCli bogosortCli = BogosortCli.getInstance();
        UserInput input = bogosortCli.prompt();
        int[] iteratedArray = createIteratedArray(input.arraySize());
        int[] shuffledArray = shuffleArray(iteratedArray);
        Bogosort bogosort = Bogosort.getInstance();
        int[] result;
        if (input.threads() > 1) {
            result = bogosort.multithreadSort(input.threads(), shuffledArray);
        } else {
            result = bogosort.sort(shuffledArray);
        }
        String stringResult = convertArrayToString(result);
        System.out.printf("Result:%n%s%n", stringResult);
    }
}