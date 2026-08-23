import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

/**
 * Mosier_WriteDataFile
 *
 * Generates an array of five random integers and an array of five random
 * double values, then writes both arrays to "MosierDataFile.dat".
 *
 * File-handling requirement:
 *   - If the file does not exist yet, it is created automatically.
 *   - If the file already exists, the new data is APPENDED to the end
 *     rather than overwriting what is already there.
 *
 * This is accomplished by opening the FileWriter in "append" mode
 * (the boolean true passed to the FileWriter constructor).
 */
public class Mosier_WriteDataFile {

    public static final String FILE_NAME = "MosierDataFile.dat";
    public static final int ARRAY_SIZE = 5;

    public static void main(String[] args) {

        // ----- Build the two arrays of random values -----
        int[] randomInts = new int[ARRAY_SIZE];
        double[] randomDoubles = new double[ARRAY_SIZE];
        Random rand = new Random();

        for (int i = 0; i < ARRAY_SIZE; i++) {
            // random integers between 1 and 100
            randomInts[i] = rand.nextInt(100) + 1;
            // random doubles between 0.0 and 100.0, rounded to 2 decimals
            randomDoubles[i] = Math.round(rand.nextDouble() * 10000.0) / 100.0;
        }

        // ----- Write (append) the arrays to the data file -----
        // 'true' as the second FileWriter argument enables append mode.
        try (FileWriter fw = new FileWriter(FILE_NAME, true);
             PrintWriter writer = new PrintWriter(fw)) {

            writer.println("Integer Array: " + arrayToString(randomInts));
            writer.println("Double Array: " + arrayToString(randomDoubles));

            System.out.println("Data successfully written to " + FILE_NAME);

        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }

        // Echo what was generated so we can visually confirm it during testing
        System.out.println("Integers written: " + arrayToString(randomInts));
        System.out.println("Doubles written:  " + arrayToString(randomDoubles));
    }

    // Helper to turn an int[] into a comma-separated String
    private static String arrayToString(int[] arr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            sb.append(arr[i]);
            if (i < arr.length - 1) sb.append(", ");
        }
        return sb.toString();
    }

    // Helper to turn a double[] into a comma-separated String
    private static String arrayToString(double[] arr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            sb.append(arr[i]);
            if (i < arr.length - 1) sb.append(", ");
        }
        return sb.toString();
    }
}
