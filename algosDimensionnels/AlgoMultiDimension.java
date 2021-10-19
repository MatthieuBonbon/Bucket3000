package algosDimensionnels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


/**
 * Classe permettant d'anoynmiser un tableau a 2 dimensions (String[][]) via un algorithme multidimensionnel, <br>
 *     ou chaque ligne du tableau est anonymisee selon plusieurs quasi-identifiants.
 */
public class AlgoMultiDimension extends AlgoUniDimension{

    private int indexAttribut;
    private String[][] tableauQID;

    /**
     *{@inheritDoc}
     */
    public AlgoMultiDimension(String[][] tableau, int nbID, int nbQID, int k) {
        super(tableau, nbID, nbQID, k);
    }

    /**
     * Methode qui va s'occuper de la gestion de l'algorithme multidimensionnel
     * @return le tableau anonymise par l'algorithme multidimensionnel
     */
    @Override
    public String[][] gestionAlgoDimensionnel() {
        tableauQID = QIDtableau(tableau);
        algoMultiDimensionnel(tableauQID);
        implementerQidAnonymise(tableauQID);

        // Permet d'afficher le tableau récupéré depuis algoUniDim
        String str3 ="\n";
        for(int i = 0; i < tableau.length; i++){
            for(int j = 0; j < tableau[0].length; j++){						//	<- permet d'afficher un tableau (temp)
                str3+=" | " + tableau[i][j];
            }

            str3+=" | \n";
        }
        System.out.println(str3);

        return tableau;
    }

    /**
     * Algorithme multidimensionnel
     * @param tabQID tableau contenant toutes les colonnes de quasi-identifiants issues du tableau initial
     */
    private void algoMultiDimensionnel(String[][] tabQID){
        indexAttribut = choixIndexAttribut();

        attribut = choisirAttribut(tabQID, 0, indexAttribut);
        // On utilise une copie de l'attribut car sinon, on trierait directement en ordre croissant et bousculerait l'ordre initial de l'attribut
        attributCopy = Arrays.copyOf(attribut, attribut.length);

        String[] frequence = trierAttribut(attributCopy);

        int mediane = mediane(frequence);
        System.out.println("\n mediane : " + mediane + " \n");
        String strMediane = attributCopy[mediane-1];
        System.out.println("strMediane impair : " + strMediane);

        String[][] lhs;
        String[][] rhs;

        ArrayList<ArrayList<String>> aLhs = new ArrayList();
        ArrayList<ArrayList<String>> aRhs = new ArrayList();

        for(int i = 0; i < tabQID.length; i++){ // pour chaque ligne
            int valeurMediane = Integer.parseInt(strMediane);
            int valeurI = Integer.parseInt(tabQID[i][indexAttribut]);

            ArrayList<String> aRow = new ArrayList();

            if (valeurI <= valeurMediane){

                // pour chaque colonne de cette ligne
                for( int j = 0; j < nbQID; j++){
                    aRow.add(tabQID[i][j]);

                }

                aLhs.add(aRow);
            }

            else {

                for(int k = 0; k < nbQID; k++){
                    aRow.add(tabQID[i][k]);
                }

                aRhs.add(aRow);
            }
        }

        lhs = ArrayListToArray(aLhs);
        rhs = ArrayListToArray(aRhs);


        if (k < lhs.length) {
            algoMultiDimensionnel(lhs);
            if (k < rhs.length) {
                algoMultiDimensionnel(rhs);
            }
        }



        else { // on compare k uniquement à lhs car lhs forcément >= rhs

            for(int j = 0; j < nbQID; j++){
                // pour chaque QID de lhs

                String[] colLhs = choisirAttribut(lhs, 0 , j);

                anonymiserValeurs(colLhs);

                String[] colRhs = choisirAttribut(rhs, 0, j);

                anonymiserValeurs(colRhs);
            }

        }

    }


    /**
     * Methode retournant un tableau a 2 dimensions des QID du tableau passe en entree
     * @param tab d'ou proviennent les QID
     * @return un tableau a 2 dimensions contenant les QID
     */
    private String[][] QIDtableau (String[][] tab) {
        String[][] tabQID =new String[tableau.length-1][nbQID];

        for (int i = nbID; i < (nbID + nbQID); i++) { // pour chaque QID

            for (int j = 1; j < tableau.length; j++) { // On le récupère sous forme de tableau (sans les noms des colonnes)
                tabQID[j-1][i-nbID] = tableau[j][i];

            }
        }

        return tabQID;
    }


    /**
     * Methode retournant l'index de l'attribut choisi
     * @return l'index de l'attribut choisi
     */
    private int choixIndexAttribut (){
        Random random = new Random();

        return random.nextInt(nbQID);
    }


    /**
     * Methode issue de l'Algo Uni Dimensionnel, revue ici dans le but de retourner une colonne de quasi-identifiants <br>
     * a partir d'un index donne a la place du nombre de quasi-identifiants.
     * @param tab tableau contenant les QID
     * @param nbID 0 dans l'algo Multi Dimensionnel, on ne s'en sert pas ici
     * @param index index de la colonne de quasi-identifiants choisi
     * @return le tableau contenant cette colonne de quasi-identifiants
     */
    @Override
    String[] choisirAttribut(String[][] tab, int nbID, int index) {
        String[] attribut;
        attribut = new String[tab.length];

        for(int i = 0; i <= tab.length; i++){
            try {
                attribut[i] = tab[i][index];
            } catch (ArrayIndexOutOfBoundsException e ){}
        }

        return attribut;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void anonymiserValeurs(String[] tab) {
        String intervalle;
        intervalle = "[ " + min(tab) + " - " + max(tab) + " ]";

        for(int i = 0; i < this.tableauQID.length; i++){
            try {
                if(Integer.parseInt(this.tableauQID[i][indexAttribut])>=Integer.parseInt(min(tab)) && Integer.parseInt(this.tableauQID[i][indexAttribut])<=Integer.parseInt(max(tab))){

                    this.tableauQID[i][indexAttribut]=intervalle;

                }
            }
            catch (NumberFormatException e){
                // permet de ne pas bloquer la boucle for si on passe dans une donnée déjà compris dans un intervalle
                ;
            }

        }
    }



    /**
     * Methode implemantant le quasi-identifiant anonymise dans le tableau initial
     * @param tabQID tableau contenant les quasi-identifiants anonymises qui vont être implementes dans le tableau initial
     */
    private void implementerQidAnonymise(String[][] tabQID){
        for(int i = 0; i < tabQID.length; i++){ // pour chaque ligne
            for(int j = 0; j  < nbQID; j++){
                tableau[i+1][j+nbID] = tabQID[i][j];
            }
        }
    }



    /**
     * Methode permettant de convertir un ArrayList a 2 dimensions en tableau a 2 dimensions
     * @param arrayList2d L'ArrayList a 2 dimensions que l'on doit convertir
     * @return le tableau a 2 dimensions
     */
    private String[][] ArrayListToArray(ArrayList<ArrayList<String>> arrayList2d){
        String[][] array = new String[arrayList2d.size()][];
        for (int i = 0; i < arrayList2d.size(); i++) {
            ArrayList<String> row = arrayList2d.get(i);
            array[i] = row.toArray(new String[row.size()]);
        }

        return array;
    }




}
