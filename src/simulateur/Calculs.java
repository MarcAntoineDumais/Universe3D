package simulateur;

import java.util.ArrayList;

import affichage3d.Vector3;

/**
 * Cette classe est comporte l'ensemble des methodes de calculs utilises par l'application.
 * 
 * @author David-Olivier Duperron
 * 
 * <br>Date de creation : ~ 2/15/14 ~
 * 
 */

public class Calculs {


	private static final double  GRAVITATIONAL_CONSTANT= 6.67*Math.pow(10,-11);

	/**
	 * Utile pour calculer deux astres a la fois.
	 * Utilise les valeurs initiales des astres a1 et a2 pour ainsi calculer la position finale.Grace a cette position, on calcule la vitesse finale des astres.
	 * 
	 * @param a1 Le premier astre utilise pour faire les calculs
	 * @param a2 Le deuxieme astre utilise pour faire les calculs
	 * @param delta L'incrementation du temps qui cera utilise pour faire les calculs.
	 */
	public static void calculAttractionEulerPourDeuxAstres(Astre a1,Astre a2,double delta){

		if(verifierCollision(a1, a2) == null){
			calculAttractionEuler(a1,a2,delta);
			calculAttractionEuler(a2,a1,delta);
		}	
	}

	/**
	 * Utilise les valeurs initiales de l'astre et de la particule pour calculer la position et la vitesse de la particule.
	 * 
	 * @param a1 L'astre avec lequel les calculs se feront.
	 * @param p La particule avec lequel les calculs se feront.
	 * @param delta L'incrementation du temps qui cera utilise pour faire les calculs.
	 */
	public static void calculAttractionParticule(Astre a1,Particule p,double delta){
		double deltaSec= delta/1000;

		double distance = Vector3.difference(a1.getPosition(),p.getPosition()).magnitude();

		if(distance > a1.getRayon() + p.getRayon()) {
			Vector3 acceleration=accelerationGravitationelle(a1,p.getPosition().copy());

			p.getVitesseFutur().translate(acceleration.x * deltaSec, acceleration.y * deltaSec, acceleration.z * deltaSec);	

		} else {
			a1.setMasse(a1.getMasse() + p.getMasse());
			p.dispose();
		}
	}
	/**
	 * Utilise un octree pour syntétiser des astres pour ainsi réduir le nombre de calculs effectues.
	 * 
	 * @param root L'octree de base qui contient les astre dont nous voulons faire les updates.
	 * @param a L'astre qui sera modifier apres les calculs.
	 * @param delta L'incrementation du temps qui cera utilise pour faire les calculs.
	 * @param alpha valeurs qui sera utilise pour effectuer l'approximation des centres de masse.
	 */
	public static void calculAttractionBarnesHut(Octree root, Astre a, double delta, double alpha,int calcul){
		ArrayList<Octree> octreeList = root.getOctreeForAttraction(a,alpha);

		for(int i = 0; i < octreeList.size(); i++){
			Octree octree = octreeList.get(i);
			Astre temp= new Astre(octree.getMasse(),octree.getCentreDeMasse());

			switch(calcul){

			case 0://Euler
				calculAttractionEuler(a,temp,delta);

				Vector3 tempPosition = new Vector3(a.getPosition().x+a.getVitesse().x*delta/1000.0,
						a.getPosition().y+a.getVitesse().y*delta/1000.0,
						a.getPosition().z+a.getVitesse().z*delta/1000.0); 

				a.setPositionFutur(tempPosition);
				break;

			case 1://Euler inverse
				calculAttractionEuler(a,temp,delta);

				tempPosition = new Vector3(a.getPosition().x+a.getVitesseFutur().x*delta/1000.0,
						a.getPosition().y+a.getVitesseFutur().y*delta/1000.0,
						a.getPosition().z+a.getVitesseFutur().z*delta/1000.0); 

				a.setPositionFutur(tempPosition);
				break;
				
			case 2://RK4
				calculAttractionRK4(a,temp,delta);
				
				tempPosition = new Vector3(a.getPosition().x+a.getVitesse().x*delta/1000.0,
						a.getPosition().y+a.getVitesse().y*delta/1000.0 ,
						a.getPosition().z+a.getVitesse().z*delta/1000.0 ); 

				a.setPositionFutur(tempPosition);
			}
		}
	}
	/**
	 * Utilise les valeurs initiales des astres a1 et a2 pour ainsi calculer des positions fictives qui seront utiles pour obtenir la position et vitesse finale .
	 * 
	 * @param a1 Le premier astre utilise pour faire les calculs
	 * @param a2 Le deuxieme astre utilise pour faire les calculs
	 * @param delta L'incrementation du temps qui cera utilise pour faire les calculs.
	 */
	public static void calculAttractionRK4PourDeuxAstres(Astre a1, Astre a2, double delta){

			if(verifierCollision(a1, a2) == null){
			calculAttractionRK4(a1, a2, delta);
			calculAttractionRK4(a2, a1, delta);
			}
	}
	/**
	 * Calcule le resultat de la collision entre ces astres.
	 * 
	 * @param a1 L'astre utiliser pour effectuer le calcul de la collision.
	 * @param a2 L'astre utiliser pour effectuer le calcul de la collision.
	 */
	public static void calculCollision(Astre a1, Astre a2){
		a1.setVitesse(
				(((a1.getMasse()*a1.getVitesse().x)+(a2.getMasse()*a2.getVitesse().x))/(a1.getMasse()+a2.getMasse())),
				(((a1.getMasse()*a1.getVitesse().y)+(a2.getMasse()*a2.getVitesse().y))/(a1.getMasse()+a2.getMasse())),
				(((a1.getMasse()*a1.getVitesse().z)+(a2.getMasse()*a2.getVitesse().z))/(a1.getMasse()+a2.getMasse()))
				);
		a1.setMasse(a1.getMasse() + a2.getMasse());
		a2.dispose();
	}


