package algosDimensionnels;

import java.util.Arrays;

/**
 * Classe permettant d'anonymiser un tableau a 2 dimensions (String[][]) via un algorithme unidimensionnel, <br>
 *     ou chaque ligne du tableau est anonymisee selon le meme quasi-identifiant.
 */
public class AlgoUniDimension {
    protected String[][] tableau;
    protected int nbID; // permet de déterminer l'index de la première colonne de QID
    protected int nbQID;
    protected int k; // k-anonymisation
    protected String[] attribut;
    protected String[] attributCopy;


    /**
     * Constructeur de la classe.
     * @param tableau tableau à anonymiser.
     * @param nbID nombre d'identifiant(s) du tableau à anonymiser
     * @param nbQID nombre de quasi-identifiant(s) du tableau à anonymiser
     * @param k le degre de k-anonymisation
     */
    public AlgoUniDimension(String[][] tableau, int nbID, int nbQID, int k) { // constructeur
        this.tableau = tableau;
        this.nbID = nbID;
        this.nbQID = nbQID;
        this.k = k;
        this.attribut = choisirAttribut(tableau, nbID, nbQID);
        // On utilise une copie de l'attribut car sinon, on trierait directement en ordre croissant et bousculerait l'ordre initial de l'attribut
        this.attributCopy = Arrays.copyOf(attribut, attribut.length);
    }


    /**
     * Methode qui va s'occuper de la gestion de l'algorithme unidimensionnel
     * @return le tableau anonymise par l'algorithme unidimensionnel
     */
    public String[][] gestionAlgoDimensionnel(){ // appelle l'algo unidimensionnel puis la méthode permettant d'insérer les valeurs anonymisés dans le tableau
        algoDimensionnel(this.attributCopy);
        implementerAttributAnonymise(this.attribut);

        return tableau;
    }


    /**
     * Algorithme unidimensionnel
     * @param attribut colonne du quasi-identifiant qui a ete choisi pour anonymiser le jeu de donnees
     */
    void algoDimensionnel(String[] attribut){ // algo unidimensionnel
        String[] frequence = trierAttribut(attribut);
        int mediane = mediane(frequence);

        String[] lhs = cutter(frequence, 0, mediane);
        String[] rhs = cutter(frequence, mediane, frequence.length);


        if (k < lhs.length) {
            algoDimensionnel(lhs);
            if (k < rhs.length) {
                algoDimensionnel(rhs);
            }
        }

        else { // on compare k uniquement à lhs car lhs forcément >= rhs

            anonymiserValeurs(lhs);

            anonymiserValeurs(rhs);
        }

    }


    /**
     * Methode qui choisit le quasi-identifiant qui va être utilise pour anonymiser le tableau à 2 dimensions via l'algorithme unidimensionnel
     * @param tableau tableau qui doit être anonymise
     * @param nbID nombre d'identifiant(s) du tableau à anonymiser
     * @param nbQID nombre de quasi-identifiant(s) du tableau à anonymiser
     * @return la colonne de tout les elements de ce quasi-identifiant sous la forme d'un tableau à 1 dimension
     */
     String[] choisirAttribut(String[][] tableau, int nbID, int nbQID){ // choisit et retourne l'attribut qui va être utilisé dans l'algo
        // extrait et retourne une colonne d'attribut
        String[] attribut;
        attribut = new String[tableau.length - 1];

        for(int i = 1; i < tableau.length; i++){
            attribut[i-1] = tableau[i][nbID];
        }

        return attribut;
    }


    /**
     * Methode qui trie dans l'ordre croissant les elements du tableau a 1 dimension passe en parametre et les retournent
     * @param attribut tableau a 1 dimension que l'on doit anonymiser
     * @return une tableau a 1 dimension contenant le @param attribut trie dans l'ordre croissant
     */
     String[] trierAttribut(String[] attribut){ // permet de trier les données dans l'ordre croissant ( -> fréquence)
        int j;
        String cle;

        for (int i = 1; i < attribut.length; i++) {
            cle = attribut[i];
            j = i;
            while ((j >= 1) && (Integer.parseInt(attribut[j - 1]) > Integer.parseInt(cle))) {
                attribut[j]  = attribut[j - 1] ;
                j = j - 1;
            }
            attribut[j] = cle;
        }

        return attribut;
    }


