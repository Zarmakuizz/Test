import java.awt.*;
import java.awt.event.*;
import java.lang.Math;

public class Balle{
	private Color fond;
	private Point orig;
	private int diametre;
	
	public Balle(Color c, Point p, int d){
		fond=c;
		orig=p;
		diametre=d;
	}
	
	public void deplacer(int x, int y){
		orig.setPoint(x-14,y-14);
	}
	public boolean appartient(int x,int y){
		if ( this.distance(new Point(x,y)) < diametre ){
			return true;
		} else {
			return false;
		}
	}
	public double distance(Point p){
		return Math.sqrt( (p.abs()-orig.abs()+(diametre/2))*(p.abs()-orig.abs()+(diametre/2)) + (p.ord()-orig.ord()+(diametre/2))*(p.ord()-orig.ord()+(diametre/2)) );
	}
	public void dessiner(Graphics g){
		g.setColor(fond);
		g.fillOval(orig.abs(),orig.ord(),diametre,diametre);
	}
}
