package simulateur;

import java.io.Serializable;
import java.util.ArrayList;
import affichage3d.Vector3;

/**
 * Cette classe represente un arbre de donnees ordonne par huit branche. (comme un arbre binaire, mais a huit branche a chaques noeuds) 
 * 
 * @author David-Olivier Duperron
 * 
 * <br>Date de creation : inconnu (probleme avec SVN a ce momment)
 */

public class Octree implements Serializable{

	private static final long serialVersionUID = -798136598732350763L;

	private int MAX_LEVEL = 1000;

	private int level,nbAstre;
	private ArrayList<Astre> astre,listAstreCollisions;
	private double proportion;
	private Octree[] nodes;
	private Vector3 centre, centreDeMasse;
	private double masse;

	/**
	 * Contructeur de l'octree et cree huits nodes(branches).
	 * 
	 * @param Level nivau actuelle de l'octree.
	 * @param c Centre de l'octree.
	 * @param prop La proportion de l'octree.(hauteur, largeur et profondeur)
	 */
	public Octree(int Level,Vector3 c ,double prop) {
		this.level = Level;
		nbAstre=0;
		astre = new ArrayList<Astre>();
		listAstreCollisions = new ArrayList<Astre>();
		proportion=prop;
		masse=0;
		centreDeMasse=new Vector3();
		centre = c;
		nodes = new Octree[8];
	}

	/**
	 * Vider l'Octree.
	 */
	public void clear() {
		astre.clear();

		for (int i = 0; i < nodes.length; i++)
			if (nodes[i] != null) {
				if(nodes[i].nodes!= null)
					nodes[i].clear();
			}
		centreDeMasse=new Vector3();
		masse=0;
		nbAstre = 0;
	}

	/**
	 * Separe une node pour cree un octree a cet endroit.
	 * 
	 * @param index Coordonnes de la nodes.
	 */
	private void splitNode(int index) {

		double prop =  proportion/2;
		Vector3 nCentre = null;

		switch(index){
		case 0 : nCentre= new Vector3(prop+centre.x,prop+centre.y,prop+centre.z);
		break;
		case 1 : nCentre= new Vector3(-prop+centre.x,prop+centre.y,prop+centre.z);
		break;
		case 2 : nCentre= new Vector3(-prop+centre.x,prop+centre.y,-prop+centre.z);
		break;
		case 3 : nCentre= new Vector3(prop+centre.x,prop+centre.y,-prop+centre.z);
		break;
		case 4 : nCentre= new Vector3(prop+centre.x,-prop+centre.y,prop+centre.z);
		break;
		case 5 : nCentre= new Vector3(-prop+centre.x,-prop+centre.y,prop+centre.z);
		break;
		case 6 : nCentre= new Vector3(-prop+centre.x,-prop+centre.y,-prop+centre.z);
		break;
		case 7 : nCentre= new Vector3(prop+centre.x,-prop+centre.y,-prop+centre.z);
		}		
		nodes[index] = new Octree(level+1,nCentre,prop); 	
	}

	/**
	 * Retourne l'index de la node ou ce trouve l'astre.
	 * 
	 * @param a L'astre pour lequel on veut trouver l'index.
	 * @return  L'index de la nodes de l'astre dans le present octree. Retourne -1 s'il n'y a pas d'index.
	 */
	private int getIndex(Astre a) {
		int index = -1;

		if(a.getPosition().x >= centre.x ){
			if( a.getPosition().z >= centre.z ){

				if(a.getPosition().y >= centre.y )
					index=0;
				else
					index=4;
			}else{
				
				if(a.getPosition().y >= centre.y )
					index=3;
				else
					index=7;		
			}

		}else{
			if( a.getPosition().z >= centre.z ){

				if(a.getPosition().y >= centre.y )
					index=1;
				else
					index=5;	
			}else{
				if(a.getPosition().y >= centre.y )
					index=2;
				else
					index=6;		
			}
		}
		if(index== -1)System.out.println(a.getPosition() + " ERREUR ");
		
		return index;
	}

	/**
	 * Methode pour ajouter l'astre dans l'arbre.
	 * 
	 * @param a L'astre a ajouter.
	 */
	public void insertToNode( Astre a) {
		int aIndex = getIndex(a);

		if(nbAstre > 1 && level < MAX_LEVEL){
			if(nodes[aIndex]==null)
				splitNode(aIndex);
			
			if(level != 0)
				CalculCentreDeMasse(a);
			
			nodes[aIndex].insertToNode(a);
		}else{
			if(nbAstre == 1 && level < MAX_LEVEL){
				Astre tempAstre = astre.get(0);
				int tempAstreIndex = getIndex(tempAstre);

				if(nodes[tempAstreIndex] == null)
					splitNode(tempAstreIndex);
				
				astre.remove(tempAstre);
				nodes[tempAstreIndex].insertToNode(tempAstre);

				if(nodes[aIndex] == null)
					splitNode(aIndex);
				
				if(level != 0)
					CalculCentreDeMasse(a);
				nodes[aIndex].insertToNode(a);
			}else{
				if(nbAstre == 0 || level == MAX_LEVEL){
					astre.add(a);
					if(level != 0)
						CalculCentreDeMasse(a);
				}
			}
		}
		nbAstre ++;
	}

