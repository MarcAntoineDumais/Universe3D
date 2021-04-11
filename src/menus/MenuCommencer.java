package menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import aapplication.App06SimulateurUnivers;

/**
 * Cree la fenetre qui permet de commencer la simulation.
 * @author guillaume bellemare-roy
 */
public class MenuCommencer extends JFrame implements Serializable {
	private static final long serialVersionUID = -9027496840112977272L;
	private App06SimulateurUnivers app;
	private JPanel contentPane;
	private JButton btnAncienUnivers, btnNouveauUnivers, btnSystemeSolaire;
	
	public MenuCommencer(App06SimulateurUnivers application) {
		app = application;
		setTitle("Menu Commencer");
		setEnabled(true);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 600, 300);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		btnNouveauUnivers = new JButton("Nouvel Univers");
		btnNouveauUnivers.setBounds(5, 5, 191, 252);
		btnNouveauUnivers.setToolTipText("Commencer un nouveau projet \u00E0 partir du vide");
		btnNouveauUnivers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				app.nouveauProjet(false);
			}
		});
		contentPane.setLayout(null);
		contentPane.add(btnNouveauUnivers);
		
		btnAncienUnivers = new JButton("Charger Projet"); //Charger un ancien univers cree par l'utilisateur
		btnAncienUnivers.setBounds(387, 5, 191, 252);
		btnAncienUnivers.setToolTipText("Charger un projet sauvegard\u00E9");
		btnAncienUnivers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				app.chargerProjet();
			}
		});
		contentPane.add(btnAncienUnivers);
		
		btnSystemeSolaire = new JButton("Syst\u00E8me solaire");
		btnSystemeSolaire.setBounds(206, 5, 171, 252);
		btnSystemeSolaire.setToolTipText("Commencer un nouveau projet \u00E0 partir de notre syst\u00E8me solaire");
		btnSystemeSolaire.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				app.nouveauProjet(true);
			}
		});
		contentPane.add(btnSystemeSolaire);
	}
}
