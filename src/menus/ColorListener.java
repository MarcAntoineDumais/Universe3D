package menus;
import java.awt.Color;
import java.util.EventListener;

/**
 * Ecouteur de selection de couleur.
 * @author Guillaume Bellemare-Roy
 */
public interface ColorListener extends EventListener{
	public void uneCouleurChoisie(Color coul);		
}
