//Maphosa Craig Themba


import java.util.concurrent.RecursiveAction;
import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;

//ParallelFilter class follows the same format as in the Java API documentation of a class extending RecursiveAction
public class MultithreadFilter extends RecursiveAction {
 int lo;
 int hi;
 double[] unfiltered;
 double[] filtered;
 int fieldsize;
 static final ForkJoinPool fjPool = new ForkJoinPool();
 static final int SEQUENTIAL_CUTOFF = 500;

 MultithreadFilter(double[] unfltd, double[] fltd, int fldsize, int l, int h) {
  unfiltered=unfltd; filtered=fltd; lo=l; hi=h; fieldsize = fldsize; 
 }





 protected void compute() {
  int width = fieldsize/2;
  if(hi - lo < SEQUENTIAL_CUTOFF){
	for(int i = lo; i < hi; i++) {
	   if(i < width || (unfiltered.length - i) <= width)
             filtered[i] = unfiltered[i];
           else {
             double[] field = new double[fieldsize];
	           int index = 0;
             for(int j = i - width; j < i + width + 1; j++) {
                field[index] = unfiltered[j]; 
	              index++;
             }
            Arrays.sort(field);
            filtered[i] = field[width];
           }	
	}
  }
  else {
         int mid = (hi + lo)/2;
	 MultithreadFilter left = new MultithreadFilter(unfiltered,filtered,fieldsize,0,mid);
	 MultithreadFilter right = new MultithreadFilter(unfiltered,filtered,fieldsize,mid,unfiltered.length);
   invokeAll(left, right);
	
  }
 }
 

}