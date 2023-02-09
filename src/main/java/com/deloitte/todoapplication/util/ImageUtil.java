package com.deloitte.todoapplication.util;

import nu.pattern.OpenCV;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

import static org.opencv.core.CvType.CV_64F;

public class ImageUtil {
    public static void jdkCompressImage(File file) {
        try {
            BufferedImage image = ImageIO.read(file);

            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
            ImageWriter writer = writers.next();
            File compressedImageFile = new File("compressed.jpg");
            OutputStream os = new FileOutputStream(compressedImageFile);
            ImageOutputStream ios = ImageIO.createImageOutputStream(os);
            writer.setOutput(ios);

            ImageWriteParam param = writer.getDefaultWriteParam();

            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(0.15f);

            writer.write(null, new IIOImage(image, null, null), param);

            os.close();
            ios.close();
            writer.dispose();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void openCVCompressImage(String fileName) {
        try {
            OpenCV.loadShared();    //initialize OpenCV

            //Compress image
            Mat srcImage = Imgcodecs.imread(fileName);
            MatOfInt dstImage = new MatOfInt(Imgcodecs.IMWRITE_JPEG_QUALITY, 15);
            Imgcodecs.imwrite("resized_img.jpg", srcImage, dstImage);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void blurryDetect(String fileName){
        try {
            OpenCV.loadShared();    //initialize OpenCV
            double threshold = 200d;

            Mat srcImage = Imgcodecs.imread(fileName);
            Mat dstImage = new Mat();

            //convert to grayscale
            Imgproc.cvtColor(srcImage, dstImage, Imgproc.COLOR_BGR2GRAY);
            //Generate a grayscale image
            Imgcodecs.imwrite("gray_img.jpg", dstImage);

            Mat laplacianDstImage = new Mat();
            //If the threshold is too low, the normal picture will be misjudged as a blurred picture,
            // and if the threshold is too high, the blurred picture will be misjudged as a normal picture
            Imgproc.Laplacian(dstImage, laplacianDstImage, CV_64F);
            //Generate a picture that has been convolved with a Laplacian mask
            Imgcodecs.imwrite("lap_img.jpg", laplacianDstImage);

            //matrix standard deviation
            MatOfDouble mean = new MatOfDouble();
            MatOfDouble stddev = new MatOfDouble();
            //Find the mean and standard deviation of a matrix
            Core.meanStdDev(laplacianDstImage, mean, stddev);

            double variance = Math.pow(stddev.get(0,0)[0],2);

            System.out.println("Variance value is : " + variance);
            System.out.println("This image is " + (variance < threshold? "blurry":"non-blurry"));
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