	/**
	 * Test pour voir si ces deux astres ne font pas une collision grace a leurs positions et rayons.
	 * 
	 * @param a1 L'astre utiliser pour verifier la collision.
	 * @param a2 L'astre utiliser pour verifier la collision.
	 * @return L'astre qui a la masse la plus grosse s'il y a une collision/ retourne rien s'il y a aucune collision.
	 */
	public static Astre verifierCollision(Astre a1, Astre a2){
		Astre temp = null;
		Vector3 vDistance= Vector3.difference(a2.getPosition().copy(),a1.getPosition().copy());
		double distance = vDistance.magnitude(); 
		if(distance <= a1.getRayon() + a2.getRayon()) {

			if(a1.getMasse() >= a2.getMasse()){
				calculCollision(a1,a2);
				temp = a2;
			}else{
				if(a2.getMasse() >= a1.getMasse()){
					calculCollision(a2,a1);
					temp = a1;
				}
			}
		}
		return temp;
	}

	/**
	 * Pour un Astre.
	 * Calcule la vitesse approprie pour etre en orbite autour de l'astre choisie(centre).
	 * 
	 * @param centre L'astre qui est utilisé pour l'accélération
	 * @param a La position de l'astre dont nous avons besoins de calculer son accélération
	 * @return La vitesse calculer pour etre capable de faire orbiter l'astre a autour de l'astre centre.
	 */
	public static Vector3 calculVitesseCentripete(Astre centre, Astre a){
		Vector3 vDistance= Vector3.difference(centre.getPosition().copy(),a.getPosition().copy());
		double distance = vDistance.magnitude();
		Vector3 temp = Vector3.crossProduct(vDistance,new Vector3 (0,1,0));
		temp.normalize();

		double nVitesse = Math.sqrt((GRAVITATIONAL_CONSTANT*centre.getMasse())/distance); 

		temp.scale(nVitesse);
		temp.add(centre.getVitesse());
		if(temp.magnitude()>299792458)
			temp.scale(1/(temp.magnitude()/299792458));
		return temp;
	}

	/**
	 * Pour une particule.
	 * Calcule la vitesse approprie pour etre en orbite autour de l'astre choisie(centre).
	 * 
	 * @param centre L'astre qui est utilisé pour l'accélération
	 * @param p La position de la particule dont nous avons besoins de calculer son accélération
	 * @return La vitesse calculer pour etre capable de faire orbiter la particule p autour de l'astre centre.
	 */
	public static Vector3 calculVitesseCentripeteP(Astre centre, Particule p){
		Vector3 vDistance= Vector3.difference(centre.getPosition().copy(),p.getPosition().copy());
		double distance = vDistance.magnitude();
		Vector3 temp = Vector3.crossProduct(vDistance,new Vector3 (0,1,0));
		temp.normalize();

		double nVitesse = Math.sqrt((GRAVITATIONAL_CONSTANT*centre.getMasse())/distance); 

		temp.scale(nVitesse);
		temp.add(centre.getVitesse());
		if(temp.magnitude()>299792458)
			temp.scale(1/(temp.magnitude()/299792458));
		return temp;
	}
	
