
import org.opencv.core.Mat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Queue;

public class FillingHole {

	private Mat image ;
	private Mat approxImage ;
	private ArgsProccess _args;
	private ArrayList<Point> hole;
	private HashSet<Point> boundarySet;
	private ArrayList <Point> approxBoundaries;

	public ArrayList<Point> getHole() {
		return hole;
	}

	public HashSet<Point> getBoundarySet() {
		return boundarySet;
	}

	public Mat getImage() {
		return image;
	}

	public Mat getApproxImage() {
		return approxImage;
	}

	public FillingHole(Mat image,ArgsProccess _args) {
		this.image = image;
		this.approxImage = image.clone(); // image for the approximates approach (q2)
		this._args = _args;
		this.hole = new ArrayList<Point>();
		this.boundarySet = new HashSet<Point>(); 

		Point holeBeginPoint = findHolePrefix();
		if (holeBeginPoint == null) {
			System.out.println("Couldn't find a hole in the image");
			System.exit(0);
		}
		fillHoleAlgo(holeBeginPoint);
	}

	/*Detect where is the beginning of the hole*/
	private Point findHolePrefix(){
		int height = this.image.rows();
		int width = this.image.cols();

		for (int i = 0; i < height; i++)
			for(int j = 0; j < width; j++) {
				if (isHole(i,j)){ 
					return new Point(i, j, Constants.MISSING_VISITED);
				}
			}
		return null;
	}

	private Boolean isHole(int i, int j) {
		if (this.image.get(i, j)[0] == Constants.MISSING) {
			return true;
		}
		return false;
	}

	/*
	 * Given the Image, and a pixel from the hole, return a new image with the hole filled.
	 * for each missing pixel, calculate its new value based on a weighted average of the
	 * hole-boundary pixels. if the Neighbor of the pixel is also a hole, add it the the Queue and change value to MISSING_VISITED.
	 */
	private void fillHoleAlgo(Point holeBeginPoint) {
		Queue<Point> missingPixels = new ArrayDeque<>();
		addMissingPoint(holeBeginPoint,missingPixels);

		while (!missingPixels.isEmpty()){
			Point h = missingPixels.poll();
			visitNeighbors(h,missingPixels);
		}
		fillHoles();
	}

	private void visitNeighbors(Point hPoint, Queue<Point> q) {
		approxBoundaries = new ArrayList<>();
		int holeX = hPoint.getX();
		int holeY = hPoint.getY();

		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				/* regardless the connectivity, add the missing neighbors to the Queue and to the hole list*/ 
				if (isHole(holeX + i, holeY + j)) {
					addMissingPoint(new Point(holeX + i, holeY + j, Constants.MISSING_VISITED), q);
					continue; 
				}
				/* connectivity is four -> skip diagonal neighbors */
				if (this._args.getConnectivity() == Constants.FOUR__CONNECTIVITY && Math.abs(i) + Math.abs(j) == 2 ) {  
					continue;  
				}
				addBoundaryPoint(holeX + i, holeY + j);
			}
		}
		float newApproxVal = calcNewValue(hPoint,approxBoundaries);
		setNewValue(this.approxImage ,new Point(holeX, holeY, newApproxVal));
	}

	private void addBoundaryPoint(int neighborX, int neighborY) {
		if(this.image.get(neighborX, neighborY)[0] != Constants.MISSING_VISITED) {
			float neighborValue = (float) this.image.get(neighborX, neighborY)[0] ;  
			this.boundarySet.add(new Point(neighborX, neighborY, neighborValue));
		}

		/*add Boundary for the approximates approach (q2)*/
		if( this.approxImage.get(neighborX, neighborY)[0] != Constants.MISSING_VISITED) {
			float neighborValue = (float) this.approxImage.get(neighborX, neighborY)[0] ;
			this.approxBoundaries.add(new Point(neighborX, neighborY, neighborValue));
		}
	}
	/*Add the missing pixel to the Queue.
	 *each missing pixel will get the value of MISSING_VISITED in order to avoid duplicate points in the queue */
	private void addMissingPoint(Point missingPixel, Queue <Point> q) {
		this.image.put(missingPixel.getX(), missingPixel.getY(), missingPixel.getValue());
		this.approxImage.put(missingPixel.getX(), missingPixel.getY(), missingPixel.getValue());
		q.add(missingPixel);
		this.hole.add(missingPixel);
	}

	private void fillHoles() {
		ArrayList  <Point> boundaries = new ArrayList<Point>(this.boundarySet);
		for (Point h : this.hole) {
			float newVal = calcNewValue(h,boundaries);
			h.setValue(newVal);
			setNewValue(this.image,h);
		}
	}

	private float calcNewValue(Point hole, ArrayList<Point> boundaries) {
		float numerator = 0, denominator = 0;
		for (Point b : boundaries) {
			float w = this._args.weight(hole, b);
			numerator += w * b.getValue();
			denominator += w;
		}
		if (denominator == 0) {
			System.out.println("error: denominator is 0");
			throw new ArithmeticException();
		}
		return (numerator / denominator);
	}

	private void setNewValue(Mat imageMat,Point p) {
		imageMat.put(p.getX(), p.getY(), p.getValue());
	}
}