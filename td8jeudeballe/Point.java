public class Point {
	
	private int abs;
	private int ord;
	
	
	public Point (int a, int b){
		abs = a;
		ord = b;
	}

	public Point (){
		abs = 0;
		ord = 0;
	}

	public int abscisse(){
		return abs;
	}

	public int ordonne(){
		return ord;
	}
	
	public void setAbscisse(int ab){
		abs=ab;
	}
	
	public void setOrdonne(int or){
		ord=or;
	}
	
	public String toString(){
		return ("abscisse: " + abs + " " + " ordonnée: " + ord);
	}
	
	
}


