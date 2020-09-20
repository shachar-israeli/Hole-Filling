
/* Holds all the arguments that need in order to run the program */
public class ArgsProccess  {

	private int connectivity;
	private float eps;
	private float z;
	private String imagePath;
	private String maskPath;
	
	public int getConnectivity() {
		return connectivity;
	}

	public float getEps() {
		return eps;
	}

	public float getZ() {
		return z;
	}

	public String getImagePath() {
		return imagePath;
	}

	public String getMaskPath() {
		return maskPath;
	}

	/* parsing arguments */
	public ArgsProccess(String[] args , boolean test) {
		try {
			if (test) {
				connectivity = 8;eps = 0.0001f; z = 6; imagePath = "images/red.jpg"; maskPath = "images/red_mask2.jpg";
				return;
			}
			this.imagePath = args[0];
			this.maskPath = args[1];
			this.z = Integer.parseInt(args[2]);
			this.eps = Float.parseFloat(args[3]);
			this.connectivity = Integer.parseInt(args[4]);
						
		} catch (Exception e) {
			printUsage(e.toString());	
		}

		if (connectivity != Constants.FOUR__CONNECTIVITY && connectivity != Constants.EIGHT__CONNECTIVITY) {
			printUsage("connectivity should be 4 or 8");
		}    
	}

	private void printUsage(String e) {
		System.out.println(e + "\nUsage: [image path] [mask path] [z] [epsilon] [pixel connectivity: 4/8]");
		System.exit(1);
	}

	public float weight(Point p1, Point p2) {
		return 1 / ((float) Math.pow(p1.distanceFrom(p2), z) + eps); 
	}
	
}
