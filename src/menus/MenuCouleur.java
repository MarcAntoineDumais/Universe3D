package menus;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.colorchooser.ColorSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

/**
 * classe contenant le JFrame pour la selection de la couleur.
 * @author guillaume bellemare-roy
 */
public class MenuCouleur extends JFrame{
	private static final long serialVersionUID = 5972429172538522938L;
	private JPanel contentPane;
	private JColorChooser colorChooser;
	private final EventListenerList ecouteurs = new EventListenerList();

	public MenuCouleur() {
		setTitle("Couleur");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 640, 380);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setVisible(false);
		
		colorChooser = new JColorChooser();
		colorChooser.setBounds(0, 11, 626, 330);
		contentPane.add(colorChooser);
		
		addWindowListener(new WindowAdapter(){
			@Override
			public void windowDeactivated(WindowEvent e) {
				setVisible(false);
			}			
		});
		

	    ColorSelectionModel model = colorChooser.getSelectionModel();
	    ChangeListener changeListener = new ChangeListener() {
	      public void stateChanged(ChangeEvent changeEvent) {
	    	  lanceUneCouleurChoisie(colorChooser.getColor());
	      }
	    };
	    model.addChangeListener(changeListener);
	}
	
	/**
	 * Ajoute un ecouteur de changement de couleur.
	 * @param ecout L'ecouteur.
	 */
	public void addColorChooserListener( ColorListener ecout ){
		ecouteurs.add(ColorListener.class, ecout);
	}
	
	/**
	 * Lance l'evenement de changement de couleur
	 * @param couleur La nouvelle couleur.
	 */
	private void lanceUneCouleurChoisie(Color couleur){
		for(ColorListener ecout : ecouteurs.getListeners(ColorListener.class)){
			ecout.uneCouleurChoisie(couleur);
		}
	}
	
}
