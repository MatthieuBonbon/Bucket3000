# Méthodes d'anonymisation utilisées / choix d'implémentations

## Pseudonymisation :
consiste à anonymiser des données par le biais de pseudo. Pour cela :

  - On crée un pseudo unique pour chaque identifiant que l'on va ensuite placer dans une colonne (aussi bien pour les identifiants que pour les pseudos).
  - Dans le tableau initial, on remplace chaque identifiant par le pseudo qui lui correspond.
  - On a ensuite un tableau d'association réunissant les identifiants et les pseudos qui sera créé et ce tableau nous permettra de retrouver l'identifiant à partir du pseudo qui lui a été associé.


## Bucketisation : 
agit sur les quasi-identifiants ainsi que sur les données sensibles en les regroupant par groupes de taille k. (k-anonymisation : https://en.wikipedia.org/wiki/K-anonymity)

  - Nous pouvons donc associer chaque ligne de quasi-identifiants à k différentes données sensibles.
  - Une fois que ceci est fait, on prend un tableau SANS identifiants et on le sépare en deux parties.
  - On aura d'un côté les quasi-identifiants avec les différents groupes qui leur auront été attribués.
  - Et d'un autre côté, on aura les données sensibles avec les différents groupes qui leur auront également été attribués.
  - On peut ensuite vérifier que chaque groupe de données sensibles contient l valeurs différentes grâce à la notion de diversité. (l-diversité : https://en.wikipedia.org/wiki/L-diversity)


## Algorithme unidimensionnel :
permet de mettre entre intervalles les valeurs d'un quasi-identifiant du tableau initial.

  - Pour commencer, on choisit un attribut qui, dans le cas présent, sera un quasi-identifiant.
  - On trie ensuite une copie de cet attribut dans un ordre croissant, ce qu'on appelle une fréquence.
  - Une fois que l'on a la fréquence, on peut donc récupérer la médiane de celle ci.
  - Une fois que l'on connait la médiane, on découpe cette fréquence en deux tableaux distincts.
  - Le tableau n°1 contient les valeurs qui sont inférieures ou égale à la médiane.
  - Le tableau n°2 contient les valeurs strictement supérieures à la médiane.
  - Tant que la longueur de ces deux tableaux est strictement supérieure a k, on fait une récursion.
  - Lorsque la longueur de ces tables est inférieure ou égale a k, on anonymise leurs valeurs respectives grâce a des intervalles.
  - Le minimum de l'intervalle sera égal au minimum du tableau et le maximum de l'intervalle sera égal au maximum du tableau.
  - Une fois que tout les éléments de l'attribut sélectionné ont été anonymisés, on peut donc implémenter l'attribut au tableau initial et retourner ce dernier.


## Algorithme multi-dimensionnel :
Pour commencer, on récupère un tableau à DEUX dimensions qui contient la totalité des quasi-identifiants du tableau initial. Pour chaque itération de l'algorithme, il y a plusieurs étapes :

  - On choisit l'index de l'attribut de façon aléatoire (c'est à dire l'index du quasi-identifiant).
  - On récupère tout les éléments de sa colonne dans un « String[] » .
  - On fait une copie de l'attribut que l'on trie dans un ordre croissant (principe de fréquence vu dans la partie Algorithme Unidimensionnel).
  - On détermine la médiane de cette fréquence.
  - On réalise deux tableaux à deux dimensions.
  - Pour chaque ligne du tableau des QID (Quasi-Identifiant) :
    - Si l'élément de l'attribut qui se trouve à cette ligne est inférieur ou égale à la médiane, cet élément sera trouvable dans le tableau 1.
    - Si l'élément de l'attribut est supérieur à la médiane, cet élément sera donc trouvable dans le tableau 2.
  - Si la taille du tableau 1 est supérieur à k :
    - On aura une récurrence de l'algorithme Multidimensionnel (tableau 1).
    - Si la taille du tableau 2 est supérieur à k :
      - On aura une récurrence de l'algorithme Multidimensionnel (tableau 2). 
  - Sinon :
    - On aura une mise entre intervalle pour le tableau 1.
    - On aura également une mise entre intervalle pour le tableau 2.
  - On implémente le tableau qui contient les QID anonymisés dans le tableau initial avant de le retourner.

  - choix d'implémentations spécifiques :
    - Nous avons décidés de choisir de manière aléatoire l'attribut pour chaque itération de l'algorithme multidimensionnel car, selon nous, cette façon de faire est assez sécurisée car on ne peut pas déterminer à l'avance quel attribut sera choisi pour être anonymisé. 
    - De plus, ce choix nous a permis d'augmenter grandement le nombre de combinaisons d'attribut en fonction du nombre de lignes du tableau initial à anonymiser par rapport à d'autres méthodes car celle ci ne néglige, ni ne met en valeur, certains attributs par rapport à d'autres.

Extrait d'un rapport supervisé par Tristan Biot.
