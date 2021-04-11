package aapplication;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;

import menus.MenuAide;
import menus.MenuCommencer;
import menus.MenuOptions;
import menus.MenuPrincipal;
import simulateur.VuePrincipale;

/**
 * Cree la fenetre principale de l'application.
 * @author Guillaume Bellemare-Roy
 * <br>Participation de Marc-Antoine Dumais
 */
public class App06SimulateurUnivers extends JFrame {

	private static final long serialVersionUID = 4310828814326284348L;
	public static final int LARGEUR_FENETRE = 1280, HAUTEUR_FENETRE = 700;
	public static final double RATIO = 1E6;
	private JPanel panelPrincipal, contentPane;
	private MenuPrincipal menuPrincipal;
	private VuePrincipale vuePrincipale;
	private MenuCommencer menuCommencer;
	private MenuOptions menuOptions;
	private MenuAide menuAide;
	private JMenuBar menuBar;
	private JMenu mnFichier, mnAide;
	private JMenuItem mntmSauvegarder, mntmAPropos, mntmNouvauProjet, mntmQuitter, mntmMenuPrincipal, mntmInstructions, mntmContenuScientifique;
	private JMenuItem mntmOptions;
	private JMenuItem mntmSources;
	private JMenuItem mntmCharger;


	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.jtattoo.plaf.hifi.HiFiLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					App06SimulateurUnivers frame = new App06SimulateurUnivers();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}

	public App06SimulateurUnivers() {
		setTitle("Simulation de l'univers");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		mnFichier = new JMenu("Fichier");
		menuBar.add(mnFichier);
		
		mntmSauvegarder = new JMenuItem("Sauvegarder...");
		mntmSauvegarder.setEnabled(false);
		mntmSauvegarder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sauvegarder();
			}
		});
		
		mntmCharger = new JMenuItem("Charger...");
		mntmCharger.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				chargerProjet();
			}
		});
		
		mntmNouvauProjet = new JMenuItem("Nouvau Projet");
		mntmNouvauProjet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				nouveauProjet(false);
			}
		});
		mnFichier.add(mntmNouvauProjet);
		mnFichier.add(mntmSauvegarder);
		mnFichier.add(mntmCharger);
		
		mntmQuitter = new JMenuItem("Quitter");
		mntmQuitter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		
		mntmMenuPrincipal = new JMenuItem("Menu principal");
		mntmMenuPrincipal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				retourMenu();
			}
		});
		
		mntmOptions = new JMenuItem("Options");
		mntmOptions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				menuOptions.setVisible(true);
			}
		});
		mnFichier.add(mntmOptions);
		mnFichier.add(mntmMenuPrincipal);
		mnFichier.add(mntmQuitter);
		
		mnAide = new JMenu("Aide");
		menuBar.add(mnAide);
		
		mntmInstructions = new JMenuItem("Instructions");
		mntmInstructions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				menuAide.setVisible(true);
				menuAide.afficherInstructions();
			}
		});
		mnAide.add(mntmInstructions);
		
		mntmContenuScientifique = new JMenuItem("Contenu scientifique");
		mntmContenuScientifique.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuAide.setVisible(true);
				menuAide.afficherContenuScientifique();
			}
		});
		mnAide.add(mntmContenuScientifique);
		
		mntmAPropos = new JMenuItem("\u00C0 propos");
		mntmAPropos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuAide.setVisible(true);
				menuAide.afficherAPropos();
			}
		});
		mnAide.add(mntmAPropos);
		
		mntmSources = new JMenuItem("Sources");
		mntmSources.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuAide.setVisible(true);
				menuAide.afficherSources();
			}
		});
		mnAide.add(mntmSources);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		panelPrincipal = new JPanel();
		panelPrincipal.setPreferredSize(new Dimension(LARGEUR_FENETRE, HAUTEUR_FENETRE));
		contentPane.add(panelPrincipal, BorderLayout.CENTER);
		pack();
		setLocationRelativeTo(null);
		
		menuCommencer = new MenuCommencer(this);
		menuCommencer.setVisible(false);
		
		menuOptions = new MenuOptions();
		menuOptions.setVisible(false);
		
		menuAide = new MenuAide();
		menuAide.setVisible(false);	
		
		
		retourMenu();
	}
	
	/**
	 * Ouvre le menu commencer.
	 */
	public void ouvrirMenuCommencer() {
		menuCommencer.setVisible(true);
	}
	
	/**
	 * Affiche la fenetre d'options.
	 */
	public void afficherOptions() {
		menuOptions.setVisible(true);
	}
	
	/**
	 * Affiche la fenetre d'aide.
	 */
	public void afficherAide() {
		menuAide.setVisible(true);
	}
	
	/**
	 * Retourne au menu principal.
	 */
	public void retourMenu() {
		setResizable(false);
		menuPrincipal = new MenuPrincipal(this);
		
		getContentPane().remove(panelPrincipal);
		panelPrincipal = menuPrincipal;
		getContentPane().add(panelPrincipal);	
		mntmSauvegarder.setEnabled(false);
		panelPrincipal.setPreferredSize(new Dimension(LARGEUR_FENETRE, HAUTEUR_FENETRE));
		pack();
		validate();
		repaint();
	}
	
	/**
	 * Cree un nouveau projet.
	 * @param systemeSolaire Creer le systeme solaire (false = projet vide)
	 */
	public void nouveauProjet(boolean systemeSolaire) {
		setResizable(true);
		vuePrincipale = new VuePrincipale(systemeSolaire);
		getContentPane().remove(panelPrincipal);
		panelPrincipal = vuePrincipale;
		getContentPane().add(panelPrincipal);
		menuCommencer.setVisible(false);
		mntmSauvegarder.setEnabled(true);		
		validate();
	
	}
	
	/**
	 * Charge un projet.
	 */
	public void chargerProjet() {		
		ObjectInputStream ois = null;
			
		String cheminDossier = System.getProperty("user.home") + "\\AppData\\Local\\Simulateur Univers"; 
		File dossierDeTravail = new File (cheminDossier);
		if(!dossierDeTravail.exists() ) {
			dossierDeTravail.mkdir();
		}
		
		JFileChooser openFile = new JFileChooser(dossierDeTravail);
		FileFilter filtreFichier = new FileFilter() {
			@Override
			public boolean accept(File file) {
				if (file.isDirectory())
					return true;
				
				String path = file.getAbsolutePath();

				if (path.endsWith(".dod"))
					return true;
				
				return false;
			}
			@Override
			public String getDescription() {
				return "Univers";
			}
		};
		openFile.addChoosableFileFilter(filtreFichier);
		openFile.setFileFilter(filtreFichier);
		openFile.showOpenDialog(null);
		File fichierDeTravail = openFile.getSelectedFile();
		if (fichierDeTravail == null || !fichierDeTravail.getName().endsWith(".dod")) {
			retourMenu();
			return;
		}
		
		nouveauProjet(false);
		
		try {
			ois = new ObjectInputStream(new FileInputStream(fichierDeTravail));
			vuePrincipale.chargementInitial(ois);
			menuOptions.afficherInformations();
		} 	
		catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Fichier  " + fichierDeTravail.getAbsolutePath() + "  introuvable!");
		}
		
		catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Erreur rencontree lors de la lecture");
			e.printStackTrace();
		}	
	}
	
	/**
	 * Sauvegarde le projet en cours.
	 */
	public void sauvegarder (){		
		String cheminDossier = System.getProperty("user.home")+"\\AppData\\Local\\Simulateur Univers";
		File dossierDeTravail = new File (cheminDossier);
		if(!dossierDeTravail.exists() ) {
			dossierDeTravail.mkdir();
		}
		
		JFileChooser saveFile = new JFileChooser(dossierDeTravail);
		FileFilter filtreFichier = new FileFilter() {
			@Override
			public boolean accept(File file) {
				if (file.isDirectory())
					return true;
				
				String path = file.getAbsolutePath();

				if (path.endsWith(".dod"))
					return true;
				
				return false;
			}
			@Override
			public String getDescription() {
				return "Univers";
			}
		};
		saveFile.addChoosableFileFilter(filtreFichier);
		saveFile.setFileFilter(filtreFichier);
		saveFile.showSaveDialog(null);
		File fichierDeTravail = saveFile.getSelectedFile();
		if (fichierDeTravail == null)
			return;
		if (!fichierDeTravail.getName().endsWith(".dod")) {
			String path = fichierDeTravail.getAbsolutePath();
			if (fichierDeTravail.getName().contains("."))
				path = path.substring(0, path.indexOf('.'));
			path += ".dod";
			fichierDeTravail.delete();
			fichierDeTravail = new File(path);
		}
		
		
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(new FileOutputStream(fichierDeTravail));
			vuePrincipale.sauvegarder(oos);
		}
		catch (IOException e) {
			System.out.println("Erreur à l'écriture:");
			System.out.println(cheminDossier);
			e.printStackTrace();
		}
		finally {
		  	try { 
		  		oos.close();  
		  	}
		    catch (IOException e) { 
		    	System.out.println("Erreur rencontrée lors de la fermeture!"); 
		    }
		}
	}


}
