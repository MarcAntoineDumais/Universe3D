package affichage3d;

import java.io.Serializable;

/**
 * Cette classe represente un vecteur en 3D.
 * 
 * This class represents a 3D vector.
 * 
 * @author Marc-Antoine Dumais
 *
 */
public class Vector3 implements Serializable{
	
	//FIELDS
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3095269329951677692L;
	//Les champs sont publics, car la classe Vector3 ne sert principalement qu'a stocker
	//les trois coordonees d'un vecteur, et non pas a les utiliser.
	public double x, y, z;
	
	
	//CONSTRUCTORS
	
	/**
	 * Cree un nouveau Vector3 situe a l'origine.
	 * 
	 * Creates a new Vector3 at the origin.
	 */
	public Vector3() {
		x = 0;
		y = 0;
		z = 0;
	}

	/**
	 * Cree un nouveau Vector3 situe a (x, y, z).
	 * 
	 * Creates a new Vector3 located at (x, y, z).
	 * 
	 * @param x La coordonnee x. The x coordinate.
	 * @param y La coordonnee y. The y coordinate.
	 * @param z La coordonnee z. The z coordinate.
	 */
	public Vector3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	//METHODS
	
	public String toString() {
		return "(" + x + ", " + y + ", " + z + ")";
	}
	
	public boolean equals(Vector3 a) {
		return (x == a.x && y == a.y && z == a.z);
	}
	
	/**
	 * Translate le vecteur d'une distance x, y et z dans les axes x, y et z, respectivement.
	 * 
	 * Respectively translates the vector by x, y and z in the x, y and z axis.
	 * 
	 * @param x La distance en x. The distance on the x axis.
	 * @param y La distance en y. The distance on the y axis.
	 * @param z La distance en z. The distance on the z axis.
	 */
	public void translate(double x, double y, double z) {
		this.x += x;
		this.y += y;
		this.z += z;	
	}
	
	/**
	 * Ajoute le vecteur "translation" a ce vecteur.
	 * 
	 * Adds the vector "translation" to this vector.
	 * 
	 * @param translation La translation a ajouter. The translation to add.
	 */
	public void translate(Vector3 translation) {
		x += translation.x;
		y += translation.y;
		z += translation.z;	
	}
	
	/**
	 * Adds the vector "a" to this vector.
	 * 
	 * @param a The vector to add.
	 */
	public void add(Vector3 a) {
		translate(a);
	}
	
	/**
	 * Ajoute le vecteur "a" a ce vecteur.
	 * 
	 * @param a Le vecteur a ajouter.
	 */
	public void ajouter(Vector3 a) {
		translate(a);
	}
	
	/**
	 * Subtracts the vector "a" from this vector.
	 * 
	 * @param a The vector to subtract.
	 */
	public void subtract(Vector3 a) {
		x -= a.x;
		y -= a.y;
		z -= a.z;
	}
	
	/**
	 * Soustrait le vecteur "a" a ce vecteur.
	 * 
	 * @param a Le vecteur a soustraire.
	 */
	public void soustraire(Vector3 a) {
		subtract(a);
	}
	
	/**
	 * Respectively scales the vector by sx, sy and sz in the x, y and z axis.
	 *  
	 * @param sx The amount to scale in the x axis.
	 * @param sy The amount to scale in the y axis.
	 * @param sz The amount to scale in the z axis.
	 */
	public void scale(double sx, double sy, double sz) {
		x *= sx;
		y *= sy;
		z *= sz;
	}
	
	/**
	 * Met le vecteur a l'echelle dans les trois axes, par les valeurs precisees.
	 * 
	 * @param sx La nouvelle echelle en x.
	 * @param sy La nouvelle echelle en y.
	 * @param sz La nouvelle echelle en z.
	 */
	public void mettreALEchelle(double sx, double sy, double sz) {
		scale(sx, sy, sz);
	}
	
	/**
	 * Scales the vector by s in every axis.
	 * 
	 * @param s The amount to scale.
	 */
	public void scale(double s) {
		x *= s;
		y *= s;
		z *= s;
	}
	
	/**
	 * Met les trois axes du vecteur a l'echelle par la meme valeur.
	 * 
	 * @param s La nouvelle echelle pour les trois axes.
	 */
	public void mettreALEchelle(double s) {
		scale(s);
	}
	
