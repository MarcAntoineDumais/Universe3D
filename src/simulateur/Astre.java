package simulateur;
import java.awt.Color;

import java.util.ArrayList;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

import aapplication.App06SimulateurUnivers;
import affichage3d.Sphere;
import affichage3d.Vector3;

/**
 * Cette classe represente l'objet d'un astre.Cette classe contient les informations necesaires a un astre.
 * 
 * @author Marc-Antoine Dumais
 * <br> Petite participation de David-Olivier Duperron
 * 
 * <br>Date de creation : 2/14/14
 */
public class Astre {	
	private Sphere sphere;
	private Vector3 position;
	private Vector3 rotation;
	private Vector3 vitesse;
	private Vector3 positionFutur;
	private Vector3 vitesseFutur;
	private double masse;
	private double vitesseRotation;
	private ArrayList<Vector3> trace;
	private Color couleurTrace;
	private Color couleur;
	private GL2 gl;
	private double ratio = App06SimulateurUnivers.RATIO;
	private String nom = "";
	private boolean enVie = true;

	private int nbPointsTrace = 50;
	private double tempsTotalTrace = 2000;
	private double tempsEntrePointsTrace = tempsTotalTrace / nbPointsTrace;
	private double tempsDepuisDerniereTrace = 0;

	public Astre(String a, double mas, Vector3 vit, double vitesseRotation, Vector3 pos, double radius,GL2 gl, GLU glu, InfoTexture t) {
		this.gl = gl;
		nom = a;
		sphere = new Sphere(gl, glu, radius,false, t);
		masse = mas;
		vitesse = vit;
		position = pos;
		positionFutur= pos.copy();
		vitesseFutur= vit.copy();
		rotation = new Vector3();
		this.vitesseRotation = vitesseRotation;
		trace = new ArrayList<Vector3>();
		trace.add(pos.copy());
		couleur =  Color.white;
		couleurTrace = Color.white;
	}
	/**
	 * Contructeur pour un astre agissant comme un centre de masse.
	 * 
	 * @param mas La masse de l'astre.
	 * @param pos La position de l'astre.
	 */
	public Astre(double mas, Vector3 pos){
		masse = mas;
		position = pos;
	}
	/**
	 * Dessine la sphere qui représente l'astre avec la couleur qui lui est assigne.
	 */
	public void dessinerAstre() {
		gl.glColor3d(couleur.getRed() / 255.0, couleur.getGreen() / 255.0, couleur.getBlue() / 255.0);
		sphere.dessiner(position, rotation);		
	}
	/**
	 * Dessine la trace de l'astre. 
	 * 
	 */
	public void dessinerTrace() {
		gl.glLineWidth(1);
		gl.glColor3d(couleurTrace.getRed() / 255.0, couleurTrace.getGreen() / 255.0, couleurTrace.getBlue() / 255.0);
		gl.glBegin(GL.GL_LINE_STRIP);
		for (int i = 0; i < trace.size(); i++) {
			Vector3 pos = trace.get(i);
			gl.glVertex3d(pos.x / ratio, pos.y / ratio, pos.z / ratio);				
		}
		gl.glEnd();
	}
	/**
	 * Update tout ce qui doit etre changer pendant une frame. ( position, vitesse, trace et rotation) 
	 * 
	 * @param delta
	 */
	public void update(double delta) {
		delta /= 1000;

		if(vitesseFutur.magnitude() > 3E8)
			vitesseFutur.scale(1/(vitesseFutur.magnitude()/3.0E8));
		
		if(positionFutur.magnitude() > 2.5E14){
			dispose();
			return;
		}
		position= positionFutur;
		vitesse= vitesseFutur;

		rotation.y += delta*vitesseRotation;

		//trace
		nbPointsTrace = (int)(vitesse.magnitude() / 300) + 20;
		tempsEntrePointsTrace = 2E7 / nbPointsTrace;
		//		tempsEntrePointsTrace = 5E5 - vitesse.magnitude() * 100;
		tempsDepuisDerniereTrace += delta;
		if (tempsDepuisDerniereTrace >= tempsEntrePointsTrace) {
			tempsDepuisDerniereTrace -= tempsEntrePointsTrace;
			if (trace.size() >= nbPointsTrace)
				trace.remove(0);
			trace.add(position.copy());
		}
	}
	
	// GETTER SETTER
	/**
	 * Indique si l'astre est encore en vie.
	 * 
	 * @return True si l'astre est en vie, False si l'astre n'existe plus.
	 */
	public boolean estEnVie(){	
		return enVie;
	}

