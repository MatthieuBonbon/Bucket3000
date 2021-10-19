package controleur;

import algosDimensionnels.*;
import bucketiser.Bucketiseur;
import pseudonymiser.Pseudonymiseur;
import transformationDonnees.*;
import vue.Vue;

import java.util.concurrent.TimeUnit;


/**
 * Classe qui, comme son nom l'indique, controle tout les autres elements du projet <br>
 * et les appellent a tour de role, en fonction des besoins, tout au long du parcours de l'utilisateur <br>
 * a travers le programme.
 */
public class Controleur {
    // On instancie les classes
    Vue vue;
    Pseudonymiseur pseudonymiseur;
    Bucketiseur bucketiseur;
    RecuperateurDonnees recuperateurDonnees;
    AlgoUniDimension algoUniDimension;

    // variables
    int nbID; // nombre de ID dans le tableau intitial
    int nbQID; // nombre de QID dans le tableau initial
    String pathIn; // chemin d'accès du fichier passé en entrée
    String pathOut; // chemin d'accès du dossier passé en sortie
    String[][] tabAssocIdPseudo; // tableau d'association du d'ID et de Pseudo résultant de la pseudonymisation
    String[][] tabPseudo; // tableaau pseudonymisé
    String[][] tabAlgoUni; // tableau algo uni
    String[][] qid; // tableau qid
    String[][] ds; // tableau ds

    /**
     * Constructeur de la classe.
     * @param vue qui va etre utilisee pour l'interface graphique et la communication entre l'utilisateur et la machine.
     */
    public Controleur(Vue vue) {
        this.vue = vue;
    }

    /**
     * Methode principale de la classe, en charge du bon deroulement des differentes etapes de l'anonymisation par le biais de : <br>
     *     <ul>
     *         <li>
     *             l'appel des differentes methodes de chaque classe au moment voulu
     *         </li>
     *         <li>
     *             l'attente des reponses de l'utilisateur pour executer certaines methodes
     *         </li>
     *     </ul>
     */
    public void controle() {
        vue.afficherInterface();

        while (!vue.isFichierValide()) {
            // On fait une boucle infini qui permet d'attendre de récupérer le path du fichier via la vue pour continuer

                System.out.println("Infinity loop 1");

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        pathIn = vue.getPath();
        System.out.println("\n Chemin d'accès récupéré : \n" + pathIn + "\n");

        String[][] tableau = recupDonnees(pathIn);
        tabPseudo = pseudonymiser(tableau);

        while(vue.getK()==0){
            // On fait une boucle infini qui permet d'attendre de récupérer k (+ l si besoin) via la vue pour continuer

            System.out.println("Infinity loop 2");

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(vue.getL()==0){ // Autrement dit, si on utilise l'algo multi dimensionnel
            algoMultiDimension(vue.getK());
        }
        else{ // Sinon, on utilise la bucketisation
            bucketiser(vue.getK(), vue.getL());
        }

        while(!vue.isDossierValide()){
            // On fait une boucle infini qui permet d'attendre de récupérer le path du dossier via la vue pour continuer

            System.out.println("Infinity loop 3");

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        pathOut = vue.getPathOut() + "/";
        System.out.println(pathOut);
        construireLesTabs(pathOut);

        while(!vue.isRecommencer()){
            System.out.println("Infinity loop 4");

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        controle();
    }


    /**
     * Methode se chargeant de recuperer les donnees aupres du fichier .ods fourni via son chemin d'acces <br>
     * afin de les retourner sous la forme d'un tableau a 2 dimensions. Cela grace a la classe RecuperateurDonnees et ses fonctions<br>
     * Permet aussi de recuperer le nombre d'identifiants et de quasi-identifiants du fichier .ods
     * @param path le chemin d'acces du fichier .ods qui doit etre anonymise
     * @return le tableau a 2 dimensions contenant toutes les donnees du fichier .ods
     */
    private String[][] recupDonnees(String path){ // Fini
        recuperateurDonnees = new RecuperateurDonnees(path);

        nbID = recuperateurDonnees.getNbID();
        nbQID = recuperateurDonnees.getNbQID();

        return recuperateurDonnees.readOds();
    }

    /**
     * Methode se chargeant de la pseudonymisation du tableau passe en parametre via la Classe Pseudonymiseur et l'appel de sa fonction principale.
     * @param tab tableau que l'on doit pseudonymiser
     * @return le tableau pseudonymise
     */
    private String[][] pseudonymiser(String[][] tab){ // Fini
        pseudonymiseur = new Pseudonymiseur(tab, nbID);
        String[][] tableauPseudonymise = pseudonymiseur.pseudonymiser();
        tabAssocIdPseudo = pseudonymiseur.getTabAssociationIdPseudos();

        return tableauPseudonymise;
    }

    /**
     * Methode se chargeant de la bucketisation du tableau pseudonymise via la Classe Bucketiseur.
     * @param k le degre de k-anonymat
     * @param l le degre de l-diveriste
     */
    private void bucketiser(int k, int l){ // Fini
        bucketiseur = new Bucketiseur(tabPseudo, nbID, nbQID, k, l);
        bucketiseur.bucketiser();

        qid  = bucketiseur.getTableauQID();
        ds = bucketiseur.getTableauDS();
        vue.setLDivers(bucketiseur.isL_diverse());
    }


    /**
     * Methode se chargeant de l'anonymisation par l'algorithme multi dimensionnel via les Classes AlgoUniDimension, AlgoMultiDimension <br>
     *     et le principe d'heritage puisque AlgoMultiDimension herite de AlgoUniDimension
     * @param k le degre de k-anonymat
     */
    private void algoMultiDimension(int k){ // Provisoire en attendant le multi dimension
        algoUniDimension = new AlgoMultiDimension(tabPseudo, nbID, nbQID, k);

        tabAlgoUni = algoUniDimension.gestionAlgoDimensionnel();
    }


    /**
     * Methode permettant de construire les differents fichiers .ods en fonction de la methode d'anonymisation choisie
     * @param path chemin d'acces ou les nouveaux
     */
    private void construireLesTabs(String path){

        // Pseudonymisation
        construireTab(tabAssocIdPseudo, "Association ID Pseudo", path);
        construireTab(tabPseudo, "Pseudonymisé", path);

        if(vue.getL()==0){ // Si on a choisi un Algo Dimensionnel
            construireTab(tabAlgoUni, "Algorithme unidimensionnel", path);
        }
        else{ // Si on a choisi une Bucketisation
            construireTab(qid, "Quasi-identifiants", path);
            construireTab(ds, "Données sensibles", path);
        }

    }


    /**
     * Methode permettant de construire un fichier .ods en appelant la Classe ConstructeurTableau
     * @param tab le tableau qui doit etre dans le fichier .ods
     * @param nomDoc le nom du fichier .ods
     * @param path le chemin d'acces du fichier .ods
     */
    private void construireTab(String[][] tab, String nomDoc, String path){ // Fini
        ConstructeurTableau constructeurTableau = new ConstructeurTableau(tab, nomDoc, path);

        constructeurTableau.construireTableau();
    }

}