	/**
	 * Places the vector at the specified position.
	 * 
	 * @param x The new x coordinate.
	 * @param y The new y coordinate.
	 * @param z The new z coordinate.
	 */
	public void set(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;		
	}
	
	/**
	 * Place le vecteur a l'endroit precise.
	 * 
	 * @param x La nouvelle coordonne en x.
	 * @param y La nouvelle coordonne en y.
	 * @param z La nouvelle coordonne en x.
	 */
	public void placer(double x, double y, double z) {
		set(x, y, z);
	}
	
	/**
	 * Retourne la coordonnee en x.
	 * 
	 * Return the x coordinate.
	 * 
	 * @return La coordonnee en x. The x coordinate.
	 */
	public double getX() {
		return x;
	}
	
	/**
	 * Retourne la coordonnee en y.
	 * 
	 * Return the y coordinate.
	 * 
	 * @return La coordonnee en y. The y coordinate.
	 */
	public double getY() {
		return y;
	}
	
	/**
	 * Retourne la coordonnee en z.
	 * 
	 * Return the z coordinate.
	 * 
	 * @return La coordonnee en z. The z coordinate.
	 */
	public double getZ() {
		return z;
	}
	
	/**
	 * Change la coordonnee x.
	 * 
	 * Changes the x coordinate.
	 *
	 *@param x La nouvelle coordonnee en x. The new x coordinate.
	 */
	public void setX(double x) {
		this.x = x;
	}
	
	/**
	 * Change la coordonnee y.
	 * 
	 * Changes the y coordinate.
	 *
	 *@param y La nouvelle coordonnee en y. The new y coordinate.
	 */
	public void setY(double y) {
		this.y = y;
	}
	
	/**
	 * Change la coordonnee z.
	 * 
	 * Changes the z coordinate.
	 *
	 *@param z La nouvelle coordonnee en z. The new z coordinate.
	 */
	public void setZ(double z) {
		this.z = z;
	}
	
	/**
	 * Returns the magnitude of the vector.
	 * 
	 * @return The magnitude of the vector.
	 */
	public double magnitude() {
		return Math.sqrt(x*x + y*y + z*z);
	}
	
	/**
	 * Retourne le module du vecteur.
	 * 
	 * @return Le module du vecteur.
	 */
	public double module() {
		return magnitude();
	}
	
	/**
	 * Returns the magnitude of the vector squared.
	 * Saves a square root operation.
	 * 
	 * @return The magnitude squared.
	 */
	public double magnitudeSquared() {
		return x*x + y*y + z*z;
	}
	
	/**
	 * Retourne le module du vecteur au carre.
	 * Permet d'eviter une operation racine carree.
	 * 
	 * @return Le module du vecteur au carre.
	 */
	public double moduleAuCarre() {
		return magnitudeSquared();
	}
	
	/**
	 * Returns a normalized copy of the vector.
	 * 
	 * @return The normalized copy.
	 */
	public Vector3 normalized() {
		double m = magnitude();
		if (m == 0)
			return new Vector3();
		return new Vector3(x / m, y / m, z / m);
	}
	
	/**
	 * Retourne une copie unitaire du vecteur.
	 * 
	 * @return La copie unitaire.
	 */
	public Vector3 unitaire() {
		return normalized();
	}
	
	/**
	 * Normalizes the vector.
	 */
	public void normalize() {
		double m = magnitude();
		if (m == 0)
			return;
		x /= m;
		y /= m;
		z /= m;
	}
	
	/**
	 * Rend le vecteur unitaire.
	 */
	public void rendreUnitaire() {
		normalize();
	}
	
	/**
	 * Returns a copy of the opposite of the vector.
	 * 
	 * @return The copy of the opposite of the vector.
	 */
	public Vector3 opposite(){
		return new Vector3(x*-1, y*-1 , z*-1 );
	}
	
	/**
	 * Retoune une copie du vecteur oppose.
	 * 
	 * @return La copie du vecteur oppose.
	 */
	public Vector3 oppose() {
		return opposite();
	}
	
	/**
	 * Returns a copy of the vector.
	 * 
	 * @return The copy.
	 */
	public Vector3 copy() {
		return new Vector3(x, y, z);
	}
	
	/**
	 * Retourne une copie du vecteur.
	 * 
	 * @return La copie.
	 */
	public Vector3 copie() {
		return copy();
	}
	
	//STATIC METHODS
	
