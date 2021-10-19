package main;

import controleur.Controleur;
import vue.Vue;


/**
 * Classe main contenant la methode eponyme a partir de laquelle le projet est execute.
 */
public class Main {	 // 		/!\ La bibliothèque utilisée est compatible jusqu'à OpenDocument 1.2 /!\

	/**
	 * Unique methode de la classe main permettant d'executer le projet.
	 * @param args
	 */
    public static void main(String[] args) {



		// VUE
		Vue vue = new Vue();

		// CONTROLEUR

		Controleur controleur = new Controleur(vue);
		controleur.controle();



    }
}
