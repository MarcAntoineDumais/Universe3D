package menus;

import java.math.BigDecimal;

/**
 * Cette classe contient des utilitaires permettant de faire certaines actions sur des chaines de caracteres.
 * 
 * @author Marc-Antoine Dumais
 */
public class TestEntree{
	/**
	 * Transforme une chaine de charactere quelconque en un nombre decimale valide.
	 * Retourne 0 s'il n'y avait pas de nombre dans la chaine.
	 * 
	 * @param str La chaine a nettoyer
	 * @return Un double valide
	 */
	public static double nettoyerChaine(String str) {
		if (str.equals(""))
			return 0;
		String cleanedStr = "";
		boolean virgule = false;
		boolean e = false;
		boolean negatif = false;
		if (str.charAt(0) == '-') {
			negatif = true;
			str = str.substring(1, str.length());
		}
			
		char c;
		for (int i = 0; i < str.length(); i++) {
			c = str.charAt(i);
			if (!virgule && (c == ',' || c == '.')) {
				if (cleanedStr.equals(""))
					cleanedStr += "0";
				virgule = true;
				cleanedStr += ".";
			} else if (!e && (c == 'e' || c == 'E')) {
				if (cleanedStr.equals("") )
					return 0;
				if (cleanedStr.charAt(cleanedStr.length() - 1) == '.')
					cleanedStr = cleanedStr.substring(0, cleanedStr.length() - 1);
				e = true;
				virgule = true;
				cleanedStr += "E";
				if (str.length() > i + 1 && str.charAt(i + 1) == '-') {
					cleanedStr += "-";
					i++;
				}
			} else {
				if (c >= '0' && c <= '9')
					cleanedStr += c;
			}				
		}
		if (cleanedStr.equals(""))
			return 0;
		if (e && cleanedStr.charAt(cleanedStr.length() - 1) == 'E')
			cleanedStr = cleanedStr.substring(0, cleanedStr.length() - 1);
		if (virgule && cleanedStr.charAt(cleanedStr.length() - 1) == '.')
			cleanedStr = cleanedStr.substring(0, cleanedStr.length() - 1);
		double d = Double.parseDouble(cleanedStr);
		if (negatif)
			d *= -1;
		return d;
	}
	
	/**
	 * Retourne une version arrondie à nombreDecimales decimales
	 * de la valeur passee en parametre.
	 * 
	 * @param valeur La valeur a arrondir.
	 * @param nombreDecimales Le nombre de decimales a garder.
	 * @return la valeur arrondie
	 */
	public static double arrondir(double valeur, int nombreDecimales) {
		String str = "" + valeur;
		if (str.matches(".*[eE].*")) {
			str = str.toUpperCase();
			int i = str.indexOf('E');
			double base = arrondir(Double.parseDouble(str.substring(0, i)), nombreDecimales);
			int exposant = Integer.parseInt(str.substring(i + 1, str.length()));
			str = "" + base * Math.pow(10, exposant);
			return Double.parseDouble(base + "E" + exposant);
		}
		return new BigDecimal(valeur).setScale(nombreDecimales, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
}