    /**
     * Méthode qui retourne la médiane d'un tableau à 1 dimension passe en parametre
     * @param frequence tableau à 1 dimension ou l'on cherche la médiane
     * @return l'index de la mediane du tableau passe en @param
     */
    int mediane(String[] frequence){ // retourne la médiane de la fréquence (attribut trié dans l'ordre croissant avec valeurs doublons inclus)

        if(frequence.length%2==0){ // si la longueur de la frequence est un nombre pair
            return frequence.length/2;
        }
        else{ // si la longueur de la frequence est un nombre impair
            return frequence.length/2 + 1;
        }
    }


    /**
     * Méthode qui retourne un tableau a 1 dimension rogne de l'index debut a l'index fin
     * @param tableau tableau que l'ont doit rogner
     * @param debut index de debut du rognage
     * @param fin index de fin du rognage
     * @return le tableau a 1 dimension rogne
     */
    private String[] cutter(String[] tableau, int debut, int fin){ // coupe une partie d'un tableau et la retourne
        String[] cutTab;
        cutTab = new String[fin-debut];

        for(int i = 0; i < (fin-debut); i++){
            cutTab[i] = tableau[i+debut];
        }

        return cutTab;

    }


    /**
     * Methode qui anonymise les valeurs d'un tableau d'attribut comprises entre le minimum et le maximum du tableau passe en parametre
     * @param tab le tableau ou vont etre recuperes les valeurs maximum et minimum
     */
    void anonymiserValeurs(String[] tab){ // met les valeurs de l'attribut entre intervalle
        String intervalle;
        intervalle = "[ " + min(tab) + " - " + max(tab) + " ]";

        for(int i = 0; i < this.attribut.length; i++){
            try {
                if(Integer.parseInt(this.attribut[i])>=Integer.parseInt(min(tab)) && Integer.parseInt(this.attribut[i])<=Integer.parseInt(max(tab))){

                    this.attribut[i]=intervalle;

                }
            }
            catch (NumberFormatException e){
                // permet de ne pas bloquer la boucle for si on passe dans une donnée déjà compris dans un intervalle
                ;
            }

        }

    }


    /**
     * Methode qui retourne la valeur minimum d'un tableau
     * @param tab tableau où l'on doit trouver la plus petite valeur
     * @return la plus petite valeur du tableau
     */
    String min(String[] tab){ // retourne le min d'un tableau
        String min;
        min = tab[0];

        for(int i = 0; i < tab.length; i++){
            if(tab[i] != null){
                if(Integer.parseInt(tab[i])<Integer.parseInt(min)){
                    min = tab[i];
                }
            }

        }

        return min;
    }


    /**
     * Methode qui retourne la valeur maximum d'un tableau
     * @param tab tableau ou l'on doit trouver la plus grande valeur
     * @return la plus grande valeur du tableau
     */
    String max(String[] tab){ // retourne le max d'un tableau
        String max;
        max = tab[0];

        for(int i = 0; i < tab.length; i++){
            if(tab[i] != null){
                if(Integer.parseInt(tab[i])>Integer.parseInt(max)){
                    max = tab[i];
                }
            }

        }

        return max;
    }


    /**
     * Methode permettant d'implementer la colonne d'attribut anonymisee (String[]) au tableau initial
     * @param attribut tableau de l'attribut anonymise
     */
    private void implementerAttributAnonymise(String[] attribut){ //implémente l'attribut anonymisé dans le tableau de données
        for(int i = 0; i < attribut.length; i++){
            this.tableau[i+1][nbID]=attribut[i];
        }
    }

}