	/**
	 * Methode pour ajouter les astres pour les collisions dans l'arbre
	 * 
	 * @param a L'astre a ajouter.
	 */
	public void insertToCollisionList( Astre a) {
		ArrayList<Integer> ListAstreIndex = getIndexCollision(a);

		for(int i = 0; i < ListAstreIndex.size(); i++){

			if(nodes[ListAstreIndex.get(i)] != null){
				nodes[ListAstreIndex.get(i)].insertToCollisionList(a);
			}
			if(nbAstre != 0 && !listAstreCollisions.contains(a) ){
				listAstreCollisions.add(a);
			}
		}
	}
	/**
	 * Verifie tous les nodes pour savoir si l'astre fait partie de ces nodes.
	 * 
	 * @param a L'astre qui va etre trie dans l'octree. 
	 * @return La liste de tous les nodes que l'astre touche.
	 */
	public ArrayList<Integer> getIndexCollision(Astre a){
		ArrayList<Integer> tempList = new ArrayList<Integer>();
		double rayon = a.getRayon();
		
		
		if(a.getPosition().x + rayon >= centre.x || a.getPosition().x - rayon >= centre.x){

			if( a.getPosition().y + rayon >= centre.y || a.getPosition().y - rayon >= centre.y ){

				if( a.getPosition().z + rayon >= centre.z || a.getPosition().z - rayon >= centre.z)
					tempList.add(0);
				
				if( a.getPosition().z + rayon < centre.z || a.getPosition().z - rayon < centre.z)
					tempList.add(3);
			}
			if( a.getPosition().y + rayon < centre.y || a.getPosition().y - rayon < centre.y ){
				
				if( a.getPosition().z + rayon > centre.z || a.getPosition().z - rayon > centre.z)
					tempList.add(4);
				
				if( a.getPosition().z + rayon < centre.z || a.getPosition().z - rayon < centre.z)
					tempList.add(7);
			}
		}
		if(a.getPosition().x + rayon < centre.x || a.getPosition().x - rayon < centre.x){
			if( a.getPosition().y + rayon > centre.y || a.getPosition().y - rayon > centre.y ){

				if( a.getPosition().z + rayon > centre.z || a.getPosition().z - rayon > centre.z)
					tempList.add(1);
				
				if( a.getPosition().z + rayon < centre.z || a.getPosition().z - rayon < centre.z)
					tempList.add(2);
			}
			if( a.getPosition().y + rayon < centre.y || a.getPosition().y - rayon < centre.y ){
				if( a.getPosition().z + rayon > centre.z || a.getPosition().z - rayon > centre.z)
					tempList.add(5);
				
				if( a.getPosition().z + rayon < centre.z || a.getPosition().z - rayon < centre.z)
					tempList.add(6);
			}
		}
		return tempList;

	}
	/**
	 * Passe a travers l'octree pour verifier chaque collision possible
	 */
	public void barnesCollision(){
		for (int i = 0; i < nodes.length; i++) 
			for(int u = 0 ; u < listAstreCollisions.size(); u++)
				for(int y = u+1 ; y < listAstreCollisions.size(); y++)
					Calculs.verifierCollision(listAstreCollisions.get(u),listAstreCollisions.get(y));
	}

	/**
	 * Retourne si oui ou non l'astre existe deja dans l'arbre
	 * 
	 * @param a L'astre a verifier.
	 * @return True si l'astre existe, sinon False.
	 */
	public boolean existe(Astre a){
		boolean present=false;
		int aIndex = getIndex(a);
		for(int i = 0; i < astre.size(); i++){
			if(a.equals(astre.get(i)))
				return true;
		}
		if(nodes[aIndex] != null){
			present = nodes[aIndex].existe(a);
		}
		return present;
	}

	/**
	 * Retourne le niveau de l'astre demandé.
	 * 
	 * @param a L'astre qui doit etre trouve.
	 * @return Le niveau de l'astre.
	 */
	public String getLevel( Astre a ){
		String temp ="";
		int aIndex = getIndex(a);
		for(int i = 0; i < astre.size(); i++){
			if(a.equals(astre.get(i)))
				temp = "ASTRE position : " + a.getPosition()  +"  /  Niveau : " + level;
		}
		if(nodes[aIndex] != null){
			temp = nodes[aIndex].getLevel(a);
		}
		return temp;

	}

