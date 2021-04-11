package simulateur;
import static javax.media.opengl.GL.GL_COLOR_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DEPTH_TEST;
import static javax.media.opengl.GL.GL_LINES;
import static javax.media.opengl.GL.GL_POINTS;
import static javax.media.opengl.GL.GL_TEXTURE_2D;
import static javax.media.opengl.GL.GL_VIEWPORT;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_COLOR_MATERIAL;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW_MATRIX;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION_MATRIX;
import interfacegraphique.OutilsEdition;
import interfacegraphique.OutilsModification;
import interfacegraphique.OutilsTemps;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLJPanel;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;
import javax.swing.JTextArea;

import menus.TestEntree;
import aapplication.App06SimulateurUnivers;
import affichage3d.Vector3;

import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.awt.TextRenderer;
import com.jogamp.opengl.util.texture.Texture;

/**
 * Cette classe est la classe principale de l'application.
 *  Elle permet de gerer tous les astres et particules, de les mettre a jour et de les afficher au moment approprie.
 * 
 * @author Marc-Antoine Dumais
 * <br>Participation de David-Olivier Duperron-Cristea
 */
public class VuePrincipale extends GLJPanel implements GLEventListener, Runnable, Serializable {
	private static final long serialVersionUID = 8015332482879721504L;

	//Simulation/animation
	private Thread processus;
	private double sleepTime = 10;
	private double updateTime = 10;
	private final int OBJECTIF_FPS = 60;
	private double fps;
	private boolean enPause = false;
	private boolean vuePrincipaleEnVie = true;
	private LinkedList<Astre> astres;
	private LinkedList<Particule> particules;
	private Octree tree;

	//Controle
	private boolean rightMouseButtonDown = false;
	private boolean controlDown = false;
	private int mouseX = 0, mouseY = 0;
	private int dernierMouseX, dernierMouseY;

	//Camera et focus
	private double cameraAngle1 = 180, cameraAngle2 = 30, cameraDistance = 1E12;
	private double objectifCameraDistance = cameraDistance;
	private double vitesseCamera;
	private Vector3 cameraPos = new Vector3();
	private Vector3 focus = new Vector3();
	private Vector3 objectifFocus = new Vector3();
	private Vector3 objectifFocusLastUpdate = new Vector3();
	private boolean faireClicRayFocus = false;
	private int indiceFocus = -1;
	private Vector3 rayD = new Vector3();
	private int dernierClickCount = 0;
	private boolean suivreFocusedAstre = false;

	//Affichage
	private double ratio = App06SimulateurUnivers.RATIO;
	private GL2 gl;
	private GLU glu;
	private double fov = 70; //Field of view (degres)
	private double hauteurFenetre = App06SimulateurUnivers.HAUTEUR_FENETRE;
	private TextRenderer renderer;
	private ArrayList<Texture> textures;
	private OptionsSingleton options;
	private GLUquadric fond;

	//Interface graphique
	private OutilsTemps outilsTemps;
	private OutilsEdition outilsEdition;
	private OutilsModification outilsModification;
	private JTextArea messageAide;
	private JTextArea messageAvertissement;
	private float alphaTexte = 1;
	private float alphaTexteAvertissement = 0;
	private String aideMouvements = "Clic droit: glisser pour changer l'angle de vue\r\n" + "" +
			"Molette: zoom / d\u00E9zoom\r\n" + "" +
			"Ctrl + clic droit: glisser pour d\u00E9placer lat\u00E9ralement la cam\u00E9ra";
	private String aideBienvenue = "Bienvenue dans le simulateur d'univers!\r\n" + aideMouvements;
	private String aideSelection = "Choisissez l'astre qui servira de base au nouvel astre!";
	private String aidePlacementPlan = "D\u00E9placez la souris pour modifier la position du nouvel astre sur le plan!\r\n" + 
			"Clic gauche: Passer au positionnement en hauteur.\r\n" + 
			"Clic de la molette: Placer l'astre \u00E0 la position choisie.";
	private String aidePlacementHauteur = "D\u00E9placez la souris pour modifier la position du nouvel astre en hauteur!\r\n" + 
			"Clic gauche: Passer au positionnement sur le plan.\r\n" + 
			"Clic de la molette: Placer l'astre \u00E0 la position choisie.";
	private String avertissementMinimum = "\r\n! La valeur ne peut pas \u00eatre plus petite que 0 ! ";
	private String avertissementMaximum = "! Si la valeur est trop grande alors il se peut que les \r\n calculs ne soient pas aussi pr\u00e9cis !";
	private String avertissementRK4 = "!! La m\u00e9thode de calcul Rk4 ne fonctionne pas correctement !!";

	//Autres
	private boolean faireNouvelAstre = false;
	private boolean faireCalculateRay = false;
	private boolean faireCharger = false;
	private boolean fairePlacementParticules = false;
	private boolean faireSystemeSolaire = false;
	private ObjectInputStream ois;
	private EtatCreation etat = EtatCreation.PAS_COMMENCE;
	private Astre nouvelAstre;
	private InfoAstre infoNouvelAstre;

