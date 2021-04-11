package simulateur;

/**
 * Classe contenant l'etat actuel des options.
 * @author Marc-Antoine Dumais
 */
public class OptionsSingleton {
	private static OptionsSingleton instance;
	
	private boolean afficherFPS = true;
	private int typeCalcul = 0;
	private boolean barnesHut = false;
	private double alpha = 0.5;
	private boolean barnesHutVisuel = false;
	private int qualiteAstres = 12;
	private boolean afficherNoms = true;
	
	//Constructeur prive pour empecher d'instancier le singleton	
	private OptionsSingleton() {}	
	
	/**
	 * Indique s'il faut afficher les fps.
	 * @return s'il faut afficher les fps.
	 */
	public boolean isAfficherFPS() {
		return afficherFPS;
	}

	/**
	 * Change s'il faut afficher les fps.
	 * @param afficherFPS s'il faut afficher les fps.
	 */
	public void setAfficherFPS(boolean afficherFPS) {
		this.afficherFPS = afficherFPS;
	}

	/**
	 * Retourne le type de calcul.
	 * @return le type de calcul.
	 */
	public int getTypeCalcul() {
		return typeCalcul;
	}

	/**
	 * Change le type de calcul
	 * @param typeCalcul le nouveau type de calcul.
	 */
	public void setTypeCalcul(int typeCalcul) {
		this.typeCalcul = typeCalcul;
	}

	/**
	 * Indique s'il faut utiliser l'approximation de Barnes-Hut.
	 * @return s'il faut utiliser Barnes-Hut.
	 */
	public boolean isBarnesHut() {
		return barnesHut;
	}

	/**
	 * Change s'il faut utiliser l'approximation de Barnes-Hut.
	 * @param barnesHut s'il faut utiliser Barnes-Hut.
	 */
	public void setBarnesHut(boolean barnesHut) {
		this.barnesHut = barnesHut;
	}

	/**
	 * Retourne le alpha utilise dans Barnes-Hut.
	 * @return le alpha de Barnes-Hut
	 */
	public double getAlpha() {
		return alpha;
	}

	/**
	 * Change le alpha de Barnes-Hut
	 * @param alpha le alpha de Barnes-Hut
	 */
	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}

	/**
	 * Retourne la qualite des astres
	 * @return a qualite des astres
	 */
	public int getQualiteAstres() {
		return qualiteAstres;
	}

	/**
	 * Change a qualite des astres
	 * @param qualiteAstres a qualite des astres
	 */
	public void setQualiteAstres(int qualiteAstres) {
		this.qualiteAstres = qualiteAstres;
	}

	/**
	 * Indique s'il faut afficher le nom des astres
	 * @return s'il faut afficher le nom des astres
	 */
	public boolean isAfficherNoms() {
		return afficherNoms;
	}

	/**
	 * Change s'il faut afficher le nom des astres
	 * @param afficherNoms s'il faut afficher le nom des astres
	 */
	public void setAfficherNoms(boolean afficherNoms) {
		this.afficherNoms = afficherNoms;
	}
	
	/**
	 * Indique si l'octree est visible.
	 * @return si l'octree est visible.
	 */
	public boolean isBarnesHutVisuel() {
		return barnesHutVisuel;
	}

	/**
	 * Change si l'octree est visible.
	 * @param barnesHutVisuel si l'octree est visible.
	 */
	public void setBarnesHutVisuel(boolean barnesHutVisuel) {
		this.barnesHutVisuel = barnesHutVisuel;
	}
	
	/**
	 * Retourne l'instance qui contient l'etat actuel des options.
	 * @return l'instance qui contient l'etat actuel des options.
	 */
	public static OptionsSingleton getInstance() {
		if (instance == null) {
			synchronized(OptionsSingleton.class) {
				if (instance == null) {
					instance = new OptionsSingleton();
				}
			}
		}
		return instance;
	}
}
