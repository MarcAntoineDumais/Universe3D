package menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

/**
 * Cree la fenetre d'aide.
 * @author guillaume bellemare-roy
 */
public class MenuAide extends JFrame implements Serializable  {

	private static final long serialVersionUID = -1700937314387605080L;
	private JPanel contentPane;
	private JTabbedPane tabbedPane;
	private JPanel panelContenuScientifique, panelAPropos, panelInstructions, panelSources;
	private JScrollPane scrollPaneContenuScientifique, scrollPaneAPropos, scrollPaneInstructions, scrollPaneSources;
	private JTextArea textAreaInstructions, textAreaContenuScientifique, textAreaAPropos, textAreaSources;
	private JButton buttonSuivant;
	private JFrame instructionImage;
	
	public MenuAide() {
		setTitle("Aide");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 590, 417);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 0, 575, 344);
		contentPane.add(tabbedPane);
		
		panelInstructions = new JPanel();
		tabbedPane.addTab("Instructions", null, panelInstructions, null);
		panelInstructions.setLayout(null);
		
		scrollPaneInstructions = new JScrollPane();
		scrollPaneInstructions.setBounds(0, 0, 570, 316);
		panelInstructions.add(scrollPaneInstructions);
		
		textAreaInstructions = new JTextArea();
		textAreaInstructions.setEditable(false);
		textAreaInstructions.setText("Bienvenue dans notre simulateur d\u2019univers en 3D !\r\n\r\nUne fois que vous avez appuyez sur \u00AB commencer \u00BB vous avez trois choix :\r\n-\tCr\u00E9\u00E9 un nouvel univers contenant aucun astre\r\n-\tCharger le syst\u00E8me solaire\r\n-\tCharger un univers d\u00E9j\u00E0 fait par un utilisateur\r\n\r\n\r\nUne fois dans un univers vous pouvez :\r\nInteragir avec les diff\u00E9rents outils pr\u00E9sents \u00E0 l\u2019\u00E9cran ou interagir avec les astres (si pr\u00E9sent).   \r\nPour ajouter/cr\u00E9er un astre vous devez mettre l\u2019application sur pause (Voir explication de l\u2019outil temps\r\n et l'outil d'\u00E9dition) et tant que l\u2019application n\u2019est pas remis en marche rien ne va bouger ! \r\n\r\n\r\nL\u2019outil temps sert \u00E0 : \r\n-\tChanger la vitesse du temps de l\u2019animation par rapport \u00E0 le temps r\u00E9el\r\n-\tMettre l\u2019application sur pause et la remettre en marche\r\n-\tVoir la date par rapport avec le temps dans l\u2019application\r\n\r\n\r\nL\u2019outil d\u2019\u00E9dition sert \u00E0 :\r\n-\tAjouter un astre en orbite autour de l\u2019astre s\u00E9lectionn\u00E9\r\n-\tAnnuler l\u2019ajout d\u2019un astre\r\n\r\n\r\nL\u2019outil Modification sert \u00E0 :\r\n-\tVoir et changer le nom de l\u2019astre pr\u00E9sentement s\u00E9lectionn\u00E9\r\n-\tVoir et changer la masse de l\u2019astre pr\u00E9sentement s\u00E9lectionn\u00E9\r\n-\tVoir et changer le rayon de l\u2019astre pr\u00E9sentement s\u00E9lectionn\u00E9\r\n-\tVoir et changer la v\u00E9locit\u00E9 de l\u2019astre pr\u00E9sentement s\u00E9lectionn\u00E9\r\n-\tVoir et changer la couleur  de la trace et de l\u2019astre pr\u00E9sentement s\u00E9lectionn\u00E9\r\n\r\n\r\nDurant l\u2019ajout d\u2019un astre, Vous pouvez s\u00E9lectionner la texture de l\u2019astre que vous \r\nvoulez ajouter et l\u2019astre s\u00E9lectionner a d\u00E9j\u00E0 des param\u00E8tres de base\r\n(c\u2019est-\u00E0-dire : les param\u00E8tres de l\u2019astre en r\u00E9alit\u00E9).\r\n\r\nA tout instant durant une simulation, vous pouvez sauvegard\u00E9 votre univers ou\r\nen charger un nouveau \u00E0 partir du menu \"Fichier\"\r\nVous pouvez aussi acc\u00E9der au menu de options \u00E0 partir de \"Fichier\". \r\n\r\n\r\nRaccourci Clavier :\r\nP - Une fois qu'un astre a le focus, cette touche ajoute 100 particules\r\nY- Cette touche permet de s\u00E9lectionner l'astre suivant sans y mettre le focus\r\nT- Cette touche permet de s\u00E9lectionner l'astre pr\u00E9c\u00E9dant sans y mettre le focus\r\nU- Cette touche permet de mettre le focus sur l'astre pr\u00E9sentement s\u00E9lectionner\r\nEspace- Cette touche met la vitesse le l'astre s\u00E9lectionner \u00E0 0\r\n\u00C9chape- Cette touche retire le focus sur un astre ");
		scrollPaneInstructions.setViewportView(textAreaInstructions);
		
		panelContenuScientifique = new JPanel();
		tabbedPane.addTab("Contenu scientifique", null, panelContenuScientifique, null);
		panelContenuScientifique.setLayout(null);
		
		scrollPaneContenuScientifique = new JScrollPane();
		scrollPaneContenuScientifique.setBounds(0, 0, 570, 316);
		panelContenuScientifique.add(scrollPaneContenuScientifique);
		
		textAreaContenuScientifique = new JTextArea();  //contenu scientifique de l'application
		textAreaContenuScientifique.setText("Notre application utilise principalement les \u00E9quations de la gravit\u00E9 \r\npour faire tous les calculs entres les plan\u00E8tes et \u00E9toiles. Nous offrons \r\nla possibilit\u00E9 d\u2019utiliser diff\u00E9rentes m\u00E9thode de calculs (i.e. Euler,\r\nEuler invers\u00E9 et RK4). On a aussi le choix de calculer la gravit\u00E9 avec \r\nune m\u00E9thode qui diminue largement la quantit\u00E9 de calcul, c\u2019est-\u00E0-dire\r\nla m\u00E9thode de Barnes-Hut. \r\n\r\n\r\nLa m\u00E9thode de calcul d'euler consiste a utiliser les donn\u00E9es initiales, \r\nc'est-\u00E0-dire, la vitesse initiale (vx0), la position initiale (x0) et le temps\r\ninitiale (t0) pour \u00E9valuer la position et la vitesse finale. Cette technique\r\npr\u00E9supose que l'objet se d\u00E9place \u00E0 vitesse constante et \u00E9value la vitesse\r\nfinale avec une acc\u00E9l\u00E9ration constante (ax0).\r\n\r\nCette m\u00E9thode a ses plus et ses moins. Par exemple, cette m\u00E9thode est \r\ntr\u00E8s facile a implanter et demande peu de calculs, mais devient rapidement\r\nimpr\u00E9cise lorsque le delta t (\u2206t) est \u00E9lev\u00E9. Aussi, plus le delta t (\u2206t) est petit\r\nplus cette m\u00E9thode demande de calcul, mais plus elle devient pr\u00E9cise.\r\n\r\n\r\n\r\nLa m\u00E9thode de calcul d'euler invers\u00E9 est pareil comme la m\u00E9thode d'euler \r\nnormal, sauf qu'au lieu de calculer avec les conditions initiales, elle propose\r\nde calculer la vitesse finale (vx) avec l'acc\u00E9l\u00E9ration initiale (ax0) constante et\r\nde d\u00E9placer l'objet avec une vitesse constante \u00E9gale \u00E0 la vitesse finale (vx).\r\n\r\nTout comme la m\u00E9thode d'euler normal, cette m\u00E9thode est facile \u00E0 implanter\r\net rapide a calculer, mais cette m\u00E9thode diverge rapidement pour des probl\u00E8mes\r\nfaisant intervenir la vitesse dans la d\u00E9finition de l'acc\u00E9l\u00E9ration.\r\n\r\n\r\n\r\nLa m\u00E9thode de calcul RK4 (Runge-Kutta d'ordre 4) propose d'\u00E9valuer une position et\r\nune vitesse interm\u00E9diaire afin de mieux estimer la position finale. Cet algorithme\r\nfait en sorte qu'on doit calculer les valeurs interm\u00E9diaires \u00E0 un temps situ\u00E9 \r\n\u00E0 mi-chemin dans l'it\u00E9ration (t0 + \u2206t/2). \r\n\r\nCet algorithme converge vers la solution exacte rapidement avec un grand \u2206t.\r\nPar contre, il est moins efficace pour calculer des oscillation comme dans un \r\nsyst\u00E8me bloc ressort.\r\n\r\n\r\n\r\nLa m\u00E9thode d'approximation du centre de masse de Barnes-hut est tr\u00E8s bien pens\u00E9.\r\nLe principe est simple, On divise notre univers en cube et si'il y a plus d'un astre\r\non divise le cube en quatre autres petits cube dans le grand cube pour avoir qu'un\r\nseul astre dans chaque cube. On r\u00E9pette le proc\u00E9ssus jusqu'\u00E0 temps que tout les astres\r\nsoient dans un et un seul dans un cube. Ensuite, on peut approximer la masse de chaque\r\nastres avec le centre de masse de chaque cube.\r\n\r\nCet algorithme permet de diminuer le nombre de calcul de fa\u00E7on significative, c'est-\u00E0-dire\r\non passe de 2^n nombres de calcul \u00E0 log 2 en base n. Cet important de noter que l'approximation \r\nde barnes-hut n'est pas une fa\u00E7on de calculer la gravit\u00E9, mais un fa\u00E7on d'aider nos algorithme de \r\ncalcul (euler, euler invers\u00E9 ou RK4) \u00E0 calculer la gravit\u00E9 plus rapidement.\r\n\r\n\r\n");
		textAreaContenuScientifique.setEditable(false);
		scrollPaneContenuScientifique.setViewportView(textAreaContenuScientifique);
		
		panelAPropos = new JPanel();
		tabbedPane.addTab("\u00C0 propos", null, panelAPropos, null);
		panelAPropos.setLayout(null);
		
		scrollPaneAPropos = new JScrollPane();
		scrollPaneAPropos.setBounds(0, 0, 570, 351);
		panelAPropos.add(scrollPaneAPropos);
		
		textAreaAPropos = new JTextArea(); //A Propos de l'application
		textAreaAPropos.setText("Nous nous sommes beaucoup inspirer de l'application \"Universe Sandbox\".\r\nCette application est essentiellement un simulateur d'univers tr\u00E8s d\u00E9taill\u00E9s\r\navec beaucoup de fonctionalit\u00E9s diff\u00E9rentes. Avec cette source d'inspiration\r\nnous avons r\u00E9ussi \u00E0 faire notre projet comme il est pr\u00E9sentement. Notre projet \r\nest un simulateur d'univers en trois dimension qui repr\u00E9sente la r\u00E9alit\u00E9. \r\n\r\nNotre programme offre principalement la possibilit\u00E9 de voir et modifi\u00E9\r\nnotre syst\u00E8me solaire. On peut aussi voir diff\u00E9rentes m\u00E9thodes de calcul\r\nde la gravit\u00E9 et de la vitesse et position. Nous offrons aussi la possibilit\u00E9\r\nde pouvoir cr\u00E9er un nouveau syst\u00E8me solaire dans l'univers et de le \r\nsauvegarder.\r\n\r\nIl y a trois d\u00E9veloppeurs pour ce projet: Marc-Antoine Dumais, \r\nDavid-Olivier Dupperon et Guillaume Bellemare-Roy. Marc-Antoine\r\na principalement travaill\u00E9 sur tout l'aspect 3D ainsi que la correction\r\nde diff\u00E9rent bug, Davvid-Olivier a travailler sur la partie calcul et \r\noptimisation des calculs ainsi qu'\u00E0 leurs impl\u00E9mentations. Puis, \r\nGuillaume a fait tout l'aspect graphique et esth\u00E9tique, ainsi que\r\nles outils et les menus. \r\n\r\nNotre application a comme but principal d'\u00EAtre un outil d'aide\r\npour faire une simulation d'un syst\u00E8me solaire ou d'un syst\u00E8me\r\nplan\u00E8tes/\u00E9toiles. L'application n'est pas concu pour \u00EAtre r\u00E9cr\u00E9atif\r\n(principalement), mais d'\u00EAtre utiliser acad\u00E9miquement par des \r\n\u00E9tudiants curieux de voir des int\u00E9ractions entre astres dans l'univers.\r\nElle pourrait \u00EAtre utiliser dans des cours de physique entre autre ou\r\njuste de d\u00E9monstration dans des cours d'informatique.");
		textAreaAPropos.setEditable(false);
		scrollPaneAPropos.setViewportView(textAreaAPropos);
		
		panelSources = new JPanel();
		tabbedPane.addTab("Sources", null, panelSources, null);
		panelSources.setLayout(null);
		
		scrollPaneSources = new JScrollPane();
		scrollPaneSources.setBounds(0, 0, 570, 316);
		panelSources.add(scrollPaneSources);
		
		textAreaSources = new JTextArea(); //Sources de l'application
		textAreaSources.setText("Voici toutes les sources utilis\u00E9es dans la r\u00E9alisation du projet.\r\n\r\n\r\nPartie calcul de gravit\u00E9:\r\n\r\nhttp://arborjs.org/docs/barnes-hut\r\n\r\nhttp://profs.cmaisonneuve.qc.ca/svezina/nya/note_nya/NYA_XXI_Chap%201.X1.pdf\r\n\r\n\r\nPartie pour le 3D:\r\n\r\nhttp://www.swiftless.com/tutorials/opengl/reshape.html\r\n\r\nhttp://www.java2s.com/Code/Java/Swing-Components/ToolTipComboBoxExample.htm\r\n\r\nhttp://jogamp.org/wiki/index.php/Main_Page");
		textAreaSources.setEditable(false);
		scrollPaneSources.setViewportView(textAreaSources);
		
		buttonSuivant = new JButton("Explication avec image");
		buttonSuivant.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				instructionImage.setVisible(true);
			}
		});
		buttonSuivant.setBounds(136, 356, 255, 23);
		contentPane.add(buttonSuivant);
		
		setLocationRelativeTo(null);
		
		instructionImage = new InstructionImage();
	}
	
	/**
	 * Affiche l'onglet instructions
	 */
	public void afficherInstructions() {
		tabbedPane.setSelectedIndex(0);
	}
	
	/**
	 * Affiche l'onglet contenu scientifique
	 */
	public void afficherContenuScientifique() {
		tabbedPane.setSelectedIndex(1);
	}
	
	/**
	 * Affiche l'onglet a propos
	 */
	public void afficherAPropos() {
		tabbedPane.setSelectedIndex(2);
	}
	
	/**
	 * Affiche l'onglet sources
	 */
	public void afficherSources() {
		tabbedPane.setSelectedIndex(3);
	}
}
