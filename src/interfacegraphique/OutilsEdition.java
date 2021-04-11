package interfacegraphique;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import simulateur.InfoAstre;
import simulateur.InfoTexture;
import simulateur.ResourcesSingleton;
import simulateur.VuePrincipale;

/**
 * Cette classe represente l'outil permettant d'ajouter de nouveaux astres.
 * @author Guillaume Bellemare-Roy
 * <br>Participation de Marc-Antoine Dumais
 */
public class OutilsEdition extends JPanel {
	private static final long serialVersionUID = -2447920287641141958L;
	private JButton btnAjouter;
	private boolean creationEnCours = false;
	private VuePrincipale vue;
	private JTabbedPane tabbedPane;
	private JPanel tabPlanete, tabAutre;
	private JButton btnMercure, btnVenus, btnTerre, btnMars, btnJupiter, btnSaturne, 
					btnUranus, btnNeptune, btnPluton, btnSoleil, btnLune;

	public OutilsEdition(VuePrincipale vuePrincipale) {
		vue = vuePrincipale;
		
		setSize(670,180);
		
		btnAjouter = new JButton("Ajouter");
		btnAjouter.setBounds(10, 54, 117, 23);
		btnAjouter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (creationEnCours)
					vue.annulerCreation();
				else{
					vue.initierAjoutAstre();
				changeCreationEnCours();}
			}
		});
		btnAjouter.setToolTipText("Ajouter un nouvel astre");
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(166, 11, 493, 159);
		
		tabPlanete = new JPanel();
		tabbedPane.addTab("Plan\u00E8tes", null, tabPlanete, null);
		tabPlanete.setLayout(null);
		
		btnMercure = new JButton("");
		btnMercure.setToolTipText("Mercure");
		btnMercure.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				vue.faireNouvelAstre(InfoAstre.MERCURE);
			}
		});
		btnMercure.setBounds(10, 11, 50, 50);
		tabPlanete.add(btnMercure);
		
		btnVenus = new JButton("");
		btnVenus.setToolTipText("V\u00E9nus");
		btnVenus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				vue.faireNouvelAstre(InfoAstre.VENUS);
			}
		});
		btnVenus.setBounds(70, 11, 50, 50);
		tabPlanete.add(btnVenus);
		
		btnTerre = new JButton("");
		btnTerre.setToolTipText("Terre");
		btnTerre.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				vue.faireNouvelAstre(InfoAstre.TERRE);
			}
		});
		btnTerre.setBounds(130, 11, 50, 50);
		tabPlanete.add(btnTerre);
		
		btnMars = new JButton("");
		btnMars.setToolTipText("Mars");
		btnMars.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				vue.faireNouvelAstre(InfoAstre.MARS);
			}
		});
		btnMars.setBounds(190, 11, 50, 50);
		tabPlanete.add(btnMars);
		
		btnJupiter = new JButton("");
		btnJupiter.setToolTipText("Jupiter");
		btnJupiter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				vue.faireNouvelAstre(InfoAstre.JUPITER);
			}
		});
		btnJupiter.setBounds(250, 11, 50, 50);
		tabPlanete.add(btnJupiter);
		
		btnSaturne = new JButton("");
		btnSaturne.setToolTipText("Saturne");
		btnSaturne.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				vue.faireNouvelAstre(InfoAstre.SATURNE);
			}
		});
		btnSaturne.setBounds(310, 11, 50, 50);
		tabPlanete.add(btnSaturne);
		
		btnUranus = new JButton("");
		btnUranus.setToolTipText("Uranus");
		btnUranus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				vue.faireNouvelAstre(InfoAstre.URANUS);
			}
		});
		btnUranus.setBounds(370, 11, 50, 50);
		tabPlanete.add(btnUranus);
		
		btnNeptune = new JButton("");
		btnNeptune.setToolTipText("Neptune");
		btnNeptune.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				vue.faireNouvelAstre(InfoAstre.NEPTUNE);
			}
		});
		btnNeptune.setBounds(430, 11, 50, 50);
		tabPlanete.add(btnNeptune);
		
		tabAutre = new JPanel();
		tabbedPane.addTab("Autres", null, tabAutre, null);
		tabAutre.setLayout(null);
		
		btnPluton = new JButton("");
		btnPluton.setToolTipText("Pluton");
		btnPluton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				vue.faireNouvelAstre(InfoAstre.PLUTON);
			}
		});
		btnPluton.setBounds(10, 11, 50, 50);
		tabAutre.add(btnPluton);
		
		btnSoleil = new JButton("");
		btnSoleil.setToolTipText("Soleil");
		btnSoleil.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				vue.faireNouvelAstre(InfoAstre.SOLEIL);
			}
		});
		btnSoleil.setBounds(70, 11, 50, 50);
		tabAutre.add(btnSoleil);
		
		btnLune = new JButton("");
		btnLune.setToolTipText("Lune");
		btnLune.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				vue.faireNouvelAstre(InfoAstre.LUNE);
			}
		});
		btnLune.setBounds(130, 11, 50, 50);
		tabAutre.add(btnLune);
		setLayout(null);
		add(btnAjouter);
		add(tabbedPane);
		
		JLabel lblOutilDdition = new JLabel("Outil \r\nd'\u00E9dition");
		lblOutilDdition.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblOutilDdition.setBounds(10, 11, 207, 23);
		add(lblOutilDdition);
	}
	
	/**
	 * Change l'etat de creation d'astre.
	 * <br>Si la creation etait en cours, elle cesse,
	 * <br>sinon, elle commence.
	 */
	public void changeCreationEnCours() {
		creationEnCours = !creationEnCours;
		if (creationEnCours) {
			btnAjouter.setText("Annuler");
			btnAjouter.setToolTipText("Annuler la cr\u00E9ation d'astre");
		} else {
			btnAjouter.setText("Ajouter");
			btnAjouter.setToolTipText("Ajouter un nouvel astre");
		}
	}
	
	/**
	 * Charge les icones des boutons.
	 * <br>Doit etre appele apres que les ressources aient ete chargees.
	 */
	public void chargerIcones() {
		ResourcesSingleton res = ResourcesSingleton.getInstance();
		ArrayList<Image> icones = res.getIcones();
		for(int i = 0; i < icones.size(); i++) {
			Image img = icones.get(i);
			icones.set(i, img.getScaledInstance(50, 50, Image.SCALE_SMOOTH));
		}
		
		btnMercure.setIcon(new ImageIcon(icones.get(InfoTexture.MERCURE.getNumero())));
		btnVenus.setIcon(new ImageIcon(icones.get(InfoTexture.VENUS.getNumero())));
		btnTerre.setIcon(new ImageIcon(icones.get(InfoTexture.TERRE.getNumero())));
		btnMars.setIcon(new ImageIcon(icones.get(InfoTexture.MARS.getNumero())));
		btnJupiter.setIcon(new ImageIcon(icones.get(InfoTexture.JUPITER.getNumero())));
		btnSaturne.setIcon(new ImageIcon(icones.get(InfoTexture.SATURNE.getNumero())));
		btnUranus.setIcon(new ImageIcon(icones.get(InfoTexture.URANUS.getNumero())));
		btnNeptune.setIcon(new ImageIcon(icones.get(InfoTexture.NEPTUNE.getNumero())));
		btnPluton.setIcon(new ImageIcon(icones.get(InfoTexture.PLUTON.getNumero())));
		btnSoleil.setIcon(new ImageIcon(icones.get(InfoTexture.SOLEIL.getNumero())));
		btnLune.setIcon(new ImageIcon(icones.get(InfoTexture.LUNE.getNumero() - 1)));
	}
}
