import java.awt.*;
import java.awt.event.*;

public class Case{
	private Color fond;
	private Point orig;
	private int largeur,hauteur;
	
	public Case(Color c, Point p, int l, int h){
		fond=c;
		orig=p;
		largeur=l;
		hauteur=h;
		
	}
	
	public Point origine(){
		return orig;
	}
	
	public void dessiner(Graphics g){
		g.setColor(fond);
		g.fillRect(orig.abs(),orig.ord(),largeur,hauteur);
	}
	
}
