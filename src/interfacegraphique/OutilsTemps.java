package interfacegraphique;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import menus.TestEntree;
import simulateur.OptionsSingleton;
import simulateur.VuePrincipale;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Cette classe represente l'outil permettant de controler le passage du temps dans l'application.
 * @author Guillaume Bellemare-Roy
 * <br>Participation de Marc-Antoine Dumais
 */
public class OutilsTemps extends JPanel {
	private static final long serialVersionUID = -7416713039360397578L;
	private static final int NOMBRE_DECIMALES = 4;
	private OptionsSingleton options;
	private JComboBox<String> comboBoxTemps;
	private JTextField textFieldSec;
	private JButton btnControle;
	private ImageIcon iconPlay, iconPause;
	private double ratioTemps = 86400;
	private JLabel labelDateChangee, lblFPSAffiche, lblDateEnCours, lblSeconde;
	private VuePrincipale vue;
	private Calendar calendar;
	private JLabel lblNewLabel;
	private int imagesDepuisAffichageDate = 0;
	
	public OutilsTemps(VuePrincipale vuePrincipale) {
		calendar = Calendar.getInstance();
		vue = vuePrincipale;
		options = OptionsSingleton.getInstance();
		setLayout(null);
		setFocusable(true);
		
		lblDateEnCours = new JLabel("Date en cours");
		lblDateEnCours.setToolTipText("Date en cours dans la simulation\r\n");
		lblDateEnCours.setBounds(10, 45, 97, 14);
		add(lblDateEnCours);
		
		lblSeconde = new JLabel("1 seconde = ");
		lblSeconde.setBounds(10, 70, 79, 14);
		add(lblSeconde);
		
		labelDateChangee = new JLabel();
		labelDateChangee.setToolTipText("Date dans l'animation pr\u00E9sentement");
		labelDateChangee.setBounds(97, 45, 161, 14);
		add(labelDateChangee);
		
		comboBoxTemps = new JComboBox<String>();
		comboBoxTemps.setToolTipText("Unit\u00E9 de temps possible");
		comboBoxTemps.setModel(new DefaultComboBoxModel<String>(new String[] {"secondes","minutes", "heures", "jours", "ann\u00E9es"}));
		comboBoxTemps.setBounds(184, 70, 97, 20);
		comboBoxTemps.setSelectedIndex(3);
		comboBoxTemps.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				switch(comboBoxTemps.getSelectedIndex()){
				case 0:
					ratioTemps = 1;
					break;
				case 1:
					ratioTemps = 60;
					break;
				case 2:
					ratioTemps = 3600; 
					break;
				case 3:
					ratioTemps = 86400;
					break;
				case 4:
					ratioTemps = 31536000;
					break;
				}
				afficherInformations();
			}
		});
		add(comboBoxTemps);

		try {
			Image imgPlay = ImageIO.read(getClass().getClassLoader().getResource("bouton_play.png"));
			iconPlay = new ImageIcon(imgPlay);
			Image imgPause = ImageIO.read(getClass().getClassLoader().getResource("bouton_pause.png"));
			iconPause = new ImageIcon(imgPause);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		btnControle = new JButton(); 
		btnControle.setBounds(20, 95, 40, 40);
		btnControle.setFocusable(false);
		btnControle.setToolTipText("Mettre l'animation en pause\r\n");
		btnControle.setIcon(iconPause);
		btnControle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (vue.isEnPause()) {
					vue.play();
					btnControle.setToolTipText("Mettre l'animation en pause");
					btnControle.setIcon(iconPause);
				} else {
					vue.pause();
					btnControle.setToolTipText("D\u00E9marrer l'animation");
					btnControle.setIcon(iconPlay);
				}
			
			}
		});
		add(btnControle);		

		textFieldSec = new JTextField();	
		textFieldSec.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					vue.requestFocus();
				}
			}
		});
		textFieldSec.setTransferHandler(null);
		textFieldSec.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				double d = TestEntree.nettoyerChaine(textFieldSec.getText());
				if (d < 0){
					d *= -1;
					vue.setMessageAvertissement(-1);
				}
				if (d * ratioTemps >= 25228800)
					vue.setMessageAvertissement(1);
				
				if (d == 0) {
					textFieldSec.setText("" + TestEntree.arrondir(vue.getVitesseTemps() / ratioTemps, NOMBRE_DECIMALES));
					return;
				}
				textFieldSec.setText("" + TestEntree.arrondir(d, NOMBRE_DECIMALES));
				vue.setVitesseTemps(d * ratioTemps);
			}
		});
		vue.setVitesseTemps(4.32E6);
		textFieldSec.setBounds(87, 70, 86, 20);
		add(textFieldSec);
		textFieldSec.setColumns(5);
		
		lblFPSAffiche = new JLabel("");
		lblFPSAffiche.setToolTipText("Images Par Secondes (FPS)\r\n");
		lblFPSAffiche.setBounds(198, 116, 45, 20);
		add(lblFPSAffiche);
		
		JLabel lblImagesParSecondes = new JLabel("Images Par Secondes");
		lblImagesParSecondes.setBounds(136, 101, 133, 14);
		add(lblImagesParSecondes);
		
		lblNewLabel = new JLabel("Outil Temps");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewLabel.setBounds(10, 11, 115, 20);
		add(lblNewLabel);
	}

	/**
	 * Met a jour les informations affichees dans l'outil.
	 */
	public void afficherInformations() {
		if(options.isAfficherFPS()) {
			lblFPSAffiche.setText("" + vue.getFps());
		} else
			lblFPSAffiche.setText("");
		
		if (!textFieldSec.hasFocus())
			textFieldSec.setText("" + TestEntree.arrondir(vue.getVitesseTemps() / ratioTemps, NOMBRE_DECIMALES));
		imagesDepuisAffichageDate++;
		if (imagesDepuisAffichageDate > 10) {
			labelDateChangee.setText(new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(calendar.getTime()));
			imagesDepuisAffichageDate = 0;
		}
	}

	/**
	 * Met a jour la date de l'application.
	 * @param delta Le temps ecoule en ms.
	 */
	public void update(double delta) {
		int sec = (int)(delta / 1000.0);
		calendar.add(Calendar.SECOND, sec);
	}
}