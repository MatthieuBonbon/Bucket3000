# Bucket3000

Projet d'Anonymisation de données.

Les bases de données sont représentées par des tableurs auf format .ods .
Les données sont catégorisées en 3 groupes dans les bases de données :

  - Les Identifiants : permettent d'identifier directement un individu
  
  - Les Quasi-identifiants : demandent un autre jeu de données mais représentent un risque
  
  - Les Données sensibles : rendent l'anonymisation obligatoire

La répartition des données est la suivante : les identifiants en premières colonnes, suivis des quasi-identifiants,
d'éventuels attributs non-sensibles et enfin d'une unique colonne de données sensibles.

Plusieurs techniques d'anonymisation de données sont implémentées dans ce projet :
  - La pseudonymisation
  - La bucketisation (repose sur les travaux de k-anonymisation de Mme Latanya Sweeney : https://en.wikipedia.org/wiki/Latanya_Sweeney )
  - Les algorithmes unidimensionnel et multi-dimensionnel

Le principe de fonctionnement de ces méthodes d'anonymisation est détaillé ici : https://github.com/MatthieuBonbon/Bucket3000/blob/main/anonymisation.md

Implémenté en Java. Code commenté au format JavaDoc qu'il est possible de générer.

Librairies utilisées :
  - Interface graphique créée via la librairie graphique Swing (description : https://fr.wikipedia.org/wiki/Swing_(Java) ).
  - Prise en charge des tableurs au format .ods (Open Document) via la librairie jOpenDocument (site officiel : https://www.jopendocument.org/ ). Les spécificités de l'installation de la librairie jOpenDocument sont accessibles ici : https://github.com/MatthieuBonbon/Bucket3000/blob/main/install_jOpenDocument.md
 
 Avec le soutien de Tristan Biot & others.
