import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Case{

	//Attributs
	private int coteCase;//COTECASE qu'on récupère de Damier
	private int UNITE = 1; //Permet de doubler ou pas la vitesse
	private int nbObstacles; //NBOBSTACLES qu'on récupère de Damier
	private int nbCase; //nombre de cases par côté de la grille
	protected Color fond;
	protected Point orig;
	protected int largeur, longueur;
	private static Graphics g;
	private Random r;
	//Utile pour l'extension de mouvement
	private boolean XX; //Si la case va à gauche ou à droite
	private boolean XY; //Si la case fait un mvt horizontal ou vertical
	private boolean XYZ; //Si la case fait un mvt oblique ou pas
	private boolean T; //Si la case fait un mvt aléatoire frénétique spécial ou pas
	private int XXZ; //Quelle est la direction de la case dans le cas d'un mouvement oblique
	
	//Variables utiles pour le mode légendaire
	private boolean isRoundRect; //Permet de faire appel aux graphiques spéciaux
	private int angle; // angle de courbure des carrés
	private int hauteurPic; //hauteur des pics
	private boolean L; //Détermine si une case est en phase de déploiment de pics ou pas
	private int LL; //Si on est dans cette phase, détermine l'étape à laquelle on est
	private static int NBETAPES = 90; //Nombre d'étapes de l'animation des pics
	
	

/********************************** Constructeur ****************************************/	

	public Case(Color f, Point p, int larg, int lng, int cote, int obstacles, int cases){
		fond = f;
		orig = p;
		largeur = larg;
		longueur = lng;
		coteCase = cote;
		nbCase = cases;
		nbObstacles = obstacles; 
		hauteurPic=14; angle=0;
		isRoundRect=false;
		r = new Random();
		
		
		int rand1=r.nextInt(3);
		if(rand1 == 1) XYZ = true;
		else XYZ = false;
		
		int rand2=r.nextInt(4);
		if(rand2 == 1) T = true;
		else T = false;
		
		XXZ = r.nextInt(4);
		
		if(r.nextInt(2) == 1) XY = true;
		else XY = false;
		
		if(r.nextInt(2) == 1) XX = true;
		else XX = false;
		
		LL = 0;
		
		L = false;
		
		
		
	}

	
/********************************** Accesseurs *********************************************/

	public Point origine() {
		return orig;
	}
	
	//Renvoid la taille courante de la case
	public int getTailleCase(){
		if(!isRoundRect) return coteCase;
		else return (int)(hauteurPic*1.3);
	}

/*********************************** Mutateurs *********************************************/	

	public void setOrig(int x, int y) {
		orig.setAbscisse(x);
		orig.setOrdonne(y);
	}
	
/******************************** Bouger : facile et normal *********************************/	
	
	//Sert pour le mouvement de difficulté facile et normale
	public void bougerXY(){
	
		if(XY){ //Mouvement Horizontal
			//Permet d'éviter les parois horizontales
			if (orig.abscisse() > coteCase*(nbCase-1)) XX = false;
			if (orig.abscisse() < 0) XX = true;
			//Pour éviter les cases de départ et d'arrivée+
			if ((orig.abscisse()<(getTailleCase()+1) && orig.ordonne()<=1) || (orig.abscisse()>coteCase*(nbCase-2)+1 && orig.ordonne()>coteCase*(nbCase-1))){
				if(XX) XX = false;
				else XX = true;
			}
			//Déplacement
			if (XX)
				orig.setAbscisse(orig.abscisse()+1*UNITE); //Vers la gauche
			else
				orig.setAbscisse(orig.abscisse()-1*UNITE); //Vers la droite
		}
		else { //Mouvement vertical
			//Permet d'éviter les parois verticales
			if (orig.ordonne() > coteCase*(nbCase-1)) XX = false;
			if (orig.ordonne() < 0) XX = true;
			//Pour éviter les cases de départ et d'arrivée
			if ((orig.abscisse()<=1 && orig.ordonne()<(getTailleCase()+1)) || (orig.abscisse()>coteCase*(nbCase-1) && orig.ordonne()>coteCase*(nbCase-2)+1)){
				if(XX) XX = false;
				else XX = true;
			}
			//Déplacement
			if (XX)
				orig.setOrdonne(orig.ordonne()+1*UNITE); //Vers le haut
			else
				orig.setOrdonne(orig.ordonne()-1*UNITE); //Vers le bas
		}
	}

/******************************** Bouger : difficile ***************************************/	

	//Utile pour bouger une case dans une direction oblique
	public void bougerOblique(int direction){
		switch(direction){
			case 0: orig.setAbscisse(orig.abscisse()+1*UNITE); orig.setOrdonne(orig.ordonne()+1*UNITE); break;
			case 1: orig.setAbscisse(orig.abscisse()+1*UNITE); orig.setOrdonne(orig.ordonne()-1*UNITE); break;
			case 2: orig.setAbscisse(orig.abscisse()-1*UNITE); orig.setOrdonne(orig.ordonne()-1*UNITE); break;
			case 3: orig.setAbscisse(orig.abscisse()-1*UNITE); orig.setOrdonne(orig.ordonne()+1*UNITE); break;
		}
	}
	
	//Sert pour le mouvement de difficulté difficile
	public void bougerXYZ(){
		// 1/3 des cases bougeront de amnière oblique
		if (XYZ){
			//Pour éviter les cases de départ et d'arrivée
			if(orig.ordonne()<=1 && orig.abscisse()<=getTailleCase()+2){
				bougerOblique(0);
				XXZ = 0;
			}
			if(orig.ordonne()<=getTailleCase()+2 && orig.abscisse()<=1){
				bougerOblique(0);
				XXZ = 0;
			}
			if(orig.ordonne()==coteCase*(nbCase-2) && orig.abscisse()==coteCase*(nbCase-1)){
				bougerOblique(2);
				XXZ = 2;
			}
			if(orig.ordonne()==coteCase*(nbCase-1) && orig.abscisse()==coteCase*(nbCase-2)){
				bougerOblique(2);
				XXZ = 2;
			}
			if (orig.ordonne()==orig.abscisse() && (XXZ==2 || XXZ==0)){
				if(r.nextInt(2)==1) XXZ = 1;
				XXZ = 3;
			}
			
			//Traitement des parois
			if(orig.abscisse()<=0){ //Paroi gauche
				//Déplacement
				if (XX){ //Vers bas-droite
					bougerOblique(0); XXZ = 0;
				}
				else{ //Vers haut-droite
					bougerOblique(1); XXZ = 1;
				}			
				//Pour alternet un peu le sens de déplacement
				if (r.nextInt(2)==1) XX = !(XX);
			}
			//Paroi droite
			else if(orig.abscisse()>=coteCase*(nbCase-1)){
				if (XX){ //Vers haut-gauche
					bougerOblique(2); XXZ = 2;
				}
				else{ //Vers bas-gauche
					bougerOblique(3); XXZ = 3;
				}	
				if (r.nextInt(2)==1) XX = !(XX);
			}
			//Paroi haut
			else if(orig.ordonne()<=0){
				if (XX){ //Vers bas-gauche
					bougerOblique(3); XXZ = 3;
				}
				else{ //Vers bas-droite
					bougerOblique(0); XXZ = 0;
				}
				if (r.nextInt(2)==1) XX = !(XX);
			}
			//Paroi bas
			else if(orig.ordonne()>=coteCase*(nbCase-1)){
				if (XX){ //Vers haut-droite
					bougerOblique(1); XXZ = 1;
				}
				else{ //Vers haut-gauche
					bougerOblique(2); XXZ = 2;
				}	
				if (r.nextInt(2)==1) XX = !(XX);
			}
			
			//Les 4 directions obliques
			else if(XXZ==0){
				bougerOblique(0);
			}
			else if(XXZ==1){
				bougerOblique(1);
			}
			else if(XXZ==2){
				bougerOblique(2);
			}
			else if(XXZ==3){
				bougerOblique(3);
			}
			
		}
		// 2/3 des cases bougeront horizontalement ou verticalement
		else{
			bougerXY();
		}
	}
	
/******************************** Bouger : légendaire ******************************************/	
	
	public void pics(){
		isRoundRect=true; //permet d'activer la fonction spéciale dans la fonction dessiner
		L = true;
		//Si le cycle spécial est fini, on traitera la case normalement
		if(LL >= NBETAPES+1){
			L = false;
			LL = 0;
			hauteurPic = 14;
			angle = 0;
			isRoundRect=false;
			
		}
		//Scénario du déploiement des pics
		else{	
			//Phase de déploiement des pics
			if(LL < NBETAPES/5){
				angle += 4;
				hauteurPic += 1;
				LL++;
			}
			//Phase de rétractation des pics
			else if(LL < 2*NBETAPES/5){
				angle -= 4;
				hauteurPic -= r.nextInt(2);
				LL++;
			}
			//Phase de déploiement des pics
			else if(LL < 5*NBETAPES/10){
				angle += 4;
				hauteurPic += 1;
				LL++;
			}
			//Phase de stagnation
			else if(LL < 8*NBETAPES/10){
				LL++;
			}
			//Phase de rétractation des pics
			else{
				angle -= 2;
				hauteurPic -= 1;
				LL++;
			}
		}
	}
	
	//Sert pour le mouvement de difficulté légendaire
	public void bougerL(){
		//UNITE = 2; //histoire de multiplier par 2 les déplacements
		//Si la case est déjà en cours de déploiment de pics on continue
		if(L){
			pics();
		}
		//Sinon si aucune autre case n'est en cours de déploiment de pics, on donne une chance de l'être
		else{
			if(r.nextInt(300)==1){ //1 chance sur 300 d'être une case spéciale pendant un moment
					pics();
			}
		}
		bougerXYZ();
	}
	
	//Permet de dessiner un pic sur une case pour le mode légendaire
	public void dessinerUnPic(int base1X, int base1Y, int base2X, int base2Y, double picX, double picY, Graphics g){
		int[] coordonneX = {(orig.abscisse()+coteCase/2)+base1X, (orig.abscisse()+coteCase/2)+base2X, (orig.abscisse()+coteCase/2)+(int)(hauteurPic*picX)};
		int[] coordonneY = {(orig.ordonne()+coteCase/2)+base1Y, (orig.ordonne()+coteCase/2)+base2Y, (orig.ordonne()+coteCase/2)+(int)(hauteurPic*picY)};
		g.fillPolygon(coordonneX, coordonneY, 3);
	}
	
	//Permet de dessiner des pics sur les cases dangereuses pour le mode légendaire
	public void dessinerPics(Graphics g){
		//pics de la partie haut-droite
		dessinerUnPic(-3, 0, 3, 0, 0, -1, g);
		dessinerUnPic(-2, -1, 2, 1, 0.5, -0.8660, g);
		dessinerUnPic(-1, -2, 1, 2, 0.8660, -0.5, g);
		dessinerUnPic(0, -3, 0, 3, 1, 0, g);
		//pics de la partie bas-droite
		dessinerUnPic(-3, 0, 3, 0, 0, 1, g);
		dessinerUnPic(-2, -1, 2, 1, 0.5, 0.8660, g);
		dessinerUnPic(-1, -2, 1, 2, 0.8660, 0.5, g);
		dessinerUnPic(0, -3, 0, 3, 1, 0, g);
		//pics de la partie haut-gauche
		dessinerUnPic(-3, 0, 3, 0, 0, -1, g);
		dessinerUnPic(-2, 1, 2, -1, -0.5, -0.8660, g);
		dessinerUnPic(-1, 2, 1, -2, -0.8660, -0.5, g);
		dessinerUnPic(0, 3, 0, -3, -1, 0, g);
		//pics de la partie bas-gauche
		dessinerUnPic(-3, 0, 3, 0, 0, 1, g);
		dessinerUnPic(-2, -1, 2, 1, -0.5, 0.8660, g);
		dessinerUnPic(-1, -2, 1, 2, -0.8660, 0.5, g);
		dessinerUnPic(0, -3, 0, 3, -1, 0, g);
	}
	
/******************************** Bouger : Contre la montre **************************************/	
	
	//Utile pour bouger une case dans une direction oblique rapidement + changement de la taille des cases
	public void bougerBizarre(int direction){
		int var=0;
		if(r.nextInt(20)==2) var=1; //Une fois sur 20 on augmente la taille de la case
		switch(direction){
			case 0: orig.setAbscisse(orig.abscisse()+r.nextInt(4));
			orig.setOrdonne(orig.ordonne()+r.nextInt(4)); 
			largeur+=var; longueur+=var; break;
			
			case 1: orig.setAbscisse(orig.abscisse()+r.nextInt(4));
			orig.setOrdonne(orig.ordonne()-r.nextInt(4)); 
			largeur+=var; longueur+=var; break;
			
			case 2: orig.setAbscisse(orig.abscisse()-r.nextInt(4));
			orig.setOrdonne(orig.ordonne()-r.nextInt(4)); 
			largeur+=var; longueur+=var; break;
			
			case 3: orig.setAbscisse(orig.abscisse()-r.nextInt(4));
			orig.setOrdonne(orig.ordonne()+r.nextInt(4)); 
			largeur+=var; longueur+=var; break;
		}
	}
	
	//Sert pour le mouvement de difficulté contre la montre
	public void bougerT(){
		//UNITE = 2; //histoire de multiplier par 2 les déplacements
		// 1/4 des cases bougeront de la nouvelle manière
		if (T){
			if(orig.abscisse()<0) orig.setAbscisse(0);
			if(orig.ordonne()<0) orig.setOrdonne(0);
			if(orig.abscisse()>coteCase*(nbCase-1)) orig.setAbscisse(coteCase*(nbCase-1));
			if(orig.ordonne()>largeur*(nbCase-1)) orig.setOrdonne(largeur*(nbCase-1));
			
			int rand1=r.nextInt(4);
			//Mouvements aléatoires frénétiques
			if(rand1==0){
				bougerBizarre(0);
			}
			else if(rand1==1){
				bougerBizarre(1);
			}
			else if(rand1==2){
				bougerBizarre(2);
			}
			else if(rand1==3){
				bougerBizarre(3);
			}
			
		}
		// 3/4 des cases bougeront horizontalement, verticalement, ou de manière oblique
		else{
			bougerXYZ();
		}
	}

/************************************ dessiner ***************************************************/	
	
	//Permet de dessiner la case
	public void dessiner(Graphics g){
		g.setColor(fond);
		if(isRoundRect){ //Dans le cas de carrés arrondis avec des pics
			g.fillRoundRect(orig.abscisse(), orig.ordonne(), largeur, longueur, angle, angle);
			dessinerPics(g);
		}
		else{ //Cases carrées classiques
			g.fillRect(orig.abscisse(), orig.ordonne(), largeur, longueur);
		}
	}

}
