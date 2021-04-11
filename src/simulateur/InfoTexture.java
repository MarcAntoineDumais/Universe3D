package simulateur;

/**
 * Cette enumeration contient les informations permettant de charger les bonnes textures
 * pour chaque astre.
 * <br><br>Date de creation: 01 avril 2014
 * 
 * @author Marc-Antoine Dumais
 */
public enum InfoTexture {
	MERCURE("mercure.jpg", 0, "icone_mercure.png", InfoAstre.MERCURE),
	VENUS("venus.jpg", 1, "icone_venus.png", InfoAstre.VENUS), 
	TERRE("terre.jpg", 2, "icone_terre.png", InfoAstre.TERRE),
	MARS("mars.jpg", 3, "icone_mars.png", InfoAstre.MARS),
	JUPITER("jupiter.jpg", 4, "icone_jupiter.png", InfoAstre.JUPITER),
	SATURNE("saturne.jpg", 5, "icone_saturne.png", InfoAstre.SATURNE), 
	URANUS("uranus.jpg", 6, "icone_uranus.png", InfoAstre.URANUS),
	NEPTUNE("neptune.jpg", 7, "icone_neptune.png", InfoAstre.NEPTUNE), 
	PLUTON("pluton.jpg", 8, "icone_pluton.png", InfoAstre.PLUTON),
	SOLEIL("soleil.jpg", 9, "icone_soleil.png", InfoAstre.SOLEIL), 
	FOND("espace.jpg", 10, "icone_soleil.png", null),
	LUNE("lune.jpg", 11, "icone_lune.png", InfoAstre.LUNE);
	
	private final String nomTexture, nomIcone;
	private final InfoAstre infoAstre;
	private final int numero;
	
	private InfoTexture(String nomTexture, int numero, String nomIcone, InfoAstre info) {
		this.nomTexture = nomTexture;
		this.numero = numero;
		this.nomIcone = nomIcone;
		infoAstre = info;
	}
	
	/**
	 * Retourne le nom du fichier de la texture de l'astre precise.
	 * @return Le nom du fichier de la texture.
	 */
	public String getNomTexture() {
		return nomTexture;
	}
	
	/**
	 * Retourne le numero de la texture de l'astre precise dans la liste des textures.
	 * @return Le numero de la texture.
	 */
	public int getNumero() {
		return numero;
	}
	
	/**
	 * Retourne le nom du fichier de l'icone de l'astre precise.
	 * @return Le nom du fichier de l'icone.
	 */
	public String getNomIcone() {
		return nomIcone;
	}
	
	/**
	 * Retourne l'element InfoAstre associe a cet astre;
	 * @return L'element InfoAstre.
	 */
	public InfoAstre getInfoAstre() {
		return infoAstre;
	}
}
