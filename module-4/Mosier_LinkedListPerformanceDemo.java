import java.util.LinkedList;
import java.util.Iterator;
/*
 * RESULTS/DISCUSSION
 * Actual results:
 * At 50,000:
 * -Iterator traversal: 4 ms
 * -get(index) traversal: 1,436 ms (~359x slower than the iterator)
 * At 500,000 elements (10x the data):
 * -Iterator traversal: 8 ms (2x the 50,000 time)
 * -get(index) traversal: 165,954 ms (~116x the 50,000 time (approx.  2.75 minutes))
 * 
 * My results match the times expected.  The iterator's time grew roughly in proportion to the list size.
 * while the get(index) traversal's time grew faster than the size increase itself
 * since going from 50,000 to 500,000 elements is a 10x increase in suze but cause a approx. 116x increase in
 * get(index) time. The gap between the two methods also increases substantially as the list grew larger.
 *  
 * AlinkedList stores items in separate connected nodes, not a continuous memory block. 
 * Accessing an item by its index using get(index) is slow because the list must be stepped
 * through one note at a time, making it very inefficient for large lists. 
 * Using an Iterator is much faster, as it moves directly from node to node.
 * Using an Iterator is hundreds or thousands of times faster than using get(index) a
 * as the list grows.  Iterator or an enhanced for-loop like LinkedLists is your more efficient choice.
 */
public class Mosier_LinkedListPerformanceDemo {
    public static void main(String[] args) {
        runTests();

        System.out.println("Testing with 50,000 integers");
        testPerformance(50000);

        System.out.println();

        System.out.println("Testing with 500,000 integers");
        testPerformance(500000);
    }
    public static void testPerformance(int size) {
        LinkedList<Integer> list = new LinkedList<>();

        for (int i = 0; i < size; i++) {
            list.add(i);
        }

            long iteratorStart = System.nanoTime();
            long iteratorSum = 0;
            Iterator<Integer> it = list.iterator();
            while (it.hasNext()) {
                iteratorSum += it.next();
            }
            long iteratorEnd = System.nanoTime();
            long iteratorTimeMs = (iteratorEnd - iteratorStart)/1_000_000;
        
            long getStart = System.nanoTime();
            long getSum = 0;
            for (int i = 0; i < list.size(); i++) {
                getSum += list.get(i);
            }
            long getEnd = System.nanoTime();
            long getTimeMs = (getEnd - getStart)/1_000_000;

        System.out.println("Iterator traversal time: " + iteratorTimeMs + " ms (sum = " + iteratorSum + ")");
        System.out.println("get(index) traversal time: " + getTimeMs + " ms (sum = " + getSum + ")");
    }
    public static void runTests() {
        System.out.println("Running correctness tests");

        LinkedList<Integer> testList = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            testList.add(i);
        }

            long iteratorSum = 0;
            Iterator<Integer> it = testList.iterator();
            while (it.hasNext()) {
                iteratorSum += it.next();
            }
            System.out.println("Test 1 - Iterator sum of 0-9 (expected 45): " + iteratorSum + (iteratorSum == 45 ? " -> PASS" : " -> FAIL"));

            long getSum = 0;
            for (int i = 0; i < testList.size(); i++) {
                getSum += testList.get(i);
            }
            System.out.println("Test 2 - get(index) sum of 0-9 (expected 45): " + getSum
            + (getSum == 45 ? " -> PASS" : " -> FAIL"));

            System.out.println("Test 3 - Iterator sum equals get(index) sum: "
            + (iteratorSum == getSum ? "PASS" : "FAIL"));

            System.out.println("Test 4 - List size equals 10: "
            + (testList.size() == 10 ? "PASS" : "FAIL"));

            System.out.println();
            
        
    }
}