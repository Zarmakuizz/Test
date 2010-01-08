import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;
import java.lang.Math;

public class Case{

	//Attributs
	private int coteCase;//COTECASE qu'on récupère de Damier
	private int UNITE = 1; //Permet de doubler ou pas la vitesse, pour une fonctionnalité à venir
	private int nbObstacles; //NBOBSTACLES qu'on récupère de Damier
	private int nbCase; //nombre de cases par côté de la grille
	protected Color fond;
	protected Point orig;
	protected int largeur, longueur;
	private static Graphics g;
	private Random r;
	//Utile pour l'extension de mouvement
	private boolean T; //Si la case fait un mouvement aléatoire frénétique spécial ou pas
	private boolean XYZ; //Si la case fait un mouvement oblique ou pas
	private int XXZ; //Indique la direction de la case dans le cas d'un mouvement oblique
	private boolean XY; //Si la case fait un mvt horizontal ou vertical
	private boolean XX; //Si la case va à gauche/en haut ou à droite/en bas
	
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
		hauteurPic=coteCase/2; 
		angle=0;
		isRoundRect=false; // Ne pas activer les graphismes spéciaux (réservés au mode légendaire)
		r = new Random();
		
		//Déterminer aléatoirement le comportement de la case
		// Mouvement oblique ? En Difficile et supérieurs, minoritaire devant le mouvement tremblant
		if(r.nextInt(3) == 1){
			XYZ = true;
			XXZ = r.nextInt(4); // Le sens du mouvement
		} else XYZ = false;
		// Mouvement tremblant ? Uniquement en mode Apocalypse
		if(r.nextInt(4) == 1) T = true;
		else T = false;
		// Mouvement dans quel axe ? Minoritaire devant les mouvements précédents
		XY = r.nextBoolean();
		// Mouvement dans quel sens ?
		XX = r.nextBoolean();
		
		// Aucune case, dans le mode Légendaire, n'a déjà déployé ses pics avant le début de la partie
		L = false;
		LL = 0;
		
	}
	
/********************************** Accesseurs *********************************************/
	public Point origine() {
		return orig;
	}
	
	public int getTailleCase(){
		if(!isRoundRect) return coteCase;
		else return (int)(hauteurPic*1.3);
	}

/*********************************** Modificateurs *********************************************/	

	public void setOrig(int x, int y) {
		orig.setAbscisse(x);
		orig.setOrdonne(y);
	}
	
/*********************** Bouger : mouvements horizontaux/verticaux à partir de facile ***********************/	
	
	//Sert pour le mouvement de difficulté facile et normale
	public void bougerXY(){
	
		if(XY){ // En cas de mouvement horizontal
			// Changer le mouvement si la case "touche" une paroi
			if (orig.abscisse() > coteCase*(nbCase-1)) XX = false;
			if (orig.abscisse() < 0) XX = true;
			//Pour éviter les cases de départ et d'arrivée
			if ((orig.abscisse()<(getTailleCase()+1) && orig.ordonne()<=1) || (orig.abscisse()>coteCase*(nbCase-2)+1 && orig.ordonne()>coteCase*(nbCase-1))){
				if(XX) XX = false;
				else XX = true;
			}
			// Régler le sens du mouvement
			if (XX)
				orig.setAbscisse(orig.abscisse()+1*UNITE); //Vers la gauche
			else
				orig.setAbscisse(orig.abscisse()-1*UNITE); //Vers la droite
		}
		else { // En cas de mouvement vertical
			// Changer le mouvement si la case "touche" une paroi
			if (orig.ordonne() > coteCase*(nbCase-1)) XX = false;
			if (orig.ordonne() < 0) XX = true;
			//Pour éviter les cases de départ et d'arrivée
			if ((orig.abscisse()<=1 && orig.ordonne()<(getTailleCase()+1)) || (orig.abscisse()>coteCase*(nbCase-1) && orig.ordonne()>coteCase*(nbCase-2)+1)){
				if(XX) XX = false;
				else XX = true;
			}
			// Régler le sens du mouvement
			if (XX)
				orig.setOrdonne(orig.ordonne()+1*UNITE); //Vers le haut
			else
				orig.setOrdonne(orig.ordonne()-1*UNITE); //Vers le bas
		}
	}