	/**
	 * Elimine l'astre .
	 * 
	 */
	public void dispose() {
		sphere.dispose();
		enVie = false;
	}
	/**
	 * Change si l'astre est en vie ou non .
	 * 
	 * @param b True si l'astre vie, sinon False.
	 */
	public void setEnVie(boolean b) {
		enVie = b;
	}
	/**
	 * Indique si l'astre a le focus ou non .
	 * 
	 * @return True si l'astre a le focus, sinon False.
	 */
	public boolean isFocus() {
		return sphere.isFocus();
	}
	/**
	 * Change le nom de l'astre.
	 * 
	 * @param n Le nouveau nom de l'astre.
	 */
	public void setNom(String n){
		nom = n + "";
	}
	/**
	 * Donne ou enleve le focus a l'astre.
	 * 
	 * @param b True si l'astre aura le focus,sinon False.
	 */
	public void setFocus(boolean b) {
		sphere.setFocus(b);
	}
	/**
	 * Donne une nouvelle vitesse avec les coordonnes en x, y et z. 
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
	 * @param nVitesse La nouvelle vitesse.
	 */
	public void setVitesse(Vector3 nVitesse){
		vitesse =  nVitesse;
	}
	/**
	 * Change la masse de l'astre.
	 * 
	 * @param masse La nouvelle masse.
	 */
	public void setMasse(double masse){
		this.masse = masse;
	}

	/**
	 * Change la position futur avec un Vector3(x,y,z).
	 * 
	 * @param posFutur La nouvelle position futur.
	 */
	public void setPositionFutur(Vector3 posFutur){
		positionFutur= posFutur;
	}
	/**
	 * Change la vitesse futur avec un Vector3(x,y,z).
	 * 
	 * @param vitFutur La nouvelle vitesse futur.
	 */
	public void setVitesseFutur(Vector3 vitFutur){
		vitesseFutur= vitFutur;
	}
	/**
	 * Retourne le nom de l'astre.
	 * 
	 * @return Le nom de l'astre.
	 */
	public String getNom(){
		return nom;
	}
	/**
	 * Retourne la vitesse de l'astre.
	 * 
	 * @return La vitesse de l'astre.
	 */
	public Vector3 getVitesse(){
		return vitesse;
	}
	/**
	 * Retourne la vitesse de rotation.
	 * 
	 * @return La vitesse de rotation
	 */
	public double getVitesseRotation() {
		return vitesseRotation;
	}
	/**
	 * Retourne la position de l'astre.
	 * 
	 * @return La position de l'astre.
	 */
	public Vector3 getPosition() {
		return position;
	}
	/**
	 * Retourne la rotation presente de l'astre.
	 * 
	 * @return La rotation de l'astre presentement.
	 */
	public Vector3 getRotation() {
		return rotation;
	}
	/**
	 * Change la rotation de l'astre (de son axe y)
	 * 
	 * @param rot La nouvelle rotation (de son axe y).
	 */
	public void setRotation(Vector3 rot) {
		rotation = rot;
	}
	/**
	 * Retourne la position futur de l'astre.
	 * 
	 * @return La position futur de l'astre.
	 */
	public Vector3 getPositionFutur(){
		return positionFutur;
	}
	/**
	 * Retourne la vitesse futur de l'astre.
	 * 
	 * @return La vitesse futur de l'astre.
	 */
	public Vector3 getVitesseFutur(){
		return vitesseFutur;
	}
	/**
	 * Retourne la masse de l'astre.
	 * 
	 * @return La masse de l'astre.
	 */
	public double getMasse(){
		return masse;
	}
	/**
	 * Retourne le rayon de la sphere qui constite l'astre.
	 * 
	 * @return Le rayon de la sphere qui constite l'astre.
	 */
	public double getRayon() {
		return sphere.getRayon();
	}
	/**
	 * Change le rayon de la sphere qui constitu l'astre.
	 * 
	 * @param r Le rayon de la sphere qui constitu l'astre.
	 */
	public void setRayon(double r) {
		sphere.setRayon(r);
	}
	/**
	 * Retourne la couleur attribue a l'astre.
	 * 
	 * @return La couleur attribue a l'astre.
	 */
	public Color getCouleur() {
		return couleur;
	}
	/**
	 * Change la couleur qui sera attribuer a l'astre.
	 * 
	 * @param c La couleur disire.
	 */
	public void setCouleur(Color c) {
		couleur = c;
	}
	/**
	 * Retourne la couleur attribue a la trace de l'astre.
	 * 
	 * @return La couleur attribue a la trace de l'astre.
	 */
	public Color getCouleurTrace() {
		return couleurTrace;
	}
	/**
	 * Change la couleur qui sera attribue a la trace de l'astre.
	 * 
	 * @param c La couleur disire pour la trace.
	 */
	public void setCouleurTrace(Color c) {
		couleurTrace = c;
	}
	/**
	 * Change la texture de l'astre.
	 * 
	 * @param info La texture. 
	 */
	public void setTexture(InfoTexture info) {
		sphere.setTexture(info);
	}
	/**
	 * Retourne la texture attribue a l'astre.
	 * 
	 * @return La texture attribue a l'astre.
	 */
	public InfoTexture getTexture() {
		return sphere.getTexture();
	}
	/**
	 * Retourne le numero de la texture attribue a l'astre.
	 * 
	 * @return Le numero de la texture attribue a l'astre.
	 */
	public int getNumeroTexture() {
		return getTexture().getNumero();
	}
	/**
	 * Change la position du premier point de la trace de l'astre.
	 * 
	 */
	public void setPremiereTrace() {
		trace.set(0, position.copy());
	}
}
