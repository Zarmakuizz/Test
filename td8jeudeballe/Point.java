

public class Point {
	private int x;
	private int y;
	
	public Point(int a, int b){
		x=a;
		y=b;
	}
	public Point(){
		x=0;
		y=0;
	}
	public int abs(){
		return x;
	}
	public int ord(){
		return y;
	}
	public void setPoint(int ab, int or){
		x=ab;
		y=or;
	}
	public String toString(){
		return "("+this.abs()+";"+this.ord()+")";
	}
}