	public VuePrincipale(boolean systemeSolaire) {
		faireSystemeSolaire = systemeSolaire;
		astres = new LinkedList<Astre>();
		particules = new LinkedList<Particule>();
		addGLEventListener(this);
		setLayout(null);

		outilsTemps = new OutilsTemps(this);
		outilsTemps.setBounds(10, 10, 284, 141);
		add(outilsTemps);

		outilsEdition = new OutilsEdition(this);
		outilsEdition.setBounds(10, 480, 140, 180);
		add(outilsEdition);
		outilsEdition.setVisible(false);

		outilsModification = new OutilsModification(this);
		outilsModification.setBounds(1030, 10, 240, 330);
		add(outilsModification);
		outilsModification.setVisible(false);

		messageAide = new JTextArea() {
			private static final long serialVersionUID = 5512915928513050751L;
			@Override
			public void paintComponent(Graphics g) {
				Graphics2D g2d = (Graphics2D) g;
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaTexte));
				super.paintComponent(g);				
			}
		};
		messageAide.setForeground(Color.GREEN);
		messageAide.setFont(new Font("Tahoma", Font.BOLD, 16));
		messageAide.setOpaque(false);
		messageAide.setWrapStyleWord(true);
		messageAide.setLineWrap(true);
		messageAide.setText(aideBienvenue);
		messageAide.setEditable(false);
		messageAide.setFocusable(false);
		messageAide.setBounds(770, 600, 500, 90);
		add(messageAide);

		messageAvertissement = new JTextArea() {
			private static final long serialVersionUID = 5512915928513050751L;
			@Override
			public void paintComponent(Graphics g) {
				Graphics2D g2d = (Graphics2D) g;
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaTexteAvertissement));
				super.paintComponent(g);				
			}
		};
		messageAvertissement.setForeground(Color.RED);
		messageAvertissement.setFont(new Font("Tahoma", Font.BOLD, 17));
		messageAvertissement.setOpaque(false);
		messageAvertissement.setWrapStyleWord(true);
		messageAvertissement.setLineWrap(true);
		messageAvertissement.setText("");
		messageAvertissement.setEditable(false);
		messageAvertissement.setFocusable(false);
		messageAvertissement.setBounds(770, 560, 500, 40);
		add(messageAvertissement);


		//Fait fonctionner les ecouteurs de la vue principale meme si la souris
		//est par-dessus le message d'aide.
		messageAide.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				e.translatePoint(messageAide.getX(), messageAide.getY());
				gestionMouseDragged(e);
			}
			@Override
			public void mouseMoved(MouseEvent e) {
				e.translatePoint(messageAide.getX(), messageAide.getY());
				gestionMouseMoved(e);
			}
		});
		messageAide.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				e.translatePoint(messageAide.getX(), messageAide.getY());
				gestionMousePressed(e);
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				e.translatePoint(messageAide.getX(), messageAide.getY());
				gestionMouseReleased(e);
			}
		});
		//Fait fonctionner les ecouteurs de la vue principale meme si la souris
		//est par-dessus le message d'aide.
		messageAvertissement.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				e.translatePoint(messageAvertissement.getX(), messageAvertissement.getY());
				gestionMouseDragged(e);
			}
			@Override
			public void mouseMoved(MouseEvent e) {
				e.translatePoint(messageAvertissement.getX(), messageAvertissement.getY());
				gestionMouseMoved(e);
			}
		});
		messageAvertissement.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				e.translatePoint(messageAvertissement.getX(), messageAvertissement.getY());
				gestionMousePressed(e);
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				e.translatePoint(messageAvertissement.getX(), messageAvertissement.getY());
				gestionMouseReleased(e);
			}
		});

	}

	@Override
	public void init(GLAutoDrawable drawable) {
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				gestionMouseDragged(e);
			}
			@Override
			public void mouseMoved(MouseEvent e) {
				gestionMouseMoved(e);
			}
		});
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				gestionMousePressed(e);
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				gestionMouseReleased(e);
			}
		});

		addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				gestionMouseWheelMoved(e);
			}
		});
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				gestionKeyPressed(e);
			}
			public void keyReleased(KeyEvent e) {
				gestionKeyReleased(e);
			}
		});

		ResourcesSingleton res = ResourcesSingleton.getInstance();
		res.loadTextures();
		res.loadIcones();
		textures = res.getTextures();
		outilsModification.createComboBoxTexture();
		outilsEdition.chargerIcones();

		options = OptionsSingleton.getInstance();
		gl = drawable.getGL().getGL2();
		glu = new GLU();

		//Sphere de fond
		fond = glu.gluNewQuadric();


		//initiation d'openGL
		gl.glViewport(0, 0, getWidth(), getHeight());
		gl.glMatrixMode(GL_PROJECTION);
		glu.gluPerspective(fov, (float)getWidth() / getHeight() , 1, 1E38);
		gl.glMatrixMode(GL_MODELVIEW);
		gl.glEnable(GL_DEPTH_TEST);
		gl.glEnable(GL_TEXTURE_2D);
		gl.glEnable(GL_COLOR_MATERIAL);

		renderer = new TextRenderer(new Font("SansSerif", Font.BOLD, 12));
		FPSAnimator animator = new FPSAnimator(this, OBJECTIF_FPS, true);
		animator.setUpdateFPSFrames(15, null);
		animator.start();

		processus = new Thread(this);
		processus.start();
	}

	/**
	 * Cree notre systeme solaire.
	 */
	public void creerSystemeSolaire() {
		faireSystemeSolaire = false;
		Astre a = new Astre(InfoAstre.SOLEIL.getNom(), InfoAstre.SOLEIL.getMasse(), Vector3.zero(),
				InfoAstre.SOLEIL.getRotation(), Vector3.zero(), InfoAstre.SOLEIL.getRayon(),
				gl, glu, InfoTexture.SOLEIL);
		a.setCouleurTrace(InfoAstre.SOLEIL.getCouleur());
		astres.add(a);

		InfoAstre[] infos = {InfoAstre.MERCURE, InfoAstre.VENUS, InfoAstre.TERRE, InfoAstre.MARS, InfoAstre.JUPITER,
				InfoAstre.SATURNE, InfoAstre.URANUS, InfoAstre.NEPTUNE, InfoAstre.PLUTON};
		InfoTexture[] textures = {InfoTexture.MERCURE, InfoTexture.VENUS, InfoTexture.TERRE, InfoTexture.MARS, InfoTexture.JUPITER,
				InfoTexture.SATURNE, InfoTexture.URANUS, InfoTexture.NEPTUNE, InfoTexture.PLUTON};
		for (int i = 0; i < infos.length; i++) {
			InfoAstre info = infos[i];
			double d = info.getDistanceOrbite();
			double angle = info.getAngleInclinaison();
			a = new Astre(info.getNom(), info.getMasse(), Vector3.zero(), info.getRotation(), 
					new Vector3(d * Math.cos(angle), d * Math.sin(angle), 0), info.getRayon(),
					gl, glu, textures[i]);
			a.setCouleurTrace(info.getCouleur());
			a.setVitesse(Calculs.calculVitesseCentripete(astres.get(0), a));
			a.setVitesseFutur(a.getVitesse());
			astres.add(a);
		}

		a = new Astre(InfoAstre.LUNE.getNom(), InfoAstre.LUNE.getMasse(), Vector3.zero(),
				InfoAstre.LUNE.getRotation(), new Vector3(InfoAstre.TERRE.getDistanceOrbite() + InfoAstre.LUNE.getDistanceOrbite(), 0, 0), InfoAstre.LUNE.getRayon(),
				gl, glu, InfoTexture.LUNE);
		a.setCouleurTrace(InfoAstre.LUNE.getCouleur());
		a.setVitesse(Calculs.calculVitesseCentripete(astres.get(3), a));
		a.setVitesseFutur(a.getVitesse());
		astres.add(a);
	}

	public void run() {
		while(vuePrincipaleEnVie) {
			if(!enPause)
				updateAnimation(updateTime);
			updateAffichage(sleepTime);
			try {
				Thread.sleep((long)sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}		
	}

	/**
	 * Met a jour l'application et calcule la nouvelle position des astres.
	 * 
	 * @param delta Le nombre de temps ecoule (ms)
	 */
	public void updateAnimation(double delta) {
		outilsTemps.update(delta);
		if(options.getTypeCalcul() == 2)
			setMessageAvertissement(2);

		if(options.isBarnesHut()){
			double plusLoins = 0;
			for(int i = 0; i < astres.size(); i++){
				if( astres.get(i).getPosition().magnitude() > plusLoins )
					plusLoins = astres.get(i).getPosition().magnitude();
			}
			tree= new Octree(0 ,new Vector3(), plusLoins* 3/2);	

			for (int i = 0; i < astres.size(); i++)
				if(!tree.existe(astres.get(i))){
					tree.insertToNode(astres.get(i));
					tree.insertToCollisionList(astres.get(i));
				}
			tree.barnesCollision();

			for(int i = 0; i < astres.size(); i++){
				Calculs.calculAttractionBarnesHut(tree, astres.get(i), delta,options.getAlpha(),options.getTypeCalcul());
				for(int u = 0 ; u < particules.size() ; u++){
					Calculs.calculAttractionParticule(astres.get(i),particules.get(u),delta);
				}
			}
		}else
			switch(options.getTypeCalcul()){

			case 0://Euler
				for(int i = 0; i < astres.size(); i++){
					for(int y = i+1; y < astres.size(); y++){
						Calculs.calculAttractionEulerPourDeuxAstres(astres.get(i),astres.get(y),delta);
					}
					for(int u = 0 ; u < particules.size() ; u++){
						Calculs.calculAttractionParticule(astres.get(i),particules.get(u),delta);
					}
					Vector3 tempPosition = new Vector3(astres.get(i).getPosition().x+astres.get(i).getVitesse().x*delta/1000.0,
							astres.get(i).getPosition().y+astres.get(i).getVitesse().y*delta/1000.0,
							astres.get(i).getPosition().z+astres.get(i).getVitesse().z*delta/1000.0); 

					astres.get(i).setPositionFutur(tempPosition); 
				}
				break;


			case 1://Euler Inverse
				for(int i = 0; i < astres.size(); i++){
					for(int y = i+1; y < astres.size(); y++){
						Calculs.calculAttractionEulerPourDeuxAstres(astres.get(i),astres.get(y),delta);
					}
					for(int u = 0 ; u < particules.size() ; u++){
						Calculs.calculAttractionParticule(astres.get(i),particules.get(u),delta);
					}

					Vector3 tempPosition = new Vector3(astres.get(i).getPosition().copy().x+astres.get(i).getVitesseFutur().x*delta/1000.0,
							astres.get(i).getPosition().copy().y+astres.get(i).getVitesseFutur().y*delta/1000.0,
							astres.get(i).getPosition().copy().z+astres.get(i).getVitesseFutur().z*delta/1000.0); 

					astres.get(i).setPositionFutur(tempPosition);

				}
				break;

			case 2://RK4
				for(int i = 0; i < astres.size(); i++){
					//astres.get(i).setPositionFutur(new Vector3());

					for(int y = i+1; y < astres.size(); y++){
						Calculs.calculAttractionRK4PourDeuxAstres(astres.get(i),astres.get(y),delta);
					}
					for(int u = 0 ; u < particules.size() ; u++){
						Calculs.calculAttractionParticule(astres.get(i),particules.get(u),delta);
					}
					astres.get(i).getPositionFutur().translate( astres.get(i).getVitesse().x*delta/1000.0,
							astres.get(i).getVitesse().y*delta/1000.0,
							astres.get(i).getVitesse().z*delta/1000.0);

					//System.out.println("i : "+i +"  PF : "+astres.get(i).getPositionFutur());

					//					Vector3 tempPosition = new Vector3(astres.get(i).getPosition().x + astres.get(i).getVitesse().x*delta/1000.0 + astres.get(i).getPositionFutur().x,
					//							astres.get(i).getPosition().y + astres.get(i).getVitesse().y*delta/1000.0 + astres.get(i).getPositionFutur().y,
					//							astres.get(i).getPosition().z + astres.get(i).getVitesse().z*delta/1000.0 + astres.get(i).getPositionFutur().z);
					//
					//					astres.get(i).setPositionFutur(tempPosition);
				}
				break;
			}

		for(int i = 0; i < astres.size(); i++){

			astres.get(i).update(delta);
			if(!astres.get(i).estEnVie())
				astres.remove(astres.get(i));
		}

		for(int u = 0 ; u < particules.size() ; u++){
			Vector3 tempPosition = new Vector3(particules.get(u).getPosition().x+particules.get(u).getVitesse().x*delta/1000.0,
					particules.get(u).getPosition().y+particules.get(u).getVitesse().y*delta/1000.0,
					particules.get(u).getPosition().z+particules.get(u).getVitesse().z*delta/1000.0); 
			particules.get(u).setPositionFutur(tempPosition); 

			particules.get(u).update(delta);
			if(!particules.get(u).estEnVie()){
				particules.remove(particules.get(u));
			}
		}

	}

	/**
	 * Met a jour l'affichage et calcule la nouvelle position de la camera.
	 * 
	 * @param delta Le nombre de temps ecoule (ms)
	 */
	public void updateAffichage(double delta) {
		updateCamera(delta);
		if (mouseX > messageAide.getX() && mouseX < messageAide.getX() + messageAide.getWidth() &&
				mouseY > messageAide.getY() && mouseY < messageAide.getY() + messageAide.getHeight()) {
			if (alphaTexte < 0.99)
				alphaTexte = (float)Math.min(0.99, alphaTexte * 1.15);
		} else 
			if (alphaTexte > 0.08f) 
				alphaTexte *= 0.999;

		if (alphaTexteAvertissement > 0.08f) 
			alphaTexteAvertissement *= 0.9995;
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		fps = TestEntree.arrondir(drawable.getAnimator().getLastFPS(), 1);

		if (outilsModification.isVisible())
			outilsModification.afficherInformations();

		if (outilsTemps.isVisible())
			outilsTemps.afficherInformations();

		//Les methodes ci-dessous doivent etre appelees dans le thread qui gere l'affichage OpenGL,
		//plutot que dans l'update, sinon certaines fonctionnalites ne sont pas disponibles.
		if (faireClicRayFocus)
			clicRayFocus();
		if (faireNouvelAstre)
			nouvelAstre();
		if (faireCalculateRay)
			calculateRay();
		if (faireCharger)
			charger();
		if(fairePlacementParticules)
			placerParticulesOrbitale();
		if(faireSystemeSolaire)
			creerSystemeSolaire();

		//Effacement de l'ecran
		gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();
		//Positionnement de la camera
		glu.gluLookAt((float)(cameraPos.x / ratio), (float)(cameraPos.y / ratio), (float)(cameraPos.z / ratio), 
				focus.x / ratio, focus.y / ratio, focus.z / ratio, 0, 1, 0);

		//Dessin du fond
		gl.glPushMatrix();
		gl.glTranslated(cameraPos.x / ratio, cameraPos.y / ratio, cameraPos.z / ratio);
		gl.glRotated(-90, 1, 0, 0);
		textures.get(InfoTexture.FOND.getNumero()).bind(gl);
		gl.glColor3d(1, 1, 1);
		glu.gluQuadricTexture(fond, true);
		gl.glDepthMask(false); //Permet d'eviter que les astres ne se dessinent derriere la sphere de fond
		glu.gluSphere(fond, 10, 100, 100);
		gl.glDepthMask(true);
		gl.glPopMatrix();

		gl.glBindTexture(GL.GL_TEXTURE_2D, 0);

		//Dessin des astres
		for (int i = 0; i < astres.size(); i++)
			dessinerAstre(astres.get(i));
		//Dessin des particules
		for (int i = 0; i < particules.size(); i++){
			particules.get(i).dessiner();

		}

		//Dessin de l'arbre de Barnes-Hut
		if(options.isBarnesHutVisuel()){
			ArrayList<Octree> temp = tree.OctreeUtilise();
			drawOctree(tree);
			for(int i = 0; i< temp.size(); i++)
				drawOctree(temp.get(i));
		}
		//Dessin des axes
		if(enPause){
			Astre a = getFocusedAstre();
			Vector3 p = new Vector3();
			if (a != null)
				p = a.getPosition();
			gl.glPushMatrix();
			gl.glTranslated(p.x / ratio, p.y / ratio, p.z / ratio);
			gl.glBegin(GL_LINES);
			gl.glColor3d(1, 0, 0);
			gl.glVertex3d(0, 0, 0);
			gl.glVertex3d(1E6, 0, 0);
			gl.glColor3d(0, 1, 0);
			gl.glVertex3d(0, 0, 0);
			gl.glVertex3d(0, 1E6, 0);
			gl.glColor3d(0, 0, 1);
			gl.glVertex3d(0, 0, 0);
			gl.glVertex3d(0, 0, 1E6);
			gl.glEnd();
			gl.glPopMatrix();
		}

		//Dessin de la trajectoire de la nouvelle planete

		if(etat != EtatCreation.PAS_COMMENCE && etat != EtatCreation.SELECTION_ASTRE && getFocusedAstre() != null) {
			//Les vecteurs d1 et d2 sont les deux vecteurs directeurs du plan sur lequel se deplacera le nouvel astre
			Vector3 d = focus.copie();
			Astre a = getFocusedAstre();
			if (a != null)
				d = a.getPosition().copie();
			Vector3 d1Base = Vector3.difference(d, nouvelAstre.getPosition());
			Vector3 d1 = d1Base.copie();
			Vector3 d2Base = Vector3.produitVectoriel(Vector3.haut(), d1).unitaire();
			double distance = d1.module();
			d2Base.mettreALEchelle(distance);
			Vector3 d2 = d2Base.copie();

			gl.glColor3d(1, 1, 1);
			gl.glPointSize(2f);
			gl.glBegin(GL_POINTS);
			double nPoints = 360;
			for(int i = 0; i < nPoints; i++) {
				double angle = 2*Math.PI / nPoints * i;
				d1.mettreALEchelle(Math.cos(angle));
				d2.mettreALEchelle(Math.sin(angle));

				Vector3 p = Vector3.somme(d, Vector3.somme(d1, d2));
				gl.glVertex3d(p.x / ratio, p.y / ratio, p.z / ratio);

				d1 = d1Base.copie();
				d2 = d2Base.copie();

			}
			gl.glEnd();
		}
	}

	/**
	 * Dessine l'astre passe en parametre, ou l'approxime par un point s'il est trop loin.
	 * 
	 * @param a L'astre a dessiner.
	 */
	public void dessinerAstre(Astre a) {
		if (a == null)
			return;

		//Ne pas afficher l'astre s'il est derriere
		Vector3 look = Vector3.difference(focus, cameraPos);
		Vector3 versAstre = Vector3.difference(a.getPosition(), cameraPos);
		boolean derriere = Vector3.produitScalaire(look, versAstre) / Vector3.produitScalaire(look, look) < 0;

		double diametreAngulaire = (a.getRayon() * 2 / Vector3.distance(cameraPos, a.getPosition())) * (180 / Math.PI);
		if (diametreAngulaire < 0.6) {
			Color couleur = a.getCouleurTrace();
			gl.glColor3d(couleur.getRed() / 255.0, couleur.getGreen() / 255.0, couleur.getBlue() / 255.0);
			gl.glPointSize((float)diametreAngulaire * 100 / 15);
			gl.glBegin(GL.GL_POINTS);
			Vector3 pos = a.getPosition();
			gl.glVertex3d(pos.x / ratio, pos.y / ratio, pos.z / ratio);
			gl.glEnd();
		} else if (!derriere)
			a.dessinerAstre();
		a.dessinerTrace();

		if(derriere || !options.isAfficherNoms())
			return;
		//Dessin du nom de l'astre
		int viewport[] = new int[4];
		double mvmatrix[] = new double[16];
		double projmatrix[] = new double[16];
		double pos[] = new double[4];
		Vector3 p = a.getPosition();

		gl.glGetIntegerv(GL_VIEWPORT, viewport, 0);
		gl.glGetDoublev(GL_MODELVIEW_MATRIX, mvmatrix, 0);
		gl.glGetDoublev(GL_PROJECTION_MATRIX, projmatrix, 0);
		glu.gluProject(p.x / ratio, p.y / ratio, p.z / ratio, mvmatrix, 0, projmatrix, 0, viewport, 0, pos, 0);
		renderer.beginRendering(getWidth(), getHeight());
		Color c = a.getCouleurTrace();
		renderer.setColor(c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f, 1f);
		renderer.draw(a.getNom(), (int)pos[0] - 25, (int)pos[1] + 5 * (int)diametreAngulaire + 5);
		renderer.endRendering();
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		for (int i = 0; i < astres.size(); i++) {
			astres.get(i).dispose();
		}

		glu.gluDeleteQuadric(fond);

		for (int i = 0; i < particules.size(); i++) {
			particules.get(i).dispose();
		}

		vuePrincipaleEnVie = false;
	}

	/**
	 * Determine si une planete a ete cliquee ou non pour lui attribuer le focus.
	 */
	private void clicRayFocus() {
		faireClicRayFocus = false;
		if (etat != EtatCreation.PAS_COMMENCE && etat != EtatCreation.SELECTION_ASTRE)
			return;

		calculateRay();

		double d1 = rayD.x;
		double d2 = rayD.y;
		double d3 = rayD.z;		

		double normeDSquared = d1*d1 + d2*d2 + d3*d3;

		LinkedList<Astre> astresTrouves = new LinkedList<Astre>();

		for (int i = 0; i < astres.size(); i++) {
			Astre a = astres.get(i);
			Vector3 p = a.getPosition();			
			double a1 = p.x - cameraPos.x;
			double a2 = p.y - cameraPos.y;
			double a3 = p.z - cameraPos.z;

			//Algebre lineaire
			double distanceSquared = (a2*a2*d3*d3 - 2*a2*a3*d2*d3 + a3*a3*d2*d2 + a1*a1*d3*d3 - 2*a1*a3*d1*d3 +
					a3*a3*d1*d1 + a1*a1*d2*d2 - 2*a1*a2*d1*d2 + a2*a2*d1*d1) / normeDSquared;
			double tailleAngulaire = (3 * Vector3.distance(cameraPos, a.getPosition())) / (180 / Math.PI);
			tailleAngulaire *= tailleAngulaire;

			if (distanceSquared <= a.getRayon() * a.getRayon() || distanceSquared <= tailleAngulaire)
				astresTrouves.add(a);
		}

		if (astresTrouves.isEmpty())
			return;
		//Donne le focus a l'astre trouve le plus pres de la camera
		Astre astreLePlusPres = astresTrouves.getFirst();
		for (int i = 1; i < astresTrouves.size(); i++) {
			Astre a = astresTrouves.get(i);
			if (Vector3.distance(a.getPosition(), cameraPos) < Vector3.distance(astreLePlusPres.getPosition(), cameraPos))
				astreLePlusPres = astresTrouves.get(i);
		}
		donnerFocus(astreLePlusPres);
		if (dernierClickCount >= 2)
			zoomSurFocusedAstre();
	}

	/**
	 * Calcule le rayon projete dans la scene a partir de la position de la souris.
	 */
	private void calculateRay() {
		faireCalculateRay = false;
		int x = mouseX;
		int y = 0;
		int viewport[] = new int[4];
		double mvmatrix[] = new double[16];
		double projmatrix[] = new double[16];
		double near[] = new double[4];
		double far[] = new double[4];

		gl.glGetIntegerv(GL_VIEWPORT, viewport, 0);
		gl.glGetDoublev(GL_MODELVIEW_MATRIX, mvmatrix, 0);
		gl.glGetDoublev(GL_PROJECTION_MATRIX, projmatrix, 0);
		y = viewport[3] - mouseY - 1;
		glu.gluUnProject((double)x, (double)y, 0.0, mvmatrix, 0, projmatrix, 0, viewport, 0, near, 0);
		glu.gluUnProject((double)x, (double)y, 0.95, mvmatrix, 0, projmatrix, 0, viewport, 0, far, 0);

		rayD = new Vector3(far[0] - near[0], far[1] - near[1], far[2] - near[2]);
	}

	/**
	 * Retourne l'astre qui possede le focus, s'il y en a un.
	 * @return L'astre qui a le focus. <br>Null si aucun astre n'a le focus.
	 */
	public Astre getFocusedAstre() {
		for (int i = 0; i < astres.size(); i++) {
			if (astres.get(i).isFocus())
				return astres.get(i);
		}
		return null;
	}

	/**
	 * Enleve le focus a l'astre qui le possede.
	 */
	private void enleverFocus() {
		suivreFocusedAstre = false;
		outilsModification.setVisible(false);
		Astre a = getFocusedAstre();
		if (a != null)
			a.setFocus(false);
	}

	/**
	 * Donne le focus a l'astre passe en parametre.
	 * 
	 * @param a L'astre qui aura le focus.
	 */
	private void donnerFocus(Astre a) {
		if (a == null)
			return;

		enleverFocus();
		outilsModification.setVisible(true);
		a.setFocus(true);
		outilsModification.afficherInformations();
		objectifFocusLastUpdate = objectifFocus.copy();
	}

	private void zoomSurFocusedAstre() {
		suivreFocusedAstre = true;
		Astre a = getFocusedAstre();
		if (a == null)
			return;

		objectifFocus = a.getPosition().copy();
		vitesseCamera = Vector3.distance(focus, objectifFocus) / 300;
		objectifCameraDistance = a.getRayon() * 5;
	}

	/**
	 * Determine la position de la camera en fonction de ses angles et de sa distance.
	 */
	private void determinerPositionCamera() {
		cameraPos.x = focus.x + cameraDistance * Math.cos(cameraAngle1 / 180 * Math.PI) * Math.cos(cameraAngle2 / 180 * Math.PI);
		cameraPos.y = focus.y + cameraDistance * Math.sin(cameraAngle2 / 180 * Math.PI);
		cameraPos.z = focus.z + cameraDistance * Math.sin(cameraAngle1 / 180 * Math.PI) * Math.cos(cameraAngle2 / 180 * Math.PI);
	}

	/**
	 * Met a jour la positon de la camera et du focus.
	 * @param delta Le nombre de temps ecoule.
	 */
	private void updateCamera(double delta) {
		Astre a = getFocusedAstre();
		if (a != null && suivreFocusedAstre) {
			Vector3 deltaAstre = Vector3.difference(getFocusedAstre().getPosition(), objectifFocusLastUpdate);
			objectifFocus.translate(deltaAstre);
			focus.translate(deltaAstre);
		}

		if (cameraDistance > objectifCameraDistance)
			cameraDistance = Math.max(objectifCameraDistance, cameraDistance * 0.9);
		else if (cameraDistance < objectifCameraDistance)
			cameraDistance = Math.min(objectifCameraDistance, cameraDistance * 1.1);

		determinerPositionCamera();

		//Deplace le focus vers l'astre qui possede le focus.
		Vector3 deltaFocus = Vector3.difference(objectifFocus, focus).unitaire();
		deltaFocus.scale(vitesseCamera * delta);
		if (Vector3.distance(focus, objectifFocus) <= deltaFocus.magnitude())
			focus = objectifFocus.copy();
		else
			focus.translate(deltaFocus);

		objectifFocusLastUpdate = objectifFocus.copy();
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		gl.glViewport(0, 0, width, height);
		outilsEdition.setLocation(10, height - outilsEdition.getHeight() - 10);
		outilsModification.setLocation(width - outilsModification.getWidth() - 10,
				(height - outilsModification.getHeight() - messageAide.getHeight() - 10) / 2);
		messageAide.setLocation(width - messageAide.getWidth() - 10,
				height - messageAide.getHeight() - 10);
		messageAvertissement.setLocation(width - messageAvertissement.getWidth() - 10,
				height - (messageAvertissement.getHeight()+messageAide.getHeight()) - 10);

		fov = fov / 180 * Math.PI;
		fov = Math.atan(Math.tan( fov / 2.0 ) * height / hauteurFenetre ) * 2.0 * 180 / Math.PI;
		hauteurFenetre = height;
		gl.glMatrixMode(GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluPerspective(fov, (float)width / height , 1, 1E38);
		gl.glMatrixMode(GL_MODELVIEW);		
	}

	/**
	 * Gere les actions a effectuer lorsque la souris est glissee.
	 * @param e L'evenement souris genere.
	 */
	public void gestionMouseDragged(MouseEvent e) {
		if (rightMouseButtonDown) {
			int dx = e.getX() - dernierMouseX;
			int dy = e.getY() - dernierMouseY;

			dernierMouseX = e.getX();
			dernierMouseY = e.getY();

			if (controlDown) {
				enleverFocus();
				Vector3 devant = Vector3.difference(focus, cameraPos);
				Vector3 haut = Vector3.up();
				Vector3 droite = Vector3.produitVectoriel(devant, haut);
				haut = Vector3.produitVectoriel(droite, devant);

				haut.rendreUnitaire();
				haut.mettreALEchelle(cameraDistance * dy / 4E2);
				droite.rendreUnitaire();
				droite.mettreALEchelle(cameraDistance * -dx / 4E2);

				focus.ajouter(haut);
				focus.ajouter(droite);
				objectifFocus = focus.copie();
			} else {	
				cameraAngle1 += dx / 5.0;
				cameraAngle1 %= 360;

				cameraAngle2 += dy / 5.0;
				if (cameraAngle2 <= -90)
					cameraAngle2 = Math.max(-89, cameraAngle2);
				if (cameraAngle2 >= 90)
					cameraAngle2 = Math.min(cameraAngle2, 89);
			}
		}
	}

	/**
	 * Gere les actions a effectuer lorsque la souris est deplacee.
	 * @param e L'evenement souris genere.
	 */
	public void gestionMouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();

		if(etat != EtatCreation.PAS_COMMENCE && etat != EtatCreation.SELECTION_ASTRE)
			faireCalculateRay = true;
		if(etat == EtatCreation.PLACEMENT_SUR_PLAN) {
			Vector3 p = nouvelAstre.getPosition();
			double k = 1;
			if (rayD.magnitudeSquared() != 0)
				k = (p.y - cameraPos.y) / rayD.y;
			double px = cameraPos.x + rayD.x * k;
			double pz = cameraPos.z + rayD.z * k;
			p.set(px, p.y, pz);

			double distance = Vector3.distance(p, focus);
			p.subtract(focus);
			p.normalize();
			p.scale(distance);
			p.add(focus);
		} else if (etat == EtatCreation.PLACEMENT_HAUTEUR) {
			Vector3 p = nouvelAstre.getPosition();
			Vector3 planD1 = new Vector3(0, 1, 0);
			Vector3 planD2 = Vector3.difference(p, focus);
			Vector3 planN = Vector3.produitVectoriel(planD2, planD1);
			double planD = planN.x * focus.x + planN.y * focus.y + planN.z * focus.z;			
			double k = (planD - Vector3.produitScalaire(planN, cameraPos)) / Vector3.produitScalaire(planN, rayD);

			double distance = Vector3.distance(p, focus);
			p.y = cameraPos.y + k * rayD.y;
			p.subtract(focus);
			p.normalize();
			p.scale(distance);
			p.add(focus);
		}
	}

	/**
	 * Gere les actions a effectuer lorsqu'un bouton de la souris est enfonce.
	 * @param e L'evenement souris genere.
	 */
	public void gestionMousePressed(MouseEvent e) {
		requestFocusInWindow(true);
		dernierClickCount = e.getClickCount();
		dernierMouseX = mouseX;
		dernierMouseY = mouseY;
		if (e.getButton() == 1) {
			switch(etat) {
			case PAS_COMMENCE:
			case SELECTION_ASTRE:
				faireClicRayFocus = true;
				break;
			case PLACEMENT_SUR_PLAN:
				messageAide.setText(aidePlacementHauteur);
				alphaTexte = 1;
				etat = EtatCreation.PLACEMENT_HAUTEUR;
				break;
			case PLACEMENT_HAUTEUR:
				messageAide.setText(aidePlacementPlan);
				alphaTexte = 1;
				etat = EtatCreation.PLACEMENT_SUR_PLAN;
				break;
			}
		}
		if (e.getButton() == 3)
			rightMouseButtonDown = true;

		if (e.getButton() == 2 && etat != EtatCreation.PAS_COMMENCE && etat != EtatCreation.SELECTION_ASTRE){
			messageAide.setText(aideMouvements);
			alphaTexte = 1;
			outilsEdition.setSize(140, outilsEdition.getHeight());
			etat = EtatCreation.PAS_COMMENCE;
			nouvelAstre.setPremiereTrace();
			Astre a = getFocusedAstre();
			if(a != null){
				nouvelAstre.setVitesse(Calculs.calculVitesseCentripete(a, nouvelAstre));
				nouvelAstre.setVitesseFutur(nouvelAstre.getVitesse());
			}
			outilsEdition.changeCreationEnCours();
			nouvelAstre = null;
		}
	}

	/**
	 * Gere les actions a effectuer lorsqu'un bouton de la souris est relache.
	 * @param e L'evenement souris genere.
	 */
	public void gestionMouseReleased(MouseEvent e) {
		if (e.getButton() == 3)
			rightMouseButtonDown = false;
	}

	/**
	 * Gere les actions a effectuer lorsque la molette de la souris est deplacee.
	 * <br>Permet d'ajuster la distance entre le focus et la camera (zoom).
	 * @param e L'evenement molette de la souris genere.
	 */
	public void gestionMouseWheelMoved(MouseWheelEvent e) {
		int dWheel = e.getWheelRotation();
		if (controlDown) {
			enleverFocus();
			Vector3 devant = Vector3.difference(focus, cameraPos);
			devant.rendreUnitaire();
			devant.mettreALEchelle(-dWheel * cameraDistance / 5 + 1E6);
			focus.ajouter(devant);
			objectifFocus = focus.copie();
		} else {
			if (dWheel > 0)
				cameraDistance *= 1.2;
			else if (dWheel < 0) {
				cameraDistance /= 1.2;
				Astre a = getFocusedAstre();
				if (a != null)
					cameraDistance = Math.max(cameraDistance, a.getRayon() * 2);
			}
			objectifCameraDistance = cameraDistance;
		}		
	}

	/**
	 * Gere les actions a effectuer lorsqu'une touche du clavier est enfoncee.
	 * @param e L'evenement clavier genere.
	 */
	public void gestionKeyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_SPACE:
			stop(getFocusedAstre());
			break;
		case KeyEvent.VK_ESCAPE:
			enleverFocus();
			break;
		case KeyEvent.VK_P:
			fairePlacementParticules = true;
			break;
		case KeyEvent.VK_T:
			if(--indiceFocus < 0)
				indiceFocus += astres.size();
			donnerFocus(astres.get(indiceFocus));
			break;
		case KeyEvent.VK_Y:
			indiceFocus = (indiceFocus + 1) % astres.size();
			donnerFocus(astres.get(indiceFocus));
			break;
		case KeyEvent.VK_U:
			zoomSurFocusedAstre();
			break;
		case KeyEvent.VK_CONTROL:
			controlDown = true;
			break;
		case KeyEvent.VK_M:
			outilsModification.setVisible(!outilsModification.isVisible());
			break;
		}
	}

	/**
	 * Gere les actions a effectuer lorsqu'une touche du clavier est relachee.
	 * @param e L'evenement clavier genere.
	 */
	public void gestionKeyReleased(KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_CONTROL:
			controlDown = false;
			break;
		}
	}

	/**
	 * Cree des particules en orbite avec l'astre selectionne
	 */
	public void placerParticulesOrbitale() {
		Astre tempAstre= getFocusedAstre();
		Vector3 tempVectorVitesse = null;
		fairePlacementParticules= false;
		if(tempAstre != null){
			double rayon = tempAstre.getRayon(); 
			for (int i = 0; i < 100; i++) {

				Particule tempParticule= new Particule(10000,new Vector3() ,
						new Vector3(tempAstre.getPosition().x + (Math.random() * 10*rayon- 5*rayon),
								tempAstre.getPosition().y,
								tempAstre.getPosition().z + (Math.random() * 10*rayon - 5*rayon)),
								rayon/10, gl, glu, InfoTexture.LUNE);

				tempVectorVitesse=Calculs.calculVitesseCentripeteP(tempAstre,tempParticule);
				tempParticule.setVitesse(tempVectorVitesse);
				tempParticule.setVitesseFutur(tempVectorVitesse);

				particules.add(tempParticule);
			}
		}else{
			for (int i = 0; i < 200; i++) {
				particules.add( new Particule(10000, new Vector3(0,0,0),
						new Vector3(Math.random() * 0.8E13- 0.4E13,0, Math.random() * 0.8E13 - 0.4E13),
						1.0E6, gl, glu, InfoTexture.LUNE));
			}
		}
	}

	/**
	 * Fait cesser le mouvement de l'astre a.
	 * @param a L'astre qui a affecter.
	 */
	public void stop(Astre a){
		if (a != null)
			a.setVitesseFutur(new Vector3());
	}

	/**
	 * Fait demarrer l'animation.
	 */
	public void play() {
		enPause = false;
		outilsEdition.setVisible(false);

		if(etat != EtatCreation.PAS_COMMENCE) {
			annulerCreation();
		}
	}

	/**
	 * Met l'animation en pause.
	 */
	public void pause() {
		enPause = true;
		outilsEdition.setVisible(true);
	}

	/**
	 * Indique si l'animation est en pause ou non.
	 * @return Si l'animation est en pause ou non.
	 */
	public boolean isEnPause() {
		return enPause;
	}

	/**
	 * Decide qu'il faudra ajouter un astre (la creation doit se faire dans le thread d'OpenGL).
	 */
	public void initierAjoutAstre() {
		if(etat == EtatCreation.PAS_COMMENCE) {
			etat = EtatCreation.SELECTION_ASTRE;
			messageAide.setText(aideSelection);
			alphaTexte = 1;
			outilsEdition.setSize(670, outilsEdition.getHeight());
		}
	}

	/**
	 * Indique au thread d'OpenGL d'appeler la methode nouvelAstre.
	 * @param infoNouvelAstre Le type d'astre a creer.
	 */
	public void faireNouvelAstre(InfoAstre infoNouvelAstre) {
		this.infoNouvelAstre = infoNouvelAstre;
		if (etat == EtatCreation.SELECTION_ASTRE)
			faireNouvelAstre = true;
	}

	/**
	 * Ajoute un nouvel astre.
	 */
	public void nouvelAstre() {
		faireNouvelAstre = false;
		Vector3 pos = focus.copie();
		Astre a = getFocusedAstre();
		if (a != null)
			pos = a.getPosition().copie();
		nouvelAstre = new Astre(infoNouvelAstre.getNom(), infoNouvelAstre.getMasse(), new Vector3(), infoNouvelAstre.getRotation(), pos,
				infoNouvelAstre.getRayon(), gl, glu, InfoTexture.TERRE);
		//On cherche la bonne texture
		InfoTexture info = InfoTexture.SOLEIL;
		for (InfoTexture i : InfoTexture.values()) {
			InfoAstre ia = i.getInfoAstre();
			if (ia != null && ia.getNom().equals(infoNouvelAstre.getNom()))
				info = i;
		}
		nouvelAstre.setTexture(info);
		nouvelAstre.setCouleurTrace(infoNouvelAstre.getCouleur());
		astres.add(nouvelAstre);
		etat = EtatCreation.PLACEMENT_SUR_PLAN;
		messageAide.setText(aidePlacementPlan);
		alphaTexte = 1;
	}

	/**
	 * Annule la creation d'un nouvel astre.
	 */
	public void annulerCreation() {
		astres.remove(nouvelAstre);
		etat = EtatCreation.PAS_COMMENCE;
		messageAide.setText(aideMouvements);
		alphaTexte = 1;
		outilsEdition.changeCreationEnCours();
		outilsEdition.setSize(140, outilsEdition.getHeight());
	}

	/**
	 * Dessine l'arbre en 3D.
	 * @param octree L'arbre a dessiner.
	 */
	public void drawOctree(Octree octree){
		gl.glPushMatrix();
		gl.glTranslated(octree.getCentre().x / ratio, octree.getCentre().y / ratio, octree.getCentre().z / ratio);
		Color couleur = Color.getHSBColor(0, 150, 255);

		double r = octree.getProportion() / ratio;

		if(octree.getnbAstre() == 1){
			Astre a = octree.getAstreSeul();
			if (a != null)
				couleur = a.getCouleurTrace();
		}

		gl.glColor3d(couleur.getRed()/ 255.0,couleur.getGreen()/ 255.0,couleur.getBlue()/ 255.0);
		//Dessine les arretes d'un cube
		gl.glBegin(GL.GL_LINE_STRIP);
		gl.glVertex3d(-r, r, -r);
		gl.glVertex3d(-r, r, r);
		gl.glVertex3d(r, r, r);
		gl.glVertex3d(r, r, -r);
		gl.glVertex3d(-r, r, -r);
		gl.glVertex3d(-r, -r, -r);
		gl.glVertex3d(-r, -r, r);
		gl.glVertex3d(r, -r, r);
		gl.glVertex3d(r, -r, -r);
		gl.glVertex3d(-r, -r, -r);
		gl.glEnd();
		gl.glBegin(GL.GL_LINES);
		gl.glVertex3d(-r, r, -r);
		gl.glVertex3d(-r, -r, -r);
		gl.glVertex3d(-r, r, r);
		gl.glVertex3d(-r, -r, r);
		gl.glVertex3d(r, r, r);
		gl.glVertex3d(r, -r, r);
		gl.glVertex3d(r, r, -r);
		gl.glVertex3d(r, -r, -r);
		gl.glEnd();

		gl.glPopMatrix();	
	}

	/**
	 * Change la vitesse d'ecoulement du temps.
	 * @param vitesseTemps La nouvelle vitesse d'ecoulement du temps.
	 */
	public void setVitesseTemps(double vitesseTemps) {
		updateTime =  vitesseTemps * sleepTime;
	}

	/**
	 * Retourne la vitesse courante d'ecoulement du temps.
	 * @return La vitesse d'ecoulement du temps.
	 */
	public double getVitesseTemps() {
		return updateTime / sleepTime;
	}

	/**
	 * Retourne le dernier nombre d'images par seconde calcule.
	 * @return Le nombre d'images par seconde.
	 */
	public double getFps() {
		return fps;
	}

	/**
	 * Sauvegarde la vue principale dans le fichier choisi.
	 * @param oos Le flux d'ecriture du fichier.
	 * @throws IOException
	 */
	public void sauvegarder(ObjectOutputStream oos) throws IOException {		
		//Astres
		oos.writeObject(new Integer(astres.size()));
		for (int i = 0; i < astres.size(); i++) {
			Astre a = astres.get(i);
			oos.writeObject(a.getPosition());
			oos.writeObject(a.getRotation());
			oos.writeObject(a.getVitesse());
			oos.writeObject(new Double(a.getMasse()));
			oos.writeObject(new Double(a.getVitesseRotation()));
			oos.writeObject(a.getCouleurTrace());
			oos.writeObject(a.getCouleur());
			oos.writeObject(a.getNom());
			oos.writeObject(new Boolean(a.estEnVie()));
			oos.writeObject(new Double(a.getRayon()));
			oos.writeObject(a.getTexture());
		}

		//focus et camera
		oos.writeObject(focus);
		oos.writeObject(new Double(cameraAngle1));
		oos.writeObject(new Double(cameraAngle2));
		oos.writeObject(new Double(cameraDistance));

		//options
		oos.writeObject(new Boolean(options.isAfficherFPS()));
		oos.writeObject(new Boolean(options.isAfficherNoms()));
		oos.writeObject(new Boolean(options.isBarnesHut()));
		oos.writeObject(new Integer(options.getTypeCalcul()));
		oos.writeObject(new Double(options.getAlpha()));
		oos.writeObject(new Integer(options.getQualiteAstres()));
	}

	/**
	 * Charge la vue principale a partir du flux de lecture ouvert.
	 */
	public void charger(){
		faireCharger = false;
		enPause = true;
		astres.clear();
		int nAstres;
		try {
			nAstres = (Integer)ois.readObject();
			for (int i = 0; i < nAstres; i++) {
				Vector3 pos = (Vector3)ois.readObject();
				Vector3 rot = (Vector3)ois.readObject();
				Vector3 vit = (Vector3)ois.readObject();
				double masse = (Double)ois.readObject();
				double vitRot = (Double)ois.readObject();
				Color couleurTrace = (Color)ois.readObject();
				Color couleurAstre = (Color)ois.readObject();
				String n = (String)ois.readObject();
				boolean enVie = (Boolean)ois.readObject();
				double rayon = (Double)ois.readObject();
				InfoTexture t = (InfoTexture)ois.readObject();
				Astre a = new Astre(n, masse, vit, vitRot, pos, rayon, gl, glu, t);
				a.setRotation(rot);
				a.setEnVie(enVie);
				a.setCouleurTrace(couleurTrace);
				a.setCouleur(couleurAstre);
				astres.add(a);
			}

			focus = (Vector3)ois.readObject();
			cameraAngle1 = (Double)ois.readObject();
			cameraAngle2 = (Double)ois.readObject();
			cameraDistance = (Double)ois.readObject();

			options.setAfficherFPS((Boolean)ois.readObject());
			options.setAfficherNoms((Boolean)ois.readObject());
			options.setBarnesHut((Boolean)ois.readObject());
			options.setTypeCalcul((Integer)ois.readObject());
			options.setAlpha((Double)ois.readObject());
			options.setQualiteAstres((Integer)ois.readObject());

		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		} finally {
			try {
				ois.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		enPause = false;
	}

	/**
	 * Initie le chargement et specifie le flux de lecture.
	 * @param ois Le flux de lecture.
	 */
	public void chargementInitial(ObjectInputStream ois) {
		faireCharger = true;
		this.ois = ois;
	}
	/**
	 * Change le message afficher pas le message d'avertissement et le rend visible.
	 * 
	 * @param message =1 pour signaler une valeur trop petite, 1 pour signaler une valeur trop grosse, 2 pour signaler le RK4 probleme.
	 */
	public void setMessageAvertissement(int message){
		if(message < 0) {
			messageAvertissement.setText(avertissementMinimum);
		}else{
			if(message == 1)
				messageAvertissement.setText(avertissementMaximum);	
			else
				messageAvertissement.setText(avertissementRK4);	
		}
		alphaTexteAvertissement = 1;
	}
}
