
import org.opencv.core.Core;
import org.opencv.core.CvException;
import org.opencv.core.Mat;

public class Main {

	public static void main(String[] args) {
		try {	
			System.loadLibrary(Core.NATIVE_LIBRARY_NAME);    		
			ArgsProccess _args = new ArgsProccess(args, true);        
			Mat merged = ImageProcces.getMergedImage(_args.getImagePath(),_args.getMaskPath());
			FillingHole fill = new FillingHole(merged, _args);
			ImageProcces.saveImage(fill.getImage(), _args.getImagePath(),Constants.FILLED_SUFFIX_FILENAME);
			ImageProcces.saveImage(fill.getApproxImage(),_args.getImagePath(),Constants.FILLED_APPROX_SUFFIX_FILENAME);
		} 
		catch (CvException e ) {
			System.out.println("Error: invalid images path");
			System.exit(1);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
	}
}
