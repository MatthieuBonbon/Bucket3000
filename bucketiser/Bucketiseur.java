package bucketiser;

import java.util.ArrayList;

/**
 * Classe qui permet de bucketiser un tableau a 2 dimensions (String[][]) et de tester si cette bucketisation est l-diverse ou non.
 */
public class Bucketiseur {
    private String[][] tableau;
    private int nbID;
    private int nbQID;
    private int k;
    private int l;
    private String[][] tableauQID;
    private String[][] tableauDS;
    private boolean isL_diverse;

    /**
     * Constructeur de cette classe.
     * @param tableau qui va être bucketisé
     * @param nbID le nombre d'identifiants de ce tableau
     * @param nbQID le nombre de quasi-identifiants de ce tableau
     * @param k le degré de k-anonymisation
     * @param l le degré de l-diversité
     */
    public Bucketiseur(String[][] tableau, int nbID, int nbQID, int k, int l) {
        this.tableau = tableau;
        this.nbID = nbID;
        this.nbQID = nbQID;
        this.k = k; // k-anonymisation
        this.l = l; // l-diversité
    }


    /**
     * Methode principale de cette classe, permettant de bucketiser un tableau a 2 dimensions.
     */
    public void bucketiser(){
        String[][] tab = retirerID(this.tableau); // tableau sans ID
        int numGroupe = 0;

        // initialisation des tableaux contenant QID+Groupe et DS+Groupe
        tableauQID = new String[tab.length][nbQID+1];
        tableauDS = new String[tab.length][2]; // 2 car une unique colonne de Données Sensibles et une pour le groupe

        tableauQID[0][tableauQID[0].length-1] = "Groupe"; // tab[0].length - 1 permet de récupérer l'index de la dernière colonne
        tableauDS[0][1] = "Groupe";

        for(int i = 0; i < tab.length; i++){

            // 1. boucle for -> colonnes QID
            for(int j = 0; j < nbQID; j++){
                tableauQID[i][j] = tab[i][j];
            }

            // 2. Données sensibles
            tableauDS[i][0] = tab[i][tab[0].length - 1];

            // 3. condition pour num de groupe
            if(i >= 1){ // On commence à partir de 1 car i = 0 est le titre de la colonne
                if(i%k==1){ // Si i % k == 1 car cela permet de conserver la k anonymisation pour le premier groupe
                    numGroupe+=1;;
                }
                tableauQID[i][tableauQID[0].length - 1] = String.valueOf(numGroupe);
                tableauDS[i][tableauDS[0].length - 1] = String.valueOf(numGroupe);
            }
        }

        isL_diverse = diversite(tableauDS);
        System.out.println(l + "-divers : " + isL_diverse);

    }


    /**
     * Methode retournant le tableau pris en parametre sans la (ou les) colonne(s) d'identifiant(s)
     * @param tableau a partir duquel le tableau sans identifiant(s) est cree.
     * @return le tableau sans la (ou les) colonne(s) d'identifiant(s)
     */
    private String[][] retirerID(String[][] tableau){
        String[][] tabSansID;
        tabSansID = new String[tableau.length][tableau[0].length - nbID];
        for(int i = 0; i < tabSansID.length; i++){ // pour chaque ligne
            for(int j = 0; j < tabSansID[0].length; j++){ // pour chaque colonne
                tabSansID[i][j] = tableau[i][j+nbID];
            }
        }
            return tabSansID;
    }


    /**
     *  Methode testant la l-diversité de la bucketisation effectuee
     * @param tabDS le tableau contenant les Donnees sensibles regroupees par groupes
     * @return le booleen signifant si la l-diversite est respectee ou non
     */
    private boolean diversite(String[][] tabDS){
        int nbGroupe = Integer.parseInt(tabDS[tabDS.length-1][1]); // la dernière ligne appartient forcément au dernier groupe
        System.out.println("nb de grp : " + nbGroupe);
        for(int i = 1; i <= nbGroupe; i++){ // pour chaque groupe

            ArrayList<String> diversite = new ArrayList<>();

            for(int j = (i-1) * k + 1; j <= (i*k); j++){ // pour chaque élément d'un même groupe
                System.out.println("index : " + j);
                System.out.println("valeur : " + tabDS[j][0]);
                System.out.println("groupe : " + tabDS[j][1]);
                if(!diversite.contains(tabDS[j][0])){
                    diversite.add(tabDS[j][0]);
                }

            }
            if(diversite.size() != l){
                return false;
            }
        }
        return true;
    }

    /**
     * Getter qui retourne le tableau de Quasi-identifiants regroupes par groupes
     * @return le tableau de Quasi-identifiants regroupes par groupes
     */
    public String[][] getTableauQID() {
        return tableauQID;
    }

    /**
     * Getter qui retourne le tableau de Donnees sensibles regroupees par groupes
     * @return le tableau de Donnees sensibles regroupees par groupes
     */
    public String[][] getTableauDS() {
        return tableauDS;
    }

    /**
     * Getter qui retourne si le tableau est l-divers
     * @return le booleen permettant de savoir si bucketisation est l-diverse ou non
     */
    public boolean isL_diverse() {
        return isL_diverse;
    }
}
