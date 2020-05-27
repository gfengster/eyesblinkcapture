import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

public class App {

	public static void main(String[] args) {
		 System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		 VideoCapture capture = new VideoCapture(0);
		 
		 if (!capture.isOpened()) {
	            System.err.println("--(!)Error opening video capture");
	            System.exit(0);
	        }

	        Mat frame = new Mat();
	        while (capture.read(frame)) {
	            if (frame.empty()) {
	                System.err.println("--(!) No captured frame -- Break!");
	                break;
	            }

	            //-- 3. Apply the classifier to the frame
	            detectAndDisplay(frame);
	            
	            if (HighGui.waitKey(10) == 27) {
	                break;// escape
	            }
	        }

	        capture.release();
	        
	        HighGui.destroyAllWindows();
	        
	}
	
    public static void detectAndDisplay(Mat frame) {
        Mat frameGray = new Mat();
        Imgproc.cvtColor(frame, frameGray, Imgproc.COLOR_BGR2GRAY);
        
        String filenameFaceCascade = "./data/haarcascades/haarcascade_frontalface_default.xml";
        String filenameRightEyesCascade = "./data/haarcascades/haarcascade_righteye_2splits.xml";
        String filenameLeftEyesCascade = "./data/haarcascades/haarcascade_eye_tree_eyeglasses.xml";
        
        CascadeClassifier faceCascade = new CascadeClassifier();
        faceCascade.load(filenameFaceCascade);
        CascadeClassifier rightEyesCascade = new CascadeClassifier();
        rightEyesCascade.load(filenameLeftEyesCascade);
        
        MatOfRect faces = new MatOfRect();
        faceCascade.detectMultiScale(frameGray, faces);

        List<Rect> listOfFaces = faces.toList();
        for (Rect face : listOfFaces) {
            Point center = new Point(face.x + face.width / 2, face.y + face.height / 2);
            Imgproc.ellipse(frame, center, new Size(face.width / 2, face.height / 2), 0, 0, 360,
                    new Scalar(255, 0, 255));

            Mat faceROI = frameGray.submat(face);

            // -- In each face, detect eyes
            MatOfRect eyes = new MatOfRect();
            rightEyesCascade.detectMultiScale(faceROI, eyes);

            List<Rect> listOfEyes = eyes.toList();
            for (Rect eye : listOfEyes) {
                Point eyeCenter = new Point(face.x + eye.x + eye.width / 2, face.y + eye.y + eye.height / 2);
                int radius = (int) Math.round((eye.width + eye.height) * 0.25);
                //Imgproc.circle(frame, eyeCenter, radius, new Scalar(255, 0, 0), 4);
                Imgproc.ellipse(frame, eyeCenter, new Size(eye.width*0.5, eye.height*0.25), 0, 0, 360, new Scalar(255, 0, 0), 4);
            }
        }

        //-- Show what you got
        HighGui.imshow("Capture - Face detection", frame);
    }

}
