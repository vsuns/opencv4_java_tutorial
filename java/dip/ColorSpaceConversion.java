package dip;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.scijava.nativelib.NativeLoader;

import java.io.IOException;

public class ColorSpaceConversion {
    public static void main(String[] args) throws IOException {
        NativeLoader.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        
        Mat mat = Imgcodecs.imread("data/dip/digital_image_processing.jpg");
        Mat mat1 = new Mat(mat.width(), mat.height(), CvType.CV_8UC3);
        Imgproc.cvtColor(mat, mat1, Imgproc.COLOR_RGB2HSV);
        Imgcodecs.imwrite("hsv.jpg", mat1);

    }
}