	/**
	 * Retourne la distance entre les deux vecteurs.
	 * 
	 * Returns the distance between the two vectors.
	 * 
	 * @param a Le premier vecteur. The first vector.
	 * @param b Le deuxieme vecteur. The second vector.
	 * @return La distance. The distance.
	 */
	public static double distance(Vector3 a, Vector3 b) {
		return Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2) + Math.pow(a.z - b.z, 2));
	}
	
	/**
	 * Returns the sum of two vectors.
	 * 
	 * @param a The first vector.
	 * @param b The second vector.
	 * @return The sum.
	 */
	public static Vector3 sum(Vector3 a, Vector3 b) {
		return new Vector3(a.x + b.x, a.y + b.y, a.z + b.z);
	}
	
	/**
	 * Retourne la somme de deux vecteurs.
	 * 
	 * @param a Le premier vecteur.
	 * @param b Le deuxieme vecteur.
	 * @return La somme.
	 */
	public static Vector3 somme(Vector3 a, Vector3 b) {
		return sum(a, b);
	}
	
	/**
	 * Retourne la difference entre deux vecteurs (a - b).
	 * 
	 * Returns the difference of two vectors (a - b).
	 * 
	 * @param a Le premier vecteur. The first vector.
	 * @param b Le deuxieme vecteur. The second vector.
	 * @return La difference (a - b). The difference (a - b).
	 */
	public static Vector3 difference(Vector3 a, Vector3 b) {
		return new Vector3(a.x - b.x, a.y - b.y, a.z - b.z);
	}
	
	/**
	 * Returns the up vector.
	 * 
	 * @return The up vector.
	 */
	public static Vector3 up() {
		return new Vector3(0, 1, 0);
	}
	
	/**
	 * Retourn le vecteur haut.
	 * 
	 * @return Le vecteur haut.
	 */
	public static Vector3 haut() {
		return up();
	}
	
	/**
	 * Returns the right vector.
	 * 
	 * @return The right vector.
	 */
	public static Vector3 right() {
		return new Vector3(1, 0, 0);
	}
	
	/**
	 * Retourne le vecteur qui pointe vers la droite.
	 * 
	 * @return Le vecteur qui pointe vers la droite.
	 */
	public static Vector3 droite() {
		return right();
	}
	
	/**
	 * Returns the forward vector.
	 * 
	 * @return The forward vector.
	 */
	public static Vector3 forward() {
		return new Vector3(0, 0, 1);
	}
	
	/**
	 * Retourne le vecteur qui pointe vers l'avant.
	 * 
	 * @return Le vecteur qui pointe vers l'avant.
	 */
	public static Vector3 avant() {
		return forward();
	}
	
	/**
	 * Retourne le vecteur nul.
	 * 
	 * Returns the null vector.
	 * 
	 * @return Le vecteur nul. The null vector.
	 */
	public static Vector3 zero() {
		return new Vector3(0, 0, 0);
	}
	
	/**
	 * Returns the cross product of two vectors (a X b).
	 * 
	 * @param a The first vector.
	 * @param b The second vector.
	 * @return The cross product (a X b).
	 */
	public static Vector3 crossProduct(Vector3 a, Vector3 b) {
		return new Vector3(a.y * b.z - a.z * b.y, 
						   b.x * a.z - b.z * a.x,
						   a.x * b.y - a.y * b.x);
	}
	
	/**
	 * Retourne le produit vectoriel de deux vecteurs (a X b).
	 * 
	 * @param a Le premier vecteur.
	 * @param b Le deuxieme vecteur.
	 * @return Le produit vectoriel (a X b).
	 */
	public static Vector3 produitVectoriel(Vector3 a, Vector3 b) {
		return crossProduct(a, b);
	}
	
	/**
	 * Returns the dot product of two vectors.
	 * 
	 * @param a The first vector.
	 * @param b The second vector.
	 * @return The dot product.
	 */
	public static double dotProduct(Vector3 a, Vector3 b) {
		return a.x*b.x + a.y*b.y + a.z*b.z;
	}
	
	/**
	 * Retourne le produit scalaire de deux vecteurs.
	 * 
	 * @param a Le premier vecteur.
	 * @param b Le deuxieme vecteur.
	 * @return Le produit scalaire.
	 */
	public static double produitScalaire(Vector3 a, Vector3 b) {
		return dotProduct(a, b);
	}
}
