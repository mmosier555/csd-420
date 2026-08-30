import java.util.ArrayList;
import java.util.Random;

public class Mosier_RemoveDuplicatesDemo {
    public static void main(String[] args) {
        ArrayList<Integer> originalList = new ArrayList<>();
        Random rand = new Random();

        //Fill the orignial ArrayList with 50 randowm values from 1-20
        for(int i = 0; i<50; i++) {
            originalList.add(rand.nextInt(20) + 1);
        }

        System.out.println("Original list (" + originalList.size() + " values):");
        System.out.println(originalList);

        //Get a new list with duplicates removed
        ArrayList<Integer> noDuplicatesList = removeDuplicates(originalList);

        System.out.println("\nList with duplicates removed (" + noDuplicatesList.size() + " values):"); 
        System.out.println(noDuplicatesList);
    }
    /**
     * Returns a new ArrayList containing all the values from the original list, but with no duplicate values.
     * 
     * @param list the original ArrayList to check fro duplicates
     * @param <E> the type of elements held in the list
     * @return a new ArrayList containing only the unique values from list
     */
    public static <E> ArrayList<E> removeDuplicates(ArrayList<E> list) {
        ArrayList<E> result = new ArrayList<>();

        for (E value : list) {
            if (!result.contains(value)) {
                result.add(value);
            }
        }

        return result;
    }
}