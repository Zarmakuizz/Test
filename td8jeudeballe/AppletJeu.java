import java.awt.Button;
import java.awt.Panel;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.applet.Applet;
import java.awt.Label;
import javax.swing.JOptionPane;

public class AppletJeu extends Applet implements ActionListener {

	private static Damier d;
	private static DamierScore ds;
	private static Panel p2;
	private static Panel p3;
	private static Panel p4;
	private static Panel p5;
	private static Label regle;
	private static Label regle2;
//Permet de recommancer la partie quel que soit le mode de jeu
	private static Button recommencer;
//Bouton de choix du mode de jeu
	private static boolean modeActuel=true; //Permet de savoir le mode actuel de jeu
	private static Label mode;
	private static Button modeParcours; // Parcours : atteindre la case d'arrivée
	private static Button modeScore; // Score : amasser les pierres
//Boutons de difficulté
	private static Panel p1;
	private static Label difficulte;
	private static Button tresFacile;
	private static Button facile;
	private static Button normal;
	private static Button hard;
	private static Button legendaire;
	private static Button apocalypse;
	private int type; //Type de difficulté actuelle
	private boolean isblockApocalypse; //Détermine si le niveau Apocalypse est bloqué


	public void init() {

		setLayout(new BorderLayout());
		type = 1;
		isblockApocalypse = true;
		//panel de droite
		p1 = new Panel();
		p1.setLayout(new GridLayout(12, 1));
		add(p1, BorderLayout.EAST);
		p1.setVisible (true);
		//Panel de gauche et ses sous-panels
		p2 = new Panel();
		p3 = new Panel();
		p4 = new Panel();
		p5 = new Panel();
		p2.setLayout(new BorderLayout());
		p3.setLayout(new FlowLayout());
		p4.setLayout(new FlowLayout());
		p5.setLayout(new BorderLayout());
		add(p2, BorderLayout.WEST);
		p2.setVisible (true);
		p2.add(p3, BorderLayout.NORTH);
		p2.add(p4, BorderLayout.SOUTH);
		p3.setVisible (true);
		p4.setVisible (true);
		p5.setVisible (true);
		//Bouton du choix de mode de jeu
		mode = new Label("Mode : Parcours", Label.CENTER);
		modeParcours = new Button("Parcours");
		modeScore = new Button("Score");
		p1.add(mode);
		p1.add(modeParcours);
		p1.add(modeScore);
		//Boutons de difficulté
		tresFacile = new Button("Super facile");
		facile = new Button("Facile");
		normal = new Button("Normal");
		hard = new Button("Difficile");
		legendaire = new Button("Légendaire");
		apocalypse = new Button(" ??? ");
		difficulte = new Label("     Difficulté : Facile    ", Label.CENTER);
		p1.add(difficulte);
		p1.add(tresFacile);
		p1.add(facile);
		p1.add(normal);
		p1.add(hard);
		p1.add(legendaire);
		p1.add(apocalypse);
		//Bouton pour recommencer
		recommencer = new Button("Recommencer");
		p3.add(recommencer);
		//Damier
		d = new Damier(17, 17, type);
		p4.add(d);

		//Texte de règles
		regle = new Label("Déplacez la balle verte vers la case bleue d'arrivée, évitez à tout prix les cases jaunes meurtrières !", Label.CENTER);
		regle2 = new Label(" ", Label.CENTER);
		p5.add(regle, BorderLayout.NORTH);
		p5.add(regle2, BorderLayout.SOUTH);
		add(p5, BorderLayout.SOUTH);

		//Les listeners
		recommencer.addActionListener(this);
		modeParcours.addActionListener(this);
		modeScore.addActionListener(this);
		tresFacile.addActionListener(this);
		facile.addActionListener(this);
		normal.addActionListener(this);
		hard.addActionListener(this);
		legendaire.addActionListener(this);
		apocalypse.addActionListener(this);

		//Hack impropre pour avoir des boutons pas trop gros
		p1.add(new Label(" ", Label.CENTER));
		p1.add(new Label(" ", Label.CENTER));

		setVisible (true);


	}

	//Permet de redéfinir le Damier
	public void newDamier() {
		if (modeActuel) {
			p4.removeAll();
			d = null;
			ds = null;
			d = new Damier(17, 17, type);
			p4.add(d);
		} else {
			p4.removeAll();
			d = null;
			ds = null;
			ds = new DamierScore(17, 17, type);
			p4.add(ds);
		}
	}

