//Just practising parallel programming
//Maphosa Craig Themba

import java.io.*;
import java.util.*;
import java.util.concurrent.ForkJoinPool;

public class SequentialFilter {
    static double[] unfiltered;
    static double[] filtered;
    static long startTime = 0;
    static int fieldsize;
    static final ForkJoinPool fjPool = new ForkJoinPool();

    //main method of the program. 
    //args array of String type must have the data file name, filter size(odd integer >= 3) and output file name as its values.
    public static void main(String[] args) {
        readFile(args[0]);
        fieldsize = Integer.parseInt(args[1]);

        tick();
        sequentialfiltering(unfiltered, filtered, fieldsize);
        System.out.println("Time taken to run sequential program = " + tock() + " milliseconds");

        System.out.println("");
        tick();
        parallelfiltering(unfiltered, filtered, fieldsize);
        System.out.println("Time taken to run parallel program = " + tock() + " milliseconds");

        writeToFile(args[2]);
    }
    
    //method that reads the data from a file. Data which must be median filtered 
    public static void readFile(String f) {
        try {
            File file = new File(f);
            Scanner scanner = new Scanner(file);
            int lines = Integer.parseInt(scanner.nextLine());
            unfiltered = new double[lines];
            filtered = new double[lines];
            for(int i = 0; i < lines; i++){
                StringTokenizer st = new StringTokenizer(scanner.nextLine());
                st.nextToken();
                unfiltered[i] = Double.parseDouble(st.nextToken().replace(",","."));
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    //Does median filtering using the parallel approach.
    static void parallelfiltering(double[] unfltd, double[] fltd, int fldsize) {
        System.out.println("Running parallel program...");
	    fjPool.execute(new MultithreadFilter(unfltd,fltd,fldsize,0,unfltd.length));
    }

    //Does median filtering using the sequential approach.
    static void sequentialfiltering(double[] unfltd, double[] fltd, int fldsize) {
     System.out.println("Running sequential program...");
     int width = fldsize/2;
	 for(int i =0; i < unfltd.length; i++) {
         if(i < width || (unfltd.length - i) <= width)
            fltd[i] = unfltd[i];
         else {
            double[] field = new double[fldsize];
	        int index = 0;
            for(int j = i - width; j < i + width + 1; j++) {
               field[index] = unfltd[j];
	       index++;
            }
            Arrays.sort(field);
            fltd[i] = field[fldsize/2];
         }
      }
	
    } 
    
    //Method that writes the output of the data to a file. filename comes from the main method 's args array.
    //If the file already exists when the method is run again, that file will be overwritten with new data.
    public static void writeToFile(String f) {
        try {
            PrintWriter write = new PrintWriter(f);
            write.println(filtered.length);
            for(int i = 0; i < filtered.length; i++) {
                 write.println(i + " " +filtered[i]);
                }
                
            write.close();

        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    //Along with tock() measures the time taken to run the parallel filtering and the sequential filtering methods.
    public static void tick() {
	System.gc();
	startTime = System.nanoTime();
    }

    //Along with tick() measures the time taken to run the parallel filtering and the sequential filtering methods.
    public static float tock() {
	return (System.nanoTime() - startTime) / 10000000.0f;
    }
    
    
}