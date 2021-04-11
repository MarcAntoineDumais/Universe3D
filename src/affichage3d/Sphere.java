package affichage3d;

import java.io.Serializable;
import java.util.ArrayList;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

import com.jogamp.opengl.util.texture.Texture;

import simulateur.InfoTexture;
import simulateur.OptionsSingleton;
import simulateur.ResourcesSingleton;
import aapplication.App06SimulateurUnivers;

/**
 * Cette classe permet de creer et de dessiner une sphere a un endroit precis avec une texture precise.
 * 
 * @author Marc-Antoine Dumais
 */
public class Sphere implements Serializable {
	private static final long serialVersionUID = -8607523509762679245L;
	private GLUquadric quadric;
	private double rayon;
	private ArrayList<Texture> textures;
	private OptionsSingleton options;
	private InfoTexture texture;
	private GLU glu;
	private GL2 gl;
	private boolean focus;
	private double ratio = App06SimulateurUnivers.RATIO;
	
	public Sphere(GL2 gl, GLU glu, double rayon,boolean particule ,  InfoTexture t) {
		textures = ResourcesSingleton.getInstance().getTextures();
		options = OptionsSingleton.getInstance();
		texture = t;
		this.gl = gl;
		this.glu = glu;
		
		this.rayon = rayon;
		quadric = this.glu.gluNewQuadric();
		focus = false;
	}
	
	/**
	 * Dessine la sphere a un endroit precis.
	 * @param position L'endroit ou dessiner la sphere.
	 * @param rotation La rotation a donner a la sphere.
	 */
	public void dessiner(Vector3 position, Vector3 rotation) {
		textures.get(texture.getNumero()).bind(gl);
		gl.glPushMatrix();
		gl.glTranslated(position.x / ratio, position.y / ratio, position.z / ratio);
		//Permet que la texture soit affichee a l'endroit
		gl.glRotated(-90, 1, 0, 0);
		gl.glRotated(rotation.x, 1, 0, 0);
		gl.glRotated(rotation.y, 0, 0, 1);
		gl.glRotated(-rotation.z, 0, 1, 0);
		glu.gluQuadricTexture(quadric, true);
        glu.gluQuadricDrawStyle(quadric, GLU.GLU_FILL);
        glu.gluQuadricNormals(quadric, GLU.GLU_FLAT);
        glu.gluQuadricOrientation(quadric, GLU.GLU_OUTSIDE);
        glu.gluSphere(quadric, rayon / ratio, options.getQualiteAstres(), options.getQualiteAstres());
        
        gl.glBindTexture(GL.GL_TEXTURE_2D, 0);
        //Cube rouge autour de la sphere pour indiquer qu'elle a un focus
        if (focus) {
			double r = rayon / ratio;
			gl.glColor3d(1, 0, 0);
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
		}
        gl.glPopMatrix();
	}

	/**
	 * Supprime le quadric utilise pour afficher la sphere.
	 */
	public void dispose() {
		glu.gluDeleteQuadric(quadric);
	}
	
	/**
	 * Indique si la sphere a le focus.
	 * @return Si la sphere a le focus.
	 */
	public boolean isFocus() {
		return focus;
	}
	
	/**
	 * Donne le focus a la sphere.
	 * @param b Le focus.
	 */
	public void setFocus(boolean b) {
		focus = b;
	}
	
	/**
	 * Change le rayon de la sphere.
	 * @param r Le nouveau rayon.
	 */
	public void setRayon(double r){
		rayon = r;
	}
	
	/**
	 * Retourne le rayon de la sphere.
	 * @return Le rayon de la sphere.
	 */
	public double getRayon() {
		return rayon;
	}
	
	/**
	 * Change la texture de la sphere.
	 * @param info La nouvelle texture.
	 */
	public void setTexture(InfoTexture info) {
		texture = info;
	}
	
	/**
	 * Retourne la texture de la sphere.
	 * @return La texture.
	 */
	public InfoTexture getTexture() {
		return texture;
	}
}
