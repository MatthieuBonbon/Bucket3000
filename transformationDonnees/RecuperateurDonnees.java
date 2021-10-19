package transformationDonnees;

import java.io.File;
import java.io.IOException;

import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;

/**
 * Classe permettant de creer un tableau a 2 dimensions (String[][]) a partir d'un fihcier .ods
 * et de recuperer le nombre d'identifiants et de quasi-identifiants a partir de ce meme fichier.
 */
public class RecuperateurDonnees {
    private Sheet sheetDonnees;
    private Sheet sheetAttributs;
    private int nbID;
    private int nbQID;


    /**
     * Constructeur de la classe.
     * @param path chemin d'accès du fichier .ods
     */
    public RecuperateurDonnees(String path) {
        final File file = new File(path);

        try {
            this.sheetDonnees = SpreadSheet.createFromFile(file).getSheet(0); // feuille contenant les données
            this.sheetAttributs = SpreadSheet.createFromFile(file).getSheet(1); // feuille contenant les attributs
            initNbID_QID(); // On initialise les variables prenant comme valeur les nombres d'ID et de QID

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Methode permettant de lire la première feuille du fichier .ods et de la retourner sous forme d'un tableau a 2 dimensions (String[][]).
     * @return le tableau a 2 dimensions contenant les donnees de la premiere feuille du fichier .ods
     */
    public String[][] readOds () {

        //On crée la variable contenant le tableau
        String[][] tableau = new String[this.sheetDonnees.getRowCount()][this.sheetDonnees.getColumnCount()];

        // On insert les éléments dans le tableau
        for(int i = 0; i < this.sheetDonnees.getRowCount(); i++){

            for (int j = 0; j < this.sheetDonnees.getColumnCount(); j++){

                tableau[i][j] = this.sheetDonnees.getCellAt(j,i).getValue().toString();

            }
        }

        return tableau;
    }


    /**
     * Methode permettant de compter le nombre d'identifiants et de quasi-identifiants a partir de la deuxieme feuille
     * du fichier .ods
     */
    private void initNbID_QID(){
        System.out.println("colonnes : " + this.sheetAttributs.getColumnCount() + " | lignes : " + this.sheetAttributs.getRowCount());

        for(int i = 0; i < this.sheetAttributs.getRowCount(); i++){
            System.out.println(i);

            if(this.sheetAttributs.getCellAt(0,i).getValue().toString()!=""){
                this.nbID++;
            }

            if(this.sheetAttributs.getCellAt(1,i).getValue().toString()!=""){
                this.nbQID++;
            }
        }

        this.nbID--; // On ne compte pas la première ligne car elle contient le nom du type de données
        this.nbQID--; // et non le nom d'un champ ayant ce type de données (ID, QID, DS)

        System.out.println(nbID + " " + nbQID);
    }


    /**
     * Getter qui retourne le nombre d'identifiants
     * @return le nombre d'identifiants
     */
    public int getNbID() {
        return nbID;
    }


    /**
     * Getter qui retourne le nombre de quasi-iedntifiants
     * @return le nombre de quasi-identifiants
     */
    public int getNbQID() {
        return nbQID;
    }
}
