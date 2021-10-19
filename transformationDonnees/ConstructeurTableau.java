package transformationDonnees;


import org.jopendocument.dom.spreadsheet.SpreadSheet;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import java.io.File;
import java.io.IOException;

/**
 *  Classe qui permet la gestion de creation d'un fichier.ods contenant un tableau anonymise comme par exemple : <br>
 *  <ul>
 *      <li>
 *          Le tableau pseudonymise
 *      </li>
 *      <li>
 *          Le tableau de correspondance entre les identifiants et les pseudos
 *      </li>
 *      <li>
 *          Dans le cadre d'une bucketisation :
 *          <ul>
 *              <li>
 *                  Le tableau contenant les Quasi-identifiants
 *              </li>
 *              <li>
 *                  Le tableau contenant les Donnees sensibles
 *              </li>
 *          </ul>
 *      </li>
 *      <li>
 *          Dans le cadre d'un Algorithme Multi Dimensionnel :
 *          <ul>
 *              <li>
 *                  Le tableau contenant les données anonymises par l'algorithme multi dimensionnel
 *              </li>
 *          </ul>
 *      </li>
 *  </ul>
 */
public class ConstructeurTableau {
    private String[][] tableau;
    private TableModel model;
    private String fileName;
    private String path;

    /**
     * Constructeur de la classe
     * @param tableau qui va être integre dans le fichier .ods
     * @param fileName le nom du fichier .ods cree
     * @param path le chemin d'accès du fichier .ods cree
     */
    public ConstructeurTableau(String[][] tableau, String fileName, String path) {
        this.tableau = tableau;
        this.fileName = fileName;
        this.path = path;
    }

    /**
     *  Prend en entree un tableau a 2 dimensions et retourne la première ligne de ce tableau sous forme de tableau a 1 dimension
     *  afin de recuperer le nom de chaque colonne.
     * @param tableau dont va etre extrait les noms des colonnes
     * @return le tableau a 1 dimension contenant les noms de chaque colonne
     */
    private String[] columnsName(String[][] tableau){
        String[] name = new String [tableau[0].length];
            for(int index_column = 0; index_column < tableau[0].length; index_column++){
                name[index_column] = tableau[0][index_column];
            }
        return name;
    }

    /**
     *  Prend en entree un tableau a 2 dimensions et retourne ce meme tableau sans la premiere ligne, celle contenant les noms de colonnes.
     * @param tableau dont va être extrait le tableau a 2 dimensions sans les colonnes
     * @return le tableau a 2 dimensions contenant le tableau initial a partir de la ligne 1, permiere ligne sans les noms
     */
    private String[][] data(String[][] tableau){

        String[][] dataArray = new  String[tableau.length][tableau[0].length];

            for(int index_column = 0; index_column < tableau[0].length; index_column++){

                for(int index_row = 1; index_row < tableau.length; index_row++){

                    dataArray[index_row-1][index_column] = tableau[index_row][index_column];
                }
            }

        return dataArray;
    }

    /** Methode principale de cette classe. Permet de convertir un tableau a 2 dimensions (String[][]) sous la forme d'un fichier .ods
     * avec un nom et un chemin d'acces predefini.
     */
    public void construireTableau(){

        String[] columns = columnsName(tableau);
        String[][] data = data(tableau);

        System.out.println(this.path+this.fileName);

        this.model = new DefaultTableModel(data, columns);

        final File file = new File(this.path+this.fileName);
        try {
            SpreadSheet.createEmpty(model).saveAs(file);
            System.out.println("Nouveau document exporté");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
