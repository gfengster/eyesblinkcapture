import org.opencv.core.Core; 
import org.opencv.core.Mat;  
import org.opencv.imgcodecs.Imgcodecs;
 
public class ReadingImages {
   public static void main(String args[]) { 
      //Loading the OpenCV core library  
      System.loadLibrary( Core.NATIVE_LIBRARY_NAME ); 
     
 
      
     
      //Reading the Image from the file  
      String file ="./sample.jpg"; 
      Mat matrix = Imgcodecs.imread(file); 
      
      System.out.println(matrix.cols() + " " + matrix.rows());
     
      System.out.println("Image Loaded");     
   } 
}