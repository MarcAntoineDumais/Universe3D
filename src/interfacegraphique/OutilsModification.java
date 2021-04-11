package interfacegraphique;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

import simulateur.Astre;
import simulateur.InfoTexture;
import simulateur.ResourcesSingleton;
import simulateur.VuePrincipale;
import menus.ColorListener;
import menus.MenuCouleur;
import menus.TestEntree;
import affichage3d.Vector3;

import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Cette classe represente l'outil permettant d'ajouter de modifier des astres existants.
 * @author Guillaume Bellemare-Roy
 * <br>Participation de Marc-Antoine Dumais
 */
public class OutilsModification extends JPanel {
	private static final long serialVersionUID = 956726776013116702L;
	private static final int NOMBRE_DECIMALES = 4;	
	private JTextField textNomAstre;
	private JLabel lblMasse, lblRayon, lblVelocite, lblCouleurEnCours, lblCouleurTrace, lblNomDeLastre;
	private JComboBox<String> comboBoxMasse, comboBoxRayon, comboBoxVelocite, comboBoxTexture;
	private JTextField textFieldMasse, textFieldRayon,textFieldVitesse;
	private double ratioMasse = 1/*kg*/, ratioRayon = 1000/*km*/, ratioVitesse = 1/*m/s*/;
	private MenuCouleur menuCouleur;
	private VuePrincipale vue;
	private JButton btnCouleurAstre;
	private JLabel lblCouleurAstre;
	private boolean couleurTrace;
	private JLabel lblNewLabel;
	private JLabel lblTexture;
	private String[] tooltips;
	private JPanel panEsthetique;

