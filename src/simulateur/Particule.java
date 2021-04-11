package simulateur;
import java.io.Serializable;
import java.util.ArrayList;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

import aapplication.App06SimulateurUnivers;
import affichage3d.Sphere;
import affichage3d.Vector3;

/**
 * Cette classe represente l'objet d'une particule.Elle contient les informations necesaires a une particule.
 * 
 * @author David-Olivier Duperron
 * 
 * <br>Date de creation : ~2/24/14~
 */

public class Particule implements Serializable{
	private static final long serialVersionUID = 4799220226699798792L;

	private static final int LONGUEUR_TRACE = 1;

	private Sphere sphere;
	private Vector3 position;
	@SuppressWarnings("unused")
	private Vector3 rotation; // Present que pour remplir les besoins de la creation d'une sphere.
	private Vector3 vitesse;
	private Vector3 vitesseFutur;
	private Vector3 positionFutur;
	private double masse;
	private ArrayList<Vector3> trace;
	private GL2 gl;
	private double ratio = App06SimulateurUnivers.RATIO;
	private boolean enVie = true;

	public Particule(double mas,Vector3 vit, Vector3 pos, double radius,GL2 gl, GLU glu, InfoTexture t) {
		this.gl = gl;
		sphere = new Sphere(gl, glu, radius,true, t);
		masse = mas;
		vitesse = vit;
		position = pos;
		positionFutur = pos;
		vitesseFutur = vit;
		setRotation(new Vector3());
		trace = new ArrayList<Vector3>();
	}
	/**
	 * Dessine la sphere qui represente la particule.
	 */
	public void dessiner() {
		if(enVie){
			sphere.dessiner(position, new Vector3());

			//trace
			gl.glColor3d(1, 1, 1);
			for (int i = 0; i < trace.size(); i++) {
				gl.glPointSize((i + 1) / 100f);
				gl.glBegin(GL.GL_POINTS);
				Vector3 pos = trace.get(i);
				gl.glVertex3d(pos.x / ratio, pos.y / ratio, pos.z / ratio);
				gl.glEnd();
			}
		}
	}
	/**
	 * Update tout ce qui doit etre changer pendant une frame. ( position et trace) 
	 * 
	 * @param delta
	 */
	public void update(double delta) {
		delta /= 1000;
		
		if(vitesseFutur.magnitude() > 3E8){
			vitesseFutur.scale(1/(vitesseFutur.magnitude()/3.0E8));
		}
		if( positionFutur.magnitude() > 2.5E14  ){
			dispose();
		}
			position = positionFutur;
			vitesse = vitesseFutur;		

			//trace
			if (trace.size() >= LONGUEUR_TRACE)
				trace.remove(0);
			trace.add(position.copy());
	}
	
	//GETTER ET SETTER
	/**
	 * Indique si la particule est encore en vie.
	 * 
	 * @return True si la particule est en vie, False si la particule n'existe plus.
	 */
	public boolean estEnVie(){	
		return enVie;
	}
	/**
	 * Elimine la particule.
	 */
	public void dispose() {
		sphere.dispose();
		enVie = false;
	}
	/**
	 * Donne une nouvelle vitesse avec les coordonnees en x, y et z. 
	 * 
	 * @param x La coordonnee x.
	 * @param y La coordonnee y.
	 * @param z La coordonnee z.
	 */
	public void setVitesse(double x,double y, double z){
		vitesse = new Vector3(x,y,z);
	}
	/**
	 * Donne une nouvelle vitesse avec un Vector3(x,y,z).
	 * 
	 * @param vit La nouvelle vitesse.
	 */
	public void setVitesse(Vector3 vit){
		vitesse= vit;
	}
	/**
	 * Change la position de la particule par les coordonnees en x, y et z
	 * 
	 * @param x La coordonnee x.
	 * @param y La coordonnee y.
	 * @param z La coordonnee z.
	 */
	public void setPosition(double x,double y, double z){
		position = new Vector3(x,y,z);
	}
	/**
	 *  Change la masse de la particule.
	 * 
	 * @param masse La nouvelle masse.
	 */
	public void setMasse(double masse){
		this.masse = masse;
	}
	/**
	 * Change la vitesse futur avec un Vector3(x,y,z).
	 * 
	 * @param vitFutur La nouvelle vitesse futur.
	 */
	public void setVitesseFutur(Vector3 vitFutur){
		vitesseFutur = vitFutur;
	}
	/**
	 * Change la position futur avec un Vector3(x,y,z).
	 * 
	 * @param posFutur La nouvelle position futur.
	 */
	public void setPositionFutur(Vector3 posFutur){
		positionFutur = posFutur;
	}
	/**
	 * Retourne la vitesse futur de la particule.
	 * 
	 * @return La vitesse futur de la particule.
	 */
	public Vector3 getVitesseFutur(){
		return vitesseFutur;
	}
	/**
	 * Retourne la position futur de la particule.
	 * 
	 * @return La position futur de la particule.
	 */
	public Vector3 getPositionFutur() {
		return positionFutur;
	}
	/**
	 * Retourne la vitesse de la particule.
	 * 
	 * @return La vitesse de la particule.
	 */
	public Vector3 getVitesse(){
		return vitesse;
	}
	/**
	 * Retourne la position de la particule.
	 * 
	 * @return La position de la particule.
	 */
	public Vector3 getPosition() {
		return position;
	}
	/**
	 * Retourne la masse de la particule.
	 * 
	 * @return La masse de la particule.
	 */
	public double getMasse(){
		return masse;
	}
	/**
	 * Retourne le rayon de la particule.
	 * 
	 * @return Le rayon de la particule.
	 */
	public double getRayon() {
		return sphere.getRayon();
	}
	/**
	 * Change la texture de la particule.
	 * 
	 * @param info La texture. 
	 */
	public void setTexture(InfoTexture info) {
		sphere.setTexture(info);
	}
	/**
	 * Retourne la texture attribue a cette particule.
	 * 
	 * @return La texture attribue a cette particule.
	 */
	public InfoTexture getTexture() {
		return sphere.getTexture();
	}
	/**
	 * Retourne le numero de la texture attribue a cette particule.
	 * 
	 * @return Le numero de la texture attribue a cette particule.
	 */
	public int getNumeroTexture() {
		return getTexture().getNumero();
	}
	/**
	 * Change la rotation de la particule (de son axe y)
	 * 
	 * @param rotation La nouvelle rotation (de son axe y).
	 */
	public void setRotation(Vector3 rotation) {
		this.rotation = rotation;
	}
}