	/**
	 * Retourne la liste de tous les astres de l'octree.
	 * 
	 * @return La liste de tous les astres de l'octree.
	 */
	public ArrayList<Astre> getList(){
		ArrayList<Astre> listTemp = new ArrayList<Astre>();
		for (int i = 0; i < nodes.length; i++) {
			if (nodes[i]!=null ) {
				for(int u = 0; u < nodes[i].getList().size(); u++ )
					listTemp.add(nodes[i]. getList().get(u));

				for (int u = 0; u < nodes[i].astre.size();u++)
					listTemp.add(nodes[i].astre.get(u));
			}
		}
		return listTemp;
	}

	/**
	 * Retourne le nombre d'astre dans l'arbre.
	 *
	 * @return Le nombre d'astre dans l'arbre.
	 */
	public int getnbAstre(){
		return nbAstre;
	}

	/**
	 * Retourne le noeud qui respecte l'alpha demandé.
	 * 
	 * @param astre L'astre qui sera modifier.
	 * @param alpha L'alpha demandé
	 * @return Tous les octrees qui on respecter la valeur du alpha et qui contient des astres.
	 */
	public ArrayList<Octree> getOctreeForAttraction(Astre astre,double alpha){
		ArrayList<Octree> tempList = new ArrayList<Octree>();

		for(int i = 0; i< nodes.length; i++){
			if(nodes[i] != null){
				Vector3 vDistance= Vector3.difference(astre.getPosition(),nodes[i].getCentreDeMasse());
				double distance = vDistance.magnitude(); 
				if(distance > astre.getRayon()*2 && nodes[i].nbAstre == 1 || level == MAX_LEVEL-1 ||  alpha >= (proportion*2/distance))
					tempList.add(nodes[i]);
				else{
					ArrayList<Octree> retour = nodes[i].getOctreeForAttraction(astre,alpha);
					for(int u = 0; u<  retour.size();u++)
						tempList.add(retour.get(u));
				}
			}
		}
		return tempList;
	}

	/**
	 * Ajoute la massse de l'astre et change le centre de masse
	 * 
	 * @param a L'astre avec lequel on va faire les changements.
	 */
	public void CalculCentreDeMasse(Astre a){

		centreDeMasse= new Vector3( ((centreDeMasse.x*masse) + (a.getPosition().x*a.getMasse()))/(masse+a.getMasse()),
				((centreDeMasse.y*masse) + (a.getPosition().y*a.getMasse()))/(masse+a.getMasse()),
				((centreDeMasse.z*masse) + (a.getPosition().z*a.getMasse()))/(masse+a.getMasse()));
		masse += a.getMasse();
	}
	/**
	 * Passe a travers tout l'octree pour savoir quels noeuds sont utilises.
	 * 
	 * @return L'arrayList de tous les nodes qui sont utilises.
	 */
	public ArrayList<Octree> OctreeUtilise(){
		ArrayList<Octree> tempList = new ArrayList<Octree>();
		ArrayList<Octree> tempListRetour = new ArrayList<Octree>();

		for(int i = 0; i< nodes.length; i++){		
			if(nodes[i] != null){
				tempListRetour=nodes[i].OctreeUtilise();
				for(int u = 0; u< tempListRetour.size(); u++)
					tempList.add(tempListRetour.get(u));

			}
			if( tempListRetour != null) 
				tempList.add(this);
			if(nbAstre == 1 || level == MAX_LEVEL){
				tempList.add(this);
				break;
			}
		}
		return tempList;
	}
	/**
	 * Retourne le centre du octree.
	 * 
	 * @return Le centre du octree.
	 */
	public Vector3 getCentre(){
		return centre;
	}
	/**
	 * Retourne la proportion de l'octree.
	 * 
	 * @return La proportion de l'octree.
	 */
	public double getProportion(){
		return proportion;
	}
	/**
	 * Retourne le centre de masse de cet octree.
	 * 
	 * @return Le centre de masse de cet octree.
	 */
	public Vector3 getCentreDeMasse(){
		return centreDeMasse;
	}
	/**
	 * Retourne la masse totale qui ce trouve dans cet octree.
	 * 
	 * @return La masse totale qui ce trouve dans cet octree.
	 */
	public double getMasse(){
		return masse;
	}
	/**
	 * Indique si l'astre est seul dans cet octree.
	 * 
	 * @return True si l'astre est seul, sinon False.
	 */
	public Astre getAstreSeul(){
		if (astre.size() == 0)
			return null;
		return astre.get(0);
	}

	@Override 
	public String toString() {
		String test="( Niveau: "+level+" ) (Centre: "+ centre +") (Proportion: "+ proportion + " )";

		return test;
	}
}