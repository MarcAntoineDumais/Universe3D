package simulateur;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.media.opengl.GLProfile;

import aapplication.App06SimulateurUnivers;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;

/**
 * Classe contenant les ressources utilisees dans le projet
 * @author Marc-Antoine Dumais
 */
public class ResourcesSingleton {
	private static ResourcesSingleton instance;
	private ArrayList<Texture> textures = new ArrayList<Texture>();
	
	private ArrayList<URL> urlIcones = new ArrayList<URL>();
	private ArrayList<Image> icones = new ArrayList<Image>();
	
	//Constructeur prive pour empecher d'instancier le singleton	
	private ResourcesSingleton() {}
	
	/**
	 * Charge les textures.
	 */
	public void loadTextures() {
		try {
			InputStream stream;
			TextureData data;
			InfoTexture[] infos = InfoTexture.values();

			for(int i = 0; i < infos.length; i++){
				stream = App06SimulateurUnivers.class.getClassLoader().getResourceAsStream(infos[i].getNomTexture());
				data = TextureIO.newTextureData(GLProfile.getDefault(), stream, false, "jpg");
				textures.add(TextureIO.newTexture(data));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Charge les icones.
	 */
	public void loadIcones() {
		try {
			URL url;
			InfoTexture[] infos = InfoTexture.values();
			for(int i = 0; i < infos.length; i++){
				if (infos[i] != InfoTexture.FOND) {
					url = App06SimulateurUnivers.class.getClassLoader().getResource(infos[i].getNomIcone());
					urlIcones.add(url);
					icones.add(ImageIO.read(url));
				}				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Retourne les textures du projet.
	 * @return La liste des textures du projet.
	 */
	public ArrayList<Texture> getTextures() {
		return textures;
	}
	
	/**
	 * Retourne les URLs des icones du projet.
	 * @return La liste des URLs.
	 */
	public ArrayList<URL> getUrlIcones() {
		return urlIcones;
	}
	
	/**
	 * Retourne les icones du projet.
	 * @return La liste des icones du projet.
	 */
	public ArrayList<Image> getIcones() {
		return icones;
	}
	
	/**
	 * Retourne l'instance qui contient toutes les ressources chargees du projet.
	 * @return L'instance qui contient toutes les ressources chargees du projet.
	 */
	public static ResourcesSingleton getInstance() {
		if (instance == null) {
			synchronized(ResourcesSingleton.class) {
				if (instance == null) {
					instance = new ResourcesSingleton();
				}
			}
		}
		return instance;
	}
}