	//Permet de régénérer les contenu du panel p1 si on change de mode de jeu
	public void newBoutons() {
		if (modeActuel) { //Si c'est le mode Parcours
			p1.removeAll();
			p1.add(mode);
			p1.add(modeParcours);
			p1.add(modeScore);
			p1.add(difficulte);
			p1.add(tresFacile);
			p1.add(facile);
			p1.add(normal);
			p1.add(hard);
			p1.add(legendaire);
			p1.add(apocalypse);
			p1.add(new Label(" ", Label.CENTER));
			p1.add(new Label(" ", Label.CENTER));
		} else { //Si c'est le mode Score
			p1.removeAll();
			p1.add(mode);
			p1.add(modeParcours);
			p1.add(modeScore);
			p1.add(difficulte);
			p1.add(facile);
			p1.add(normal);
			p1.add(hard);
			p1.add(legendaire);
			p1.add(apocalypse);
			p1.add(new Label(" ", Label.CENTER));
			p1.add(new Label(" ", Label.CENTER));
			p1.add(new Label(" ", Label.CENTER));
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == recommencer) {
			if (modeActuel) d.restart();
			else newDamier();
		} else if (e.getSource() == modeParcours) {
			mode.setText("Mode : Parcours");
			modeActuel = true;
			//On remet la difficulté à facile par défaut
			type = 1;
			difficulte.setText("Difficulté : Facile");
			newDamier();
			regle.setText("Déplacez la balle verte vers la case bleue d'arrivée, évitez à tout prix les cases jaunes meurtrières !");
			regle2.setText(" ");
			newBoutons();
		} else if (e.getSource() == modeScore) {
			mode.setText("Mode : Score");
			modeActuel = false;
			//On remet la difficulté à facile par défaut
			type = 1;
			difficulte.setText("Difficulté : Facile");
			newDamier();
			regle.setText("Déplacez la balle verte pour absorber les pierres précieuses. Ces joyaux sont d'une grande rareté.");
			regle2.setText("Collectez les amulettes de pouvoir bleues et évitez les cases jaunes, ou vous mourrez !");
			newBoutons();
		} else if (e.getSource() == tresFacile) {
			difficulte.setText("Difficulté : Super facile");
			type = 0;
			newDamier();
		} else if (e.getSource() == facile) {
			difficulte.setText("Difficulté : Facile");
			type = 1;
			newDamier();
		} else if (e.getSource() == normal) {
			difficulte.setText("Difficulté : Normale");
			type = 2;
			newDamier();
		} else if (e.getSource() == hard) {
			difficulte.setText("Difficulté : Difficile");
			type = 3;
			newDamier();
		} else if (e.getSource() == legendaire) {
			difficulte.setText("Difficulté : Légendaire");
			type = 4;
			newDamier();
		} else if (e.getSource() == apocalypse) {
			//Si le niveau est bloqué
			if (isblockApocalypse) {
				String inputWord = JOptionPane.showInputDialog(this, "Entrez le mot de passe de ce niveau");
				if (inputWord.equals("toast")) {
					isblockApocalypse = false;
					JOptionPane.showMessageDialog(this, "C'est bien ça, vous pouvez maintenant accéder à ce niveau'");
					difficulte.setText("Apocalypse");
					apocalypse.setLabel("Apocalypse");
					type = 5;
					newDamier();
					regle.setText("Déplacez la balle verte vers la case bleue d'arrivée, évitez à tout prix les cases jaunes meurtrières !");
					regle2.setText("Certaines cases hostiles se développent de manière anormale, vous n'avez que peu de temps pour espérer agir.");
				} else {
					JOptionPane.showMessageDialog(this, "Essaye encore =)");
				}
			}
			//Si le mot de passe a été donné le niveau est débloqué
			else {
				difficulte.setText("Apocalypse");
				apocalypse.setLabel("Apocalypse");
				type = 5;
				newDamier();
				regle.setText("Déplacez la balle verte vers la case bleue d'arrivée, évitez à tout prix les cases jaunes meurtrières !");
				regle2.setText("Certaines cases hostiles se développent de manière anormale, vous n'avez que peu de temps pour espérer agir.");
			}
		}
	}



}

