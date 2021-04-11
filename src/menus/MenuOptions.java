package menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.Serializable;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import simulateur.OptionsSingleton;

/**
 * Cree la fenetre d'options.
 * @author Guillaume Bellemare-Roy
 */
public class MenuOptions extends JFrame implements Serializable {

	private static final long serialVersionUID = -8925911333312046803L;
	private OptionsSingleton options;
	private JPanel contentPane;
	private JCheckBox chckbxFPS;
	private JCheckBox chckbxAfficherBarneshut ;
	private JComboBox<String> comboBoxCalcul;
	private JLabel lblChoixDuType;
	private JCheckBox chckbxBarneshut;
	private JCheckBox chckbxAfficherLeNom;
	private JTextField textField;
	private JLabel lblAlpha;
	private JSlider slider;
	private JLabel lblQualiteDesPlantes;

	public MenuOptions() {
		options = OptionsSingleton.getInstance();
		setTitle("Options");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		chckbxFPS = new JCheckBox("Afficher Images par secondes");
		chckbxFPS.setBounds(6, 7, 211, 23);
		chckbxFPS.setSelected(true);
		chckbxFPS.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				options.setAfficherFPS(chckbxFPS.isSelected());
			}
		});
		contentPane.add(chckbxFPS);

		comboBoxCalcul = new JComboBox<String>();
		comboBoxCalcul.setModel(new DefaultComboBoxModel<String>(new String[] {"Euler", "Euler inversé", "Runge-Kutta 4e Ordre (RK4)"}));
		comboBoxCalcul.setBounds(20, 97, 169, 20);
		comboBoxCalcul.setSelectedIndex(0);
		comboBoxCalcul.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				options.setTypeCalcul(comboBoxCalcul.getSelectedIndex());
			}
		});
		contentPane.add(comboBoxCalcul);

		lblChoixDuType = new JLabel("Choix du Type de calcul");
		lblChoixDuType.setBounds(10, 72, 140, 14);
		contentPane.add(lblChoixDuType);

		chckbxBarneshut = new JCheckBox("Barnes-Hut");
		chckbxBarneshut.setBounds(20, 124, 97, 23);
		chckbxBarneshut.setSelected(false);
		chckbxBarneshut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(chckbxBarneshut.isSelected()){
					lblAlpha.setEnabled(true);
					textField.setEnabled(true);
					chckbxAfficherBarneshut.setEnabled(true);
				}else{
					lblAlpha.setEnabled(false);
					textField.setEnabled(false);
					chckbxAfficherBarneshut.setEnabled(false);
				}

				options.setBarnesHut(chckbxBarneshut.isSelected());
			}
		});
		contentPane.add(chckbxBarneshut);

		chckbxAfficherLeNom = new JCheckBox("Afficher le nom des astres");
		chckbxAfficherLeNom.setSelected(true);
		chckbxAfficherLeNom.setBounds(20, 232, 197, 23);
		chckbxAfficherLeNom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				options.setAfficherNoms(chckbxAfficherLeNom.isSelected());
			}
		});
		contentPane.add(chckbxAfficherLeNom);

		textField = new JTextField();
		textField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				double d = TestEntree.nettoyerChaine(textField.getText());
				options.setAlpha(d);
			}
		});
		textField.setText("0.5");
		textField.setEnabled(false);
		textField.setBounds(159, 125, 86, 20);
		contentPane.add(textField);
		textField.setColumns(10);

		lblAlpha = new JLabel("Alpha:");
		lblAlpha.setBounds(127, 128, 46, 14);
		contentPane.add(lblAlpha);

		slider = new JSlider();
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				options.setQualiteAstres(slider.getValue() + 1);
			}
		});
		slider.setMaximum(50);
		slider.setMinimum(10);
		slider.setPaintLabels(true);
		slider.setPaintTicks(true);
		slider.setMinorTickSpacing(2);
		slider.setMajorTickSpacing(10);
		slider.setBounds(17, 180, 200, 45);
		contentPane.add(slider);

		lblQualiteDesPlantes = new JLabel("Qualit\u00E9e des plan\u00E8tes");
		lblQualiteDesPlantes.setBounds(10, 163, 140, 14);
		contentPane.add(lblQualiteDesPlantes);

		chckbxAfficherBarneshut = new JCheckBox("Afficher Barnes-Hut");
		chckbxAfficherBarneshut.setEnabled(false);
		chckbxAfficherBarneshut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				options.setBarnesHutVisuel(chckbxAfficherBarneshut.isSelected());
			}
		});
		chckbxAfficherBarneshut.setToolTipText("Permet de montrer l'emplacement des Octrees de Barnes-Hut");
		chckbxAfficherBarneshut.setSelected(false);
		chckbxAfficherBarneshut.setBounds(265, 123, 159, 23);
		contentPane.add(chckbxAfficherBarneshut);

		setLocationRelativeTo(null);
	}

	/**
	 * Cette méthode permet d'avoir accès au information et de les afficher
	 */
	public void afficherInformations() {
		chckbxAfficherLeNom.setSelected(options.isAfficherNoms());
		chckbxBarneshut.setSelected(options.isBarnesHut());
		textField.setEnabled(options.isBarnesHut());
		textField.setText("" + options.getAlpha());
		chckbxFPS.setSelected(options.isAfficherFPS());
		slider.setValue(options.getQualiteAstres());
		comboBoxCalcul.setSelectedIndex(options.getTypeCalcul());
	}
}
