package simulateur;

import java.awt.Color;

/**
 * Cette enumeration contient les informations necessaires a la creation d'astres 
 * connus, notamment dans notre system solaire.
 * <br><br>Date de creation: 01 avril 2014
 * 
 * @author Marc-Antoine Dumais
 */
public enum InfoAstre {
	MERCURE("Mercure", 3.302E23, 2.4397E6, 5.06703168E6, 5.79091755E10, 7.01, new Color(193, 130, 84)),
	VENUS("Venus", 4.8685E24, 6.0518E6, -2.09971872E7, 1.08209184E11, 3.39, new Color(215, 96, 17)),
	TERRE("Terre", 5.9736E24, 6.378137E6, 8.6164083936E4, 1.495978875E11, 0, new Color(17, 124, 215)),
	MARS("Mars", 6.4185E23, 3.3962E6, 8.86426848E4, 2.279366375E11, 1.85, new Color(184, 45, 17)),
	JUPITER("Jupiter", 1.8986E27, 7.1492E7, 3.5727E4, 7.7857E11, 1.31, new Color(205, 172, 83)),
	SATURNE("Saturne", 5.6846E24, 6.0268E7, 3.83616E4, 1.426725412E12, 2.49, new Color(205, 201, 83)),
	URANUS("Uranus", 8.681E24, 2.559E7, -6.2063712E4, 2.8766790825E12, 0.77, new Color(47, 152, 222)),
	NEPTUNE("Neptune", 1.0243E24, 2.4764E7, 5.7996E4, 4.5034436615E12, 1.77, new Color(41, 100, 207)),
	PLUTON("Pluton", 1.314E22, 1.153E6, 5.518368E5, 5.906376272E12, 17.15, new Color(166, 180, 205)),
	SOLEIL("Soleil", 1.9891E30, 6.96342E8, 2.356992E6, 0, 0, new Color(248, 222, 0)),
	LUNE("Lune", 7.3477E22, 1.7374E6, 2.3605846848E6, 3.844E8, 0, new Color(169, 176, 191));
	
	private final String nom;
	private final double masse, rayon, rotation, distanceOrbite, angleInclinaison;
	private final Color couleur;
	
	private InfoAstre(String nom, double masse, double rayon, double periodeRotation, double distanceOrbite, 
					  double angleInclinaison, Color couleur) {
		this.nom = nom;
		this.masse = masse;
		this.rayon = rayon;
		this.rotation = 360 / periodeRotation;
		this.distanceOrbite = distanceOrbite;
		this.angleInclinaison = angleInclinaison / 180 * Math.PI;
		this.couleur = couleur;
	}
	
	/**
	 * Retourne le nom de l'astre precise.
	 * @return Le nom de l'astre.
	 */
	public String getNom() {
		return nom;
	}
	
	/**
	 * Retourne la masse de l'astre precise.
	 * @return La masse de l'astre.
	 */
	public double getMasse() {
		return masse;
	}
	
	/**
	 * Retourne le rayon de l'astre precise.
	 * @return Le rayon de l'astre.
	 */
	public double getRayon() {
		return rayon;
	}
	
	/**
	 * Retourne la rotation de l'astre precise sur lui-meme.
	 * @return La rotation de l'astre (degres).
	 */
	public double getRotation() {
		return rotation;
	}
	
	/**
	 * Retourne la distance de l'astre precise avec l'astre autour duquel il tourne.
	 * @return La distance orbitale de l'astre.
	 */
	public double getDistanceOrbite() {
		return distanceOrbite;
	}
	
	/**
	 * Retourne l'angle d'inclinaison de l'orbite de l'astre precise par rapport à l'orbite de la terre.
	 * @return L'angle d'inclinaison de l'orbite.
	 */
	public double getAngleInclinaison() {
		return angleInclinaison;
	}
	
	/**
	 * Retourne la couleur de la trace de l'astre precise.
	 * @return La couleur de la trace de l'astre.
	 */
	public Color getCouleur() {
		return couleur;
	}
}
