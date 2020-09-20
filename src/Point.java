
public class Point {

	private int x;
	private int y;
	private float value;
	
	public Point(int x, int y, float value) {
		this.x = x;
		this.y = y;
		this.value = value;
	}
		
	public double distanceFrom(Point other) {
		 return  Math.sqrt((this.getY() - other.getY()) * (this.getY() - other.getY()) + (this.getX() - other.getX()) * (this.getX() - other.getX()));
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Point)) {
			return false;
		}
		Point other = (Point)obj;
		return x == other.x && y == other.y && value == other.value;
	}
	
	@Override
	public int hashCode() {
		  return ((Float)value).hashCode() + x + y; // ##
	}
	
} 