	/**
	 * Utile pour caculer uniquement les changements appliques a l' a1 .
	 * Utilise les valeurs initiales des astres a1 et a2 pour ainsi calculer la position finale.Grace a cette position, on calcule la vitesse finale des astres.
	 * 
	 * @param a1 L'astre avec lequel les calculs se feront.( a1 sera midifie )
	 * @param a2 L'astre avec lequel les calculs se feront.
	 * @param delta L'incrementation du temps qui cera utilise pour faire les calculs.
	 */
	public static void calculAttractionEuler(Astre a1, Astre a2, double delta){
		double deltaSec= delta/1000.0;
		Vector3 acceleration = accelerationGravitationelle(a2,a1.getPosition().copy());
		a1.getVitesseFutur().translate(acceleration.x * deltaSec, acceleration.y * deltaSec, acceleration.z * deltaSec);
	}

	/**
	 * Utile pour caculer uniquement les changements appliques a l' a1 avec les formules de la methode de Runge-Kutta de 4e ordres .
	 * 
	 * @param a1 L'astre avec lequel les calculs se feront.( a1 sera midifie )
	 * @param a2 L'astre avec lequel les calculs se feront.
	 * @param delta L'incrementation du temps qui cera utilise pour faire les calculs.
	 */
	public static void calculAttractionRK4(Astre a1, Astre a2, double delta){

		Vector3 accelerationIni;
		Vector3 accelerationMil;
		Vector3 accelerationF2;
		double deltaSec= delta/1000;
		
		accelerationIni= accelerationGravitationelle(a2,a1.getPosition().copy());

		Vector3 tempMil= new Vector3 (a1.getPosition().x + a1.getVitesse().x*(deltaSec/2.0) + (1.0/2)*accelerationIni.x *((deltaSec/2.0)*(deltaSec/2.0)),
				a1.getPosition().y + a1.getVitesse().y*(deltaSec/2.0) + (1.0/2)*accelerationIni.y *((deltaSec/2.0)*(deltaSec/2.0)),
				a1.getPosition().z + a1.getVitesse().z*(deltaSec/2.0) + (1.0/2)*accelerationIni.z *((deltaSec/2.0)*(deltaSec/2.0)));
		
		accelerationMil= accelerationGravitationelle(a2,tempMil);

		Vector3 tempF2= new Vector3 (a1.getPosition().x + (a1.getVitesse().x)*(deltaSec) + ((1.0/2)*accelerationIni.x) *((deltaSec)*(deltaSec)),
				a1.getPosition().y + (a1.getVitesse().y)*(deltaSec) + ((1.0/2)*accelerationIni.y) *((deltaSec)*(deltaSec)),
				a1.getPosition().z + (a1.getVitesse().z)*(deltaSec) + ((1.0/2)*accelerationIni.z) *((deltaSec)*(deltaSec)));

		accelerationF2= accelerationGravitationelle(a2,tempF2);

		a1.getPositionFutur().translate(0.5*(1.0/3*accelerationIni.x + 2.0/3*accelerationMil.x)*(deltaSec*deltaSec),
				0.5*(1.0/3*accelerationIni.y + 2.0/3*accelerationMil.y)*(deltaSec*deltaSec),
				0.5*(1.0/3*accelerationIni.z + 2.0/3*accelerationMil.z)*(deltaSec*deltaSec));

		a1.getVitesseFutur().translate((1.0/6*(accelerationIni.x) + 4.0/6*(accelerationMil.x) + 1.0/6*(accelerationF2.x))*deltaSec,
				(1.0/6*(accelerationIni.y) + 4.0/6*(accelerationMil.y) + 1.0/6*(accelerationF2.y))*deltaSec,
				(1.0/6*(accelerationIni.z) + 4.0/6*(accelerationMil.z) + 1.0/6*(accelerationF2.z))*deltaSec);
	}
	
	
	/**
	 * Calcul accélération gravitationnelle entre ces deux astres et retourne celle qui affecte l'astre 2 ( a2 ).
	 * 
	 * @param a1  l'astre qui est utilisé pour l'accélération
	 * @param posA2  la position de l'astre dont nous avons besoins de calculer son accélération
	 * @return l'accélération de l'astre a2 par rapport à a1
	 */
	private static Vector3 accelerationGravitationelle(Astre a1,Vector3 posA2){
		Vector3 acceleration;

		Vector3 forceG= Vector3.difference(a1.getPosition(),posA2);
		double distance = forceG.magnitude(); 
		double g= GRAVITATIONAL_CONSTANT*a1.getMasse()/(distance*distance);

		forceG.normalize();
		forceG.scale(g);
		acceleration = forceG.copy();

		return acceleration;
	}	
}