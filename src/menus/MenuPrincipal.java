package menus;

import static javax.media.opengl.GL.GL_COLOR_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DEPTH_TEST;
import static javax.media.opengl.GL.GL_TEXTURE_2D;
import static javax.media.opengl.GL.GL_VIEWPORT;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_COLOR_MATERIAL;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW_MATRIX;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION_MATRIX;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import simulateur.Astre;
import simulateur.Calculs;
import simulateur.InfoAstre;
import simulateur.InfoTexture;
import simulateur.ResourcesSingleton;
import aapplication.App06SimulateurUnivers;
import affichage3d.Vector3;

import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.awt.TextRenderer;
import com.jogamp.opengl.util.texture.Texture;

/**
 * Cree le menu principal (avec animation).
 * @author Guillaume Bellemare-Roy
 * <br>Participation de Marc-Antoine Dumais
 */
public class MenuPrincipal extends GLJPanel implements GLEventListener, Runnable, Serializable {
	private static final long serialVersionUID = -3428502020841704854L;
	private App06SimulateurUnivers app;
	private JLabel lblTitre;
	private JButton btnCommencer, btnOptions, btnAide, btnQuitter;
	
	//Animation
	private final double VITESSE_ANIMATION = 300000;
	private double ratio = App06SimulateurUnivers.RATIO;
	private Thread processus;
	private boolean menuPrincipalEnVie = true;
	private LinkedList<Astre> astres;
	private GL2 gl;
	private GLU glu;
	private TextRenderer renderer;
	private ArrayList<Texture> textures;
	private GLUquadric fond;
	private Vector3 cameraPos = getPositionCamera(120, 25, 2E11);
	private Vector3 focus = new Vector3(5E11, 0, 0);
	
