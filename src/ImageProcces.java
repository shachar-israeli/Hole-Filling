
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import static org.opencv.core.CvType.CV_64FC1;
import static org.opencv.core.CvType.CV_8UC1;
import static org.opencv.imgcodecs.Imgcodecs.IMREAD_GRAYSCALE;

/*Image processing utilities
 * return a image with values:
 * A hole pixel value is -1, otherwise the value of the color will scale to [0,1]
 */
public class ImageProcces{

	
	public static Mat getMergedImage(String imagePath,String maskPath) {
		Mat image = carveOutHole(readImage(imagePath), readImage(maskPath));
		image.convertTo(image, -1, (1d / 255), 0);   // scale to [0,1]
		return image;
	}

	private static Mat readImage(String file) {
		Mat image = Imgcodecs.imread(file, IMREAD_GRAYSCALE);
		image.convertTo(image, CV_64FC1, 1, 0);
		return image;
	}

	/*Pre-processing the image in order to simulate the missing pixels.
	 * each pixel in the mask image with the color of MASK_HOLE_COLOR will be part of the hole in the 
	 * image */
	private static Mat carveOutHole(Mat image, Mat mask) {	
		Mat merged = image.clone();
		for (int i = 0; i < mask.rows(); i++) {
			for (int j = 0; j < mask.cols(); j++) {
				double pixelVal = mask.get(i, j)[0];
				if (pixelVal > Constants.MASK_HOLE_THRESHOLD) {
					merged.put(i, j, Constants.MISSING * 255);
				}
			}
		}
		return merged;
	}

	/*save the result in the same path as the image*/
	public static void saveImage(Mat image, String ImagePath, String title) {
		String fileName = ImagePath.split("\\.")[0];
		String extension = ImagePath.split("\\.")[1];

		image.convertTo(image, CV_8UC1, 255, 0); //scale back the color
		Imgcodecs.imwrite(fileName + title + "." + extension , image);
		System.out.println(title.substring(1) + " image saved successfully");
	}
}