/*********************** Bouger : mouvement oblique ajouté à partir de difficile **************************/	

	//Utile pour bouger une case dans une direction oblique
	public void bougerOblique(int direction){
		switch(direction){
			case 0: orig.setAbscisse(orig.abscisse()+1*UNITE); orig.setOrdonne(orig.ordonne()+1*UNITE); break;
			case 1: orig.setAbscisse(orig.abscisse()+1*UNITE); orig.setOrdonne(orig.ordonne()-1*UNITE); break;
			case 2: orig.setAbscisse(orig.abscisse()-1*UNITE); orig.setOrdonne(orig.ordonne()-1*UNITE); break;
			case 3: orig.setAbscisse(orig.abscisse()-1*UNITE); orig.setOrdonne(orig.ordonne()+1*UNITE); break;
		}
	}
	
	//Sert pour le mouvement de difficulté Difficile
	public void bougerXYZ(){
		// 1/3 des cases bougeront de manière oblique
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
				//Pour alterner un peu le sens de déplacement
				if (r.nextBoolean()) XX = !(XX);
			}
			//Paroi droite
			else if(orig.abscisse()>=coteCase*(nbCase-1)){
				if (XX){ //Vers haut-gauche
					bougerOblique(2); XXZ = 2;
				}
				else{ //Vers bas-gauche
					bougerOblique(3); XXZ = 3;
				}	
				if (r.nextBoolean()) XX = !(XX);
			}
			//Paroi haut
			else if(orig.ordonne()<=0){
				if (XX){ //Vers bas-gauche
					bougerOblique(3); XXZ = 3;
				}
				else{ //Vers bas-droite
					bougerOblique(0); XXZ = 0;
				}
				if (r.nextBoolean()) XX = !(XX);
			}
			//Paroi bas
			else if(orig.ordonne()>=coteCase*(nbCase-1)){
				if (XX){ //Vers haut-droite
					bougerOblique(1); XXZ = 1;
				}
				else{ //Vers haut-gauche
					bougerOblique(2); XXZ = 2;
				}	
				if (r.nextBoolean()) XX = !(XX);
			}
			// Sinon on continue le même mouvement que précédemment 
			else bougerOblique(XXZ);
		}
		// 2/3 des cases bougeront horizontalement ou verticalement
		else{
			bougerXY();
		}
	}
	
/************************* Bouger : changement de forme des cases pour Légendaire ***************************/	
	
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
		//Si la case est déjà en cours de déploiment de pics on continue
		if(L){
			pics();
		//Sinon si aucune autre case n'est en cours de déploiment de pics, on donne une chance de l'être pendant un moment
		} else if(r.nextInt(300)==1){ //1 chance sur 300 à chaque appel de bougerL, pour éviter d'avoir trop de pics à l'écran
				pics();
		}
		bougerXYZ(); // La case doit continuer à se mouvoir
	}
	
	//Permet de dessiner un pic sur une case pour le mode légendaire
	public void dessinerUnPic(int base1X, int base1Y, int base2X, int base2Y, double picX, double picY, Graphics g){
	// Retenir les coordonnées de chacun des 3 points du pic (un triangle) à dessiner
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
	
/****************************** Bouger : en difficulté Apocalypse ***********************************/	
	
	//Utile pour bouger une case dans une direction oblique rapidement + changement de la taille des cases
	public void bougerBizarre(int direction){
		int var=0;
		if(r.nextInt(20)==0) var=2; //Une fois sur 20 on augmente la taille de la case
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
	
	//Sert pour le mouvement de difficulté Apocalypse
	public void bougerT(){
		// 1/4 des cases bougeront de la nouvelle manière
		if (T){
			if(orig.abscisse()<0) orig.setAbscisse(0);
			if(orig.ordonne()<0) orig.setOrdonne(0);
			if(orig.abscisse()>coteCase*(nbCase-1)) orig.setAbscisse(coteCase*(nbCase-1));
			if(orig.ordonne()>largeur*(nbCase-1)) orig.setOrdonne(largeur*(nbCase-1));
			
			//Mouvements aléatoires frénétiques
			bougerBizarre(r.nextInt(4));
			
		}
		// 3/4 des cases bougeront horizontalement, verticalement, ou de manière oblique
		else{
			bougerXYZ();
		}
	}

/************************************ dessiner **********************************************/	
	
	//Permet de dessiner la case
	public void dessiner(Graphics g){
		g.setColor(fond);
		if(isRoundRect){ //Dans le cas de carrés arrondis avec des pics
			g.fillRoundRect(orig.abscisse(), orig.ordonne(), largeur, longueur, angle, angle);
			dessinerPics(g);
		}
		else{ //Dans le cas de cases carrées classiques
			g.fillRect(orig.abscisse(), orig.ordonne(), largeur, longueur);
		}
	}

}
