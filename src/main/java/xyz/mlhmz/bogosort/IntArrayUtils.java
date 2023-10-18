package xyz.mlhmz.bogosort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class IntArrayUtils {
    private IntArrayUtils() {}

    public static int[] createIteratedArray(int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = i + 1;
        }
        return array;
    }

    public static int[] shuffleArray(int[] arr) {
        List<Integer> integerList = IntStream.of(arr).boxed().collect(Collectors.toCollection(ArrayList::new));
        Collections.shuffle(integerList);
        return convertIntegerArrayToPrimitiveArray(integerList.toArray(new Integer[0]));
    }

    public static int[] convertIntegerArrayToPrimitiveArray(Integer[] objArray) {
        int[] primitiveArray = new int[objArray.length];

        for (int i = 0; i < objArray.length; i++) {
            primitiveArray[i] = objArray[i];
        }
        return primitiveArray;
    }

    public static String convertArrayToString(int[] arr) {
        String joinedArray = IntStream.of(arr)
                .mapToObj(Integer::toString)
                .collect(Collectors.joining(", "));
        return String.format("[%s]", joinedArray);
    }
}
