import java.awt.*;
import java.awt.event.*;

public class Jeu extends Frame{
	private MenuBar barre;
	private Menu mgestion;
	private MenuItem replay;
	private MenuItem quit;
	
	public Jeu(){
		addWindowListener(new WindowAdapter(){ public void windowClosing(WindowEvent e){ System.exit(0);}});
		setVisible(true);
		setTitle("Jeu");
		cMenu();//Appeler une fonction qui gère la construction du menu
		
		add(new Damier(15,15));
		pack();
	}
	
	private void cMenu(){
		barre=new MenuBar();
		mgestion=new Menu("Gestion");
		replay=new MenuItem("Recommencer");
//		replay.addActionListener(new ActionReplay());
		quit=new MenuItem("Quitter");
		quit.addActionListener(new ActionQuitter());
		mgestion.add(replay);
		mgestion.add(quit);
		barre.add(mgestion);
		setMenuBar(barre);
	}
}


class ActionQuitter implements ActionListener {
	public void actionPerformed(ActionEvent e){
		System.exit(0);
	}
}
//class ActionQuitter implements ActionListener {
//	public void actionPerformed(ActionEvent e){
//		//Quelque chose...
//	}
//}
