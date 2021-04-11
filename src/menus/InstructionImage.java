package menus;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import aapplication.App06SimulateurUnivers;

/**
 * Cree la fenetre d'aide avec images.
 * @author Guillaume Bellemare-Roy
 */
public class InstructionImage extends JFrame {
	private static final long serialVersionUID = 180301260046332676L;

	private JPanel panelAideP1,panelAideP2,panelAideP3,panelAideP4,panelAideP5;
	private Image img1, img2, img3, img4, img5;
	private JButton suivant, precedant;
	private int nbPage;

	public InstructionImage() {
		nbPage =0;
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 1000, 600);

		URL fichierAideP1 = App06SimulateurUnivers.class.getClassLoader().getResource("menu_commencer.jpg");
		URL fichierAideP2 = App06SimulateurUnivers.class.getClassLoader().getResource("menu_options.jpg");
		URL fichierAideP3 = App06SimulateurUnivers.class.getClassLoader().getResource("outil_edition.jpg");
		URL fichierAideP4 = App06SimulateurUnivers.class.getClassLoader().getResource("outil_modification.jpg");
		URL fichierAideP5 = App06SimulateurUnivers.class.getClassLoader().getResource("outil_temps.jpg");
		
		//Charger toutes les images
		
		try{
			img1 = ImageIO.read(fichierAideP1);
			img2 = ImageIO.read(fichierAideP2);
			img3 = ImageIO.read(fichierAideP3);
			img4 = ImageIO.read(fichierAideP4);
			img5 = ImageIO.read(fichierAideP5);
		}
		catch (IOException e){
			System.out.println("erreur de load d'image");

		}	

		// créer le panel 1 avec l'image 1
		panelAideP1 = new JPanel()	{   
			private static final long serialVersionUID = -5848102489795814113L;

			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;
				g2d.drawImage(img1, 100, 50, null);     
			}     
		};
		setContentPane( panelAideP1 );
		panelAideP1.setBounds(50, 11, 900, 500);
		nbPage=1;

		suivant = new JButton();
		suivant.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if(nbPage==1){
					panelAideP1.setVisible(false);
					creePanel2();
				}else if(nbPage==2){
					panelAideP2.setVisible(false);
					creePanel3();
				}else if(nbPage==3){
					panelAideP3.setVisible(false);
					creePanel4();
				}else {
					panelAideP4.setVisible(false);
					creePanel5();
				}


			}
		});
		suivant.setBounds(800, 550, 100, 50);
		suivant.setText("Suivant");
		panelAideP1.add(suivant);


		precedant = new JButton();
		precedant.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if(nbPage==2){
					panelAideP2.setVisible(false);
					creePanel1();
				}else if(nbPage==3){
					panelAideP3.setVisible(false);
					creePanel2();
				}else if(nbPage==4){
					panelAideP4.setVisible(false);
					creePanel3();
				}else if(nbPage==5) {
					panelAideP5.setVisible(false);
					creePanel4();
				} 
			}
		});
		precedant.setBounds(100, 550, 100, 50);
		precedant.setText("précedant");




	}
	/**
	 * Cette methode cree le panel avec l'image 1
	 */
	private void creePanel1(){

		panelAideP1 = new JPanel()	{   
			private static final long serialVersionUID = -5848102489795814113L;

			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;
				g2d.drawImage(img1, 100, 50, null);     
			}     
		};
		setContentPane( panelAideP1 );
		panelAideP1.setBounds(50, 11, 900, 500);
		panelAideP1.add(suivant);
		nbPage=1;

	}
	/**
	 * Cette methode cree le panel avec l'image 2
	 */
	private void creePanel2(){
		panelAideP2 = new JPanel()	{   
			private static final long serialVersionUID = -5848102489795814113L;
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;
				g2d.drawImage(img2, 100, 50, null);     
			}     
		};
		setContentPane( panelAideP2 );
		panelAideP2.setBounds(50, 11, 900, 500);
		panelAideP2.add(precedant);
		panelAideP2.add(suivant);
		nbPage=2;
	}
	/**
	 * Cette methode cree le panel avec l'image 3
	 */
	private void creePanel3(){
		panelAideP3 = new JPanel()	{   
			private static final long serialVersionUID = -5848102489795814113L;

			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;
				g2d.drawImage(img3, 100, 50, null);     
			}     
		};
		setContentPane( panelAideP3 );
		panelAideP3.setBounds(50, 11, 900, 500);
		panelAideP3.add(precedant);
		panelAideP3.add(suivant);
		nbPage=3;

	}
	/**
	 * Cette methode cree le panel avec l'image 4
	 */
	private void creePanel4(){

		panelAideP4 = new JPanel()	{   
			private static final long serialVersionUID = -5848102489795814113L;

			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;
				g2d.drawImage(img4, 100, 50, null);     
			}     
		};
		setContentPane( panelAideP4 );
		panelAideP4.setBounds(50, 11, 900, 500);
		panelAideP4.add(precedant);
		panelAideP4.add(suivant);
		suivant.setEnabled(true);
		nbPage=4;
	}
	/**
	 * Cette methode cree le panel avec l'image 5
	 */
	private void creePanel5(){

		panelAideP5 = new JPanel()	{   
			private static final long serialVersionUID = -5848102489795814113L;

			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;
				g2d.drawImage(img5, 100, 50, null);     
			}     
		};
		setContentPane( panelAideP5 );
		panelAideP5.setBounds(50, 11, 900, 500);
		panelAideP5.add(precedant);
		suivant.setEnabled(false);
		nbPage=5;
	}
}

