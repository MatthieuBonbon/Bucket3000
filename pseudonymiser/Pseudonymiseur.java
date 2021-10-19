package pseudonymiser;

import java.util.Random;

/**
 * Classe qui permet de pseudonymiser un tableau a 2 dimensions (String[][]) et permet de retourner : <br>
 *     <ul>
 *         <li>
 *             Le tableau pseudonymise, avec les pseudos a la place des identifiants
 *         </li>
 *         <li>
 *             Le tableau d'association, qui associe les identifiants au pseudos
 *         </li>
 *     </ul>
 */
public class Pseudonymiseur {

    private String[][] tableau;
    private String[][] tabAssociationIdPseudos;
    private int nbColId;
    private Random random = new Random();

    /**
     * Constructeur de la classe.
     * @param tab le tableau qui doit etre pseudonymise
     * @param nb_colonne_id le nombre de colonne(s) d'identifiant(s)
     */
    public Pseudonymiseur(String[][] tab, int nb_colonne_id) {
        this.tableau = tab;
        this.nbColId = nb_colonne_id-1; // Car il y a au minimum une colonne d'identifiants
    }

    /**
     * Méthode principale de cette classe. Permet de pseudonymiser un tableau a 2 dimensions.
     * @return le tableau pseudonymise
     */
    public String[][] pseudonymiser(){
        String[][] tableau = this.tableau;
        String[] pseudo;
        tabAssociationIdPseudos = new String[tableau.length][(nbColId+1)*2];

        for(int index_col = 0; index_col <= this.nbColId; index_col++){

            pseudo = creerTabPseudo(tableau.length);
            tabAssociationIdPseudos[0][index_col*2]="Id " + String.valueOf(index_col+1);
            tabAssociationIdPseudos[0][(index_col*2)+1]="Pseudo " + String.valueOf(index_col+1);

            for (int index_row = 1; index_row < tableau.length; index_row++){
                // On commence ligne 1 car ligne 0 il y a le titre de la colonne

                tabAssociationIdPseudos[index_row][index_col*2]=tableau[index_row][index_col];
                tabAssociationIdPseudos[index_row][(index_col*2)+1]=pseudo[index_row-1];

                tableau[index_row][index_col]=pseudo[index_row-1];
            }
        }

        return tableau;
    }

    /**
     * Methode permettant de faire un tableau a 1 dimension contenant des pseudos uniques et de le retourner
     * @param nbRow nombre de ligne du tableau initial
     * @return le tableau a 1 dimension contenant des pseudos uniques
     */
    private String[] creerTabPseudo(int nbRow){
        String[] tab ;

        tab = new String[nbRow-1];
        String pseudo;


        for (int i = 0; i < nbRow-1; i++){
            pseudo = pseudoUnique(tab);
            tab[i]=pseudo;

        }

        return tab;
    }

    /**
     * Methode recursive retournant un pseudo unique pour chaque element de d'une colonne d'identifiants
     * @param tab tableau contenant la liste des pseudos deja utilises
     * @return le pseudo unique
     */
    private String pseudoUnique(String[] tab){  //retourne un pseudo unique
        boolean valide;
        int i; // pour la boucle while
        String pseudo;

        valide = true;
        i = 0;
        pseudo = genererPseudo(); // On génère un pseudo

        while (i<tab.length){ // On vérifie que le pseudo soit unique
            if(tab[i]!=pseudo){
                i++;
            }
            else {
                valide = false;
                break;
            }


        }

        if(!valide){ // Si le pseudo n'est pas unique on fait une récursion jusqu'à temps d'avoir un pseudo unique
            pseudoUnique(tab);
        }

        return pseudo;
    }


    /**
     * Methode generant un pseudo <b>unique</b> compris entre 10 000 et 99 999.
     *  <br> /!\ S'il y a plus de 89 999 identifiants, a partir du 90 000ème, il y aura des doublons
     * @return un pseudo aleatoire
     */
    private String genererPseudo(){ //Génère un pseudo
        int x;

        x = random.nextInt(99999-10000) + 10000;

        return String.valueOf(x);
    }

    /**
     * Getter retournant le tableau a 2 dimensions d'association des identifiants et des pseudos
     * @return le tableau d'association des identifiants et des pseudos
     */
    public String[][] getTabAssociationIdPseudos() {
        return tabAssociationIdPseudos;
    }
}
