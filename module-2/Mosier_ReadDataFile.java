import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Mosier_ReadDataFile
 *
 * Reads "MosierDataFile.dat" line by line and displays its contents
 * to the console. Because Mosier_WriteDataFile appends a new
 * "Integer Array" / "Double Array" pair every time it runs, this
 * program numbers each pair as a separate "Data Set" so it is clear
 * how many times data has been written to the file.
 */
public class Mosier_ReadDataFile {

    public static final String FILE_NAME = "MosierDataFile.dat";

    public static void main(String[] args) {

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {

            String line;
            int lineCount = 0;
            int dataSetNumber = 1;

            System.out.println("Contents of " + FILE_NAME + ":");
            System.out.println("--------------------------------------");

            while ((line = reader.readLine()) != null) {
                // Every two lines (Integer Array + Double Array) = one data set
                if (lineCount % 2 == 0) {
                    System.out.println("Data Set #" + dataSetNumber + ":");
                }

                System.out.println("  " + line);

                lineCount++;
                if (lineCount % 2 == 0) {
                    dataSetNumber++;
                    System.out.println();
                }
            }

            System.out.println("--------------------------------------");
            System.out.println("End of file.");

        } catch (IOException e) {
            System.out.println("The file \"" + FILE_NAME
                    + "\" could not be found or read. Make sure you have "
                    + "run Mosier_WriteDataFile at least once first.");
        }
    }
}
