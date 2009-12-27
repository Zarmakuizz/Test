import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class Damier extends Canvas implements MouseListener,MouseMotionListener {
	private static int COTECASE = 30;
	private int largDamier;
	private int hautDamier;
	private static int NBOBSTACLES = 20;
	private Case[] lesObstacles;
	private Random r;
	private int lcase;
	private int hcase;
	private Case arrivee;
	private Balle ball;
	
	public Damier(int larg, int haut) {
		largDamier=larg;
		hautDamier=haut;
		setSize(largDamier*COTECASE+1,hautDamier*COTECASE+1);
		r=new Random();
		ball = new Balle(Color.decode("#00ff00"),new Point(1,1),COTECASE-2);
		this.addMouseListener(this);
		lesObstacles = new Case[NBOBSTACLES];
		for (int i=0;i<NBOBSTACLES;i++){
			lcase=r.nextInt(largDamier)*COTECASE+1;
			hcase=r.nextInt(hautDamier)*COTECASE+1; 
			lesObstacles[i]=new Case(Color.yellow, new Point(lcase,hcase),COTECASE-1,COTECASE-1 );
		}
		arrivee=new Case(Color.blue, new Point((largDamier-1)*COTECASE+1,(hautDamier-1)*COTECASE+1), COTECASE-1,COTECASE-1);

	}

	public void paint(Graphics g) {
		g.setColor(Color.decode("#000000"));
		g.fillRect(0,0, COTECASE*largDamier, COTECASE*hautDamier);
		for (int i=0;i<=largDamier;i++) {
			g.setColor(Color.decode("#FFFFFF"));
			g.drawLine(i*COTECASE,0,COTECASE*i,COTECASE*hautDamier);
		}
		for (int i=0;i<=hautDamier;i++) {
			g.setColor(Color.decode("#FFFFFF"));
			g.drawLine(0,i*COTECASE,COTECASE*largDamier,i*COTECASE);
		}
		for (int i=0;i<NBOBSTACLES;i++){
			lesObstacles[i].dessiner(g);
		}
		arrivee.dessiner(g);
		ball.dessiner(g);
	}
	
		//méthode inutile
	public void mouseExited(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {}
	public void mouseMoved(MouseEvent e) {}
	
	public void mousePressed(MouseEvent e){
		if (ball.appartient(e.getX(),e.getY())){
			this.addMouseMotionListener(this);
		}
	}

	public void mouseDragged(MouseEvent e){
		if (ball.appartient(arrivee.origine().abs(),arrivee.origine().ord())){
			System.out.println("C'est gagné !");
		}else{
			boolean contact=false;
			for(int i=0,i<lesObstacles.length,i++){
				// if ball.appartient(obstacle.abs,obstacle.ord) { contact = True } if contact=TRUE { défaite }
			}
			ball.deplacer(e.getX(),e.getY());
		}
		repaint();
	}

} // End of class Damier

	
	