	public OutilsModification(VuePrincipale vuePrincipale) {
		vue = vuePrincipale;
		setLayout(null);
		
		menuCouleur = new MenuCouleur();
		menuCouleur.setVisible(false);
		menuCouleur.addColorChooserListener(new ColorListener(){
			@Override
			public void uneCouleurChoisie(Color coul) {
				if(couleurTrace) {
					lblCouleurTrace.setBackground(coul);
					Astre a = vue.getFocusedAstre();
					if(a != null)
						a.setCouleurTrace(coul);
				} else {
					lblCouleurAstre.setBackground(coul);
					Astre a = vue.getFocusedAstre();
					if(a != null)
						a.setCouleur(coul);
				}
			}
		});
		
		lblNomDeLastre = new JLabel("Nom de l'astre :");
		lblNomDeLastre.setBounds(10, 44, 92, 14);
		add(lblNomDeLastre);
		
		textNomAstre = new JTextField();
		textNomAstre.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					vue.requestFocus();
				}
			}
		});
		textNomAstre.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				Astre a = vue.getFocusedAstre();
				if (a != null)
					a.setNom(textNomAstre.getText());
			}
		});
		textNomAstre.setToolTipText("D\u00E9finir le nom de l'astre pr\u00E9sentement en focus");
		textNomAstre.setBounds(98, 41, 134, 20);
		add(textNomAstre);
		textNomAstre.setColumns(10);
		
		lblMasse = new JLabel("Masse :");
		lblMasse.setBounds(10, 84, 57, 14);
		add(lblMasse);
		
		comboBoxMasse = new JComboBox<String>();
		comboBoxMasse.setModel(new DefaultComboBoxModel<String>(new String[] {"g", "Kg", "Lune", "Terre", "Jupiter", "Soleil"}));
		comboBoxMasse.setToolTipText("Unit\u00E9 de masse voulue\r\n");
		comboBoxMasse.setBounds(165, 81, 66, 20);
		comboBoxMasse.setSelectedIndex(1);
		comboBoxMasse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				switch(comboBoxMasse.getSelectedIndex()){
				case 0:
					ratioMasse = 0.001;
					break;
				case 1:
					ratioMasse = 1;
					break;
				case 2:
					ratioMasse = 7.3477E22; 
					break;
				case 3:
					ratioMasse = 5.9736E24;
					break;
				case 4:
					ratioMasse = 1.8986E27;
					break;
				case 5:
					ratioMasse = 1.9891E30;
					break;
				}

				afficherInformations();
			}
		});
		add(comboBoxMasse);
		
		lblRayon = new JLabel("Rayon :");
		lblRayon.setBounds(10, 124, 71, 14);
		add(lblRayon);
		
		comboBoxRayon = new JComboBox<String>();
		comboBoxRayon.setModel(new DefaultComboBoxModel<String>(new String[] {"m", "Km", "Lune", "Terre", "Jupiter", "Soleil"}));
		comboBoxRayon.setToolTipText("Unit\u00E9 de distance pour le rayon\r\n");
		comboBoxRayon.setBounds(165, 121, 67, 20);
		comboBoxRayon.setSelectedIndex(1);
		comboBoxRayon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				switch (comboBoxRayon.getSelectedIndex()){
				case 0:
					ratioRayon = 1;
					break;
				case 1:
					ratioRayon = 1000;
					break;
				case 2:
					ratioRayon = 1.7374E6;
					break;
				case 3:
					ratioRayon = 6.378137E6;
					break;
				case 4:
					ratioRayon = 7.1492E7;
					break;
				case 5:
					ratioRayon = 6.96342E8;
					break;
				}
				
				afficherInformations();
			}
		});
		add(comboBoxRayon);
		
		lblVelocite = new JLabel("V\u00E9locit\u00E9 :");
		lblVelocite.setBounds(10, 164, 57, 14);
		add(lblVelocite);
		
		comboBoxVelocite = new JComboBox<String>();
		comboBoxVelocite.setToolTipText("Unit\u00E9 de vitesse de l'astre");
		comboBoxVelocite.setModel(new DefaultComboBoxModel<String>(new String[] {"m/h", "km/h", "m/s", "km/s", "lumi\u00E8re"})); 
		comboBoxVelocite.setSelectedIndex(2);
		comboBoxVelocite.setBounds(165, 161, 67, 20);
		comboBoxVelocite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				switch (comboBoxVelocite.getSelectedIndex()){
				case 0:
					ratioVitesse = 2.77778E-4;
					break;
				case 1:
					ratioVitesse = 2.77778E-1;
					break;
				case 2:
					ratioVitesse = 1;
					break;
				case 3:
					ratioVitesse = 1000;
					break;
				case 4:
					ratioVitesse = 2.99792458E8;
					break;
				}
				
				afficherInformations();
			}
		});
		add(comboBoxVelocite);
		
		panEsthetique = new JPanel();
		panEsthetique.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Esth\u00E9tique", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panEsthetique.setBounds(0, 198, 241, 132);
		add(panEsthetique);
		panEsthetique.setLayout(null);
		
		JButton btnCouleurTrace = new JButton("Couleur trace");
		btnCouleurTrace.setBounds(6, 30, 115, 23);
		panEsthetique.add(btnCouleurTrace);
		btnCouleurTrace.setToolTipText("Ouvre le menu de s\u00E9lection de couleur pour le trac\u00E9 de l'astre en focus");
		
		lblCouleurEnCours = new JLabel("Couleur en cours");
		lblCouleurEnCours.setBounds(131, 16, 104, 14);
		panEsthetique.add(lblCouleurEnCours);
		
		lblCouleurTrace = new JLabel(); 
		lblCouleurTrace.setBounds(152, 39, 46, 14);
		panEsthetique.add(lblCouleurTrace);
		lblCouleurTrace.setOpaque(true);		
		lblCouleurTrace.setBackground(Color.WHITE);
		lblCouleurTrace.setToolTipText("Couleur du trac\u00E9 de l'astre s\u00E9lectionn\u00E9\r\n");
		
		btnCouleurAstre = new JButton("Couleur astre");
		btnCouleurAstre.setBounds(6, 66, 115, 23);
		panEsthetique.add(btnCouleurAstre);
		btnCouleurAstre.setToolTipText("Ouvre le menu de s\u00E9lection de couleur pour l'astre en focus");
		
		lblCouleurAstre = new JLabel();
		lblCouleurAstre.setBounds(152, 70, 46, 14);
		panEsthetique.add(lblCouleurAstre);
		lblCouleurAstre.setToolTipText("Couleur de l'astre s\u00E9lectionn\u00E9\r\n");
		lblCouleurAstre.setOpaque(true);
		lblCouleurAstre.setBackground(Color.WHITE);
		
		lblTexture = new JLabel("Texture :");
		lblTexture.setBounds(6, 100, 115, 14);
		panEsthetique.add(lblTexture);
		lblTexture.setHorizontalAlignment(SwingConstants.CENTER);
		btnCouleurAstre.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				menuCouleur.setVisible(true);
				couleurTrace = false;
			}
		});
		btnCouleurTrace.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				menuCouleur.setVisible(true);
				couleurTrace = true;
			}
		});
		
		textFieldMasse = new JTextField();
		textFieldMasse.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					vue.requestFocus();
				}
			}
		});
		textFieldMasse.setTransferHandler(null);
		textFieldMasse.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				double d = TestEntree.nettoyerChaine(textFieldMasse.getText());
				if (d < 0){
					d *= -1;
					vue.setMessageAvertissement(-1);
				}
				if (d == 0) {
					afficherInformations();
					return;
				}
				textFieldMasse.setText("" + TestEntree.arrondir(d, NOMBRE_DECIMALES));
				Astre a = vue.getFocusedAstre();
				if (a != null)
					a.setMasse(d * ratioMasse);
			}
		});

		textFieldMasse.setBounds(69, 81, 86, 20);
		add(textFieldMasse);
		textFieldMasse.setColumns(10);
		
		textFieldRayon = new JTextField();
		textFieldRayon.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					vue.requestFocus();
				}
			}
		});
		textFieldRayon.setTransferHandler(null);
		textFieldRayon.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				double d = TestEntree.nettoyerChaine(textFieldRayon.getText());
				if (d < 0){
					d *= -1;
					vue.setMessageAvertissement(-1);
				}
				if (d == 0) {
					afficherInformations();
					return;
				}
				textFieldRayon.setText("" + TestEntree.arrondir(d, NOMBRE_DECIMALES));
				Astre a = vue.getFocusedAstre();
				if (a != null)
					a.setRayon(d * ratioRayon);
			}
		});
		textFieldRayon.setBounds(69, 121, 86, 20);
		add(textFieldRayon);
		textFieldRayon.setColumns(10);
		
		textFieldVitesse = new JTextField();
		textFieldVitesse.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					vue.requestFocus();
				}
			}
		});
		textFieldVitesse.setTransferHandler(null);
		textFieldVitesse.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				double d = TestEntree.nettoyerChaine(textFieldVitesse.getText());
				if (d < 0){
					d *= -1;
					vue.setMessageAvertissement(-1);
				}
				if (d * ratioVitesse > 2.99792458E8)
					d = 2.99792458E8 / ratioVitesse;
				textFieldVitesse.setText("" + TestEntree.arrondir(d, NOMBRE_DECIMALES));
				Astre a = vue.getFocusedAstre();
				if (a != null) {
					Vector3 v;
					v = a.getVitesseFutur();
					v.rendreUnitaire();
					v.mettreALEchelle(d * ratioVitesse);
				}
			}
		});
		textFieldVitesse.setBounds(69, 161, 86, 20);
		add(textFieldVitesse);
		textFieldVitesse.setColumns(10);
		
		lblNewLabel = new JLabel("Outils Modifications");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel.setBounds(10, 11, 148, 20);
		add(lblNewLabel);
		
		JButton btnNewButton = new JButton("Suprimer");
		btnNewButton.setToolTipText("Suprime l'astre");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Astre a = vue.getFocusedAstre();
				if (a != null)
					a.dispose();
			}
		});
		btnNewButton.setBounds(149, 11, 76, 19);
		add(btnNewButton);
	}

	/**
	 * Cree la liste deroulante permettant de changer la texture de l'astre selectionne.
	 * (Doit etre appele apres avoir charge les ressources)
	 */
	@SuppressWarnings("unchecked")
	public void createComboBoxTexture() {
		String[] items = {"Mercure", "Venus", "Terre", "Mars", "Jupiter", "Saturne", "Uranus", "Neptune", "Pluton", "Soleil", "Lune"};
		ResourcesSingleton res = ResourcesSingleton.getInstance();
		ArrayList<URL> urls = res.getUrlIcones();
		
		tooltips = new String[urls.size()];
		for (int i = 0; i < tooltips.length; i++) {
			tooltips[i] = "<html><body><img src='" + urls.get(i) + "'></body></html>";
		}
		comboBoxTexture = new JComboBox<String>(items);
		comboBoxTexture.setSelectedIndex(0);
		comboBoxTexture.setBounds(150, 101, 74, 20);
		comboBoxTexture.setToolTipText("La texture de l'astre s\u00E9lectionn\u00E9\r\n");
		comboBoxTexture.setRenderer(new BasicComboBoxRenderer() {
			private static final long serialVersionUID = 7121103233876060432L;
			@SuppressWarnings("rawtypes")
			public Component getListCellRendererComponent(JList list, Object value, int index, 
														  boolean isSelected, boolean cellHasFocus) {
				if (isSelected) {
					setBackground(list.getSelectionBackground());
					setForeground(list.getSelectionForeground());
					if (-1 < index) {
						list.setToolTipText(tooltips[index]);
					}
				} else {
					setBackground(list.getBackground());
					setForeground(list.getForeground());
				}
				setFont(list.getFont());
				setText((value == null) ? "" : value.toString());
				return this;
			}			
		});
		comboBoxTexture.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int i = comboBoxTexture.getSelectedIndex();
				if (i == 10)
					i++;
				Astre a = vue.getFocusedAstre();
				if (a == null)
					return;
				for (InfoTexture info : InfoTexture.values()) {
					if (info.getNumero() == i)
						a.setTexture(info);
				}
				
				afficherInformations();
			}
		});
		panEsthetique.add(comboBoxTexture);
	}
		
	/**
	 * Met a jour les informations affichees dans l'outil.
	 */
	public void afficherInformations() {
		Astre a = vue.getFocusedAstre();
		if (a == null)
			return;
		
		if(!textFieldMasse.hasFocus())
			textFieldMasse.setText("" + TestEntree.arrondir(a.getMasse() / ratioMasse, NOMBRE_DECIMALES));
		if(!textFieldRayon.hasFocus())
			textFieldRayon.setText("" + TestEntree.arrondir(a.getRayon() / ratioRayon, NOMBRE_DECIMALES));
		if(!textFieldVitesse.hasFocus())
			textFieldVitesse.setText("" + TestEntree.arrondir(a.getVitesse().magnitude() / ratioVitesse, NOMBRE_DECIMALES));
		if(!textNomAstre.hasFocus())
			textNomAstre.setText(a.getNom());
		lblCouleurTrace.setBackground(a.getCouleurTrace());
		lblCouleurAstre.setBackground(a.getCouleur());
		int n = a.getNumeroTexture();
		if (n == 11)
			n--;
		comboBoxTexture.setSelectedIndex(n);
	}
}
