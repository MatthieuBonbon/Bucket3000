package vue;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.io.File;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

/**
 * Classe qui s'occupe de l'affichage de l'interface graphique.
 */
public class Vue {

    // on crée une fenêtre dont le titre est "Anonymisation"
    private JFrame frame = new JFrame("Anonymisation");
    private String path;
    private String pathOut;
    private String name;
    private boolean fichierValide;
    private boolean dossierValide;
    private boolean recommencer;
    private boolean isLDivers;
    private int k;
    private int l;



    /**
     * Constructeur de la classe.
     */
    public Vue() { // constructeur
    }



    /**
     * Methode principale de cette classe, permettant d'afficher l'interface graphique.
     */
    public void afficherInterface() {
        fichierValide = false;
        dossierValide = false;
        recommencer = false;
        isLDivers = false;
        path = null;
        pathOut = null;
        name = null;
        k = 0;
        l = 0;
        initFenetre();
        menuCommencer();
    }



    /**
     * Methode permettant de configurer la fenetre.
     */
    private void initFenetre(){

        // la fenêtre doit se fermer quand on clique sur la croix rouge
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // on modifie la taille de la fenêtre
        frame.setSize(800,600);
        // on centre la fenêtre
        frame.setLocationRelativeTo(null);
        // on rend la fenêtre visible
        frame.setVisible(true);
        // on bloque la taille de la fenêtre
        frame.setResizable(false);



        // Si la personne ferme la fenêtre, même si une boucle infini tourne, le programme s'arrête
        frame.addWindowListener(new java.awt.event.WindowAdapter(){
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent){
                System.out.println("Arrêt du programme");
                System.exit(0);
            }
        });

    }


    /**
     * Methode permettant d'afficher le premier menu.
     */
    private void menuCommencer(){
        JPanel panel = new JPanel(); // Panel texte
        panel.setLayout(new GridLayout(2,1));
        panel.setBackground(Color.white);


        // on crée le texte d'accueil
        JLabel label = new JLabel("Bienvenue dans Anonymisation");
        label.setHorizontalAlignment(0);
        label.setVerticalAlignment(0);
        label.setFont(new Font("Copper Black", Font.BOLD, 24));
        panel.add(label);


        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.white);
        // on crée le bouton pour commencer
        JButton button = new JButton("Je commence");

        button.setBounds(550,480,180,30);

        buttonPanel.add(button);
        panel.add(buttonPanel);

        // onc crée la liste d'actions que doit effectuer le bouton
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Enfin quelque chose qui fonctionne ?");
                menuChoixFichier();
            }
        });




        frame.getContentPane().removeAll();
        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }



    /**
     * Methode permettant d'afficher le menu du choix du fichier et d'afficher egalement le nom du fichier .ods choisi<br>
     * que le programme doit anonymiser.
     */
    private void menuChoixFichier(){

        // On crée la nouvelle fenêtre
        JPanel panel = new JPanel( new GridLayout(3,1));
        panel.setBackground(Color.white);
        panel.setPreferredSize(new Dimension(800, 600));

        // On met le texte de choix de document
        JLabel label = new JLabel("Veuillez choisir un document à anonymiser : ");
        label.setFont(new Font("Copper Black", Font.BOLD, 24));
        label.setHorizontalAlignment(0);
        panel.add(label);



        // Panel 2
        JPanel parcourirPanel = new JPanel( new FlowLayout());
        parcourirPanel.setBackground(Color.WHITE);
        // Le texte affichant le  fichier choisi
        JLabel labelName = new JLabel();
        //labelName.setVisible(false);

        // Le bouton parcourir
        JButton buttonParcourir = new JButton("Parcourir");
        buttonParcourir.setBounds(550,480,180,30);
        parcourirPanel.add(buttonParcourir);

        buttonParcourir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { // affichage nom fichier
                choisirFichier();

                if(name!=null){
                    labelName.setText("Fichier choisi : " + name);
                    labelName.setVisible(true);
                    parcourirPanel.add(labelName);
                    frame.pack();
                }

            }

        });


        //Panel 3
        JPanel validerPanel = new JPanel( new BorderLayout());
        validerPanel.setBackground(Color.WHITE);

        // Le bouton valider
        JButton buttonValider = new JButton("Valider");
        buttonValider.setBounds(550,480,180,30);
        validerPanel.add(buttonValider, BorderLayout.PAGE_END);

        buttonValider.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(path!=null){ // permet de ne pas valider si aucun fichier n'a été choisi
                    fichierValide=true;

                    choisirMethode();
                }
            }
        });

        // On affiche la nouvelle fenêtre à la place de l'ancienne
        frame.getContentPane().removeAll();
        panel.add(parcourirPanel);
        panel.add(validerPanel);
        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }



    /**
     * Methode du menu du choix du fichier permettant de recuperer le nom et le chemin d'acces du fichier .ods choisi.
     */
    private void choisirFichier() {
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jfc.setDialogTitle("Choisir un document");
        jfc.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Tableur (.ods)", "ods");
        jfc.addChoosableFileFilter(filter);

        int returnValue = jfc.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            System.out.println(jfc.getSelectedFile().getPath());
            path = jfc.getSelectedFile().getPath();
            name = jfc.getSelectedFile().getName();

        }
    }



    /**
     * Methode permettant l'affichage du menu du choix de la methode d'anonymisation <br>
     * et de recuperer les valeurs de k (k-anonymisation) et l (l-diversite)
     */
    private void choisirMethode(){
        JPanel panel = new JPanel( new GridLayout(3,1));
        panel.setBackground(Color.white);
        panel.setPreferredSize(new Dimension(800, 600));

        // On met le texte de choix de méthode
        JLabel label = new JLabel("Veuillez choisir une méthode d'anonymisation : ");
        label.setFont(new Font("Copper Black", Font.BOLD, 24));
        label.setHorizontalAlignment(0);
        panel.add(label);


        //Panel 2
        JPanel panelKL = new JPanel( new GridLayout(2,1));
        JPanel panelK = new JPanel( new FlowLayout());
        panelK.setBackground(Color.white);
        JPanel panelL = new JPanel( new FlowLayout());
        panelL.setBackground(Color.white);

        // On met le texte de choix de k
        JLabel labelK = new JLabel(" - Veuillez choisir une valeur pour k : ");
        labelK.setFont(new Font("Copper Black", Font.BOLD, 18));
        panelK.add(labelK);

        // La zone de texte pour k
        JTextField textFieldK = new JTextField();
        textFieldK.setColumns(8);
        panelK.add(textFieldK);

        // On met le texte de choix de l
        JLabel labelL = new JLabel(" - Veuillez choisir une valeur pour l : ");
        labelL.setFont(new Font("Copper Black", Font.BOLD, 18));
        panelL.add(labelL);

        // La zone de texte pour l
        JTextField textFieldL = new JTextField();
        textFieldL.setColumns(8);
        panelL.add(textFieldL);

        panelKL.add(panelK);
        panelKL.add(panelL);

        panel.add(panelKL);

        // Panel 3
        JPanel panelMethode = new JPanel(new FlowLayout());
        panelMethode.setBackground(Color.WHITE);
        // Le bouton bucketisation
        JButton buttonBucket = new JButton("Bucketisation");
        panelMethode.add(buttonBucket);
        buttonBucket.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String strK = textFieldK.getText();
                String strL =textFieldL.getText();
                System.out.println("Lancer la bucketisation");


                try {
                    int K = Integer.parseInt(strK);
                    int L = Integer.parseInt(strL);
                    if(K>=2 && L>=2 && K>=L){
                        System.out.println("Bucketisation validée");
                        k = K;
                        l = L;
                        menuChoixDossier();
                    }
                    else{
                        System.out.println("Bucketisation invalidée");
                    }
                } catch (NumberFormatException f){
                    System.out.println("k ou l invalide");
                }

            }
        });

        // Le bouton AlgoMultiDimensionnel
        JButton buttonAlgoDim = new JButton("Algorithme Multi Dimensionnel");
        panelMethode.add(buttonAlgoDim);
        buttonAlgoDim.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String strK = textFieldK.getText();
                System.out.println("Lancer l'algo Uni Dimensionnel");

                try {
                    int K = Integer.parseInt(strK);
                    if(K>=2){
                        System.out.println("Algo validé");
                        k = K;
                        menuChoixDossier();
                    }
                    else{
                        System.out.println("Algo invalidé");
                    }
                } catch (NumberFormatException f){
                    System.out.println("k invalide");
                }
            }
        });


        panel.add(panelMethode);

        frame.getContentPane().removeAll();
        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }



    /**
     * Methode permettant d'afficher le menu du choix du dossier <br>
     * et d'afficher egalement le chemin d'acces du dossier choisi ou les fichiers anonymises seront enregistres.
     */
    private void menuChoixDossier(){ // menu du choix du dossier dans lequel vont êtres mis les fichier anonymisés
        JPanel panel = new JPanel( new GridLayout(4, 1));
        panel.setBackground(Color.white);
        panel.setPreferredSize(new Dimension(800, 600));

        // On met le texte de choix de méthode
        JLabel label = new JLabel("Veuillez choisir le dossier dans lequel les fichiers anonymisés seront stockés : ");
        label.setFont(new Font("Copper Black", Font.BOLD, 20));
        label.setHorizontalAlignment(0);
        panel.add(label);

        // Si on a choisit la bucketisation
        JPanel diversPanel = new JPanel( new FlowLayout());
        diversPanel.setBackground(Color.WHITE);
        if(l!=0){
            if(isLDivers){ // si c'est l-divers
                JLabel labelDiversite = new JLabel(" ( La bucketisation est " + l + "-diverse. ) ");
                labelDiversite.setFont(new Font("Copper Black", Font.ITALIC, 16));
                labelDiversite.setHorizontalAlignment(0);
                labelDiversite.setForeground(Color.gray);
                diversPanel.add(labelDiversite);
            }
            else{ // si ce n'est pas l-divers
                JLabel labelDiversite = new JLabel(" ( La bucketisation n'est pas" + l + "-diverse. ) ");
                labelDiversite.setFont(new Font("Copper Black", Font.ITALIC, 16));
                labelDiversite.setHorizontalAlignment(0);
                labelDiversite.setForeground(Color.gray);
                diversPanel.add(labelDiversite);
            }
        }

        panel.add(diversPanel);


        // Panel 2
        JPanel parcourirPanel = new JPanel( new FlowLayout());
        parcourirPanel.setBackground(Color.WHITE);
        // Le texte affichant le  fichier choisi
        JLabel labelName = new JLabel();

        // Le bouton parcourir
        JButton buttonParcourir = new JButton("Parcourir");
        buttonParcourir.setBounds(550,480,180,30);
        parcourirPanel.add(buttonParcourir);

        buttonParcourir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { // affichage nom fichier
                choisirDossier();

                if(pathOut!=null){
                    labelName.setText("Dossier choisi : " + pathOut);
                    labelName.setVisible(true);
                    parcourirPanel.add(labelName);
                    frame.pack();
                }

            }

        });



        //Panel 3
        JPanel validerPanel = new JPanel( new BorderLayout());
        validerPanel.setBackground(Color.WHITE);

        // Le bouton valider
        JButton buttonValider = new JButton("Valider");
        buttonValider.setBounds(550,480,180,30);
        validerPanel.add(buttonValider, BorderLayout.PAGE_END);

        buttonValider.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(pathOut!=null){ // permet de ne pas valider si aucun fichier n'a été choisi
                    dossierValide=true;

                    System.out.println("dossier cible accessible");

                    menuFin();
                }
            }
        });

        // On affiche la nouvelle fenêtre à la place de l'ancienne
        frame.getContentPane().removeAll();
        panel.add(parcourirPanel);
        panel.add(validerPanel);
        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }



    /**
     * Methode du menu du choix du dossier permettant de recuperer le chemin d'acces du dossier d'enregistrement des nouveaux fichiers choisi.
     */
    private void choisirDossier() {
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jfc.setDialogTitle("Choisir un dossier");
        jfc.setAcceptAllFileFilterUsed(false);

        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int returnValue = jfc.showSaveDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            System.out.println(jfc.getSelectedFile().getPath());
            pathOut = jfc.getSelectedFile().getPath();
        }
    }



    /**
     * Methode permettant l'affichage du menu final.
     */
    private void menuFin(){

        // Panel main
        JPanel mainPanel = new JPanel( new GridLayout(2, 1));
        mainPanel.setBackground(Color.white);
        mainPanel.setPreferredSize(new Dimension(800, 600));



        // Panels textes

        JPanel mainTextesPanel = new JPanel( new GridLayout(3,1));
        mainPanel.setBackground(Color.white);
        mainPanel.setPreferredSize(new Dimension(800, 600));



        JPanel panel1 = new JPanel();
        panel1.setBackground(Color.white);
        panel1.setPreferredSize(new Dimension(800, 600));

        // On met le texte de validation de l'anonymisation
        JLabel labelFin = new JLabel("Votre document est maintenant anonymisé !");
        labelFin.setFont(new Font("Copper Black", Font.BOLD, 24));
        panel1.add(labelFin);



        JPanel panel2 = new JPanel();
        panel2.setBackground(Color.white);
        panel2.setPreferredSize(new Dimension(800, 600));

        // On met le texte de remerciement
        JLabel labelMerci = new JLabel("Nous vous remercions d'avoir utilisé notre service.");
        labelMerci.setFont(new Font("Copper Black", Font.BOLD, 24));
        panel2.add(labelMerci);



        JPanel panel3 = new JPanel();
        panel3.setBackground(Color.white);
        panel3.setPreferredSize(new Dimension(800, 600));

        // On met le texte pour quitter
        JLabel labelQuit = new JLabel("\n Cliquez sur la croix rouge pour quitter.");
        labelQuit.setFont(new Font("Copper Black", Font.ITALIC, 18));
        labelQuit.setForeground(Color.gray);
        panel3.add(labelQuit);



        mainTextesPanel.add(panel1);
        mainTextesPanel.add(panel2);
        mainTextesPanel.add(panel3);



        // Panel btn recommencer
        JPanel recommencerPanel = new JPanel( new FlowLayout() );
        recommencerPanel.setBackground(Color.white);

        // Le bouton recommencer
        JButton buttonRecommencer = new JButton("Anonymiser un autre fichier");
        buttonRecommencer.setBounds(550,480,180,30);
        recommencerPanel.add(buttonRecommencer);

        buttonRecommencer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("recommencer");
                recommencer = true;
                frame.getContentPane().removeAll();
                frame.setVisible(false);
            }
        });

        // On affiche la nouvelle fenêtre à la place de l'ancienne
        frame.getContentPane().removeAll();
        mainPanel.add(mainTextesPanel);
        mainPanel.add(recommencerPanel);
        frame.getContentPane().add(mainPanel);
        frame.setVisible(true);

    }

    /**
     * Getter retournant le chemin d'accès du fichier .ods choisi
     * @return le chemin d'accès du fichier .ods choisi
     */
    public String getPath() {
        return path;
    }

    /**
     * Getter retournant le chemin d'accès du dossier choisi
     * @return le chemin d'accès du dossier choisi
     */
    public String getPathOut() {
        return pathOut;
    }

    /**
     * Getter retournant si le fichier choisi est valide
     * @return si le fichier choisi est valide
     */
    public boolean isFichierValide() {
        return fichierValide;
    }

    /**
     * Getter retournant la valeur k (k-anonyme)
     * @return la valeur k (k-anonyme)
     */
    public int getK() {
        return k;
    }

    /**
     * Getter retournant la valeur l (l-diversité)
     * @return la valeur l (l-diversité)
     */
    public int getL() {
        return l;
    }

    /**
     * Getter retournant si le dossier choisi est valide
     * @return si le dossier choisi est valide
     */
    public boolean isDossierValide() {
        return dossierValide;
    }

    /**
     * Getter retournant si l'utilisateur souhaite recommencer
     * @return si l'utilisateur souhaite recommencer
     */
    public boolean isRecommencer() {
        return recommencer;
    }

    /**
     * Setter permettant de modifier la valeur de LDivers
     * @param LDivers si la bucketisation est l-diverse ou non
     */
    public void setLDivers(boolean LDivers) {
        isLDivers = LDivers;
    }
}