	public MenuPrincipal(App06SimulateurUnivers application) {
		app = application;
		addGLEventListener(this);
		setLayout(null);
		setBounds(0, 0, 1280, 700);
		
		lblTitre = new JLabel("Simulateur d'univers ");
		lblTitre.setOpaque(false);
		lblTitre.setForeground(Color.GREEN);
		lblTitre.setFont(new Font("Tahoma", Font.BOLD, 60));
		lblTitre.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitre.setBounds(210, 20, 918, 89);
		add(lblTitre);
		
		btnCommencer = new JButton("Commencer");
		btnCommencer.setBounds(1128, 425, 130, 44);
		add(btnCommencer);
		
		btnOptions = new JButton("Options");
		btnOptions.setBounds(1128, 493, 130, 44);
		add(btnOptions);
		
		btnAide = new JButton("Aide");
		btnAide.setBounds(1128, 561, 130, 44);
		add(btnAide);
		
		btnQuitter = new JButton("Quitter");
		btnQuitter.setBounds(1128, 629, 130, 44);
		add(btnQuitter);
		
		
		btnQuitter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnAide.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				app.afficherAide();
			}
		});
		btnOptions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				app.afficherOptions();
			}
		});
		btnCommencer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				app.ouvrirMenuCommencer();
			}
		});
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		astres = new LinkedList<Astre>();
		gl = drawable.getGL().getGL2();
		glu = new GLU();
		
		ResourcesSingleton res = ResourcesSingleton.getInstance();
		res.loadTextures();
		textures = res.getTextures();
		
		//Systeme solaire
		double[] rayons = {1.46E10, 1.01E9, 2.07E9, 2.15E9, 1.33E9, 7.77E9, 7.29E9, 5E9, 4.92E9, 5.16E8, 7.48E8};
		Astre a = new Astre(InfoAstre.SOLEIL.getNom(), InfoAstre.SOLEIL.getMasse(), Vector3.zero(),
							InfoAstre.SOLEIL.getRotation(), Vector3.zero(), rayons[0],
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
						  new Vector3(d * Math.cos(angle), d * Math.sin(angle), 0), rayons[i + 1],
						  gl, glu, textures[i]);
			a.setCouleurTrace(info.getCouleur());
			a.setVitesse(Calculs.calculVitesseCentripete(astres.get(0), a));
			a.setVitesseFutur(a.getVitesse());
			astres.add(a);
		}
		
		//Sphere de fond
		fond = glu.gluNewQuadric();
		
		//initiation d'openGL
		gl.glViewport(0, 0, getWidth(), getHeight());
		gl.glMatrixMode(GL_PROJECTION);
		glu.gluPerspective(70, (float)getWidth() / getHeight() , 1, 1E38);
		gl.glMatrixMode(GL_MODELVIEW);
		gl.glEnable(GL_DEPTH_TEST);
		gl.glEnable(GL_TEXTURE_2D);
		gl.glEnable(GL_COLOR_MATERIAL);

		renderer = new TextRenderer(new Font("SansSerif", Font.BOLD, 12));
		FPSAnimator animator = new FPSAnimator(this, 60, true);
		animator.setUpdateFPSFrames(1, null);
		animator.start();
		
		processus = new Thread(this);
		processus.start();
	}
	
	@Override
	public void run() {
		while(menuPrincipalEnVie) {
			update(10 * VITESSE_ANIMATION);
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Met l'animation a jour.
	 * @param delta Le temps ecoule.
	 */
	public void update(double delta) {
		for(int i = 0; i < astres.size(); i++){
			for(int y = i+1; y < astres.size(); y++)
				Calculs.calculAttractionEulerPourDeuxAstres(astres.get(i),astres.get(y),delta);
			
			Vector3 tempPosition = new Vector3(astres.get(i).getPosition().x+astres.get(i).getVitesse().x*delta/1000.0,
					astres.get(i).getPosition().y+astres.get(i).getVitesse().y*delta/1000.0,
					astres.get(i).getPosition().z+astres.get(i).getVitesse().z*delta/1000.0); 
			 
			astres.get(i).setPositionFutur(tempPosition); 
		}
		for(int i = 0; i < astres.size(); i++){
			astres.get(i).update(delta);
			if(!astres.get(i).estEnVie())
				astres.remove(astres.get(i));
		}
	}
	
	@Override
	public void display(GLAutoDrawable drawable) {
		//Effacement de l'ecran
		gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();
		//Positionnement de la camera
		glu.gluLookAt((float)(cameraPos.x / ratio), (float)(cameraPos.y / ratio), (float)(cameraPos.z / ratio), 
				focus.x / ratio, focus.y / ratio, focus.z / ratio, 0, 1, 0);

		//Dessin du fond
		gl.glPushMatrix();
		gl.glTranslated(cameraPos.x / ratio, cameraPos.y / ratio, cameraPos.z / ratio);
		gl.glRotated(-90, 1, 0, 0); //Met la texture du fond dans le bon sens
		try {
			textures.get(InfoTexture.FOND.getNumero()).bind(gl);
		} catch (IndexOutOfBoundsException e) {
			System.out.println("OUT OF BOUNDS");
			return;
		}
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
	}
	
	public void dessinerAstre(Astre a) {
		if (a == null)
			return;
		
		a.dessinerAstre();
		a.dessinerTrace();

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
	    double diametreAngulaire = (a.getRayon() * 2 / Vector3.distance(cameraPos, a.getPosition())) * (180 / Math.PI);
	    renderer.draw(a.getNom(), (int)pos[0] - 25, (int)pos[1] + 5 * (int)diametreAngulaire + 5);
	    renderer.endRendering();
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		for (int i = 0; i < astres.size(); i++) {
			astres.get(i).dispose();
		}
		
		glu.gluDeleteQuadric(fond);
		
		menuPrincipalEnVie = false;
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int arg1, int arg2, int arg3, int arg4) {
		// TODO Auto-generated method stub
		
	}
	
	private Vector3 getPositionCamera(double cameraAngle1, double cameraAngle2, double cameraDistance) {
		double x = cameraDistance * Math.cos(cameraAngle1 / 180 * Math.PI) * Math.cos(cameraAngle2 / 180 * Math.PI);
		double y = cameraDistance * Math.sin(cameraAngle2 / 180 * Math.PI);
		double z = cameraDistance * Math.sin(cameraAngle1 / 180 * Math.PI) * Math.cos(cameraAngle2 / 180 * Math.PI);
		return new Vector3(x, y, z);
	}
}
